package com.fangdeng.server.util;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;




@Slf4j
public class OkHttpUtil {

    private static final OkHttpClient client = new OkHttpClient();
    private static Integer connectTimeout = 3000;//默认链接超时3000毫秒
    private static Integer readTimeout = 2000;//默认读超时2000毫秒
    private static Integer writeTimeout = 1000;//默认写超时1000毫秒

    private static final MediaType jsonMediaType =  MediaType.parse("application/json; charset=utf-8");
    private static final MediaType xmlMediaType =  MediaType.parse("application/xml; charset=utf-8");


    /**
     * execute
     * @param request
     * @return
     */
    public static String execute(Request request){
        try {
            return client.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 带超时时间
     * @param request
     * @param connectTimeout
     * @param readTimeout
     * @param writeTimeout
     * @return
     */
    public static String executeWithTimeout(Request request, Integer connectTimeout, Integer readTimeout, Integer writeTimeout){
        try {
            OkHttpClient.Builder copy = client.newBuilder();
            if(connectTimeout != null) {
                copy.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
            }
            if(writeTimeout != null) {
                copy.writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);
            }
            if(readTimeout != null) {
                copy.readTimeout(readTimeout, TimeUnit.MILLISECONDS);
            }

            return copy.build().newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * executeAsync
     * 异步请求
     * @param request
     */
    public static void executeAsync(Request request){
        try {

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    //暂不做处理
                }

                @Override
                public void onFailure(Call call, IOException e) {

                    log.error("调用失败");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * post
     * @param url
     * @param body
     * @return
     */
    public static String post(String url, RequestBody body){
        return execute(new Request.Builder().url(url).post(body).build());
    }

    public static void postAsync(String url, RequestBody body){
        executeAsync(new Request.Builder().url(url).post(body).build());
    }

    /**
     * post
     * @param url
     * @param formMap
     * @return
     */
    public static String post(String url, Map<String, Object> formMap){
        FormBody.Builder form = new FormBody.Builder();
        if (formMap!=null && !formMap.isEmpty()) {
            for(String key : formMap.keySet()){
                form.add(key, formMap.get(key).toString());
            }
            return post(url, form.build());
        }
        return null;
    }

    /**
     * post
     * @param url
     * @param body
     * @param mediaType
     * @return
     */
    public static String post(String url, String body, MediaType mediaType) {
        return post(url, RequestBody.create(mediaType, body));
    }

    public static void postAsync(String url, String body, MediaType mediaType) {
        postAsync(url, RequestBody.create(mediaType, body));
    }

    /**
     * post (json)
     * @param url
     * @return
     */
    public static String postJson(String url, String body) {
        return post(url, body, jsonMediaType);
    }

    /**
     * @param url
     * @param body
     * @param connectTimeout 连接超时，单位毫秒
     * @param readTimeout 读超时，单位毫秒
     * @param writeTimeout 写超时，单位毫秒
     * @return
     */
    public static String postJsonWithTimeout(String url, String body,
                                             Integer connectTimeout,Integer readTimeout,Integer writeTimeout) {
        RequestBody requestBody = RequestBody.create(jsonMediaType, body);
        return executeWithTimeout(new Request.Builder().url(url).post(requestBody).build(), connectTimeout, readTimeout, writeTimeout);
    }

    /**
     * 带超时时间请求
     * @param url
     * @param body
     * @return
     */
    public static String postJsonWithFixTimeout(String url, String body) {
        return postJsonWithTimeout(url, body, connectTimeout, readTimeout, writeTimeout);
    }

    /**
     * 异步请求，不做回调处理
     * @param url
     * @param body
     * @return
     */
    public static void postJsonAsync(String url, String body) {
        postAsync(url, body, jsonMediaType);
    }


    /**
     * post (xml)
     * @param url
     * @param body
     * @return
     */
    public static String postXml(String url, String body) {
        return post(url, body, xmlMediaType);
    }

}