package com.wanmi.sbc.message.pushUtil;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.redis.CacheKeyConstant;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.message.bean.constant.PushErrorCode;
import com.wanmi.sbc.message.bean.enums.MethodType;
import com.wanmi.sbc.message.bean.enums.PushPlatform;
import com.wanmi.sbc.message.pushUtil.android.AndroidFilecast;
import com.wanmi.sbc.message.pushUtil.ios.IOSFilecast;
import com.wanmi.sbc.message.pushUtil.root.PushResultEntry;
import com.wanmi.sbc.message.pushUtil.root.QueryEntry;
import com.wanmi.sbc.message.pushUtil.root.QueryResultEntry;
import com.wanmi.sbc.message.redis.RedisService;
import com.wanmi.sbc.setting.api.provider.umengpushconfig.UmengPushConfigQueryProvider;
import com.wanmi.sbc.setting.api.request.umengpushconfig.UmengPushConfigByIdRequest;
import com.wanmi.sbc.setting.api.response.umengpushconfig.UmengPushConfigByIdResponse;
import com.wanmi.sbc.setting.bean.vo.UmengPushConfigVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service("PushService")
@Slf4j
public class PushService {

	private PushClient client = new PushClient();

	@Autowired
	private UmengPushConfigQueryProvider umengPushConfigQueryProvider;
	@Autowired
	private RedisService redisService;

	public List<PushResultEntry> push(PushEntry pushEntry) {
		UmengPushConfigVO vo = this.config();

		if (Objects.nonNull(vo)){
			StringBuilder androidStringBuilder = new StringBuilder();
			StringBuilder iosStringBuilder = new StringBuilder();
			if (CollectionUtils.isNotEmpty(pushEntry.getAndroidTokenList())){
				pushEntry.getAndroidTokenList().forEach(token-> androidStringBuilder.append(token).append("\n"));
			}

			if (CollectionUtils.isNotEmpty(pushEntry.getIosTokenList())){
				pushEntry.getIosTokenList().forEach(token-> iosStringBuilder.append(token).append("\n"));
			}

			PushParams params = new PushParams();
			params.androidAppKey = vo.getAndroidKeyId();
			params.androidAppMasterSecret = vo.getAndroidKeySecret();
			params.iosAppKey = vo.getIosKeyId();
			params.iosAppMasterSecret = vo.getIosKeySecret();
			params.iosStrToken = iosStringBuilder.toString();
			params.androidStrToken = androidStringBuilder.toString();
			params.pushEntry = pushEntry;

			List<PushResultEntry> pushResultEntrys = new ArrayList<>();
			PushResultEntry android = this.sendAndroidFilecast(params);
			if (android != null){
				android.setPlatform(PushPlatform.ANDROID);
				pushResultEntrys.add(android);
			}
			PushResultEntry ios = this.sendIOSFilecast(params);
			if (ios != null){
				ios.setPlatform(PushPlatform.IOS);
				pushResultEntrys.add(ios);
			}
			return pushResultEntrys;
		} else {
			log.error("PushService.push_异常:友盟配置为空");
			throw new SbcRuntimeException(PushErrorCode.UMENG_CONFIG_NULL);
		}
	}

	/**
	 * @Description: 查询任务状态
	 * @param taskId 任务ID
	 * @Date: 2020/1/6 20:20
	 */
	public QueryResultEntry queryOrCancel(String taskId, PushPlatform pushPlatform, MethodType type) {
		UmengPushConfigVO vo = this.config();

		if (Objects.nonNull(vo)){
			String appKey = null;
			String appMasterSecret = null;
			if (PushPlatform.IOS.equals(pushPlatform)){
				appKey = vo.getIosKeyId();
				appMasterSecret = vo.getIosKeySecret();
			} else {
				appKey = vo.getAndroidKeyId();
				appMasterSecret = vo.getAndroidKeySecret();
			}
			return client.queryOrCancel(QueryEntry.builder().key(appKey).taskId(taskId).appMasterSecret(appMasterSecret).type(type).build());
		} else {
			log.error("PushService.query_异常:友盟配置为空");
			throw new SbcRuntimeException(PushErrorCode.UMENG_CONFIG_NULL);
		}

	}

