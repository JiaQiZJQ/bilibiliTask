package top.srcrs.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;

/**
 * 给钉钉机器人推送消息
 * @author srcrs
 * @Time 2020-11-16
 */
@Slf4j
public class SendDingTalk {

    private SendDingTalk(){}

    /**
     * 发送消息给用户，如果绑定了微信，会发送到微信上。
     * @param dingTalk 需要从钉钉群机器人获取
     * @author srcrs
     * @Time 2020-10-22
     */
    public static void send(String dingTalk){
        /* 将要推送的数据 */
        String desp = ReadLog.getMarkDownString("logs/logback.log");
        JSONObject markdownJson = new JSONObject();
        markdownJson.put("title", "BilibiliTask运行结果");
        markdownJson.put("text", desp);
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("msgtype", "markdown");
        bodyJson.put("markdown", markdownJson);
        HttpUriRequest httpPost = RequestBuilder.post()
                                                .addHeader("Content-Type", "application/json;charset=utf-8")
                                                .setUri(dingTalk)
                                                .setEntity(new StringEntity(bodyJson.toString(),"UTF-8"))
                                                .build();
        try(CloseableHttpClient client = HttpClients.createDefault()){
            HttpResponse resp = client.execute(httpPost);
            HttpEntity entity = resp.getEntity();
            String respContent = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            if(resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                log.info("【钉钉推送】: 正常✔");
            } else{
                log.info("【钉钉推送】: 失败, 原因为: {}❌", respContent);
            }
            System.out.println(respContent);
        } catch (Exception e){
            log.error("💔钉钉通知错误 : ", e);
        }
    }
}
