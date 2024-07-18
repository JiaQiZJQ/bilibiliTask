package top.srcrs.util;

import org.junit.jupiter.api.Test;

/**
 * ...
 *
 * @author : Ali
 * @date : 2020/11/21
 */
class SendTest {

    @Test
    void SendServerTest() {
        if(StringUtil.isNotBlank(System.getenv("SCKEY"))){
            SendServer.send(System.getenv("SCKEY"));
        }
    }

    @Test
    void SendServerChanTest() {
        if(StringUtil.isNotBlank(System.getenv("SENDKEY"))){
            SendServerChan.send(System.getenv("SENDKEY"));
        }
    }

    @Test
    void SendPushPlusTest() {
        if(StringUtil.isNotBlank(System.getenv("PUSHPLUSTK"))){
            SendPushPlus.send(System.getenv("PUSHPLUSTK"));
        }
    }

    @Test
    void SendTgBotTest() {
        if(StringUtil.isNotBlank(System.getenv("TGBOT"))){
            SendTelegram.send(System.getenv("TELEGRAM_BOT_TOKEN"),System.getenv("TELEGRAM_CHAT_ID"));
        }
    }

}
