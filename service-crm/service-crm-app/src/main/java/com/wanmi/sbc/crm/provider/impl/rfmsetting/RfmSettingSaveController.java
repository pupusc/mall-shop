package com.wanmi.sbc.crm.provider.impl.rfmsetting;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.constant.RFMSettingConstant;
import com.wanmi.sbc.crm.api.constant.RFMSettingErrorCode;
import com.wanmi.sbc.crm.api.constant.RfmConstant;
import com.wanmi.sbc.crm.api.provider.rfmsetting.RfmSettingSaveProvider;
import com.wanmi.sbc.crm.api.request.rfmsetting.*;
import com.wanmi.sbc.crm.api.response.rfmsetting.RfmSettingAddResponse;
import com.wanmi.sbc.crm.api.response.rfmsetting.RfmSettingModifyResponse;
import com.wanmi.sbc.crm.bean.enums.Period;
import com.wanmi.sbc.crm.bean.enums.RFMType;
import com.wanmi.sbc.crm.rfmsetting.model.root.RfmSetting;
import com.wanmi.sbc.crm.rfmsetting.service.RfmSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>rfm参数配置保存服务接口实现</p>
 * @author zhanglingke
 * @date 2019-10-14 14:33:42
 */
@RestController
@Validated
public class RfmSettingSaveController implements RfmSettingSaveProvider {
	@Autowired
	private RfmSettingService rfmSettingService;

	@Override
	public BaseResponse<RfmSettingAddResponse> add(@RequestBody @Valid RfmSettingAddRequest rfmSettingAddRequest) {
		RfmSetting rfmSetting = new RfmSetting();
		KsBeanUtil.copyPropertiesThird(rfmSettingAddRequest, rfmSetting);
		return BaseResponse.success(new RfmSettingAddResponse(
				rfmSettingService.wrapperVo(rfmSettingService.add(rfmSetting))));
	}

