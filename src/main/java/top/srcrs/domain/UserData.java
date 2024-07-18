package top.srcrs.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 用户的一些个人信息
 * @author srcrs
 * @Time 2020-10-13
 */
@Data
public class UserData {
    private static final UserData USER_DATA = new UserData();

    private UserData(){}
    public static UserData getInstance(){
        return USER_DATA;
    }

    public void setCookie(String biliJct, String SESSDATA, String DedeUserID) {
        this.DedeUserID = DedeUserID;
        this.biliJct = biliJct;
        this.SESSDATA = SESSDATA;
    }

    /**
     * 返回用户的Cookie
     * @return String
     */
    public String getCookie(){
        return "bili_jct="+biliJct+";SESSDATA="+SESSDATA+";DedeUserID="+DedeUserID+";";
    }

    private String DedeUserID;
    private String biliJct;
    private String SESSDATA;
    /** 登录账户的用户名 */
    private String uname;
    /** 登录账户的uid */
    private String mid;
    /** 代表账户的类型 */
    private String vipType;
    /** 硬币数 */
    private BigDecimal money;
    /** 经验数 */
    private Integer currentExp;
    /** 大会员状态 */
    private String vipStatus;
    /** B币卷余额 */
    private Integer couponBalance;
    /** 当前等级 */
    private String currentLevel;
    /** 距离升级到下一级所需要的经验 */
    private String nextExp;

}
