package top.srcrs.util;

import java.util.Arrays;

/**
 * StringUtil
 *
 * @author : Ali
 * @date : 2020/11/20
 */
public class StringUtil {

    private StringUtil(){}

    public static String get(Object o) {
        if (o == null) {
            return null;
        }

        return o.toString();
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    public static boolean isBlank(CharSequence cs) {
        return cs == null || "".contentEquals(cs);
    }

    public static boolean isAnyBlank(CharSequence ...cs) {
        return Arrays.stream(cs).anyMatch(StringUtil::isBlank);
    }

    public static String hideString(String str, int startLen, int endLen, char replaceChar)
    {
        int length = str.length() - startLen - endLen;
        String startStr = str.substring(0, startLen);
        String endStr = str.substring(str.length() - endLen);
        StringBuilder hideStr = new StringBuilder();
        length = Math.min(length, 3);
        while(length--!=0){
            hideStr.append(replaceChar);
        }
        return startStr + hideStr + endStr;
    }

}
