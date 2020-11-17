package top.srcrs.task.daily;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.srcrs.Task;
import top.srcrs.domain.Data;
import top.srcrs.util.Request;

/**
 * 完成B站每日任务，观看，分享视频
 * @author srcrs
 * @Time 2020-10-13
 */
public class DailyTask implements Task {
    /** 获取日志记录器对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(DailyTask.class);
    /** 获取DATA对象 */
    Data data = Data.getInstance();

    @Override
    public void run() {
        try {
            JSONArray regions = getRegions("6", "1");
            JSONObject report = report(regions.getJSONObject(5).getString("aid"),
                    regions.getJSONObject(5).getString("cid"), "300");
            LOGGER.info("模拟观看视频 -- {}", "0".equals(report.getString("code")) ? "成功" : "失败");
            JSONObject share = share(regions.getJSONObject(5).getString("aid"));
            LOGGER.info("分享视频 -- {}", "0".equals(share.getString("code")) ? "成功" : "失败");
        } catch (Exception e) {
            LOGGER.error("每日任务异常 -- " + e);
        }
    }

    /**
     * 获取B站推荐视频
     *
     * @param ps  代表你要获得几个视频
     * @param rid B站分区推荐视频
     * @return JSONArray
     * @author srcrs
     * @Time 2020-10-13
     */
    public JSONArray getRegions(String ps, String rid) {
        String params = "?ps=" + ps + "&rid=" + rid;
        JSONObject jsonObject = Request.get("https://api.bilibili.com/x/web-interface/dynamic/region" + params);
        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("archives");
        JSONArray jsonRegions = new JSONArray();
        for (Object object : jsonArray) {
            JSONObject json = (JSONObject) object;
            JSONObject cache = new JSONObject();
            cache.put("title", json.getString("title"));
            cache.put("aid", json.getString("aid"));
            cache.put("bvid", json.getString("bvid"));
            cache.put("cid", json.getString("cid"));
            jsonRegions.add(cache);
        }
        return jsonRegions;
    }

    /**
     * 模拟观看视频
     * @param aid     视频 aid 号
     * @param cid     视频 cid 号
     * @param progres 模拟观看的时间
     * @return JSONObject
     * @author srcrs
     * @Time 2020-10-13
     */
    public JSONObject report(String aid, String cid, String progres) {
        String body = "aid=" + aid
                + "&cid=" + cid
                + "&progres=" + progres
                + "&csrf=" + data.getBiliJct();
        return Request.post("http://api.bilibili.com/x/v2/history/report", body);
    }

    /**
     * 分享指定的视频
     *
     * @param aid 视频的aid
     * @return JSONObject
     * @author srcrs
     * @Time 2020-10-13
     */
    public JSONObject share(String aid) {
        String body = "aid=" + aid + "&csrf=" + data.getBiliJct();
        return Request.post("https://api.bilibili.com/x/web-interface/share/add", body);
    }
}
