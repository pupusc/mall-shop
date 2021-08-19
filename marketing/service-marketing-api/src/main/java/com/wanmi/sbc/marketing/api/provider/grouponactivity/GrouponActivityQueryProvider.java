package com.wanmi.sbc.marketing.api.provider.grouponactivity;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.grouponactivity.*;
import com.wanmi.sbc.marketing.api.response.grouponactivity.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;


/**
 * <p>拼团活动信息表查询服务Provider</p>
 *
 * @author groupon
 * @date 2019-05-15 14:02:38
 */
@FeignClient(value = "${application.marketing.name}", contextId = "GrouponActivityQueryProvider")
public interface GrouponActivityQueryProvider {


    /**
     * 根据商品ids，查询正在进行的活动的商品ids
     */
    @PostMapping("/marketing/${application.marketing.version}/groupon/activity/list-activitying-spu-ids")
    BaseResponse<GrouponActivityListSpuIdResponse> listActivityingSpuIds(@RequestBody @Valid GrouponActivityListSpuIdRequest request);

    /**
     * 分页查询拼团活动信息表API
     *
     * @param grouponActivityPageReq 分页请求参数和筛选对象 {@link GrouponActivityPageRequest}
     * @return 拼团活动信息表分页列表信息带拼团价信息 {@link GrouponActivityPageResponse}
     * @author groupon
     */
    @PostMapping("/marketing/${application.marketing.version}/groupon/activity/page/manager")
    BaseResponse<GrouponActivityPage4MangerResponse> page4Manager(@RequestBody @Valid GrouponActivityPageRequest
                                                           grouponActivityPageReq);

    /**
     * 分页查询拼团活动信息表API
     *
     * @param grouponActivityPageReq 分页请求参数和筛选对象 {@link GrouponActivityPageRequest}
     * @return 拼团活动信息表分页列表信息 {@link GrouponActivityPageResponse}
     * @author groupon
     */
    @PostMapping("/marketing/${application.marketing.version}/groupon/activity/page")
    BaseResponse<GrouponActivityPageResponse> page(@RequestBody @Valid GrouponActivityPageRequest
                                                           grouponActivityPageReq);

    /**
     * 列表查询拼团活动信息表API
     *
     * @param grouponActivityListReq 列表请求参数和筛选对象 {@link GrouponActivityListRequest}
     * @return 拼团活动信息表的列表信息 {@link GrouponActivityListResponse}
     * @author groupon
     */
    @PostMapping("/marketing/${application.marketing.version}/groupon/activity/list")
    BaseResponse<GrouponActivityListResponse> list(@RequestBody @Valid GrouponActivityListRequest
                                                           grouponActivityListReq);

    /**
     * 单个查询拼团活动信息表API
     *
     * @param grouponActivityByIdRequest 单个查询拼团活动信息表请求参数 {@link GrouponActivityByIdRequest}
     * @return 拼团活动信息表详情 {@link GrouponActivityByIdResponse}
     * @author groupon
     */
    @PostMapping("/marketing/${application.marketing.version}/groupon/activity/get-by-id")
    BaseResponse<GrouponActivityByIdResponse> getById(@RequestBody @Valid GrouponActivityByIdRequest
                                                              grouponActivityByIdRequest);

    /**
     * 根据活动id，查询活动是否包邮
     */
    @PostMapping("/marketing/${application.marketing.version}/groupon/activity/get-free-delivery-by-id")
    BaseResponse<GrouponActivityFreeDeliveryByIdResponse> getFreeDeliveryById(
            @RequestBody @Valid GrouponActivityFreeDeliveryByIdRequest request);

    /**
     * 根据商品ids，查询正在进行的活动
     */
    @PostMapping("/marketing/${application.marketing.version}/groupon/activity/list-activitying-by-goods-info-ids")
    BaseResponse<GrouponActivityingByGoodsInfoIdsResponse> listActivityingByGoodsInfoIds(
            @RequestBody @Valid GrouponActivityingByGoodsInfoIdsRequest request);


    @PostMapping("/marketing/${application.marketing.version}/groupon/activity/list-activity-by-unique-storeId")
    BaseResponse<GrouponActivityByUniqueStoreIdResponse> querySupplierNum(
            @RequestBody @Valid GrouponActivityListRequest request);
 }

