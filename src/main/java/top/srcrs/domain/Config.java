package top.srcrs.domain;

import java.util.List;

/**
 * 项目的配置类。
 * @author srcrs
 * @Time 2020-10-13
 */
public class Config {
    private static final Config CONFIG = new Config();

    public static Config getInstance(){
        return CONFIG;
    }
    private Config(){}

    /** 代表所需要投币的数量 */
    private static Integer coin;
    /** 送出即将过期的礼物 true 默认送出*/
    private static boolean gift;
    /** 要将银瓜子转换成硬币 true 默认转换*/
    private static boolean s2c;
    /** 自动使用 B 币卷 */
    private static String autoBiCoin;
    /** 用户设备的标识 */
    private static String platform;
    /** 投币给自定义的 up 主 */
    private static List<String> upList;
    /** 自动进行漫画签到任务 */
    private static boolean manga;
    /** 送出即将过期礼物给此 up 的直播间 */
    private static String upLive;
    /** 对于进行投币的视频选择是否点赞 */
    private static String selectLike;

    public String getSelectLike() {
        return selectLike;
    }

    public void setSelectLike(String selectLike) {
        Config.selectLike = selectLike;
    }

    public String getUpLive() {
        return upLive;
    }

    public void setUpLive(String upLive) {
        Config.upLive = upLive;
    }

    public boolean isManga() {
        return manga;
    }

    public void setManga(boolean manga) {
        Config.manga = manga;
    }

    public List<String> getUpList() {
        return upList;
    }

    public void setUpList(List<String> upList) {
        Config.upList = upList;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        Config.platform = platform;
    }

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
