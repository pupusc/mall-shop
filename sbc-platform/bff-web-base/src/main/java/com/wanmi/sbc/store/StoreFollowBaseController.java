package com.wanmi.sbc.store;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.annotation.MultiSubmit;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.follow.StoreCustomerFollowProvider;
import com.wanmi.sbc.customer.api.provider.follow.StoreCustomerFollowQueryProvider;
import com.wanmi.sbc.customer.api.provider.growthvalue.CustomerGrowthValueProvider;
import com.wanmi.sbc.customer.api.provider.points.CustomerPointsDetailSaveProvider;
import com.wanmi.sbc.customer.api.request.follow.*;
import com.wanmi.sbc.customer.api.request.follow.validGroups.StoreFollowAdd;
import com.wanmi.sbc.customer.api.request.follow.validGroups.StoreFollowDelete;
import com.wanmi.sbc.customer.api.request.follow.validGroups.StoreFollowFilter;
import com.wanmi.sbc.customer.api.request.growthvalue.CustomerGrowthValueAddRequest;
import com.wanmi.sbc.customer.api.request.points.CustomerPointsDetailAddRequest;
import com.wanmi.sbc.customer.bean.enums.GrowthValueServiceType;
import com.wanmi.sbc.customer.bean.enums.OperateType;
import com.wanmi.sbc.customer.bean.enums.PointsServiceType;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * 店铺关注Controller
 * Created by daiyitian on 17/4/12.
 */
@Api(tags = "StoreFollowBaseController", description = "店铺关注 API")
@RestController
@RequestMapping("/store")
public class StoreFollowBaseController {

    @Autowired
    private StoreCustomerFollowQueryProvider storeCustomerFollowQueryProvider;

    @Autowired
    private StoreCustomerFollowProvider storeCustomerFollowProvider;

    @Autowired
    private CustomerGrowthValueProvider customerGrowthValueProvider;

    @Autowired
    private CustomerPointsDetailSaveProvider customerPointsDetailSaveProvider;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 新增店铺关注
     * @param request 数据
     * @return 结果
     */
    @ApiOperation(value = "新增店铺关注")
    @RequestMapping(value = "/storeFollow", method = RequestMethod.POST)
    @MultiSubmit
    public BaseResponse add(@Validated({StoreFollowAdd.class}) @RequestBody StoreCustomerFollowRequest request) {
        StoreCustomerFollowAddRequest addRequest = new StoreCustomerFollowAddRequest();
        KsBeanUtil.copyPropertiesThird(request, addRequest);
        addRequest.setCustomerId(commonUtil.getOperatorId());
        addRequest.setTerminalSource(commonUtil.getTerminal());
        storeCustomerFollowProvider.addStoreCustomerFollow(addRequest);

        // 增加成长值
        customerGrowthValueProvider.increaseGrowthValue(CustomerGrowthValueAddRequest.builder()
                .customerId(commonUtil.getOperatorId())
                .type(OperateType.GROWTH)
                .serviceType(GrowthValueServiceType.FOCUSONSTORE)
                .content(JSONObject.toJSONString(Collections.singletonMap("storeId", request.getStoreId())))
                .build());
        // 增加积分
        customerPointsDetailSaveProvider.add(CustomerPointsDetailAddRequest.builder()
                .customerId(commonUtil.getOperatorId())
                .type(OperateType.GROWTH)
                .serviceType(PointsServiceType.FOCUSONSTORE)
                .content(JSONObject.toJSONString(Collections.singletonMap("storeId", request.getStoreId())))
                .build());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 取消店铺关注
     * @param request 数据
     * @return 结果
     */
    @ApiOperation(value = "取消店铺关注")
    @RequestMapping(value = "/storeFollow", method = RequestMethod.DELETE)
    public BaseResponse delete(@Validated({StoreFollowDelete.class}) @RequestBody StoreCustomerFollowRequest request) {
        storeCustomerFollowProvider.deleteStoreCustomerFollow(
                StoreCustomerFollowDeleteRequest.builder()
                        .customerId(commonUtil.getOperatorId())
                        .storeIds(request.getStoreIds())
                        .build()
        );
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 批量验证店铺是否已关注
     * @return 结果，相应的SkuId就是已收藏的商品ID
     */
    @ApiOperation(value = "批量验证店铺是否已关注")
    @RequestMapping(value = "/isStoreFollow", method = RequestMethod.POST)
    public BaseResponse<List<Long>> isGoodsFollow(@Validated({StoreFollowFilter.class}) @RequestBody StoreCustomerFollowRequest request) {
        return BaseResponse.success(storeCustomerFollowQueryProvider.queryStoreCustomerFollowByStoreIds(
                StoreCustomerFollowExistsBatchRequest.builder()
                        .customerId(commonUtil.getOperatorId())
                        .storeIds(request.getStoreIds())
                        .build()
        ).getContext().getStoreIds());
    }

    /**
     * 统计客户关注数量
     * @return 关注数量
     */
    @ApiOperation(value = "统计客户关注数量")
    @RequestMapping(value = "/storeFollowNum", method = RequestMethod.GET)
    public BaseResponse<Long> storeFollowNum() {
        return BaseResponse.success(
                storeCustomerFollowQueryProvider.queryStoreCustomerFollowCountByCustomerId(
                        StoreCustomerFollowCountRequest.builder().customerId(commonUtil.getOperatorId()).build()
                ).getContext().getFollowNum());
    }
}
