package top.srcrs.util;

import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;
import top.srcrs.domain.Config;

/**
 * 读取yml配置
 * @author srcrs
 * @Time 2020-10-13
 */
@Slf4j
public class ReadConfig {

    private ReadConfig(){}

    /**
     * 将yml的配置映射到Config.java中
     * @author srcrs
     * @Time 2020-10-13
     */
    public static void transformation(String file){
        try{
            Yaml yaml = new Yaml();
            yaml.loadAs(ReadConfig.class.getResourceAsStream(file), Config.class);
        } catch (Exception e){
            log.info("💔配置文件转换成对象出错 : ", e);
        }
    }

}
