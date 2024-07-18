package top.srcrs.task.bigvip;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import top.srcrs.Task;
import top.srcrs.domain.UserData;
import top.srcrs.util.Request;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * 每个月 1 号，年度大会员领取 B 币卷，领取会员权益。
 * @author srcrs
 * @Time 2020-10-19
 */
@Slf4j
public class CollectVipGift implements Task {
    /** 获取DATA对象 */
    UserData userData = UserData.getInstance();

    /** 不是大会员 */
    private static final String NOT_VIP = "0";
    /** 是大会员 */
    private static final String IS_VIP = "1";
    /** 年度大会员 */
    private static final String YEAR_VIP = "2";

    @Override
    public void run() {
        try{
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
            int day = cal.get(Calendar.DATE);
            String vipType = queryVipStatusType();
            if(!(YEAR_VIP.equals(vipType))){
                log.info("【年度大会员领取福利】: " + "不是年度大会员,无法领取❌");
                return ;
            }
            /* 是年度大会员的朋友可以帮忙测一测
               有没有判断该用户是否领取了年度大会员权益
               我这现在只能给写死，每个月1号领取
            */
            if(day!=1){
                log.info("【年度大会员领取福利】: " + "今日不是月初(1号)❌");
                return;
            }
            /* 每个月1号，年度大会员领取权益 */
            vipPrivilege(1);
            vipPrivilege(2);

        } catch (Exception e){
            log.error("💔领取年度大会员礼包错误 : ", e);
        }
    }

    /**
     * 领取年度大会员B卷和大会员福利/权益
     * @param type [{1,领取大会员B币卷}, {2,领取大会员福利}]
     * @author srcrs
     * @Time 2020-10-19
     */
    public void vipPrivilege(Integer type) {
        JSONObject pJson = new JSONObject();
        pJson.put("type", type);
        pJson.put("csrf", userData.getBiliJct());
        JSONObject jsonObject = Request.post("https://api.bilibili.com/x/vip/privilege/receive", pJson);
        Integer code = jsonObject.getInteger("code");
        if (0 == code) {
            if (type == 1) {
                log.info("【领取年度大会员每月赠送的B币券】: 成功✔");
            } else if (type == 2) {
                log.info("【领取大会员福利/权益】: 成功✔");
            }

        } else {
            log.warn("【领取年度大会员每月赠送的B币券/大会员福利】: 失败, 原因: {}❌", jsonObject.getString("message"));
        }
    }

    /**
     * 检查用户的会员状态。如果是会员则返回其会员类型。
     * @return Integer
     * @author srcrs
     * @Time 2020-10-19
     */
    public String queryVipStatusType() {
        if (IS_VIP.equals(userData.getVipStatus())) {
            return userData.getVipType();
        } else {
            return NOT_VIP;
        }
    }
}
