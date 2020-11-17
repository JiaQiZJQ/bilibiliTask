package top.srcrs.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.srcrs.domain.Data;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * 封装的网络请求请求工具类
 * @author srcrs
 * @Time 2020-10-13
 */
public class Request {
    /** 获取data对象 */
    private static final Data DATA = Data.getInstance();
    /** 获取日志记录器对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(Request.class);

    private Request(){}

    /**
     * 发送get请求
     * @param url 请求的地址，包括参数
     * @return JSONObject
     * @author srcrs
     * @Time 2020-10-13
     */
    public static JSONObject get(String url){
        HttpClient client = createSSLClientDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("connection","keep-alive");
        httpGet.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36");
        httpGet.addHeader("referer","https://www.bilibili.com/");
        httpGet.addHeader("Cookie",DATA.getCookie());
//        HttpHost httpHost = new HttpHost("10.8.9.136",3128);
//        RequestConfig build = RequestConfig.custom().setProxy(httpHost).build();
//        httpGet.setConfig(build);
        HttpResponse resp ;
        String respContent = null;
        try{
            resp = client.execute(httpGet);
            HttpEntity entity = resp.getEntity();
            respContent = EntityUtils.toString(entity, "UTF-8");
            return JSONObject.parseObject(respContent);
        } catch (Exception e){
            e.printStackTrace();
            LOGGER.info("get请求错误 -- "+e);
            return JSONObject.parseObject(respContent);
        }
    }

    /**
     * 发送post请求
     * @param url 请求的地址
     * @param body 携带的参数
     * @return JSONObject
     * @author srcrs
     * @Time 2020-10-13
     */
    public static JSONObject post(String url , String body){
        StringEntity entityBody = new StringEntity(body,"UTF-8");
        HttpClient client = createSSLClientDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("connection","keep-alive");
        httpPost.addHeader("referer","https://www.bilibili.com/");
        httpPost.addHeader("accept","application/json, text/plain, */*");
        httpPost.addHeader("Content-Type","application/x-www-form-urlencoded");
        httpPost.addHeader("charset","UTF-8");
        httpPost.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36");
        httpPost.addHeader("Cookie",DATA.getCookie());
        httpPost.setEntity(entityBody);
//        HttpHost httpHost = new HttpHost("10.8.9.136",3128);
//        RequestConfig build = RequestConfig.custom().setProxy(httpHost).build();
//        httpPost.setConfig(build);
        HttpResponse resp ;
        String respContent = null;
        try{
            resp = client.execute(httpPost);
            HttpEntity entity;
            entity = resp.getEntity();
            respContent = EntityUtils.toString(entity, "UTF-8");
            return JSONObject.parseObject(respContent);
        } catch (Exception e){
            LOGGER.info("post请求错误 -- "+e);
            return JSONObject.parseObject(respContent);
        }
    }

    public static CloseableHttpClient createSSLClientDefault() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                // 信任所有
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();

    }

}
