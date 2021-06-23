package com.wanmi.sbc.setting.provider.impl.operatedatalog;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.setting.api.request.operatedatalog.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.operatedatalog.OperateDataLogSaveProvider;
import com.wanmi.sbc.setting.api.response.operatedatalog.OperateDataLogAddResponse;
import com.wanmi.sbc.setting.api.response.operatedatalog.OperateDataLogModifyResponse;
import com.wanmi.sbc.setting.operatedatalog.service.OperateDataLogService;
import com.wanmi.sbc.setting.operatedatalog.model.root.OperateDataLog;
import javax.validation.Valid;

/**
 * <p>系统日志保存服务接口实现</p>
 * @author guanfl
 * @date 2020-04-21 14:57:15
 */
@RestController
@Validated
public class OperateDataLogSaveController implements OperateDataLogSaveProvider {
	@Autowired
	private OperateDataLogService operateDataLogService;

	@Override
	public BaseResponse<OperateDataLogAddResponse> add(@RequestBody @Valid OperateDataLogAddRequest operateDataLogAddRequest) {
		OperateDataLog operateDataLog = new OperateDataLog();
		KsBeanUtil.copyPropertiesThird(operateDataLogAddRequest, operateDataLog);
		operateDataLog.setDelFlag(DeleteFlag.NO);
		return BaseResponse.success(new OperateDataLogAddResponse(
				operateDataLogService.wrapperVo(operateDataLogService.add(operateDataLog))));
	}

	@Override
	public BaseResponse<OperateDataLogModifyResponse> modify(@RequestBody @Valid OperateDataLogModifyRequest operateDataLogModifyRequest) {
		OperateDataLog operateDataLog = new OperateDataLog();
		KsBeanUtil.copyPropertiesThird(operateDataLogModifyRequest, operateDataLog);
		return BaseResponse.success(new OperateDataLogModifyResponse(
				operateDataLogService.wrapperVo(operateDataLogService.modify(operateDataLog))));
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid OperateDataLogDelByIdRequest operateDataLogDelByIdRequest) {
		operateDataLogService.deleteById(operateDataLogDelByIdRequest.getId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByOperateId(@RequestBody @Valid OperateDataLogDelByOperateIdRequest operateDataLogDelByOperateIdRequest) {
		operateDataLogService.deleteByOperateId(operateDataLogDelByOperateIdRequest.getOperateId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid OperateDataLogDelByIdListRequest operateDataLogDelByIdListRequest) {
		operateDataLogService.deleteByIdList(operateDataLogDelByIdListRequest.getIdList());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse synDataLog(@RequestBody @Valid OperateDataLogSynRequest operateDataLogSynRequest) {
		operateDataLogService.synDataLog(operateDataLogSynRequest.getSupplierGoodsId(), operateDataLogSynRequest.getProviderGoodsId());
		return BaseResponse.SUCCESSFUL();
	}

}

