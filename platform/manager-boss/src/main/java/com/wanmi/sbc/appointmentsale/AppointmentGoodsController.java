package com.wanmi.sbc.appointmentsale;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.store.ListStoreByIdsRequest;
import com.wanmi.sbc.customer.api.request.store.ListStoreByNameRequest;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.api.provider.appointmentsalegoods.AppointmentSaleGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.appointmentsale.AppointmentGoodsInfoSimplePageRequest;
import com.wanmi.sbc.goods.api.request.info.DistributionGoodsPageRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByConditionRequest;
import com.wanmi.sbc.goods.api.response.appointmentsalegoods.AppointmentResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * SBC的预约商品服务
 */
@RestController
@RequestMapping("/appointment/goods")
@Api(description = "S2B的预约商品服务", tags = "AppointmentGoodsController")
public class AppointmentGoodsController {

    @Autowired
    private AppointmentSaleGoodsQueryProvider appointmentSaleGoodsQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 分页查询预约活动
     *
     * @param request 商品 {@link DistributionGoodsPageRequest}
     * @return 预约活动分页
     */
    @ApiOperation(value = "分页查询预约活动")
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public BaseResponse<AppointmentResponse> page(@RequestBody @Valid AppointmentGoodsInfoSimplePageRequest request) {
        if (StringUtils.isNotEmpty(request.getGoodsName())) {
            List<GoodsInfoVO> goodsInfos = goodsInfoQueryProvider.listByCondition(GoodsInfoListByConditionRequest.builder().likeGoodsName(request.getGoodsName()).build()).getContext().getGoodsInfos();
            if (CollectionUtils.isEmpty(goodsInfos)) {
                return BaseResponse.success(AppointmentResponse.builder().build());
            }
            request.setGoodsInfoIds(goodsInfos.stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList()));
        }
        if (StringUtils.isNotEmpty(request.getStoreName())) {
            List<StoreVO> storeVOList = storeQueryProvider.listByName(ListStoreByNameRequest.builder().storeName(request.getStoreName()).build()).getContext()
                    .getStoreVOList();
            if (CollectionUtils.isEmpty(storeVOList)) {
                return BaseResponse.success(AppointmentResponse.builder().build());
            }
            request.setStoreIds(storeVOList.stream().map(StoreVO::getStoreId).collect(Collectors.toList()));
        }
        AppointmentResponse response = appointmentSaleGoodsQueryProvider.pageBoss(request).getContext();
        if (Objects.isNull(response) || Objects.isNull(response.getAppointmentVOPage()) || CollectionUtils.isEmpty(response.getAppointmentVOPage().getContent())) {
            return BaseResponse.success(response);
        }
        Map<Long, StoreVO> storeVOMap = storeQueryProvider.listByIds(ListStoreByIdsRequest.builder().storeIds(
                response.getAppointmentVOPage().getContent().stream().map(s -> s.getAppointmentSaleGoods().getStoreId()).collect(Collectors.toList())
        ).build()).getContext().getStoreVOList().stream().collect(Collectors.toMap(StoreVO::getStoreId, v -> v));
        response.getAppointmentVOPage().getContent().forEach
                (s -> {
                    s.getAppointmentSaleGoods().setStoreName(storeVOMap.containsKey(s.getAppointmentSaleGoods().getStoreId()) ? storeVOMap.get(s.getAppointmentSaleGoods().getStoreId()).getStoreName() : "");
                    s.setServerTime(LocalDateTime.now());
                });
        return BaseResponse.success(response);
    }
}
