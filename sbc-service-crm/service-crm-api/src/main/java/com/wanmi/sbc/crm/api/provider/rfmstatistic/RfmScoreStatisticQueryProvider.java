package com.wanmi.sbc.crm.api.provider.rfmstatistic;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.request.rfmstatistic.RfmCustomerDetailByCustomerIdRequest;
import com.wanmi.sbc.crm.api.request.rfmstatistic.RfmScoreStatisticRequest;
import com.wanmi.sbc.crm.api.response.rfmgroupstatistics.RefmCustomerDetailByCustomerIdResponse;
import org.springframework.cloud.openfeign.FeignClient;;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>RFM模型分数统计查询服务Provider</p>
 * @author zhanggaolei
 * @date 2019-10-15 14:19:11
 */
@FeignClient(value = "${application.crm.name}",contextId = "RfmScoreStatisticQueryProvider")
public interface RfmScoreStatisticQueryProvider {

	/**
	 * RFM分段分布情况统计
	 * @param request
	 * @return
	 */
	@PostMapping("/crm/${application.crm.version}/rfmscore/list")
	BaseResponse list(@RequestBody @Valid RfmScoreStatisticRequest request);

    /**
     * 会员rfm分析
     * @param request
     * @return
     */
    @PostMapping("/crm/${application.crm.version}/rfmscore/customerInfo")
    BaseResponse customerInfo(@RequestBody @Valid RfmScoreStatisticRequest request);

	/**
	 * 最近一天的rfm会员统计明细
	 * @param request 参数
	 * @return
	 */
	@PostMapping("/crm/${application.crm.version}/rfmscore/customerDetail")
    BaseResponse<RefmCustomerDetailByCustomerIdResponse> customerDetail(@RequestBody @Valid
                                                                                RfmCustomerDetailByCustomerIdRequest
                                                                                request);

}

