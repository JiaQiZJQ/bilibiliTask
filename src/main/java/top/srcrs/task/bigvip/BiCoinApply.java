package top.srcrs.task.bigvip;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import top.srcrs.Task;
import top.srcrs.domain.Config;
import top.srcrs.domain.UserData;
import top.srcrs.util.Request;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * 月底 (28号) 自动使用 B 币卷给自己充电
 * @author srcrs
 * @Time 2020-10-19
 */
@Slf4j
public class BiCoinApply implements Task {
    /** 获取DATA对象 */
    UserData userData = UserData.getInstance();
    /** 获取用户自定义配置信息 */
    Config config = Config.getInstance();
    /** 28号代表月底 */
    private static final int END_OF_MONTH = 28;
    /** 代表获取到正确的json对象 code */
    private static final String SUCCESS = "0";

    @Override
    public void run() {
        try{
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
            int day = cal.get(Calendar.DATE);
            /* B币券余额 */
            int couponBalance =userData.getCouponBalance();
            log.info("【B币卷】: {}", couponBalance);
            if(couponBalance <= 0){
                log.info("【使用B币卷】: " + "B币卷为0 ,无法使用❌");
                return ;
            }
            if(day != END_OF_MONTH){
                log.info("【使用B币卷】: " + "今日不是月底(28号)❌");
                return;
            }
            switch (config.getAutoBiCoin()) {
                case "1":
                    doCharge(couponBalance);
                    break;
                case "2":
                    doMelonSeed(couponBalance);
                    break;
                default:
                    log.info("【使用B币卷】: " + "自定义配置不使用");
                    break;
            }
        } catch (Exception e){
            log.error("💔使用B币卷部分错误 : ", e);
        }
    }

    /**
     * 月底自动给自己充电。仅充会到期的B币券，低于2的时候不会充
     * @author srcrs
     * @Time 2020-10-19
     */
    public void doCharge(int couponBalance) {
        /*
         * 判断条件 是月底&&b币券余额大于2&&配置项允许自动充电
         */
        if(couponBalance < 2){
            log.warn("【使用B币卷】: B币卷不足 2 个 ,无法给自己充电❌");
            return ;
        }
        /* 被充电用户的userID */
        String userId = userData.getMid();
        JSONObject pJson = new JSONObject();
        pJson.put("bp_num", couponBalance);
        pJson.put("is_bp_remains_prior","true");
        pJson.put("up_mid", userId);
        pJson.put("otype", "up");
        pJson.put("oid", userId);
        pJson.put("csrf", userData.getBiliJct());

        JSONObject jsonObject = Request.post("https://api.bilibili.com/x/ugcpay/web/v2/trade/elec/pay/quick", pJson);

        Integer resultCode = jsonObject.getInteger("code");
        if (resultCode == 0) {
            JSONObject dataJson = jsonObject.getJSONObject("data");
            log.debug(dataJson.toString());
            Integer statusCode = dataJson.getInteger("status");
            if (statusCode == 4) {
                log.info("【使用B币卷】: 本次给自己充值了: {}个电池✔", couponBalance * 10);
                /* 获取充电留言token */
                String orderNo = dataJson.getString("order_no");
                chargeComments(orderNo);
            } else {
                log.warn("【使用B币卷】: " + "充电失败, 原因为: {}❌", jsonObject);
            }
        } else {
            log.warn("【使用B币卷】: " + "充电失败, 原因为: {}❌", jsonObject);
        }
    }

    /**
     * 自动充电完，添加一条评论
     * @param token 订单id
     * @author srcrs
     * @Time 2020-10-19
     */
    public void chargeComments(String token) {
        JSONObject pJson = new JSONObject();
        pJson.put("order_id", token);
        pJson.put("message", "BilibiliTask自动充电");
        pJson.put("csrf", userData.getBiliJct());
        JSONObject jsonObject = Request.post("https://api.bilibili.com/x/ugcpay/trade/elec/message", pJson);
        log.debug(jsonObject.toString());
    }

    /**
     * 用 B 币卷兑换成金瓜子
     * @param couponBalance 传入 B 币卷的数量
     * @author srcrs
     * @Time 2020-11-02
     */
    public void doMelonSeed(int couponBalance){
        JSONObject pJson = new JSONObject();
        pJson.put("pay_bp", couponBalance * 1000);
        pJson.put("context_id", 1);
        pJson.put("context_type", 11);
        pJson.put("goods_id", 1);
        pJson.put("goods_num", couponBalance);
        pJson.put("csrf", userData.getBiliJct());
        JSONObject post = Request.post("https://api.live.bilibili.com/xlive/revenue/v1/order/createOrder", pJson);
        String msg ;
        /* json对象的状态码 */
        String code = post.getString("code");
        if(SUCCESS.equals(code)){
            msg = "成功将 " + couponBalance + " B币卷兑换成 " + couponBalance*1000 + " 金瓜子✔";
        } else{
            msg = post.getString("message") + "❌";
        }
        log.info("【使用B币卷】: {}", msg);
    }

}
