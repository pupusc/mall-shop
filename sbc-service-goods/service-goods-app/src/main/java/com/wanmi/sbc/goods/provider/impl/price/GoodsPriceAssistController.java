package com.wanmi.sbc.goods.provider.impl.price;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.bean.enums.EnterpriseCheckState;
import com.wanmi.sbc.goods.api.provider.price.GoodsPriceAssistProvider;
import com.wanmi.sbc.goods.api.request.price.GoodsPriceSetBatchByIepRequest;
import com.wanmi.sbc.goods.api.response.price.GoodsPriceSetBatchByIepResponse;
import com.wanmi.sbc.goods.bean.enums.EnterpriseAuditState;
import com.wanmi.sbc.goods.bean.enums.SaleType;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsIntervalPriceVO;
import com.wanmi.sbc.goods.common.SystemPointsConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>商品设价辅助接口</p>
 * Created by of628-wenzhi on 2020-03-04-下午5:29.
 */
@RestController
@Validated
public class GoodsPriceAssistController implements GoodsPriceAssistProvider {

    @Autowired
    private SystemPointsConfigService systemPointsConfigService;

    @Override
    public BaseResponse<GoodsPriceSetBatchByIepResponse> goodsPriceSetBatchByIep(@RequestBody @Valid GoodsPriceSetBatchByIepRequest request) {
        GoodsPriceSetBatchByIepResponse response = new GoodsPriceSetBatchByIepResponse();
        List<GoodsInfoVO> goodsInfos = request.getGoodsInfos();
        List<GoodsIntervalPriceVO> goodsIntervalPrices = request.getGoodsIntervalPrices();
        List<String> execludePriceId = new ArrayList<>();
        //角色校验
        EnterpriseCheckState enterpriseCheckState = request.getCustomer().getEnterpriseCheckState();
        if (!(Objects.isNull(enterpriseCheckState) || enterpriseCheckState != EnterpriseCheckState.CHECKED)) {
            //积分价商品不回设企业价
            boolean goodsPointEnable = systemPointsConfigService.isGoodsPoint();
            goodsInfos.stream().filter(i -> request.getFilteredGoodsInfoIds().contains(i.getGoodsInfoId()))
                    .forEach(i -> {
                        //商品校验: 企业购商品已审核 + 设置了企业会员价
                        if (!(Objects.isNull(i.getEnterPriseAuditState()) || i.getEnterPriseAuditState()
                                != EnterpriseAuditState.CHECKED || Objects.isNull(i.getEnterPrisePrice()))
                                && (!goodsPointEnable || (Objects.isNull(i.getBuyPoint()) || i.getBuyPoint() == 0))
                            ) {
                            i.setIntervalPriceIds(null);
                            i.setIntervalMinPrice(null);
                            i.setIntervalMaxPrice(null);
                            i.setCount(null);
                            i.setMaxCount(null);
                            //将市场价和销售价改为企业会员价
                            i.setMarketPrice(i.getEnterPrisePrice());
                            i.setSalePrice(i.getEnterPrisePrice());
                            //批发模式改为零售
                            i.setSaleType(SaleType.RETAIL.toValue());
                            execludePriceId.add(i.getGoodsInfoId());
                        }
                    });
            //从区间价中删除设置企业会员价的商品
            goodsIntervalPrices = goodsIntervalPrices.stream().filter(i -> !execludePriceId.contains(i.getGoodsInfoId()))
                    .collect(Collectors.toList());
        }
        response.setGoodsInfos(goodsInfos);
        response.setGoodsIntervalPrices(goodsIntervalPrices);
        return BaseResponse.success(response);
    }

}
