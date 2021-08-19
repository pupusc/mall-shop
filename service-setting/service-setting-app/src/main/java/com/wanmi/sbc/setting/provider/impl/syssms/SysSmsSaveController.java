package com.wanmi.sbc.setting.provider.impl.syssms;

import com.wanmi.sbc.setting.api.response.syssms.SmsSupplierRopResponse;
import com.wanmi.sbc.setting.bean.vo.SysSmsVO;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.syssms.SysSmsSaveProvider;
import com.wanmi.sbc.setting.api.request.syssms.SysSmsAddRequest;
import com.wanmi.sbc.setting.api.response.syssms.SysSmsAddResponse;
import com.wanmi.sbc.setting.api.request.syssms.SysSmsModifyRequest;
import com.wanmi.sbc.setting.api.response.syssms.SysSmsModifyResponse;
import com.wanmi.sbc.setting.api.request.syssms.SysSmsDelByIdRequest;
import com.wanmi.sbc.setting.api.request.syssms.SysSmsDelByIdListRequest;
import com.wanmi.sbc.setting.syssms.service.SysSmsService;
import com.wanmi.sbc.setting.syssms.model.root.SysSms;
import javax.validation.Valid;
import java.util.Objects;

/**
 * <p>系统短信配置保存服务接口实现</p>
 * @author lq
 * @date 2019-11-05 16:13:47
 */
@RestController
@Validated
public class SysSmsSaveController implements SysSmsSaveProvider {
	@Autowired
	private SysSmsService sysSmsService;

	@Override
	public BaseResponse<SmsSupplierRopResponse> add(@RequestBody @Valid SysSmsAddRequest sysSmsAddRequest) {
		SysSms sysSms = new SysSms();
		KsBeanUtil.copyPropertiesThird(sysSmsAddRequest, sysSms);
		SmsSupplierRopResponse response= sysSmsService.wrapperSmsSupplierRopResponse(sysSmsService.wrapperVo(sysSmsService.modify(sysSms)));
		return BaseResponse.success(response);
	}

	@Override
	public BaseResponse<SmsSupplierRopResponse> modify(@RequestBody @Valid SysSmsModifyRequest sysSmsModifyRequest) {
		SysSms sysSms = new SysSms();
		KsBeanUtil.copyPropertiesThird(sysSmsModifyRequest, sysSms);
		SmsSupplierRopResponse response= sysSmsService.wrapperSmsSupplierRopResponse(sysSmsService.wrapperVo(sysSmsService.modify(sysSms)));
		return BaseResponse.success(response);
	}

	@Override
	public BaseResponse deleteById(@RequestBody @Valid SysSmsDelByIdRequest sysSmsDelByIdRequest) {
		sysSmsService.deleteById(sysSmsDelByIdRequest.getSmsId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid SysSmsDelByIdListRequest sysSmsDelByIdListRequest) {
		sysSmsService.deleteByIdList(sysSmsDelByIdListRequest.getSmsIdList());
		return BaseResponse.SUCCESSFUL();
	}

}

