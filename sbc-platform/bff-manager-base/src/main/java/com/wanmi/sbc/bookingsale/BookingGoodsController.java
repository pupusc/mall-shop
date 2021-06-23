package com.wanmi.sbc.bookingsale;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.store.ListStoreByIdsRequest;
import com.wanmi.sbc.customer.api.request.store.ListStoreByNameRequest;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.api.provider.bookingsalegoods.BookingSaleGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.bookingsale.BookingGoodsInfoSimplePageRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByConditionRequest;
import com.wanmi.sbc.goods.api.response.bookingsalegoods.BookingResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * S2B的预售商品服务
 */
@RestController
@RequestMapping("/booking/goods")
@Api(description = "S2B的预售商品服务", tags = "BookingGoodsController")
public class BookingGoodsController {


    @Autowired
    private BookingSaleGoodsQueryProvider bookingSaleGoodsQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private StoreQueryProvider storeQueryProvider;


    /**
     * 分页查询预售活动
     *
     * @param request 商品 {@link BookingGoodsInfoSimplePageRequest}
     * @return 预售活动分页
     */
    @ApiOperation(value = "分页查询预约活动")
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public BaseResponse<BookingResponse> page(@RequestBody @Valid BookingGoodsInfoSimplePageRequest request) {
        if (StringUtils.isNotEmpty(request.getGoodsName())) {
            List<GoodsInfoVO> goodsInfos = goodsInfoQueryProvider.listByCondition(GoodsInfoListByConditionRequest.builder().likeGoodsName(request.getGoodsName()).build()).getContext().getGoodsInfos();
            if (CollectionUtils.isEmpty(goodsInfos)) {
                return BaseResponse.success(BookingResponse.builder().build());
            }
            request.setGoodsInfoIds(goodsInfos.stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList()));
        }
        if (StringUtils.isNotEmpty(request.getStoreName())) {
            List<StoreVO> storeVOList = storeQueryProvider.listByName(ListStoreByNameRequest.builder().storeName(request.getStoreName()).build()).getContext()
                    .getStoreVOList();
            if (CollectionUtils.isEmpty(storeVOList)) {
                return BaseResponse.success(BookingResponse.builder().build());
            }
            request.setStoreIds(storeVOList.stream().map(StoreVO::getStoreId).collect(Collectors.toList()));
        }

        BookingResponse response = bookingSaleGoodsQueryProvider.pageBoss(request).getContext();
        if (Objects.isNull(response) || Objects.isNull(response.getBookingVOMicroServicePage()) || CollectionUtils.isEmpty(response.getBookingVOMicroServicePage().getContent())) {
            return BaseResponse.success(response);
        }
        Map<Long, StoreVO> storeVOMap = storeQueryProvider.listByIds(ListStoreByIdsRequest.builder().storeIds(
                response.getBookingVOMicroServicePage().getContent().stream().map(s -> s.getBookingSaleGoods().getStoreId()).collect(Collectors.toList())
        ).build()).getContext().getStoreVOList().stream().collect(Collectors.toMap(StoreVO::getStoreId, v -> v));
        response.getBookingVOMicroServicePage().getContent().forEach(s -> s.getBookingSaleGoods().setStoreName(storeVOMap.get(s.getBookingSaleGoods().getStoreId()).getStoreName()));
        return BaseResponse.success(response);
    }
}
