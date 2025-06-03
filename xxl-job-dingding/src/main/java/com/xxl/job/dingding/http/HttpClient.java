package com.xxl.job.dingding.http;


import com.alibaba.fastjson2.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HttpClient {


//    public static String get(String url) {
//        HttpGet httpPost = new HttpGet(url);
//        CloseableHttpClient client = HttpClients.createDefault();
//        String respContent = null;
//        HttpResponse resp;
//        try {
//            resp = client.execute(httpPost);
//            if (resp.getStatusLine().getStatusCode() == 200) {
//                HttpEntity he = resp.getEntity();
//                respContent = EntityUtils.toString(he, "UTF-8");
//            }
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        return respContent;
//    }

    /**
     * post请求
     *
     * @param url            url地址
     * @param jsonParam      参数
     * @param noNeedResponse 不需要返回结果
     * @return
     */
    public static JSONObject httpJsonPost(String url, JSONObject jsonParam, boolean noNeedResponse) throws IOException {
        //post请求返回结果
        JSONObject jsonResult = null;
        // 使用 CloseableHttpClient 替代 DefaultHttpClient
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost method = new HttpPost(url);

            if (null != jsonParam) {
                //解决中文乱码问题
                try {
                    StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
                    entity.setContentEncoding("UTF-8");
                    entity.setContentType("application/json");
                    method.setEntity(entity);
                    HttpResponse result = httpClient.execute(method);
                    url = URLDecoder.decode(url, "UTF-8");
                    /**请求发送成功，并得到响应**/
                    if (result.getStatusLine().getStatusCode() == 200) {
                        String str = "";
                        try {
                            /**读取服务器返回过来的json字符串数据**/
                            str = EntityUtils.toString(result.getEntity());
                            if (noNeedResponse) {
                                return null;
                            }
                            /**把json字符串转换成json对象**/
                            jsonResult = JSONObject.parseObject(str);
                        } catch (Exception e) {
                            System.out.println("post请求提交失败");
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }
        return jsonResult;
    }
}
