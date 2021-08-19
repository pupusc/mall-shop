package com.wanmi.sbc.goods.api.provider.enterprise;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.enterprise.goods.EnterpriseByIdRequest;
import com.wanmi.sbc.goods.api.request.enterprise.goods.EnterpriseGoodsChangeRequest;
import com.wanmi.sbc.goods.api.request.enterprise.goods.EnterpriseGoodsInfoPageRequest;
import com.wanmi.sbc.goods.api.request.enterprise.goods.EnterprisePriceGetRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsPageRequest;
import com.wanmi.sbc.goods.api.response.enterprise.EnterpriseByIdResponse;
import com.wanmi.sbc.goods.api.response.enterprise.EnterpriseCheckResponse;
import com.wanmi.sbc.goods.api.response.enterprise.EnterpriseGoodsInfoPageResponse;
import com.wanmi.sbc.goods.api.response.enterprise.EnterprisePriceResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Map;

/**
 * com.wanmi.sbc.goods.api.provider.goods.EnterpriseGoodsInfoQueryProvider
 *
 * @author baijianzhong
 */
@FeignClient(value = "${application.goods.name}", contextId = "EnterpriseGoodsInfoQueryProvider")
public interface EnterpriseGoodsInfoQueryProvider {

    /**
     * 分页查询企业购商品信息
     *
     * @param goodsInfoPageRequest {@link GoodsPageRequest}
     * @return 分页企业购商品信息 {@link GoodsPageResponse}
     */
    @PostMapping("/goods/${application.goods.version}/enterprise/page")
    BaseResponse<EnterpriseGoodsInfoPageResponse> page(@RequestBody @Valid EnterpriseGoodsInfoPageRequest goodsInfoPageRequest);


    /**
     * 根据goodsId检查商品里是否有企业购sku
     *
     * @param request
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/info/check-enterprise-in-sku")
    BaseResponse<EnterpriseCheckResponse> checkEnterpriseInSku(@RequestBody @Valid EnterpriseGoodsChangeRequest request);

    /**
     * 根据商品ID获取 企业购商品 设价详情
     *
     * @param request
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/info/detail")
    BaseResponse<EnterpriseByIdResponse> detail(@RequestBody @Valid EnterpriseByIdRequest request);

    /**
     * 根据商品ID获取 企业购商品 设价详情
     *
     * @param request
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/info/user-price")
    BaseResponse<EnterprisePriceResponse> userPrice(@RequestBody @Valid EnterprisePriceGetRequest request);
}
