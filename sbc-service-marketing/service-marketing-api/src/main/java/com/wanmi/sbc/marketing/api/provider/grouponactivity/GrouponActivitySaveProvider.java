package com.wanmi.sbc.marketing.api.provider.grouponactivity;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.grouponactivity.*;
import com.wanmi.sbc.marketing.api.response.grouponactivity.GrouponActivityAddResponse;
import com.wanmi.sbc.marketing.api.response.grouponactivity.GrouponActivityModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>拼团活动信息表保存服务Provider</p>
 *
 * @author groupon
 * @date 2019-05-15 14:02:38
 */
@FeignClient(value = "${application.marketing.name}", contextId = "GrouponActivitySaveProvider")
public interface GrouponActivitySaveProvider {

    /**
     * 新增拼团活动信息表API
     *
     * @param grouponActivityAddRequest 拼团活动信息表新增参数结构 {@link GrouponActivityAddRequest}
     * @return 新增的拼团活动信息表信息 {@link GrouponActivityAddResponse}
     * @author groupon
     */
    @PostMapping("/marketing/${application.marketing.version}/groupon/activity/add")
    BaseResponse<GrouponActivityAddResponse> add(@RequestBody @Valid GrouponActivityAddRequest
                                                         grouponActivityAddRequest);

    /**
     * 修改拼团活动信息表API
     *
     * @param grouponActivityModifyRequest 拼团活动信息表修改参数结构 {@link GrouponActivityModifyRequest}
     * @return 修改的拼团活动信息表信息 {@link GrouponActivityModifyResponse}
     * @author groupon
     */
    @PostMapping("/marketing/${application.marketing.version}/groupon/activity/modify")
    BaseResponse<GrouponActivityModifyResponse> modify(@RequestBody @Valid GrouponActivityModifyRequest
                                                               grouponActivityModifyRequest);

    /**
     * 单个删除拼团活动信息表API
     *
     * @param grouponActivityDelByIdRequest 单个删除参数结构 {@link GrouponActivityDelByIdRequest}
     * @return 删除结果 {@link BaseResponse}
     * @author groupon
     */
    @PostMapping("/marketing/${application.marketing.version}/groupon/activity/delete-by-id")
    BaseResponse deleteById(@RequestBody @Valid GrouponActivityDelByIdRequest grouponActivityDelByIdRequest);

    /**
     * 批量删除拼团活动信息表API
     *
     * @param grouponActivityDelByIdListRequest 批量删除参数结构 {@link GrouponActivityDelByIdListRequest}
     * @return 删除结果 {@link BaseResponse}
     * @author groupon
     */
    @PostMapping("/marketing/${application.marketing.version}/groupon/activity/delete-by-id-list")
    BaseResponse deleteByIdList(@RequestBody @Valid GrouponActivityDelByIdListRequest grouponActivityDelByIdListRequest);


    /**
     * 批量审核拼团活动
     *
     * @param request
     */
    @PostMapping("/marketing/${application.marketing.version}/groupon/activity/batch-check")
    BaseResponse batchCheckMarketing(@RequestBody @Valid GrouponActivityBatchCheckRequest request);


    /**
     * 驳回拼团活动
     *
     * @param request
     */
    @PostMapping("/marketing/${application.marketing.version}/groupon/activity/refuse")
    BaseResponse refuseCheckMarketing(@RequestBody @Valid GrouponActivityRefuseRequest request);

    /**
     * 批量修改拼团活动精选状态
     *
     * @param request
     */
    @PostMapping("/marketing/${application.marketing.version}/groupon/activity/batch-sticky")
    BaseResponse batchStickyMarketing(@RequestBody @Valid GrouponActivityBatchStickyRequest request);


    /**
     * 根据不同拼团状态更新不同的统计数据（已成团、待成团、团失败人数）
     *
     * @param request
     */
    @PostMapping("/marketing/${application.marketing.version}/groupon/activity/modify-statistics-num-by-id")
    BaseResponse modifyStatisticsNumById(@RequestBody @Valid GrouponActivityModifyStatisticsNumByIdRequest request);

    /**
     * 更新拼团活动待成团人数
     *
     * @param modifyRequest 请求参数bean {@link GrouponActivityWaitNumModifyRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/groupon/activity/modify-wait-groupon-num-by-id")
    BaseResponse modifyWaitGrouponNumById(@RequestBody GrouponActivityWaitNumModifyRequest modifyRequest);
}

