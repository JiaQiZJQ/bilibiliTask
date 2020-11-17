package top.srcrs;

import com.alibaba.fastjson.JSONObject;
import top.srcrs.domain.Data;
import top.srcrs.util.PackageScanner;
import top.srcrs.util.ReadConfig;
import top.srcrs.util.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.srcrs.util.SendServer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 启动类，程序运行开始的地方
 * @author srcrs
 * @Time 2020-10-13
 */
public class BiliStart {
    /** 获取日志记录器对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(BiliStart.class);
    /** 获取DATA对象 */
    private static final Data DATA = Data.getInstance();
    /** 访问成功 */
    private static final String SUCCESS = "0";

    public static void main(String[] args) {

        /*
         * 存储所有 class 全路径名
         * 因为测试的时候发现，在 windows 中是按照字典排序的
         * 但是在 Linux 中并不是字典排序我就很迷茫
         * 因为部分任务是需要有顺序的去执行
         */
        final List<String> list = new ArrayList<>();
        String[] arry = {"61704779c6d18f2f23cdebdedcf4ffe9","1caa312e%2C1614582055%2Cc5871*91","280846108"};
        if(arry.length==0){
            LOGGER.error("请在Github Secrets中添加你的Cookie信息");
        }
        DATA.setCookie(arry[0],arry[1],arry[2]);
        /* 读取yml文件配置信息 */
        ReadConfig.transformation("/config.yml");
        /* 如果用户账户有效 */
        if(check()){
            LOGGER.info("用户名: {}",DATA.getUname());
            LOGGER.info("硬币: {}",DATA.getMoney());
            LOGGER.info("经验: {}",DATA.getCurrentExp());
            PackageScanner pack = new PackageScanner() {
                @Override
                public void dealClass(Class<?> klass) {
                    try{
                        list.add(klass.getName());
                    } catch (Exception e){
                        LOGGER.error("扫描class目录出错 -- "+e);
                    }
                }
            };
            /* 动态执行task包下的所有java代码 */
            pack.scannerPackage("top.srcrs.task");
            Collections.sort(list);
            for(String s : list){
                try{
                    Object object = Class.forName(s).newInstance();
                    Method method = object.getClass().getMethod("run", (Class<?>[]) null);
                    method.invoke(object);
                } catch (Exception e){
                    LOGGER.error("反射获取对象错误 -- "+e);
                }
            }
            LOGGER.info("本次任务运行完毕。");
            /* 如果用户填了server酱的SCKEY就会执行 */
            /* 此时数组的长度为4，就默认填写的是SCKEY */
            if(arry.length==4){
                SendServer.send(arry[3]);
            }
        } else {
            throw  new RuntimeException("账户已失效，请在Secrets重新绑定你的信息");
        }
    }

    /**
     * 检查用户的状态
     * @return boolean
     * @author srcrs
     * @Time 2020-10-13
     */
    public static boolean check(){
        JSONObject jsonObject = Request.get("https://api.bilibili.com/x/web-interface/nav");
        JSONObject object = jsonObject.getJSONObject("data");
        String code = jsonObject.getString("code");
        if(SUCCESS.equals(code)){
            /* 用户名 */
            DATA.setUname(object.getString("uname"));
            /* 账户的uid */
            DATA.setMid(object.getString("mid"));
            /* vip类型 */
            DATA.setVipType(object.getString("vipType"));
            /* 硬币数 */
            DATA.setMoney(object.getString("money"));
            /* 经验 */
            DATA.setCurrentExp(object.getJSONObject("level_info").getString("current_exp"));
            /* 大会员状态 */
            DATA.setVipStatus(object.getString("vipStatus"));
            /* 钱包B币卷余额 */
            DATA.setCouponBalance(object.getJSONObject("wallet").getString("coupon_balance"));
            return true;
        }
        return false;
    }
}
