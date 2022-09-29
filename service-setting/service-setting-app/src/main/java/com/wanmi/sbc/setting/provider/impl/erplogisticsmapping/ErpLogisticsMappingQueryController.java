package com.wanmi.sbc.setting.provider.impl.erplogisticsmapping;

import com.wanmi.sbc.setting.api.provider.erplogisticsmapping.ErpLogisticsMappingQueryProvider;
import com.wanmi.sbc.setting.api.request.erplogisticsmapping.ErpLogisticsMappingByErpLogisticsCodeRequest;
import com.wanmi.sbc.setting.api.response.erplogisticsmapping.ErpLogisticsMappingByErpLogisticsCodeResponse;
import com.wanmi.sbc.setting.erplogisticsmapping.model.root.ErpLogisticsMapping;
import com.wanmi.sbc.setting.erplogisticsmapping.service.ErpLogisticsMappingService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import javax.validation.Valid;


/**
 * <p>erp系统物流编码映射查询服务接口实现</p>
 * @author weiwenhao
 * @date 2021-04-03 13:38:51
 */
@RestController
@Validated
public class ErpLogisticsMappingQueryController implements ErpLogisticsMappingQueryProvider {



	@Autowired
	private ErpLogisticsMappingService erpLogisticsMappingService;


	@Override
	public BaseResponse<ErpLogisticsMappingByErpLogisticsCodeResponse> getByErpLogisticsCode(@RequestBody @Valid ErpLogisticsMappingByErpLogisticsCodeRequest erpLogisticsMappingByErpLogisticsCodeRequest) {
		ErpLogisticsMapping erpLogisticsMapping = erpLogisticsMappingService.getByErpLogisticsCode(erpLogisticsMappingByErpLogisticsCodeRequest.getErpLogisticsCode());
		return BaseResponse.success(new ErpLogisticsMappingByErpLogisticsCodeResponse(erpLogisticsMappingService.wrapperVo(erpLogisticsMapping)));
	}


	@Override
	public BaseResponse<ErpLogisticsMappingByErpLogisticsCodeResponse> getWmLogisticsCode(@RequestBody @Valid ErpLogisticsMappingByErpLogisticsCodeRequest erpLogisticsMappingByErpLogisticsCodeRequest) {
		ErpLogisticsMapping erpLogisticsMapping = erpLogisticsMappingService.getWmLogisticsCode(erpLogisticsMappingByErpLogisticsCodeRequest.getWmLogisticsCode());
		return BaseResponse.success(new ErpLogisticsMappingByErpLogisticsCodeResponse(erpLogisticsMappingService.wrapperVo(erpLogisticsMapping)));
	}

}

