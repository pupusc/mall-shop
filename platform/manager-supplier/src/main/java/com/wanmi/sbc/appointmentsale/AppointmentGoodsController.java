package com.wanmi.sbc.appointmentsale;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
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
import java.util.Collections;
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
            List<GoodsInfoVO> goodsInfos = goodsInfoQueryProvider.listByCondition(GoodsInfoListByConditionRequest.builder().likeGoodsName(request.getGoodsName()).storeId(commonUtil.getStoreId()).build()).getContext().getGoodsInfos();
            if (CollectionUtils.isEmpty(goodsInfos)) {
                return BaseResponse.success(AppointmentResponse.builder().build());
            }
            request.setGoodsInfoIds(goodsInfos.stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList()));
        }
        request.setStoreIds(Collections.singletonList(commonUtil.getStoreId()));
        return appointmentSaleGoodsQueryProvider.pageBoss(request);
    }
}
