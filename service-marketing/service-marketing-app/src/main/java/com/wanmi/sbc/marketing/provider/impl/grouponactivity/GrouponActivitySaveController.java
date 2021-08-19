package com.wanmi.sbc.marketing.provider.impl.grouponactivity;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.provider.grouponactivity.GrouponActivitySaveProvider;
import com.wanmi.sbc.marketing.api.request.grouponactivity.*;
import com.wanmi.sbc.marketing.api.response.grouponactivity.GrouponActivityAddResponse;
import com.wanmi.sbc.marketing.api.response.grouponactivity.GrouponActivityModifyResponse;
import com.wanmi.sbc.marketing.bean.enums.AuditStatus;
import com.wanmi.sbc.marketing.grouponactivity.model.entity.GrouponActivityAdd;
import com.wanmi.sbc.marketing.grouponactivity.model.entity.GrouponActivityEdit;
import com.wanmi.sbc.marketing.grouponactivity.model.root.GrouponActivity;
import com.wanmi.sbc.marketing.grouponactivity.service.GrouponActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>拼团活动信息表保存服务接口实现</p>
 *
 * @author groupon
 * @date 2019-05-15 14:02:38
 */
@RestController
@Validated
public class GrouponActivitySaveController implements GrouponActivitySaveProvider {

    @Autowired
    private GrouponActivityService grouponActivityService;

    @Override
    public BaseResponse<GrouponActivityAddResponse> add(@RequestBody @Valid GrouponActivityAddRequest request) {
        GrouponActivityAdd entity = new GrouponActivityAdd();
        GrouponActivity grouponActivity = new GrouponActivity();
        KsBeanUtil.copyPropertiesThird(request, grouponActivity);
        entity.setGoodsInfos(request.getGoodsInfos());
        entity.setGrouponActivity(grouponActivity);
        return BaseResponse.success(grouponActivityService.add(entity));
        
    }

    @Override
    public BaseResponse<GrouponActivityModifyResponse> modify(@RequestBody @Valid GrouponActivityModifyRequest request) {
        GrouponActivityEdit entity = new GrouponActivityEdit();
        GrouponActivity grouponActivity = new GrouponActivity();
        KsBeanUtil.copyPropertiesThird(request, grouponActivity);
        entity.setGoodsInfos(request.getGoodsInfos());
        entity.setGrouponActivity(grouponActivity);
        return BaseResponse.success(grouponActivityService.edit(entity));
    }

    @Override
    public BaseResponse deleteById(@RequestBody @Valid GrouponActivityDelByIdRequest grouponActivityDelByIdRequest) {
        grouponActivityService.deleteById(grouponActivityDelByIdRequest.getGrouponActivityId());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteByIdList(@RequestBody @Valid GrouponActivityDelByIdListRequest
                                               grouponActivityDelByIdListRequest) {
        grouponActivityService.deleteByIdList(grouponActivityDelByIdListRequest.getGrouponActivityIdList());
        return BaseResponse.SUCCESSFUL();
    }



    /**
     * 批量审核通过拼团活动
     * @param request
     * @return
     */
    @Override
    public BaseResponse batchCheckMarketing(@RequestBody @Valid GrouponActivityBatchCheckRequest request) {
        grouponActivityService.batchCheckMarketing(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 驳回拼团活动
     * @param request
     * @return
     */
    @Override
    public BaseResponse refuseCheckMarketing(@RequestBody @Valid GrouponActivityRefuseRequest request) {
        grouponActivityService.refuseCheckMarketing(request.getGrouponActivityId(),
                AuditStatus.NOT_PASS,
                request.getAuditReason());
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 批量修改拼团活动精选状态
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse batchStickyMarketing(@RequestBody @Valid GrouponActivityBatchStickyRequest request) {
        grouponActivityService.batchStickyMarketing(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse modifyStatisticsNumById(@RequestBody @Valid GrouponActivityModifyStatisticsNumByIdRequest request) {
        Integer result = grouponActivityService.updateStatisticsNumByGrouponActivityId(request.getGrouponActivityId(),request.getGrouponNum(),request.getGrouponOrderStatus());
        return BaseResponse.success(request);
    }

    @Override
    public BaseResponse modifyWaitGrouponNumById(GrouponActivityWaitNumModifyRequest modifyRequest) {
        grouponActivityService.updateWaitGrouponNumByGrouponActivityId(modifyRequest.getGrouponActivityId(),
                modifyRequest.getNum());
        return BaseResponse.SUCCESSFUL();
    }
}

