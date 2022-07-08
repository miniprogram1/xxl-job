package com.xxl.job.dingding.http;

import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
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

    /**
     * GET请求共通方法
     *
     * @param httpUrl :请求接口
     * @return 返回结果
     */
    public static JSONObject requestGet(String httpUrl) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSONObject.fromObject(result);
    }

    /**
     * POST请求共通方法(适合参数字段长的请求)
     *
     * @param httpUrl 请求地址
     * @param httpArg 请求参数
     * @return appKey,invalidTime,sign
     */
    public static JSONObject requestPost(String httpUrl, String httpArg, String appKey, String invalidTime, String sign) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/json;charset=UTF-8");
            connection.setRequestProperty("appKey",appKey);
            connection.setRequestProperty("invalidTime",invalidTime);
            connection.setRequestProperty("sign",sign);
            connection.setDoOutput(true);
            connection.getOutputStream().write(httpArg.getBytes("UTF-8"));
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSONObject.fromObject(result);
    }


    /**
     * post请求(用于key-value格式的参数)
     *
     * @param url
     * @param params
     * @return
     */
    public static String doPost(String url, Map params) {

        BufferedReader in = null;
        try {
            // 定义HttpClient
            org.apache.http.client.HttpClient client = new DefaultHttpClient();
            // 实例化HTTP方法
            HttpPost request = new HttpPost();
            request.setURI(new URI(url));

            //设置参数
            List<NameValuePair> nvps = new ArrayList<>();
            for (Iterator iter = params.keySet().iterator(); iter.hasNext(); ) {
                String name = (String) iter.next();
                String value = String.valueOf(params.get(name));
                nvps.add(new BasicNameValuePair(name, value));

                //System.out.println(name +"-"+value);
            }
            request.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

            HttpResponse response = client.execute(request);
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {    //请求成功
                in = new BufferedReader(new InputStreamReader(response.getEntity()
                        .getContent(), "utf-8"));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                String NL = System.getProperty("line.separator");
                while ((line = in.readLine()) != null) {
                    sb.append(line + NL);
                }

                in.close();

                return sb.toString();
            } else {   //
                System.out.println("状态码：" + code);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static String get(String url) {
        HttpGet httpPost = new HttpGet(url);
        CloseableHttpClient client = HttpClients.createDefault();
        String respContent = null;
        HttpResponse resp;
        try {
            resp = client.execute(httpPost);
            if (resp.getStatusLine().getStatusCode() == 200) {
                HttpEntity he = resp.getEntity();
                respContent = EntityUtils.toString(he, "UTF-8");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return respContent;
    }

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
        DefaultHttpClient httpClient = new DefaultHttpClient();
        JSONObject jsonResult = null;
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
                        jsonResult = JSONObject.fromObject(str);
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
        return jsonResult;
    }

}