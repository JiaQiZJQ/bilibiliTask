package top.srcrs.task.live;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import top.srcrs.Task;
import top.srcrs.domain.Config;
import top.srcrs.domain.UserData;
import top.srcrs.util.Request;

/**
 * B站直播送出即将过期的礼物
 * @author srcrs
 * @Time 2020-10-13
 */
@Slf4j
public class GiveGiftTask implements Task {
    /** 获取日志记录器对象 */
    UserData userData = UserData.getInstance();
    Config config = Config.getInstance();

    @Override
    public void run(){
        try{
            /* 从配置类中读取是否需要执行赠送礼物 */
            if(!config.isGift()){
                log.info("【送即将过期礼物】: 自定义配置不送出即将过期礼物✔");
                return;
            }
            /* 直播间 id */
            String roomId = "";
            /* 直播间 uid 即 up 的 id*/
            String uid = "";
            /* B站后台时间戳为10位 */
            long nowTime = System.currentTimeMillis()/1000;
            /* 获得礼物列表 */
            JSONArray jsonArray = xliveGiftBagList();
            /* 判断是否有过期礼物出现 */
            boolean flag = true;
            for(Object object : jsonArray){
                JSONObject json = (JSONObject) object;
                long expireAt = Long.parseLong(json.getString("expire_at"));
                /* 礼物还剩 1 天送出 */
                /* 永久礼物到期时间为 0 */
                if((expireAt-nowTime) < 60*60*24*1 && expireAt != 0){
                    /* 如果有未送出的礼物，则获取一个直播间 */
                    if("".equals(roomId)){
                        JSONObject uidAndRid = getuidAndRid();
                        uid = uidAndRid.getString("uid");
                        roomId = uidAndRid.getString("roomId");
                    }
                    JSONObject pJson = new JSONObject();
                    pJson.put("biz_id", roomId);
                    pJson.put("ruid", uid);
                    pJson.put("bag_id", json.get("bag_id"));
                    pJson.put("gift_id", json.get("gift_id"));
                    pJson.put("gift_num", json.get("gift_num"));
                    JSONObject jsonObject3 = xliveBagSend(pJson);
                    if("0".equals(jsonObject3.getString("code"))){
                        /* 礼物的名字 */
                        String giftName = jsonObject3.getJSONObject("data").getString("gift_name");
                        /* 礼物的数量 */
                        String giftNum = jsonObject3.getJSONObject("data").getString("gift_num");
                        log.info("【送即将过期礼物】: 给直播间 - {} - {} - 数量: {}✔",roomId,giftName,giftNum);
                        flag = false;
                    }
                    else{
                        log.warn("【送即将过期礼物】: 失败, 原因 : {}❌", jsonObject3);
                    }
                }
            }
            if(flag){
                log.info("【送即将过期礼物】: " + "当前无即将过期礼物❌");
            }
        } catch (Exception e){
            log.error("💔赠送礼物异常 : ", e);
        }
    }

    /**
     * 获取一个直播间的room_id
     * @return JSONObject
     * @author srcrs
     * @Time 2020-10-13
     */
    public String xliveGetRecommend(){
        return Request.get("https://api.live.bilibili.com/relation/v1/AppWeb/getRecommendList")
                .getJSONObject("data")
                .getJSONArray("list")
                .getJSONObject(6)
                .getString("roomid");
    }

    /**
     * B站获取直播间的uid
     * @param roomId up 主的 uid
     * @return JSONObject
     * @author srcrs
     * @Time 2020-10-13
     */
    public String xliveGetRoomUid(String roomId){
        JSONObject pJson = new JSONObject();
        pJson.put("room_id", roomId);
        return Request.get("https://api.live.bilibili.com/xlive/web-room/v1/index/getInfoByRoom", pJson)
                .getJSONObject("data")
                .getJSONObject("room_info")
                .getString("uid");
    }

    /**
     * 根据 uid 获取其 roomid
     * @param mid 即 uid
     * @return String 返回一个直播间id
     * @author srcrs
     * @Time 2020-11-20
     */
    public String getRoomInfoOld(String mid) {
        JSONObject pJson = new JSONObject();
        pJson.put("mid", mid);
        return Request.get("http://api.live.bilibili.com/room/v1/Room/getRoomInfoOld", pJson)
                .getJSONObject("data")
                .getString("roomid");
    }

    /**
     * B站直播获取背包礼物
     * @return JSONObject
     * @author srcrs
     * @Time 2020-10-13
     */
    public JSONArray xliveGiftBagList(){
        return Request.get("https://api.live.bilibili.com/xlive/web-room/v1/gift/bag_list")
                .getJSONObject("data")
                .getJSONArray("list");
    }

    /**
     * B站直播送出背包的礼物
     * @param pJson JSONObject
     * @return JSONObject
     * @author srcrs
     * @Time 2020-10-13
     */
    public JSONObject xliveBagSend(JSONObject pJson){
        pJson.put("uid", userData.getMid());
        pJson.put("csrf", userData.getBiliJct());
        pJson.put("send_ruid", 0);
        pJson.put("storm_beat_id", 0);
        pJson.put("price", 0);
        pJson.put("platform", "pc");
        pJson.put("biz_code", "live");
        return Request.post("https://api.live.bilibili.com/gift/v2/live/bag_send", pJson);
    }

    /**
     * 获取一个包含 uid 和 RooId 的 json 对象
     * @return JSONObject 返回一个包含 uid 和 RooId 的 json 对象
     * @author srcrs
     * @Time 2020-11-20
     */
    public JSONObject getuidAndRid(){
        /* 直播间 id */
        String roomId;
        /* 直播间 uid 即 up 的 id*/
        String uid;
        if(config.getUpLive() != null){
            /* 获取指定up的id */
            uid = config.getUpLive();
            roomId = getRoomInfoOld(uid);
            String status = "0";
            if(status.equals(roomId)){
                log.info("【获取直播间】: 自定义up {} 无直播间", uid);
                /* 随机获取一个直播间 */
                roomId = xliveGetRecommend();
                uid = xliveGetRoomUid(roomId);
                log.info("【获取直播间】: 随机直播间");
            } else{
                log.info("【获取直播间】: 自定义up {} 的直播间", uid);
            }

        } else{
            /* 随机获取一个直播间 */
            roomId = xliveGetRecommend();
            uid = xliveGetRoomUid(roomId);
            log.info("【获取直播间】: " + "随机直播间");
        }
        JSONObject json = new JSONObject();
        json.put("uid",uid);
        json.put("roomId",roomId);
        return json;
    }
}
