package top.srcrs.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;

/**
 * 将日志消息发送到PUSHPLUS
 * pushplus.hxtrip.com
 *
 * @author sixer
 * @Time 2020-10-22
 */
@Slf4j
public class SendPushPlus {

    private SendPushPlus(){}

    /**
     * 发送消息给用户，如果绑定了微信，会发送到微信上。
     *
     * @param token push+的token
     * @author sixer
     * @Time 2020-10-22
     */
    public static void send(String token) {
        /* 将要推送的数据 */
        JSONObject pJson = new JSONObject();
        pJson.put("token", token);
        pJson.put("title", "BilibiliTask运行结果");
        pJson.put("content", ReadLog.getHTMLString("logs/logback.log"));
        /*
         * html	支持html文本。为空默认使用html模板(默认)
         * json	可视化展示json格式内容
         */

        /* pJson.put("template", "html"); */
        HttpUriRequest httpPost = RequestBuilder.get()
                                                .setUri("http://pushplus.hxtrip.com/send")
                                                .addParameters(Request.getPairList(pJson))
                                                .build();

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpResponse resp = client.execute(httpPost);
            HttpEntity entity = resp.getEntity();
            String respContent = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                log.info("【PUSH+推送】: 正常✔");
            } else {
                log.info("【PUSH+推送】: 失败, 原因为: {}❌", respContent);
            }
        } catch (Exception e) {
            log.error("💔PUSH+发送错误 : ", e);
        }
    }

}