	/**
	 * @Description: 友盟安卓推送接口
	 * @param pushParams
	 * @Date: 2020/1/6 19:42
	 */
	private PushResultEntry sendAndroidFilecast(PushParams pushParams) {
		if (StringUtils.isBlank(pushParams.androidStrToken)){
			return null;
		}

		log.debug("sendAndroidFilecast.androidAppKey:{}" ,pushParams.androidAppKey);
		log.debug("sendAndroidFilecast.androidAppMasterSecret:{}" ,pushParams.androidAppMasterSecret);
		try {
			AndroidFilecast filecast = new AndroidFilecast(pushParams.androidAppKey, pushParams.androidAppMasterSecret);
			// TODO upload your device tokens, and use '\n' to split them if there are multiple tokens
			PushEntry pushEntry = pushParams.pushEntry;
			String fileId = client.uploadContents(pushParams.androidAppKey, pushParams.androidAppMasterSecret,
					pushParams.androidStrToken);
			filecast.setFileId(fileId);
			// 消息类型
			filecast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
			// 通知栏文字
			filecast.setTicker(pushEntry.getTicker());
			// 通知标题
			filecast.setTitle(pushEntry.getTitle());
			// 通知文字描述
			filecast.setText(pushEntry.getText());
			// http/https图片链接
			filecast.setImg(pushEntry.getImage());
			// 路由地址
//			filecast.goCustomAfterOpen("");
			filecast.goCustomAfterOpen(pushEntry.getRouter());
			// out_biz_no 不知道是否有效。待测试 https://developer.umeng.com/docs/66632/detail/68343
			filecast.setOutBizNo(pushEntry.getOutBizNo());
			filecast.setMipush(Boolean.TRUE);
			filecast.setMiactivity("com.wanmi.s2bstore.umeng.PushActivity");
			// 发送消息描述，建议填写。
			filecast.setDescription("ANDROID消息推送");
			// 定时发送
			if (Objects.nonNull(pushEntry.getSendTime())){
				filecast.setStartTime(pushEntry.getSendTime().format(DateTimeFormatter.ofPattern(DateUtil.FMT_TIME_1)));
			}
			return client.send(filecast);
		} catch (Exception e) {
			log.error("PushService.sendAndroidFilecast_参数组成异常", e);
			throw new SbcRuntimeException(PushErrorCode.UMENG_PARAM_ERROR);
		}
	}

	/**
	 * @Description: 友盟苹果推送接口
	 * @param pushParams
	 * @Date: 2020/1/6 19:42
	 */
	private PushResultEntry sendIOSFilecast(PushParams pushParams) {
		if (StringUtils.isBlank(pushParams.iosStrToken)){
			return null;
		}

		log.debug("sendAndroidFilecast.iosAppKey:{}" ,pushParams.iosAppKey);
		log.debug("sendAndroidFilecast.iosAppMasterSecret:{}" ,pushParams.iosAppMasterSecret);
		try {
			IOSFilecast filecast = new IOSFilecast(pushParams.iosAppKey, pushParams.iosAppMasterSecret);
			// TODO upload your device tokens, and use '\n' to split them if there are multiple tokens
			String fileId = client.uploadContents(pushParams.iosAppKey, pushParams.iosAppMasterSecret,
					pushParams.iosStrToken);
			filecast.setFileId(fileId);
			PushEntry pushEntry = pushParams.pushEntry;
			JSONObject alertJson = new JSONObject();
			alertJson.put("title", pushEntry.getTitle());
			alertJson.put("body", pushEntry.getText());
			filecast.setAlert(alertJson);
			filecast.setBadge(0);
			filecast.setSound("default");
			filecast.setOutBizNo(pushEntry.getOutBizNo());
			// 发送消息描述，建议填写。
			filecast.setDescription("IOS消息推送");
			// 自定义参数
			filecast.setCustomizedField("router", pushEntry.getRouter());
			// 定时发送
			if (Objects.nonNull(pushEntry.getSendTime())) {
				filecast.setStartTime(pushEntry.getSendTime().format(DateTimeFormatter.ofPattern(DateUtil.FMT_TIME_1)));
			}
			return client.send(filecast);
		} catch (Exception e) {
			log.error("PushService.sendAndroidFilecast_参数组成异常", e);
			throw new SbcRuntimeException(PushErrorCode.UMENG_PARAM_ERROR);
		}
	}

	private UmengPushConfigVO config(){
		UmengPushConfigVO vo = (UmengPushConfigVO) redisService.get(CacheKeyConstant.UMENG_CONFIG);
		if (Objects.isNull(vo)){
			UmengPushConfigByIdRequest idReq = new UmengPushConfigByIdRequest();
			idReq.setId(-1);
			UmengPushConfigByIdResponse response = umengPushConfigQueryProvider.getById(idReq).getContext();
			vo = response.getUmengPushConfigVO();
			redisService.put(CacheKeyConstant.UMENG_CONFIG, vo);
		}
		return vo;
	}

	 static class PushParams {
		// 安卓应用唯一标识
		private String androidAppKey;

		// 安卓密钥
		private String androidAppMasterSecret;

		// iOS应用唯一标识
		private String iosAppKey;

		// iOS密钥
		private String iosAppMasterSecret;

		// 解析后的ios token
		private String iosStrToken;

		 // 解析后的android token
		 private String androidStrToken;

		private PushEntry pushEntry;
	}
}
