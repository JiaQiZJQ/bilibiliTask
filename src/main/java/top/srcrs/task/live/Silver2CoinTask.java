package top.srcrs.task.live;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import top.srcrs.Task;
import top.srcrs.domain.Config;
import top.srcrs.domain.UserData;
import top.srcrs.util.Request;

/**
 * 银瓜子兑换硬币
 * @author srcrs
 * @Time 2020-10-13
 */
@Slf4j
public class Silver2CoinTask implements Task {
    UserData userData = UserData.getInstance();
    Config config = Config.getInstance();

    @Override
    public void run(){
        /* 获得银瓜子的数量 */
        Integer silver = getSilver();
        log.info("【银瓜子】: {}",silver);
        if(config.isS2c()){
            try{
                /* 如果银瓜子数量小于700没有必要再进行兑换 */
                int minSilver = 700;
                if(silver < minSilver){
                    log.info("【银瓜子兑换硬币】: {}","银瓜子余额不足❌");
                } else{
                    log.info("【银瓜子兑换硬币】: {}",silver2coin().getString("msg") + "✔");
                }
            } catch (Exception e){
                log.error("💔银瓜子兑换硬币错误 : ", e);
            }
        } else{
            log.info("【银瓜子兑换硬币】: " + "自定义配置不将银瓜子兑换硬币✔");
        }
    }

    /**
     * 银瓜子兑换成硬币
     * @return JSONObject
     * @author srcrs
     * @Time 2020-10-13
     */
    public JSONObject silver2coin(){
        JSONObject pJson = new JSONObject();
        pJson.put("csrf", userData.getBiliJct());
        return Request.post("https://api.live.bilibili.com/pay/v1/Exchange/silver2coin", pJson);
    }

    /**
     * 获取银瓜子的数量
     * @return Integer
     * @author srcrs
     * @Time 2020-10-17
     */
    public Integer getSilver(){
        JSONObject jsonObject = Request.get("https://api.live.bilibili.com/xlive/web-ucenter/user/get_user_info");
        return Integer.parseInt(jsonObject.getJSONObject("data").getString("silver"));
    }
}
