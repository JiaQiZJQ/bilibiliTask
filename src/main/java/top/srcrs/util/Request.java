package top.srcrs.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import top.srcrs.domain.UserData;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Random;

/**
 * 封装的网络请求请求工具类
 *
 * @author srcrs
 * @Time 2020-10-13
 */
@Slf4j
public class Request {
    /**
     * 获取data对象
     */
    private static final UserData USER_DATA = UserData.getInstance();

    public static String UserAgent = "";

    private Request() {}

    /**
     * 发送get请求
     *
     * @param url 请求的地址，包括参数
     * @param pJson 携带的参数
     * @return JSONObject
     * @author srcrs
     * @Time 2020-10-13
     */
    public static JSONObject get(String url, JSONObject pJson) {
        waitFor();
        HttpUriRequest httpGet = getBaseBuilder(HttpGet.METHOD_NAME)
                .setUri(url)
                .addParameters(getPairList(pJson))
                .build();
        return clientExe(httpGet);
    }

    /**
     * 发送get请求
     *
     * @param url 请求的地址，包括参数
     * @return JSONObject
     * @author srcrs
     * @Time 2020-10-13
     */
    public static JSONObject get(String url) {
        waitFor();
        return get(url, new JSONObject());
    }

    /**
     * 发送post请求
     *
     * @param url  请求的地址
     * @param pJson 携带的参数
     * @return JSONObject
     * @author srcrs
     * @Time 2020-10-13
     */
    public static JSONObject post(String url, JSONObject pJson) {
        waitFor();
        HttpUriRequest httpPost = getBaseBuilder(HttpPost.METHOD_NAME)
                .addHeader("accept", "application/json, text/plain, */*")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("charset", "UTF-8")
                .setUri(url)
                .addParameters(getPairList(pJson))
                .build();
        return clientExe(httpPost);
    }

    private static RequestBuilder getBaseBuilder(final String method) {
        return RequestBuilder.create(method)
                             .addHeader("connection", "keep-alive")
                             .addHeader("referer", "https://www.bilibili.com/")
                             .addHeader("User-Agent", UserAgent)
                             .addHeader("Cookie", USER_DATA.getCookie());
    }

    public static NameValuePair[] getPairList(JSONObject pJson) {
        return pJson.entrySet().parallelStream().map(Request::getNameValuePair).toArray(NameValuePair[]::new);
    }

    private static NameValuePair getNameValuePair(Map.Entry<String, Object> entry) {
        return new BasicNameValuePair(entry.getKey(), StringUtil.get(entry.getValue()));
    }

    public static JSONObject clientExe(HttpUriRequest request) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpResponse resp = client.execute(request);
            HttpEntity entity = resp.getEntity();
            String respContent = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            return JSON.parseObject(respContent);
        } catch (Exception e) {
            log.info("💔{}请求错误 : ", request.getMethod(), e);
            return new JSONObject();
        }
    }

    /**
     * 增加等待时间，解决风控问题
     * 暂时先设置为每次请求预等待 0-3 秒钟
     * @author srcrs
     * @Time 2020-11-28
     */
    public static void waitFor() {
        try{
            Thread.sleep(new Random().nextInt(4)*1000);
        } catch (Exception e){
            log.warn("等待过程中出错",e);
        }
    }
}
