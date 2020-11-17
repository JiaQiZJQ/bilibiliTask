package top.srcrs.domain;

/**
 * 项目的配置类。
 * @author srcrs
 * @Time 2020-10-13
 */
public class Config {
    private static final Config CONFIG = new Config();
    /** 代表所需要投币的数量 */
    static private Integer coin;
    /** 送出即将过期的礼物 true 默认送出*/
    static private boolean gift;
    /** 要将银瓜子转换成硬币 true 默认转换*/
    static private boolean s2c;
    /** 自动使用B币卷 */
    static private String autoBiCoin;
    /** 用户设备的标识 */
    static private String platform;

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        Config.platform = platform;
    }

    public static Config getInstance(){
        return CONFIG;
    }
    private Config(){}

    public String getAutoBiCoin() {
        return autoBiCoin;
    }

    public void setAutoBiCoin(String autoBiCoin) {
        Config.autoBiCoin = autoBiCoin;
    }

    public Integer getCoin() {
        return coin;
    }

    public void setCoin(Integer coin) {
        Config.coin = coin;
    }

    public boolean isGift() {
        return gift;
    }

    public void setGift(boolean gift) {
        Config.gift = gift;
    }

    public boolean isS2c() {
        return s2c;
    }

    public void setS2c(boolean s2c) {
        Config.s2c = s2c;
    }

}
