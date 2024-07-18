package top.srcrs.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
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
 * 给 Telegram 群组机器人推送消息
 * @author qiwihui
 * @Time 2020-12-16
 */
@Slf4j
public class SendTelegram {

    private SendTelegram(){}

    /**
     * 发送消息给群组
     * @param telegramBotToken 机器人 Token
     * @param telegramChatID 群组 ID
     * @author qiwihui
     * @Time 2020-12-16
     */
    public static void send(String telegramBotToken, String telegramChatID){
        /* 将要推送的数据 */
        String desp = ReadLog.getMarkDownString("logs/logback.log");
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("chat_id", telegramChatID);
        bodyJson.put("text", "BilibiliTask运行结果:\n" + desp);
        HttpUriRequest httpPost = RequestBuilder.post()
                .addHeader("Content-Type", "application/json;charset=utf-8")
                .setUri("https://api.telegram.org/bot"+telegramBotToken+"/sendMessage")
                .setEntity(new StringEntity(bodyJson.toString(),"UTF-8"))
                .build();
        try(CloseableHttpClient client = HttpClients.createDefault()){
            HttpResponse resp = client.execute(httpPost);
            HttpEntity entity = resp.getEntity();
            String respContent = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            if(resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                log.info("【Telegram推送】: 正常✔");
            } else{
                log.info("【Telegram推送】: 失败, 原因为: {}❌", respContent);
            }
            System.out.println(respContent);
        } catch (Exception e){
            log.error("💔Telegram通知错误 : ", e);
        }
    }
}
