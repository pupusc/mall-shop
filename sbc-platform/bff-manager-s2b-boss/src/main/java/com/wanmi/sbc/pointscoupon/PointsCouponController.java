package com.wanmi.sbc.pointscoupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.marketing.api.provider.pointscoupon.PointsCouponQueryProvider;
import com.wanmi.sbc.marketing.api.provider.pointscoupon.PointsCouponSaveProvider;
import com.wanmi.sbc.marketing.api.request.pointscoupon.*;
import com.wanmi.sbc.marketing.api.response.pointscoupon.PointsCouponByIdResponse;
import com.wanmi.sbc.marketing.api.response.pointscoupon.PointsCouponModifyResponse;
import com.wanmi.sbc.marketing.api.response.pointscoupon.PointsCouponPageResponse;
import com.wanmi.sbc.mq.producer.ManagerBaseProducerService;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;


@Api(description = "积分兑换券表管理API", tags = "PointsCouponController")
@RestController
@RequestMapping(value = "/pointscoupon")
public class PointsCouponController {

    @Autowired
    private PointsCouponQueryProvider pointsCouponQueryProvider;

    @Autowired
    private PointsCouponSaveProvider pointsCouponSaveProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private ManagerBaseProducerService managerBaseProducerService;

    @ApiOperation(value = "分页查询积分兑换券表")
    @PostMapping("/page")
    public BaseResponse<PointsCouponPageResponse> getPage(@RequestBody @Valid PointsCouponPageRequest pageReq) {
        pageReq.setDelFlag(DeleteFlag.NO);
        pageReq.putSort("pointsCouponId", "desc");
        return pointsCouponQueryProvider.page(pageReq);
    }

    @ApiOperation(value = "根据id查询积分兑换券表")
    @GetMapping("/{pointsCouponId}")
    public BaseResponse<PointsCouponByIdResponse> getById(@PathVariable Long pointsCouponId) {
        if (pointsCouponId == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        PointsCouponByIdRequest idReq = new PointsCouponByIdRequest();
        idReq.setPointsCouponId(pointsCouponId);
        return pointsCouponQueryProvider.getById(idReq);
    }

    @ApiOperation(value = "批量新增积分兑换券")
    @PostMapping("/batchAdd")
    public BaseResponse batchAdd(@RequestBody @Valid PointsCouponAddListRequest request) {
        List<PointsCouponAddRequest> addRequestList = request.getPointsCouponAddRequestList();
        if (addRequestList.size() == 0) {
            throw new SbcRuntimeException("K-130001");
        }
        addRequestList.forEach(addRequest -> {
            addRequest.setBeginTime(request.getBeginTime());
            addRequest.setEndTime(request.getEndTime());
            addRequest.setStatus(EnableStatus.ENABLE);
            addRequest.setExchangeCount((long) 0);
            addRequest.setSellOutFlag(BoolFlag.NO);
            addRequest.setDelFlag(DeleteFlag.NO);
            addRequest.setCreatePerson(commonUtil.getOperatorId());
            addRequest.setCreateTime(LocalDateTime.now());
        });
        operateLogMQUtil.convertAndSend("营销", "积分商城", "添加积分商品");
        List<String> activityIdList = pointsCouponSaveProvider.batchAdd(request).getContext().getActivityIdList();
        if (CollectionUtils.isNotEmpty(activityIdList)){
            managerBaseProducerService.sendMQForAddCouponActivity(activityIdList);
        }
        return BaseResponse.SUCCESSFUL();
    }

    @ApiOperation(value = "修改积分兑换券表")
    @PutMapping("/modify")
    public BaseResponse<PointsCouponModifyResponse> modify(@RequestBody @Valid PointsCouponModifyRequest modifyReq) {
        modifyReq.setUpdatePerson(commonUtil.getOperatorId());
        modifyReq.setUpdateTime(LocalDateTime.now());
        return pointsCouponSaveProvider.modify(modifyReq);
    }

    @ApiOperation(value = "启用停用积分兑换券")
    @PutMapping("/modifyStatus")
    public BaseResponse modifyStatus(@RequestBody @Valid PointsCouponSwitchRequest request) {
        request.setUpdatePerson(commonUtil.getOperatorId());
        request.setUpdateTime(LocalDateTime.now());
        return pointsCouponSaveProvider.modifyStatus(request);
    }

    @ApiOperation(value = "根据id删除积分兑换券表")
    @DeleteMapping("/{pointsCouponId}")
    public BaseResponse deleteById(@PathVariable Long pointsCouponId) {
        if (pointsCouponId == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        // 删除积分兑换券
        PointsCouponDelByIdRequest delByIdReq = new PointsCouponDelByIdRequest();
        delByIdReq.setPointsCouponId(pointsCouponId);
        delByIdReq.setOperatorId(commonUtil.getOperatorId());
        return pointsCouponSaveProvider.deleteById(delByIdReq);
    }

}
