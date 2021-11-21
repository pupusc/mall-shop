/*
 * Copyright 2013 NINGPAI, Inc.All rights reserved.
 * NINGPAI PROPRIETARY / CONFIDENTIAL.USE is subject to licence terms.
 */
package com.wanmi.sbc.common.util;

import com.google.common.collect.Maps;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * WEB请求工具类
 *
 * @author daiyitian
 * @version 1.0
 * @since 2017年5月15日下午2:49:32
 */
public class WebUtil {

    private static final String USERAGENT = "User-Agent";

    private static final String DEFAULT_CHARSET = "UTF-8";

    public static final int CONNECTIMEOUT = 5000;

    public static final int READTIMEOUT = 5000;

    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like " + "Gecko) Chrome/31.0.1650.63 Safari/537.36";

    private WebUtil() {
    }

    /**
     * Send a get request
     *
     * @param url
     * @return response
     * @throws IOException
     */
    public static String get(String url) throws IOException {
        return get(url, Maps.newHashMap());
    }

    /**
     * Send a get request
     *
     * @param url     Url as string
     * @param headers Optional map with headers
     * @return response Response as string
     * @throws IOException
     */
    public static String get(String url, Map<String, String> headers) throws IOException {
        headers.put(USERAGENT, USER_AGENT);
        return fetch("GET", url, null, headers);
    }

    /**
     * Send a post request
     *
     * @param url  Url as string
     * @param body Request body as string
     * @return response Response as string
     * @throws IOException
     */
    public static String post(String url, String body) throws IOException {
        return post(url, body, null);
    }

    /**
     * Send a post request
     *
     * @param url     Url as string
     * @param body    Request body as string
     * @param headers Optional map with headers
     * @return response Response as string
     * @throws IOException
     */
    public static String post(String url, String body, Map<String, String> headers) throws IOException {
        Map<String, String> headers1 = headers;
        // set content type
        if (headers1 == null) {
            headers1 = new HashMap<>();
        }
        headers1.put("Content-Type", "application/x-www-form-urlencoded");
        headers1.put(USERAGENT, USER_AGENT);
        return fetch("POST", url, body, headers1);
    }

    /**
     * Post a form with parameters
     *
     * @param url    Url as string
     * @param params map with parameters/values
     * @return response Response as string
     * @throws IOException
     */
    public static String postForm(String url, Map<String, String> params) throws IOException {
        return postForm(url, params, Maps.newHashMap());
    }

    /**
     * Post a form with parameters
     *
     * @param url     Url as string
     * @param params  Map with parameters/values
     * @param headers Optional map with headers
     * @return response Response as string
     * @throws IOException
     */
    public static String postForm(String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put(USERAGENT, USER_AGENT);

        StringBuilder body = new StringBuilder();
        if (params != null) {
            boolean first = true;
            for (String param : params.keySet()) {
                if (first) {
                    first = false;
                } else {
                    body.append("&");
                }
                String value = params.get(param);
                body.append(URLEncoder.encode(param, DEFAULT_CHARSET))
                        .append("=")
                        .append(URLEncoder.encode(value, DEFAULT_CHARSET));
            }
        }

        return post(url, body.toString(), headers);
    }

    /**
     * Append query parameters to given url
     *
     * @param url    Url as string
     * @param params Map with query parameters
     * @return url Url with query parameters appended
     * @throws IOException
     */
    public static String appendQueryParams(String url, Map<String, String> params) throws IOException {
        StringBuilder fullUrl = new StringBuilder(url);

        if (params != null) {
            boolean first = fullUrl.toString().indexOf('?') == -1;
            for (String param : params.keySet()) {
                if (first) {
                    fullUrl.append('?');
                    first = false;
                } else {
                    fullUrl.append('&');
                }
                String value = params.get(param);
                fullUrl.append(URLEncoder.encode(param, DEFAULT_CHARSET))
                        .append('=')
                        .append(URLEncoder.encode(value, DEFAULT_CHARSET));
            }
        }

        return fullUrl.toString();
    }

    /**
     * Retrieve the query parameters from given url
     *
     * @param url Url containing query parameters
     * @return params Map with query parameters
     * @throws IOException
     */
    public static Map<String, String> getQueryParams(String url) throws IOException {
        Map<String, String> params = new HashMap<String, String>();

        int start = url.indexOf('?');
        while (start != -1) {
            int equals = url.indexOf('=', start);
            String param;
            if (equals != -1) {
                param = url.substring(start + 1, equals);
            } else {
                param = url.substring(start + 1);
            }

            String value = "";
            if (equals != -1) {
                start = url.indexOf('&', equals);
                if (start != -1) {
                    value = url.substring(equals + 1, start);
                } else {
                    value = url.substring(equals + 1);
                }
            }

            params.put(URLDecoder.decode(param, DEFAULT_CHARSET), URLDecoder.decode(value, DEFAULT_CHARSET));
        }

        return params;
    }

    /**
     * Returns the url without query parameters
     *
     * @param url Url containing query parameters
     * @return url Url without query parameters
     */
    public static String removeQueryParams(String url) {
        int q = url.indexOf('?');
        if (q != -1) {
            return url.substring(0, q);
        } else {
            return url;
        }
    }

    /**
     * Send a request
     *
     * @param method  HTTP method, for example "GET" or "POST"
     * @param url     Url as string
     * @param body    Request body as string
     * @param headers Optional map with headers
     * @return response Response as string
     * @throws IOException
     */
    public static String fetch(String method, String url, String body, Map<String, String> headers) throws IOException {
        HttpURLConnection conn = null;
        OutputStream os = null;
        InputStream is = null;
        try {
            URL u = new URL(url);
            conn = (HttpURLConnection) u.openConnection();
            conn.setConnectTimeout(CONNECTIMEOUT);
            conn.setReadTimeout(READTIMEOUT);
            conn.setRequestMethod(method);

            if (headers != null) {
                for (String key : headers.keySet()) {
                    conn.addRequestProperty(key, headers.get(key));
                }
            }

            if (body != null) {
                conn.setDoOutput(true);
                os = conn.getOutputStream();
                os.write(body.getBytes());
                os.flush();
                os.close();
            }

            is = conn.getInputStream();
            String response = streamToString(is);
            is.close();

            if (conn.getResponseCode() == 301) {
                String location = conn.getHeaderField("Location");
                return fetch(method, location, body, headers);
            }
            return response;
        } catch (IOException e) {
            throw e;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }

            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }

            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * Read an input stream into a string
     *
     * @param in
     * @return string
     * @throws IOException
     */
    public static String streamToString(InputStream in) throws IOException {
        StringBuilder out = new StringBuilder();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    /**
     * 验证是否是图片
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static boolean isImage(String url) {
        HttpURLConnection conn = null;
        OutputStream os = null;
        InputStream is = null;
        try {
            URL u = new URL(url);
            conn = (HttpURLConnection) u.openConnection();
            conn.setConnectTimeout(CONNECTIMEOUT);
            conn.setReadTimeout(READTIMEOUT);
            int code = conn.getResponseCode();
            if (code == 301) {
                String location = conn.getHeaderField("Location");
                return isImage(location);
            } else if (code != 200) {
                return false;
            }
            is = conn.getInputStream();
            byte[] bt = new byte[10];
            int read = is.read(bt);
            if (read < 0) {
                return false;
            }
            return isImageType(bt);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }

            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }

            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(isImage("https://wanmi-b2b.oss-cn-shanghai.aliyuncs.com/201909191421592051.jpg"));
    }

    /**
     * 验证二进制数据是否是图片
     *
     * @param b
     * @return
     *//*
    public static boolean isImageType(byte[] b) {
        if (b.length == 10) {
            byte b0 = b[0];
            byte b1 = b[1];
            byte b2 = b[2];
            byte b3 = b[3];
            byte b6 = b[6];
            byte b7 = b[7];
            byte b8 = b[8];
            byte b9 = b[9];
            if (b0 == (byte) 'G' && b1 == (byte) 'I' && b2 == (byte) 'F') {
                return true;
            } if (b1 == (byte) 'P' && b2 == (byte) 'N' && b3 == (byte) 'G') {
                return true;
            } else if (b6 == (byte) 'J' && b7 == (byte) 'F' && b8 == (byte) 'I' && b9 == (byte) 'F') {
                return true;
            } else if (b6 == (byte) 'E' && b7 == (byte) 'x' && b8 == (byte) 'i' && b9 == (byte) 'f') {
                return true;
            }else {
                return false;
            }
        } else {
            return false;
        }
    }*/

    /**
     * 验证二进制数据是否是图片
     *
     * @param b
     * @return
     */
    public static boolean isImageType(byte[] b) {
        StringBuilder stringBuilder = new StringBuilder();
        if (b == null || b.length != 10) {
            return false;
        }
        for (byte b1 : b) {
            int v = b1 & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        if (stringBuilder.length() == 0) {
            return false;
        }
        String fileHead = stringBuilder.toString().toUpperCase();
        ImageType[] fileTypes = ImageType.values();

        for (ImageType type : fileTypes) {
            if (fileHead.startsWith(type.getValue())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 文件类型枚取
     */
    public enum ImageType {

        /**
         * JEPG.
         */
        JPEG("FFD8FF"),

        /**
         * PNG.
         */
        PNG("89504E47"),

        /**
         * GIF.
         */
        GIF("47494638"),

        /**
         * TIFF.
         */
        TIFF("49492A00"),

        /**
         * CAD.
         */
        DWG("41433130"),

        /**
         * Adobe Photoshop.
         */
        PSD("38425053"),

        /**
         * Windows Bitmap.
         */
        BMP("424D");


        private String value = "";

        /**
         * Constructor.
         *
         * @param value
         */
        ImageType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
