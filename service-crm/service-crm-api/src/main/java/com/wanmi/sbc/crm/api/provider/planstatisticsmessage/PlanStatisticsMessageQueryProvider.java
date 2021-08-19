package com.wanmi.sbc.crm.api.provider.planstatisticsmessage;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.request.planstatisticsmessage.PlanStatisticsMessageByIdRequest;
import com.wanmi.sbc.crm.api.response.planstatisticsmessage.PlanStatisticsMessageByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>运营计划效果统计站内信收到人/次统计数据查询服务Provider</p>
 * @author lvzhenwei
 * @date 2020-02-05 15:08:00
 */
@FeignClient(value = "${application.crm.name}", contextId = "PlanStatisticsMessageQueryProvider")
public interface PlanStatisticsMessageQueryProvider {

	/**
	 * 单个查询运营计划效果统计站内信收到人/次统计数据API
	 *
	 * @author lvzhenwei
	 * @param planStatisticsMessageByIdRequest 单个查询运营计划效果统计站内信收到人/次统计数据请求参数 {@link PlanStatisticsMessageByIdRequest}
	 * @return 运营计划效果统计站内信收到人/次统计数据详情 {@link PlanStatisticsMessageByIdResponse}
	 */
	@PostMapping("/crm/${application.crm.version}/planstatisticsmessage/get-by-id")
	BaseResponse<PlanStatisticsMessageByIdResponse> getById(@RequestBody @Valid PlanStatisticsMessageByIdRequest planStatisticsMessageByIdRequest);

}

