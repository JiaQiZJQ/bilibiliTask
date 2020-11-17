package top.srcrs.task.daily;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.srcrs.Task;
import top.srcrs.domain.Config;
import top.srcrs.domain.Data;
import top.srcrs.util.Request;

/**
 * 进行视频投币
 * @author srcrs
 * @Time 2020-10-13
 */
public class ThrowCoinTask implements Task {
    /** 获取日志记录器对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(ThrowCoinTask.class);
    /** 获取DATA对象 */
    Data data = Data.getInstance();
    /** 获取配置类 */
    Config config = Config.getInstance();

    @Override
    public void run() {
        try {
            /* 今天投币获得了多少经验 */
            Integer reward = getReward();
            /* 计算今天需要投 num1 个硬币 */
            Integer num1 = (50 - reward) / 10;
            /* 还剩多少个硬币 */
            Integer num2 = getCoin();
            /* 配置类中设置投币数 */
            Integer num3 = config.getCoin();
            /* 避免设置投币数为负数异常 */
            num3 = num3 < 0 ? 0 : num3;
            /* 实际需要投 num个硬币 */
            int num = (num2 >= num1 ? num1 : num2) >= num3 ? num3 : (num2 >= num1 ? num1 : num2);
            /* 获取分区视频信息 */
            JSONArray regions = getRegions("6", "1");
            /* 给每个视频投 1 个币,点 1 个赞 */
            for (int i = 0; i < num; i++) {
                /* 视频的aid */
                String aid = regions.getJSONObject(i).getString("aid");
                JSONObject json = throwCoin(aid, "1", "1");
                /* 输出的日志消息 */
                String msg ;
                if ("0".equals(json.getString("code"))) {
                    msg = "硬币-1";
                } else {
                    msg = json.getString("message");
                }
                LOGGER.info("投币给 -- av{} -- {}", aid, msg);
            }
        } catch (Exception e) {
            LOGGER.info("投币异常 -- " + e);
        }
    }

    /**
     * 给视频投币
     * @param aid         视频 aid 号
     * @param num         投币数量
     * @param selectLike 是否点赞
     * @return JSONObject
     * @author srcrs
     * @Time 2020-10-13
     */
    public JSONObject throwCoin(String aid, String num, String selectLike) {

        String body = "aid=" + aid
                + "&multiply=" + num
                + "&select_like=" + selectLike
                + "&cross_domain=" + "true"
                + "&csrf=" + data.getBiliJct();
        return Request.post("https://api.bilibili.com/x/web-interface/coin/add", body);
    }

    /**
     * 获取今天投币所得经验
     * @return JSONObject
     * @author srcrs
     * @Time 2020-10-13
     */
    public Integer getReward() {
        JSONObject jsonObject = Request.get("https://account.bilibili.com/home/reward");
        return Integer.parseInt(jsonObject.getJSONObject("data").getString("coins_av"));
    }

    /**
     * 获取硬币的剩余数
     * @return Integer
     * @author srcrs
     * @Time 2020-10-13
     */
    public Integer getCoin() {
        JSONObject jsonObject = Request.get("https://api.bilibili.com/x/web-interface/nav?build=0&mobi_app=web");
        return (int) (Double.parseDouble(jsonObject.getJSONObject("data").getString("money")));
    }

    /**
     * 获取B站分区视频信息
     * @param ps  获取视频的数量
     * @param rid 分区号
     * @return JSONArray
     * @author srcrs
     * @Time 2020-10-13
     */
    public JSONArray getRegions(String ps, String rid) {
        String params = "?ps=" + ps + "&rid=" + rid;
        JSONObject jsonObject = Request.get("https://api.bilibili.com/x/web-interface/dynamic/region" + params);
        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("archives");
        JSONArray jsonRegions = new JSONArray();
        for (Object object : jsonArray) {
            JSONObject json = (JSONObject) object;
            JSONObject cache = new JSONObject();
            cache.put("title", json.getString("title"));
            cache.put("aid", json.getString("aid"));
            cache.put("bvid", json.getString("bvid"));
            cache.put("cid", json.getString("cid"));
            jsonRegions.add(cache);
        }
        return jsonRegions;
    }
}
