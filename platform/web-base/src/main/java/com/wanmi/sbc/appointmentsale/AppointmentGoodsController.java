package com.wanmi.sbc.appointmentsale;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.api.provider.appointmentsalegoods.AppointmentSaleGoodsQueryProvider;
import com.wanmi.sbc.goods.api.request.appointmentsale.AppointmentGoodsInfoSimplePageRequest;
import com.wanmi.sbc.goods.api.response.appointmentsalegoods.AppointmentGoodsResponse;
import com.wanmi.sbc.goods.bean.vo.AppointmentSaleGoodsVO;
import com.wanmi.sbc.marketing.api.provider.market.MarketingQueryProvider;
import com.wanmi.sbc.marketing.api.request.market.MarketingGoodsForXsiteRequest;
import com.wanmi.sbc.marketing.bean.enums.MarketingLevelType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
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
    private MarketingQueryProvider marketingQueryProvider;

    /**
     * 分页查询预约活动
     *
     * @param request 商品 {@link AppointmentGoodsInfoSimplePageRequest}
     * @return 预约活动分页
     */
    @ApiOperation(value = "分页查询预约活动")
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public BaseResponse<AppointmentGoodsResponse> page(@RequestBody @Valid AppointmentGoodsInfoSimplePageRequest request) {
        request.setHavSpecTextFlag(Boolean.TRUE);
        List<AppointmentSaleGoodsVO> saleGoodsVOList = appointmentSaleGoodsQueryProvider.pageAppointmentGoodsInfo(request)
                .getContext().getPage().getContent();
        // 按营销优先级过滤
        if (CollectionUtils.isNotEmpty(saleGoodsVOList)) {
            List<String> ids = saleGoodsVOList.stream().map(vo -> vo.getGoodsInfoId()).collect(Collectors.toList());
            MarketingGoodsForXsiteRequest marketingRequest = MarketingGoodsForXsiteRequest.builder()
                    .goodsInfoIds(ids)
                    .marketingLevelType(MarketingLevelType.APPOINTMENT_OR_BOOKING).build();
            // 获取满足当前优先级的商品id
            List<String> goodsInfoIds = marketingQueryProvider.queryForXsite(marketingRequest).getContext().getGoodsInfoIds();
            saleGoodsVOList = saleGoodsVOList.stream().filter(vo -> goodsInfoIds.contains(vo.getGoodsInfoId())).collect(Collectors.toList());
        }
        AppointmentGoodsResponse response = AppointmentGoodsResponse.builder().page(new MicroServicePage<>(saleGoodsVOList)).build();
        return BaseResponse.success(response);
    }
}
