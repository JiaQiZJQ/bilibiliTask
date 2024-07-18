package top.srcrs.task.manga;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import top.srcrs.Task;
import top.srcrs.domain.Config;
import top.srcrs.util.Request;

/**
 * 完成漫画任务，暂时只实现了签到
 * @author srcrs
 * @Time 2020-10-13
 */
@Slf4j
public class MangaTask implements Task {
    Config config = Config.getInstance();
    @Override
    public void run(){
        if(!config.isManga()){
            log.info("【漫画签到】: 自定义配置不执行漫画签到任务✔");
            return ;
        }
        try{
            JSONObject jsonObject = mangaClockIn(config.getPlatform());
            log.info("【漫画签到设备信息】: {}", config.getPlatform());
            log.info("【漫画签到】: {}","0".equals(jsonObject.getString("code"))?"成功✔":"今天已经签过了❌");
        } catch (Exception e){
            log.error("💔漫画签到错误 : ", e);
        }
    }

    /**
     * 模拟漫画app签到
     * @param platform 设备标识
     * @return JSONObject
     * @author srcrs
     * @Time 2020-10-13
     */
    public JSONObject mangaClockIn(String platform){
        JSONObject pJson = new JSONObject();
        pJson.put("platform", platform);
        return Request.post("https://manga.bilibili.com/twirp/activity.v1.Activity/ClockIn", pJson);
    }
}
