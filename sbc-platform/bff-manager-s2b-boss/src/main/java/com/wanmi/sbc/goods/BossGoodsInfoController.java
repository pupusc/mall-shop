package com.wanmi.sbc.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.constant.CustomerErrorCode;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.elastic.api.provider.sku.EsSkuQueryProvider;
import com.wanmi.sbc.elastic.api.request.sku.EsSkuPageRequest;
import com.wanmi.sbc.elastic.api.response.sku.EsSkuPageResponse;
import com.wanmi.sbc.goods.api.response.price.GoodsIntervalPriceByCustomerIdResponse;
import com.wanmi.sbc.goods.api.response.price.GoodsIntervalPriceResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.intervalprice.GoodsIntervalPriceService;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingLevelPluginProvider;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingLevelGoodsListFilterRequest;
import com.wanmi.sbc.system.service.SystemPointsConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午4:08 2019/2/26
 * @Description: 平台端单品服务
 */
@RestController
@Api(description = "平台端单品服务", tags = "BossGoodsInfoController")
public class BossGoodsInfoController {


    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private GoodsIntervalPriceService goodsIntervalPriceService;


    @Autowired
    private MarketingLevelPluginProvider marketingLevelPluginProvider;

    @Autowired
    private EsSkuQueryProvider esSkuQueryProvider;

    @Autowired
    private SystemPointsConfigService systemPointsConfigService;

    /**
     * 分页显示商品
     *
     * @param queryRequest 商品
     * @return 商品详情
     */
    @ApiOperation(value = "分页显示商品")
    @RequestMapping(value = "/goods/skus", method = RequestMethod.POST)
    public BaseResponse<EsSkuPageResponse> skuList(@RequestBody EsSkuPageRequest queryRequest) {
        //获取会员
        CustomerGetByIdResponse customer = null;
        if (StringUtils.isNotBlank(queryRequest.getCustomerId())) {
            customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(queryRequest.getCustomerId())
            ).getContext();
            if (Objects.isNull(customer)) {
                throw new SbcRuntimeException(CustomerErrorCode.NOT_EXIST);
            }
        }

        // 代客下单时，积分开关开启 并且 积分使用方式是订单抵扣，此时不需要过滤积分价商品
        if (Boolean.TRUE.equals(queryRequest.getIntegralPriceFlag()) && !systemPointsConfigService.isGoodsPoint()){
            queryRequest.setIntegralPriceFlag(Boolean.FALSE);
        }

        //按创建时间倒序、ID升序
        queryRequest.putSort("addedTime", SortType.DESC.toValue());
        queryRequest.setDelFlag(DeleteFlag.NO.toValue());//可用
        queryRequest.setAuditStatus(CheckStatus.CHECKED);//已审核
        queryRequest.setVendibility(Constants.yes);
        queryRequest.setShowPointFlag(Boolean.TRUE);
        queryRequest.setShowProviderInfoFlag(Boolean.TRUE);
        queryRequest.setFillLmInfoFlag(Boolean.TRUE);
        queryRequest.setShowPointFlag(Boolean.TRUE);
        EsSkuPageResponse response = esSkuQueryProvider.page(queryRequest).getContext();

        List<GoodsInfoVO> goodsInfoVOList = response.getGoodsInfoPage().getContent();

        if (customer != null && StringUtils.isNotBlank(customer.getCustomerId())) {
            GoodsIntervalPriceByCustomerIdResponse priceResponse =
                    goodsIntervalPriceService.getGoodsIntervalPriceVOList(goodsInfoVOList, customer.getCustomerId());
            response.setGoodsIntervalPrices(priceResponse.getGoodsIntervalPriceVOList());
            goodsInfoVOList = priceResponse.getGoodsInfoVOList();
        } else {
            GoodsIntervalPriceResponse priceResponse =
                    goodsIntervalPriceService.getGoodsIntervalPriceVOList(goodsInfoVOList);
            response.setGoodsIntervalPrices(priceResponse.getGoodsIntervalPriceVOList());
            goodsInfoVOList = priceResponse.getGoodsInfoVOList();
        }

        //计算会员价
        if (customer != null && StringUtils.isNotBlank(customer.getCustomerId())) {
            goodsInfoVOList = marketingLevelPluginProvider.goodsListFilter(
                    MarketingLevelGoodsListFilterRequest.builder()
                            .customerDTO(KsBeanUtil.convert(customer, CustomerDTO.class))
                            .goodsInfos(KsBeanUtil.convert(goodsInfoVOList, GoodsInfoDTO.class)).build())
                    .getContext().getGoodsInfoVOList();
        }

        // 查询店铺信息
//        goodsInfoVOList.forEach(goodsInfoVO ->
//                goodsInfoVO.setStoreName(storeQueryProvider.getById(new StoreByIdRequest(goodsInfoVO.getStoreId()))
//                        .getContext().getStoreVO().getStoreName()));

        response.setGoodsInfoPage(new MicroServicePage<>(goodsInfoVOList, queryRequest.getPageRequest(),
                response.getGoodsInfoPage().getTotalElements()));
        return BaseResponse.success(response);
    }

}
