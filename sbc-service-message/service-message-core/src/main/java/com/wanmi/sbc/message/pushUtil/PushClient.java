package com.wanmi.sbc.message.pushUtil;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.message.bean.constant.PushErrorCode;
import com.wanmi.sbc.message.bean.enums.MethodType;
import com.wanmi.sbc.message.pushUtil.root.PushResultEntry;
import com.wanmi.sbc.message.pushUtil.root.QueryEntry;
import com.wanmi.sbc.message.pushUtil.root.QueryResultEntry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public class PushClient {
	
	// The user agent
	protected final String USER_AGENT = "Mozilla/5.0";

	// This object is used for sending the post request to Umeng
//	protected CloseableHttpClient client = new DefaultHttpClient();
	protected CloseableHttpClient client = HttpClientBuilder.create().build();

	// The host
	protected static final String host = "http://msg.umeng.com";
	
	// device_token上传接口
	protected static final String uploadPath = "/upload";
	
	// push发送接口
	protected static final String postPath = "/api/send";

	// 发送状态查询
	protected static final String queryPath = "/api/status";

	// 任务撤销接口
	protected static final String cancelPath = "/api/cancel";

	public PushResultEntry send(UmengNotification msg) {
		log.info("PushClient.send_调用友盟推送接口");
		String timestamp = Integer.toString((int)(System.currentTimeMillis() / 1000));
		try {
			msg.setPredefinedKeyValue("timestamp", timestamp);
		} catch (Exception e) {
			log.error("PushClient.send_参数组成异常", e);
			throw new SbcRuntimeException(PushErrorCode.UMENG_PARAM_ERROR);
		}
		String url = host + postPath;
        String postBody = msg.getPostBody();
		JSONObject json = this.execute(url, postBody, msg.getAppMasterSecret());
		String ret = json.getString("ret");
		PushResultEntry resultEntry = JSONObject.parseObject(json.getString("data"), PushResultEntry.class) ;
		resultEntry.setRet(ret);
		return resultEntry;
    }

    private JSONObject execute(String url, String postBody, String masterSecret) {
		log.info("PushClient.execute_postBody:{}", postBody);
		log.info("PushClient.execute_masterSecret:{}", masterSecret);
		String sign = DigestUtils.md5Hex(("POST" + url + postBody + masterSecret).getBytes(UTF_8));
		url = url + "?sign=" + sign;
		HttpPost post = new HttpPost(url);
		post.setHeader("User-Agent", USER_AGENT);
		StringEntity se = new StringEntity(postBody, UTF_8);
		post.setEntity(se);
		// Send the post request and get the response
		try (CloseableHttpResponse response = client.execute(post)) {
			int status = response.getStatusLine().getStatusCode();
			if (status == 200) {
				log.debug("PushClient.execute_请求成功");
				String strResult = EntityUtils.toString(response.getEntity(), UTF_8);
				log.info("PushClient.execute_成功返回json::{}", strResult);
				return JSONObject.parseObject(strResult);
			} else {
				log.error("PushClient.execute_请求失败异常,code::{}", status);
				log.error("PushClient.execute_请求失败异常,entry::{}", EntityUtils.toString(response.getEntity(), UTF_8));
				throw new SbcRuntimeException(PushErrorCode.UMENG_HTTP_FAIL);
			}
		} catch (IOException e) {
			log.error("PushClient.execute_请求IO异常", e);
			throw new SbcRuntimeException(PushErrorCode.UMENG_HTTP_IO_ERROR);
		}
	}

	// Upload file with device_tokens to Umeng
	public String uploadContents(String appkey,String appMasterSecret,String contents) {
		log.info("PushClient.uploadContents_调用友盟文件上传接口");
		// Construct the json string
		JSONObject uploadJson = new JSONObject();
		uploadJson.put("appkey", appkey);
		String timestamp = Integer.toString((int)(System.currentTimeMillis() / 1000));
		uploadJson.put("timestamp", timestamp);
		uploadJson.put("content", contents);
		// Construct the request
		String url = host + uploadPath;
		String postBody = uploadJson.toString();
		JSONObject json = this.execute(url, postBody, appMasterSecret);
		String ret = json.getString("ret");
		JSONObject data = json.getJSONObject("data");
		if (!ret.equals("SUCCESS")) {
			log.error("PushClient.uploadContents_友盟文件上传失败");
			log.error("PushClient.uploadContents_友盟文件上传失败原因：{}", data.toString());
			throw new SbcRuntimeException(PushErrorCode.UMENG_FILE_UPLOAD_NULL);
		}
		return data.getString("file_id");
	}

	public QueryResultEntry queryOrCancel(QueryEntry queryEntry) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("appkey", queryEntry.getKey());
		jsonObject.put("task_id", queryEntry.getTaskId());
		jsonObject.put("timestamp",Integer.toString((int)(System.currentTimeMillis() / 1000)));
		String url = "";
		if (MethodType.QUERY.equals(queryEntry.getType())){
			url = host + queryPath;
		} else {
			url = host + cancelPath;
		}

		String postBody = jsonObject.toString();

		JSONObject json = this.execute(url, postBody, queryEntry.getAppMasterSecret());
		String ret = json.getString("ret");
		QueryResultEntry resultEntry = JSONObject.parseObject(json.getString("data"), QueryResultEntry.class) ;
		resultEntry.setRet(ret);
		return resultEntry;
	}

}