	@Override
	public BaseResponse<RfmSettingModifyResponse> modify(@RequestBody @Valid RfmSettingModifyRequest rfmSettingModifyRequest) {
		RfmSetting rfmSetting = new RfmSetting();
		KsBeanUtil.copyPropertiesThird(rfmSettingModifyRequest, rfmSetting);
		return BaseResponse.success(new RfmSettingModifyResponse(
				rfmSettingService.wrapperVo(rfmSettingService.modify(rfmSetting))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid RfmSettingDelByIdRequest rfmSettingDelByIdRequest) {
		rfmSettingService.deleteById(rfmSettingDelByIdRequest.getId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid RfmSettingDelByIdListRequest rfmSettingDelByIdListRequest) {
		rfmSettingService.deleteByIdList(rfmSettingDelByIdListRequest.getIdList());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse allocation(@RequestBody @Valid RfmSettingRequest rfmSettingRequest) {

		List<RfmSettingRElementRequest> r = rfmSettingRequest.getR();
		List<RfmSetting> rParse = parse(r,rfmSettingRequest.getPeriod(), rfmSettingRequest.getCreateTime(), rfmSettingRequest.getCreatePerson());

		List<RfmSettingFElementRequest> f = rfmSettingRequest.getF();
		List<RfmSetting> fParse = parse(f,rfmSettingRequest.getPeriod(), rfmSettingRequest.getCreateTime(), rfmSettingRequest.getCreatePerson());

		List<RfmSettingMElementRequest> m = rfmSettingRequest.getM();
		List<RfmSetting> mParse = parse(m,rfmSettingRequest.getPeriod(), rfmSettingRequest.getCreateTime(), rfmSettingRequest.getCreatePerson());

		rParse.addAll(fParse);
		rParse.addAll(mParse);

		rfmSettingService.allocation(rParse);

		return BaseResponse.success("");
	}

	/**
	 * 封装RFM数据
	 * @param rfm
	 * @param createTime
	 * @param createPerson
	 * @return
	 */
	private List<RfmSetting> pack(List<? extends RfmSettingRFMElementBaseRequest> rfm,Period period, LocalDateTime createTime,String createPerson){
		return rfm.stream().map(i->{
			RfmSetting rfmSetting = new RfmSetting();
			rfmSetting.setCreateTime(createTime);
			rfmSetting.setCreatePerson(createPerson);
			rfmSetting.setDelFlag(DeleteFlag.NO);
			rfmSetting.setParam(i.getParam());
			rfmSetting.setScore(i.getScore());
			if(i.getClass().equals(RfmSettingFElementRequest.class)){
				rfmSetting.setPeriod(period);
				rfmSetting.setType(RFMType.F);
			}else if(i.getClass().equals(RfmSettingMElementRequest.class)){
				rfmSetting.setPeriod(period);
				rfmSetting.setType(RFMType.M);
			}else {
				rfmSetting.setType(RFMType.R);
			}
			return rfmSetting;
		}).collect(Collectors.toList());
	}

	private List<RfmSetting> parse(List<? extends RfmSettingRFMElementBaseRequest> rfm, Period period, LocalDateTime createTime, String createPerson){
			Class<? extends RfmSettingRFMElementBaseRequest> clazz = rfm.get(0).getClass();

			String modelFlag = "";

			if(clazz.equals(RfmSettingRElementRequest.class)){
				modelFlag = "R";
			}
			if(clazz.equals(RfmSettingFElementRequest.class)){
				modelFlag = "F";
			}
			if(clazz.equals(RfmSettingMElementRequest.class)){
				modelFlag = "M";
			}

		//判定阶梯个数
		if(rfm.size() < RFMSettingConstant.MIN_LADDER){
			throw new SbcRuntimeException(RFMSettingErrorCode.RFM_SETTING_LESS_THAN_FIVE,new Object[]{modelFlag});
		}

		//判定是否存在重复参数
		if(rfm.stream().map(RfmSettingRFMElementBaseRequest::getParam).distinct().count() < rfm.size()){
			throw new SbcRuntimeException(RFMSettingErrorCode.RFM_SETTING_PARAM_DUPLICATE,new Object[]{modelFlag});
		}

		//判定是否存在重复得分
		if(rfm.stream().map(RfmSettingRFMElementBaseRequest::getScore).distinct().count() < rfm.size()){
			throw new SbcRuntimeException(RFMSettingErrorCode.RFM_SETTING_SCORE_DUPLICATE,new Object[]{modelFlag});
		}
		if(modelFlag.equals("R")) {
			for (RfmSettingRFMElementBaseRequest rfmTmp : rfm) {
				//最长时间为365
				if (rfmTmp.getParam() > RfmConstant.MAX_R_VALUE ) {
					throw new SbcRuntimeException(RFMSettingErrorCode.RFM_SETTING_PARAM_OUT, new Object[]{modelFlag});
				}
			}
			;
		}
		return this.pack(rfm,period, createTime, createPerson);
//		List<? extends RfmSettingRFMElementBaseRequest> collect = rfm.stream().sorted((i, j) -> {
//			if (i.getParam() > j.getParam()) {
//				return 1;
//			} else if (Objects.equals(i.getParam(), j.getParam())) {
//				return 0;
//			} else {
//				return -1;
//			}
//		}).collect(Collectors.toList());
	}

	public static void main(String[] args) {

		List<RfmSettingRElementRequest> elementRequests = new ArrayList<>();

		RfmSettingRElementRequest rfmSettingRElementRequest1 = new RfmSettingRElementRequest();
		rfmSettingRElementRequest1.setParam(2);
		rfmSettingRElementRequest1.setScore(40);

		RfmSettingRElementRequest rfmSettingRElementRequest2 = new RfmSettingRElementRequest();
		rfmSettingRElementRequest2.setParam(5);
		rfmSettingRElementRequest2.setScore(20);

		RfmSettingRElementRequest rfmSettingRElementRequest3 = new RfmSettingRElementRequest();
		rfmSettingRElementRequest3.setParam(3);
		rfmSettingRElementRequest3.setScore(30);

		elementRequests.add(rfmSettingRElementRequest1);
		elementRequests.add(rfmSettingRElementRequest2);
		elementRequests.add(rfmSettingRElementRequest3);


//		judge(elementRequests);


	}

}

