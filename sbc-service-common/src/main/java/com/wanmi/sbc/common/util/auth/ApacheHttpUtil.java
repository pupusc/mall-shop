package com.wanmi.sbc.common.util.auth;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2019-04-17 10:49
 */
public class ApacheHttpUtil {

    public static void main(String[] args) throws IOException {
//        postLicense();
    }

//    public static VerifyResult postLicense() throws IOException {
//
//        VerifyResult verifyResult = null;
//        HttpClient client = null;
//        try {
//            client = new DefaultHttpClient();
//            HttpPost post = new HttpPost("http://localhost:8080/license/receive");
//            String filePath = ApacheHttpUtil.class.getClassLoader().getResource("wanmi.license").getFile();
//            File file = new File(filePath);
//            FileBody fileBody = new FileBody(file);
//            StringBody macAddress = new StringBody(NetworkUtil.getLocalMac());
//            MultipartEntity reqEntity = new MultipartEntity();
//            reqEntity.addPart("macAddress", macAddress);
//            reqEntity.addPart("license_file", fileBody);
//            post.setEntity(reqEntity);
//            HttpResponse response = client.execute(post);
//            int statusCode = response.getStatusLine().getStatusCode();
//            if (statusCode == HttpStatus.SC_OK) {
//                HttpEntity resEntity = response.getEntity();
//                String res = EntityUtils.toString(resEntity);
//                verifyResult = JSONObject.parseObject(res, VerifyResult.class);
//                EntityUtils.consume(resEntity);
//            } else {
//                verifyResult = new VerifyResult("0", "授权成功");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (client != null) {
//                client.getConnectionManager().shutdown();
//            }
//        }
//        return verifyResult;
//
//    }
}
