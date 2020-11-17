package top.srcrs.domain;

/**
 * 用户的一些个人信息
 * @author srcrs
 * @Time 2020-10-13
 */
public class Data {
    private static final Data DATA = new Data();

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
    private String money;
    /** 经验数 */
    private String currentExp;
    /** 大会员状态 */
    private String vipStatus;
    /** B币卷余额 */
    private String couponBalance;

    public void setCookie(String biliJct, String SESSDATA, String DedeUserID) {
        this.DedeUserID = DedeUserID;
        this.biliJct = biliJct;
        this.SESSDATA = SESSDATA;
    }

    public String getVipStatus() {
        return vipStatus;
    }

    public String getCouponBalance() {
        return couponBalance;
    }

    public void setCouponBalance(String couponBalance) {
        this.couponBalance = couponBalance;
    }

    public void setVipStatus(String vipStatus) {
        this.vipStatus = vipStatus;
    }

    public String getCurrentExp() {
        return currentExp;
    }

    public void setCurrentExp(String currentExp) {
        this.currentExp = currentExp;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getBiliJct() {
        return biliJct;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getVipType() {
        return vipType;
    }

    public void setVipType(String vipType) {
        this.vipType = vipType;
    }

    /**
     * 返回用户的Cookie
     * @return String
     */
    public String getCookie(){
        return "bili_jct="+biliJct+";SESSDATA="+SESSDATA+";DedeUserID="+DedeUserID;
    }
    private Data(){}
    public static Data getInstance(){
        return DATA;
    }
}
