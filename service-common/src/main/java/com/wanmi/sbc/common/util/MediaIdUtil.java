package com.wanmi.sbc.common.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class MediaIdUtil {

    private static final String UPLOAD_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";

    public static String uploadFile(String filePath, String accessToken, String type) throws Exception {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            throw new IOException("文件不存在！");
        }

        String url = UPLOAD_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE", type);
        URL urlObj = new URL(url);

        //连接
        HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);

        //请求头
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Charset", "UTF-8");
        //conn.setRequestProperty("Content-Type","multipart/form-data;");

        //设置边界
        String BOUNDARY = "----------" + System.currentTimeMillis();
        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);

        StringBuilder sb = new StringBuilder();
        sb.append("--");
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition:form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");
        byte[] head = sb.toString().getBytes("utf-8");

        //输出流
        OutputStream out = new DataOutputStream(conn.getOutputStream());

        out.write(head);

        //文件正文部分
        DataInputStream in = null;
        try {
            in = new DataInputStream(new FileInputStream(file));
            int bytes = 0;
            byte[] bufferOut = new byte[1024];
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(in != null){
                in.close();
            }
        }

        //结尾
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");
        out.write(foot);
        out.flush();
        out.close();

        //获取响应
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = null;
        String result = null;

        reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = null;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        if (result == null) {
            result = buffer.toString();
        }
        reader.close();


        JSONObject jsonObject = JSONObject.parseObject(result);


        String mediaId = jsonObject.getString("media_id");

        return mediaId;
    }


    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

    public static String uploadURL(String strUrl) throws Exception {
        //获取文件名，文件名实际上在URL中可以找到
        String fileName="";
        if (strUrl.endsWith("/0")) {
            fileName = strUrl.substring(strUrl.lastIndexOf("/", strUrl.lastIndexOf("/") - 1) + 1);
        } else {
            fileName = strUrl.substring(strUrl.lastIndexOf("/") + 1);
        }
        if (!ImageUtils.checkImageSuffix(fileName)) {
            fileName = fileName + ".jpg";
        }
        HttpURLConnection conn = null;
        URL url = new URL(strUrl);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-agent", userAgent);
        conn.setUseCaches(false);
        conn.setConnectTimeout(DEF_CONN_TIMEOUT);
        conn.setReadTimeout(DEF_READ_TIMEOUT);
        conn.setInstanceFollowRedirects(false);
        conn.connect();
        InputStream is = conn.getInputStream();//连接得到输入流内容
        //这里服务器上要将此图保存的路径
        String savePath = "liveImage/";
        File file = new File(savePath);
        if (!file.exists()) {//保存文件夹不存在则建立
            file.mkdirs();
        }
        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(new File(savePath, fileName));//获取网址中的图片名
            int intRead = 0;
            byte[] buf = new byte[1024 * 2];//生成2K 大小的容器用于接收图片字节流
            while ((intRead = is.read(buf)) != -1) {
                fos.write(buf, 0, intRead);//文件输出流到指定路径文件
                fos.flush();
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(fos != null){
                fos.close();//关闭输出流
            }
        }
        if (conn != null) {
            conn.disconnect();//关闭连接
        }
        return savePath + fileName;
    }
}
