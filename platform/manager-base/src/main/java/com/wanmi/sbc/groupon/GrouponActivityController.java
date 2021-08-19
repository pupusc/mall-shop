package com.wanmi.sbc.groupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.elastic.api.provider.groupon.EsGrouponActivityProvider;
import com.wanmi.sbc.elastic.api.provider.groupon.EsGrouponActivityQueryProvider;
import com.wanmi.sbc.elastic.api.request.groupon.*;
import com.wanmi.sbc.elastic.api.response.groupon.EsGrouponActivityPageResponse;
import com.wanmi.sbc.goods.api.provider.groupongoodsinfo.GrouponGoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.groupongoodsinfo.GrouponGoodsInfoListRequest;
import com.wanmi.sbc.goods.api.provider.groupongoodsinfo.GrouponGoodsInfoSaveProvider;
import com.wanmi.sbc.goods.api.request.groupongoodsinfo.GrouponGoodsInfoDeleteByGrouponActivityIdRequest;
import com.wanmi.sbc.goods.api.request.groupongoodsinfo.GrouponGoodsInfoModifyAuditStatusRequest;
import com.wanmi.sbc.goods.api.request.groupongoodsinfo.GrouponGoodsInfoModifyStickyRequest;
import com.wanmi.sbc.goods.api.request.info.DistributionGoodsPageRequest;
import com.wanmi.sbc.goods.bean.enums.AuditStatus;
import com.wanmi.sbc.goods.bean.vo.GrouponGoodsInfoVO;
import com.wanmi.sbc.marketing.api.provider.grouponactivity.GrouponActivityQueryProvider;
import com.wanmi.sbc.marketing.api.provider.grouponactivity.GrouponActivitySaveProvider;
import com.wanmi.sbc.marketing.api.request.grouponactivity.*;
import com.wanmi.sbc.marketing.api.response.grouponactivity.GrouponActivityByIdResponse;
import com.wanmi.sbc.marketing.api.response.grouponactivity.GrouponActivityPage4MangerResponse;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * S2B的拼团活动服务
 */
@Slf4j
@RestController
@RequestMapping("/groupon/activity")
@Api(description = "S2B的拼团活动服务", tags = "GrouponActivityController")
public class GrouponActivityController {

    @Autowired
    private GrouponActivityQueryProvider grouponActivityQueryProvider;

    @Autowired
    private GrouponActivitySaveProvider grouponActivitySaveProvider;

    @Autowired
    private GrouponGoodsInfoSaveProvider grouponGoodsInfoSaveProvider;

    @Autowired
    private GrouponGoodsInfoQueryProvider grouponGoodsInfoQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private EsGrouponActivityQueryProvider esGrouponActivityQueryProvider;

    @Autowired
    private EsGrouponActivityProvider esGrouponActivityProvider;

    /**
     * 分页查询拼团活动
     *
     * @param pageRequest 商品 {@link DistributionGoodsPageRequest}
     * @return 拼团活动分页
     */
    @ApiOperation(value = "分页查询拼团活动")
    @RequestMapping(value = "page", method = RequestMethod.POST)
    public BaseResponse<EsGrouponActivityPageResponse> page(@RequestBody @Valid EsGrouponActivityPageRequest
                                                                    pageRequest) {
        pageRequest.setStoreId(Objects.toString(commonUtil.getStoreId(), pageRequest.getStoreId()));
        pageRequest.setDelFlag(DeleteFlag.NO);

        //grouponActivityQueryProvider.page4Manager(pageRequest);
        return esGrouponActivityQueryProvider.page(pageRequest);
    }

    /**
     * 查询拼团活动
     *
     * @param activityId 拼团活动id
     */
    @ApiOperation(value = "查询拼团活动")
    @RequestMapping(value = "/{activityId}", method = RequestMethod.GET)
    public BaseResponse<GrouponActivityByIdResponse> info(@PathVariable String activityId) {

        // 1.查询拼团活动
        BaseResponse<GrouponActivityByIdResponse> response = grouponActivityQueryProvider.getById(
                new GrouponActivityByIdRequest(activityId));

        // 2.查询拼团活动商品
        GrouponGoodsInfoListRequest request = new GrouponGoodsInfoListRequest();
        request.setGrouponActivityId(activityId);
        List<GrouponGoodsInfoVO> goodsInfos = grouponGoodsInfoQueryProvider.list(
                request).getContext().getGrouponGoodsInfoVOList();
        response.getContext().setGrouponGoodsInfos(goodsInfos);
        return response;
    }

