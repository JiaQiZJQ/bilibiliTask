package top.srcrs;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import top.srcrs.domain.Config;
import top.srcrs.domain.UserData;
import top.srcrs.util.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * 启动类，程序运行开始的地方
 * @author srcrs
 * @Time 2020-10-13
 */
@Slf4j
public class BiliStart {
    /** 获取DATA对象 */
    private static final UserData USER_DATA = UserData.getInstance();
    /** 访问成功 */
    private static final String SUCCESS = "0";
    /** 账号未登录，即 Cookie 已失效 */
    private static final String NOT_LOGGED_IN = "-101";
    /** 获取Config配置的对象 */
    private static final Config CONFIG = Config.getInstance();
    public static void main(String ...args) {
        if(checkEnv()){
            log.error("💔请在Github Secrets中添加你的Cookie信息");
            return;
        }
        /* 读取yml文件配置信息 */
        ReadConfig.transformation("/config.yml");
        /* 如果用户账户有效 */
        if(check()){
            log.info("【用户名】: {}",StringUtil.hideString(USER_DATA.getUname(),1,1,'*'));
            log.info("【硬币】: {}", USER_DATA.getMoney());
            log.info("【经验】: {}", USER_DATA.getCurrentExp());
            /* 动态执行task包下的所有java代码 */
            scanTask();
            /* 当用户等级为Lv6时，升级到下一级 next_exp 值为 -- 代表无穷大 */
            String maxLevel = "6";
            if(maxLevel.equals(USER_DATA.getCurrentLevel())){
                log.info("【升级预计】: 当前等级为: Lv{} ,已经是最高等级", maxLevel);
                log.info("【温馨提示】: 可在配置文件中关闭每日投币操作");
            } else{
                log.info("【升级预计】: 当前等级为: Lv{} ,预计升级到下一级还需要: {} 天",
                        USER_DATA.getCurrentLevel(), getNextLevel());
            }
            log.info("本次任务运行完毕。");

        }

        // server酱
        if(StringUtil.isNotBlank(System.getenv("SCKEY"))){
            SendServer.send(System.getenv("SCKEY"));
        }
        // Server酱 测试号版
        if(StringUtil.isNotBlank(System.getenv("SENDKEY"))){
            SendServerChan.send(System.getenv("SENDKEY"));
        }
        // PUSHPLUSTK
        if(StringUtil.isNotBlank(System.getenv("PUSHPLUSTK"))){
            SendPushPlus.send(System.getenv("PUSHPLUSTK"));
        }
        /* 此时数组的长度为4，就默认填写的是填写的钉钉 webHook 链接 */
        if(StringUtil.isNotBlank(System.getenv("DINGTALK"))){
            SendDingTalk.send(System.getenv("DINGTALK"));
        }
        /* Telegram Bot推送 */
        if(StringUtil.isNotBlank(System.getenv("TELEGRAM_BOT_TOKEN"))
                && StringUtil.isNotBlank(System.getenv("TELEGRAM_CHAT_ID"))){
            SendTelegram.send(System.getenv("TELEGRAM_BOT_TOKEN"), System.getenv("TELEGRAM_CHAT_ID"));
        }
    }

    /**
     * 存储所有 class 全路径名
     * 因为测试的时候发现，在 windows 中是按照字典排序的
     * 但是在 Linux 中并不是字典排序我就很迷茫
     * 因为部分任务是需要有顺序的去执行
     */
    private static void scanTask() {
        List<Class<?>> clazzList = new ArrayList<>();
        TaskRegistry pack = new TaskRegistry() {
            @Override
            public void dealClass(String className) {
                try{
                    Class<?> clazz = Class.forName(className);
                    // 判断类是否实现了接口Task
                    if (Arrays.stream(clazz.getInterfaces()).parallel().anyMatch(taskI -> taskI.equals(Task.class))) {
                        clazzList.add(clazz);
                    }
                } catch (Exception e){
                    log.error("💔反射获取对象错误 : ", e);
                }
            }
        };
        pack.runTasks();

        clazzList.stream().sorted(Comparator.comparing(Class::getName)).forEach(clazz -> {
            try {
                Constructor<?> constructor = clazz.getConstructor();
                Object object = constructor.newInstance();
                Method method = object.getClass().getMethod("run");
                method.invoke(object);
            } catch (Exception e){
                log.error("💔反射获取对象错误 : ", e);
            }
        });
    }

    public static boolean checkEnv() {
        String BILI_JCT = System.getenv("BILI_JCT");
        String SESSDATA = System.getenv("SESSDATA");
        String DEDEUSERID = System.getenv("DEDEUSERID");
        USER_DATA.setCookie(BILI_JCT, SESSDATA, DEDEUSERID);
        return StringUtil.isAnyBlank(BILI_JCT, SESSDATA, DEDEUSERID);
    }

    /**
     * 检查用户的状态
     * @return boolean
     * @author srcrs
     * @Time 2020-10-13
     */
    public static boolean check(){
        Request.UserAgent = InitUserAgent.getOne();
        JSONObject jsonObject = Request.get("https://api.bilibili.com/x/web-interface/nav");
        JSONObject object = jsonObject.getJSONObject("data");
        String code = jsonObject.getString("code");
        if(SUCCESS.equals(code)){
            JSONObject levelInfo = object.getJSONObject("level_info");
            /* 用户名 */
            USER_DATA.setUname(object.getString("uname"));
            /* 账户的uid */
            USER_DATA.setMid(object.getString("mid"));
            /* vip类型 */
            USER_DATA.setVipType(object.getString("vipType"));
            /* 硬币数 */
            USER_DATA.setMoney(object.getBigDecimal("money"));
            /* 经验 */
            USER_DATA.setCurrentExp(levelInfo.getIntValue("current_exp"));
            /* 大会员状态 */
            USER_DATA.setVipStatus(object.getString("vipStatus"));
            /* 钱包B币卷余额 */
            USER_DATA.setCouponBalance(object.getJSONObject("wallet").getIntValue("coupon_balance"));
            /* 升级到下一级所需要的经验 */
            USER_DATA.setNextExp(levelInfo.getString("next_exp"));
            /* 获取当前的等级 */
            USER_DATA.setCurrentLevel(levelInfo.getString("current_level"));
            return true;
        }
        if(NOT_LOGGED_IN.equals(code)){
            log.info("💔账户已失效，请在Secrets重新绑定你的信息");
            return false;
        }
        return false;
    }

    /**
     * 计算到下一级所需要的天数
     * 由于风控抓的紧，为减少相关 api 的请求次数，会有一天的误差
     * @return int 距离升级到下一等级还需要几天
     * @author srcrs
     * @Time 2020-11-17
     */
    private static int getNextLevel(){
        /* 当前经验数 */
        int currentExp = USER_DATA.getCurrentExp();
        /* 到达下一级所需要的经验数 */
        int nextExp = Integer.parseInt(USER_DATA.getNextExp());
        /* 获取当前硬币数量 */
        int num1 = USER_DATA.getMoney().intValue();
        /* 获取配置中每日投币数量 */
        int num2 = CONFIG.getCoin();
        /* 避免投币数设置成负数异常 */
        num2 = Math.max(num2,0);
        /* 实际每日能需要投币数 */
        int num = Math.min(num1,num2);
        /* 距离升级到下一级所需要的天数 */
        int nextNum = 0;
        while(currentExp < nextExp){
            nextNum += 1;
            num1 += 1;
            currentExp += (15+num*10);
            num1 -= num;
            num = Math.min(num1,num2);
        }
        return nextNum;
    }
}
