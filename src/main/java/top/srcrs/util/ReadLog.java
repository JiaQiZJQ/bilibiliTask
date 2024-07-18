package top.srcrs.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * 读取日志配置文件
 * @author srcrs
 * @Time 2020-11-16
 */
@Slf4j
public class ReadLog {

    private ReadLog(){}

    /**
     * 读取输出到文件中的日志
     * @param pathName 日志文件的名字，包括路径
     * @return String 将日志拼接成了字符串
     * @author srcrs
     * @Time 2020-10-22
     */
    public static String getString(String pathName, String suffix){
        /* str代表要发送的数据 */
        StringBuilder str = new StringBuilder();
        try(FileReader reader = new FileReader(pathName);
            BufferedReader br = new BufferedReader(reader)){
            while (br.ready()){
                str.append(br.readLine()).append(suffix);
            }
        } catch (Exception e){
            log.error("💔读日志文件时出错 : ", e);
        }
        return str.toString();
    }

    public static String getMarkDownString(String pathName) {
        return getString(pathName, "\n\n");
    }

    public static String getHTMLString(String pathName) {
        return getString(pathName, "<br />");
    }
}
