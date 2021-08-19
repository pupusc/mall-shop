package com.wanmi.sbc.crm.customerplan;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.provider.planstatisticsmessage.PlanStatisticsMessageQueryProvider;
import com.wanmi.sbc.crm.api.request.planstatisticsmessage.PlanStatisticsMessageByIdRequest;
import com.wanmi.sbc.crm.api.response.planstatisticsmessage.PlanStatisticsMessageByIdResponse;
import com.wanmi.sbc.crm.bean.vo.PlanStatisticsMessageVO;
import com.wanmi.sbc.crm.customerplan.response.PlanStatisticsMessagePushResponse;
import com.wanmi.sbc.crm.customerplan.vo.PlanStatisticsMessagePushVo;
import com.wanmi.sbc.message.api.provider.pushdetail.PushDetailQueryProvider;
import com.wanmi.sbc.message.api.request.pushdetail.PushDetailListRequest;
import com.wanmi.sbc.message.api.response.pushdetail.PushDetailListResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;


@Api(description = "运营计划效果统计通知人次统计数据管理API", tags = "PlanStatisticsMessageController")
@RestController
@RequestMapping(value = "/planstatisticsmessagepush")
public class PlanStatisticsMessagePushController {

    @Autowired
    private PlanStatisticsMessageQueryProvider planStatisticsMessageQueryProvider;

    @Autowired
    private PushDetailQueryProvider pushDetailQueryProvider;

    @ApiOperation(value = "根据运营计划id查询对应运营计划效果统计通知人次统计数据信息")
    @GetMapping("/{planId}")
    public BaseResponse<PlanStatisticsMessagePushResponse> getById(@PathVariable Long planId) {
        if (planId == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        PlanStatisticsMessageVO planStatisticsMessageVO = planStatisticsMessageQueryProvider.getById(
                PlanStatisticsMessageByIdRequest.builder().planId(planId).build()).getContext().getPlanStatisticsMessageVO();
        PlanStatisticsMessagePushVo planStatisticsMessagePushVo = KsBeanUtil.convert(planStatisticsMessageVO, PlanStatisticsMessagePushVo.class);
        PushDetailListResponse pushDetailListResponse = pushDetailQueryProvider.list(PushDetailListRequest.builder().planId(planId).build()).getContext();
        Integer pushNum = pushDetailListResponse.getPushDetailVOList().stream().map(pushDetailVO ->
                pushDetailVO.getSendSum() == null ? 0 : pushDetailVO.getSendSum()).reduce(0,(i,j)->i+j);
        if(Objects.nonNull(planStatisticsMessagePushVo)){
            planStatisticsMessagePushVo.setPushNum(pushNum);
        }
        return BaseResponse.success(PlanStatisticsMessagePushResponse.builder().planStatisticsMessagePushVo(planStatisticsMessagePushVo).build());
    }

}
