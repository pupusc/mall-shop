package com.wanmi.sbc.setting.api.provider.erplogisticsmapping;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.erplogisticsmapping.ErpLogisticsMappingByErpLogisticsCodeRequest;
import com.wanmi.sbc.setting.api.response.erplogisticsmapping.ErpLogisticsMappingByErpLogisticsCodeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>erp系统物流编码映射查询服务接口实现</p>
 * @author weiwenhao
 * @date 2021-04-03 13:38:51
 */
@FeignClient(value = "${application.setting.name}", contextId = "ErpLogisticsMappingQueryProvider")
public interface ErpLogisticsMappingQueryProvider {

	/**
	 * 通过erp物流编码查询物流映射信息
	 * @param erpLogisticsMappingByIdRequest
	 * @return
	 */
	@PostMapping("/setting/${application.setting.version}/erp-logistics-mapping")
	BaseResponse<ErpLogisticsMappingByErpLogisticsCodeResponse> getByErpLogisticsCode(@RequestBody @Valid ErpLogisticsMappingByErpLogisticsCodeRequest erpLogisticsMappingByIdRequest);

	/**
	 * 通过wm物流编码查询物流映射信息
	 * @param erpLogisticsMappingByIdRequest
	 * @return
	 */
	@PostMapping("/setting/${application.setting.version}/wm-logistics-mapping")
	BaseResponse<ErpLogisticsMappingByErpLogisticsCodeResponse> getWmLogisticsCode(@RequestBody @Valid ErpLogisticsMappingByErpLogisticsCodeRequest erpLogisticsMappingByIdRequest);

}

