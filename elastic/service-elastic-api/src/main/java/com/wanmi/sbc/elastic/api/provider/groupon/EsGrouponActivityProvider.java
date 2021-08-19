package com.wanmi.sbc.elastic.api.provider.groupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.request.groupon.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>拼团分类信息表查询服务Provider</p>
 *
 * @author groupon
 * @date 2019-05-15 14:13:58
 */
@FeignClient(value = "${application.elastic.name}", contextId = "EsGrouponActivityProvider")
public interface EsGrouponActivityProvider {


    /**
     * 新增拼团活动
     * @param request
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/groupon/activity/add")
    BaseResponse add(@RequestBody @Valid EsGrouponActivityAddReqquest request);

    /**
     * 新增拼团活动
     * @param request
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/groupon/activity/delete-by-id")
    BaseResponse deleteById(@RequestBody @Valid EsGrouponActivityDelByIdRequest request);

    /**
     * 初始化拼团数据
     *
     * @param request
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/groupon/activity/init")
    BaseResponse init(@RequestBody @Valid EsGrouponActivityPageRequest request);


    /**
     * 批量设置精选
     * @param request
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/groupon/activity/batch-sticky")
    BaseResponse batchStickyMarketing(@RequestBody @Valid EsGrouponActivityBatchStickyRequest request);

    /**
     * 驳回拼团信息
     * @param request
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/groupon/activity/batch-refuse")
    BaseResponse batchRefuseMarketing(@RequestBody @Valid EsGrouponActivityRefuseRequest request);

    /**
     * 批量通过审核
     * @param request
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/groupon/activity/batch-check")
    BaseResponse batchCheckMarketing(@RequestBody @Valid EsGrouponActivityBatchCheckRequest request);

}

