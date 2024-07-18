package top.srcrs.util;

import java.util.Random;

/**
 * 随机生成 UserAgent
 * @author srcrs
 * @Time 2020-11-30
 */
public class InitUserAgent {

    /**
     * 得到一个 UserAgent
     * @return 一个 UserAgent
     * @author srcrs
     * @Time 2020-11-30
     */
    public static String getOne(){
        int firstNum = new Random().nextInt(30)+55;
        int thirdNum = new Random().nextInt(3200);
        int fourthNum = new Random().nextInt(140);
        String[] osType = {
                "(Windows NT 6.1; WOW64)",
                "(Windows NT 10.0; WOW64)",
                "(X11; Linux x86_64)",
                "(Macintosh; Intel Mac OS X 10_12_6)"};
        int index = new Random().nextInt(osType.length);
        String chromeVersion = "Chrome/" + firstNum+ ".0." + thirdNum + "." + fourthNum;
        return "Mozilla/5.0 "
                + osType[index]
                + " AppleWebKit/537.36"
                + " (KHTML, like Gecko) "
                + chromeVersion
                + " Safari/537.36";
    }
}