    /**
     * 批量审核拼团活动
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "批量审核通过拼团活动")
    @RequestMapping(value = "/batch-check", method = RequestMethod.POST)
    public BaseResponse batchCheckMarketing(@RequestBody @Valid GrouponActivityBatchCheckRequest request) {
        grouponActivitySaveProvider.batchCheckMarketing(request);
        GrouponGoodsInfoModifyAuditStatusRequest modifyAuditStatusRequest = new GrouponGoodsInfoModifyAuditStatusRequest();
        modifyAuditStatusRequest.setAuditStatus(AuditStatus.CHECKED);
        modifyAuditStatusRequest.setGrouponActivityIds(request.getGrouponActivityIdList());
        grouponGoodsInfoSaveProvider.modifyAuditStatusByGrouponActivityIds(modifyAuditStatusRequest);
        //同步es
        EsGrouponActivityBatchCheckRequest checkRequest = new EsGrouponActivityBatchCheckRequest();
        checkRequest.setGrouponActivityIdList(request.getGrouponActivityIdList());
        esGrouponActivityProvider.batchCheckMarketing(checkRequest);
        //记录操作日志
        operateLogMQUtil.convertAndSend("营销", "批量审核通过拼团活动",
                "批量审核活动商品通过");
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 驳回拼团活动
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "驳回拼团活动")
    @RequestMapping(value = "/refuse", method = RequestMethod.POST)
    public BaseResponse refuseCheckMarketing(@RequestBody @Valid GrouponActivityRefuseRequest request) {
        grouponActivitySaveProvider.refuseCheckMarketing(request);
        GrouponGoodsInfoModifyAuditStatusRequest modifyAuditStatusRequest = new GrouponGoodsInfoModifyAuditStatusRequest();
        modifyAuditStatusRequest.setAuditStatus(AuditStatus.NOT_PASS);
        modifyAuditStatusRequest.setGrouponActivityIds(Collections.singletonList(request.getGrouponActivityId()));
        grouponGoodsInfoSaveProvider.modifyAuditStatusByGrouponActivityIds(modifyAuditStatusRequest);

        //同步es
        EsGrouponActivityRefuseRequest refuseRequest = new EsGrouponActivityRefuseRequest();
        BeanUtils.copyProperties(request,refuseRequest);
        esGrouponActivityProvider.batchRefuseMarketing(refuseRequest);


        //记录操作日志
        operateLogMQUtil.convertAndSend("营销", "驳回拼团活动",
                "活动ID" + request.getGrouponActivityId() + "原因:" + request.getAuditReason());

        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 批量修改拼团活动精选状态
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "批量修改拼团活动精选状态")
    @RequestMapping(value = "/batch-sticky", method = RequestMethod.POST)
    public BaseResponse batchStickyMarketing(@RequestBody GrouponActivityBatchStickyRequest request) {
        grouponActivitySaveProvider.batchStickyMarketing(request);
        GrouponGoodsInfoModifyStickyRequest modifyStickyRequest = new GrouponGoodsInfoModifyStickyRequest();
        modifyStickyRequest.setGrouponActivityIds(request.getGrouponActivityIdList());
        modifyStickyRequest.setSticky(request.getSticky());
        grouponGoodsInfoSaveProvider.modifyStickyByGrouponActivityIds(modifyStickyRequest);

        //同步es
        EsGrouponActivityBatchStickyRequest req = new EsGrouponActivityBatchStickyRequest();
        BeanUtils.copyProperties(request, req);
        esGrouponActivityProvider.batchStickyMarketing(req);
        //记录操作日志
        String stickyStr = request.getSticky() ? "是精选" : "不是精选";
        operateLogMQUtil.convertAndSend("营销", "批量修改拼团活动精选状态",
                "批量修改拼团活动精选状态为:" + stickyStr);
        return BaseResponse.SUCCESSFUL();
    }

    @ApiOperation(value = "根据拼团活动ID删除拼团活动、拼团活动商品")
    @RequestMapping(value = "/delete-by-id", method = RequestMethod.POST)
    public BaseResponse deleteById(@RequestBody GrouponActivityDelByIdRequest request) {
        grouponActivitySaveProvider.deleteById(request);
        grouponGoodsInfoSaveProvider.deleteByGrouponActivityId(new GrouponGoodsInfoDeleteByGrouponActivityIdRequest(request.getGrouponActivityId()));

        //同步删除es
        EsGrouponActivityDelByIdRequest esGrouponActivityDelByIdRequest = new EsGrouponActivityDelByIdRequest(request.getGrouponActivityId());
        esGrouponActivityProvider.deleteById(esGrouponActivityDelByIdRequest);

        //记录操作日志
        operateLogMQUtil.convertAndSend("营销", "根据拼团活动ID删除拼团活动、拼团活动商品",
                "活动ID:" + request.getGrouponActivityId());
        return BaseResponse.SUCCESSFUL();
    }
}
