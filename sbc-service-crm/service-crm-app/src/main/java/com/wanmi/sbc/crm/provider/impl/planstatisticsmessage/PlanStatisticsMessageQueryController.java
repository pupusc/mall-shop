package com.wanmi.sbc.crm.provider.impl.planstatisticsmessage;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.provider.planstatisticsmessage.PlanStatisticsMessageQueryProvider;
import com.wanmi.sbc.crm.api.request.planstatisticsmessage.PlanStatisticsMessageByIdRequest;
import com.wanmi.sbc.crm.api.response.planstatisticsmessage.PlanStatisticsMessageByIdResponse;
import com.wanmi.sbc.crm.planstatisticsmessage.model.root.PlanStatisticsMessage;
import com.wanmi.sbc.crm.planstatisticsmessage.service.PlanStatisticsMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>运营计划效果统计站内信收到人/次统计数据查询服务接口实现</p>
 * @author lvzhenwei
 * @date 2020-02-05 15:08:00
 */
@RestController
@Validated
public class PlanStatisticsMessageQueryController implements PlanStatisticsMessageQueryProvider {
	@Autowired
	private PlanStatisticsMessageService planStatisticsMessageService;

	@Override
	public BaseResponse<PlanStatisticsMessageByIdResponse> getById(@RequestBody @Valid PlanStatisticsMessageByIdRequest planStatisticsMessageByIdRequest) {
		PlanStatisticsMessage planStatisticsMessage = planStatisticsMessageService.getOne(planStatisticsMessageByIdRequest.getPlanId());
		return BaseResponse.success(new PlanStatisticsMessageByIdResponse(planStatisticsMessageService.wrapperVo(planStatisticsMessage)));
	}

}

