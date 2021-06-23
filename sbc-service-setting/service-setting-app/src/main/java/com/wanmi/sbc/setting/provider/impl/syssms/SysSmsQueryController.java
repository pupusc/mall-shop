package com.wanmi.sbc.setting.provider.impl.syssms;

import com.wanmi.sbc.common.util.UUIDUtil;
import com.wanmi.sbc.setting.api.response.syssms.SmsSupplierRopResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.syssms.SysSmsQueryProvider;
import com.wanmi.sbc.setting.api.request.syssms.SysSmsPageRequest;
import com.wanmi.sbc.setting.api.request.syssms.SysSmsQueryRequest;
import com.wanmi.sbc.setting.api.response.syssms.SysSmsPageResponse;
import com.wanmi.sbc.setting.api.request.syssms.SysSmsListRequest;
import com.wanmi.sbc.setting.api.response.syssms.SysSmsListResponse;
import com.wanmi.sbc.setting.api.request.syssms.SysSmsByIdRequest;
import com.wanmi.sbc.setting.api.response.syssms.SysSmsByIdResponse;
import com.wanmi.sbc.setting.bean.vo.SysSmsVO;
import com.wanmi.sbc.setting.syssms.service.SysSmsService;
import com.wanmi.sbc.setting.syssms.model.root.SysSms;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>系统短信配置查询服务接口实现</p>
 * @author lq
 * @date 2019-11-05 16:13:47
 */
@RestController
@Validated
public class SysSmsQueryController implements SysSmsQueryProvider {
	@Autowired
	private SysSmsService sysSmsService;

	@Override
	public BaseResponse<SysSmsPageResponse> page(@RequestBody @Valid SysSmsPageRequest sysSmsPageReq) {
		SysSmsQueryRequest queryReq = new SysSmsQueryRequest();
		KsBeanUtil.copyPropertiesThird(sysSmsPageReq, queryReq);
		Page<SysSms> sysSmsPage = sysSmsService.page(queryReq);
		Page<SysSmsVO> newPage = sysSmsPage.map(entity -> sysSmsService.wrapperVo(entity));
		MicroServicePage<SysSmsVO> microPage = new MicroServicePage<>(newPage, sysSmsPageReq.getPageable());
		SysSmsPageResponse finalRes = new SysSmsPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<SysSmsListResponse> list() {
		List<SysSms> sysSmsList = sysSmsService.list();
		SysSmsListResponse response = new SysSmsListResponse();
		if(!CollectionUtils.isEmpty(sysSmsList)){
			response.setSmsSupplierRopResponses(sysSmsList.stream().map(entity ->
					sysSmsService.wrapperSmsSupplierRopResponse(sysSmsService.wrapperVo(entity)))
					.collect(Collectors.toList()));
		}else{
			response.setSmsSupplierRopResponses(Collections.singletonList(new SmsSupplierRopResponse()));
		}
		return BaseResponse.success(response);
	}

	@Override
	public BaseResponse<SmsSupplierRopResponse> getById(@RequestBody @Valid SysSmsByIdRequest sysSmsByIdRequest) {
		SmsSupplierRopResponse response = new SmsSupplierRopResponse();
		SysSms sysSms = sysSmsService.getById(sysSmsByIdRequest.getSmsId());
		if(Objects.nonNull(sysSms)) {
			SysSmsVO sysSmsVO = sysSmsService.wrapperVo(sysSms);
			response = sysSmsService.wrapperSmsSupplierRopResponse(sysSmsVO);
		}else{
			//为空，新增一条,创建时间为当前时间
			SysSms sysSms1 = sysSmsService.add(new SysSms().builder().createTime(LocalDateTime.now()).build());
			response= sysSmsService.wrapperSmsSupplierRopResponse(sysSmsService.wrapperVo(sysSms1
          ));
		}
		return BaseResponse.success(response);
	}
}

