package com.wanmi.sbc.goods.api.provider.presellsale;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.goods.GoodsPageRequest;
import com.wanmi.sbc.goods.api.request.presellsale.PresellSaleByIdRequest;
import com.wanmi.sbc.goods.api.request.presellsale.PresellSalePageRequest;
import com.wanmi.sbc.goods.api.response.goods.GoodsListByIdsResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsPageResponse;
import com.wanmi.sbc.goods.api.response.presellsale.PresellSalePageResponse;
import com.wanmi.sbc.goods.api.response.presellsale.PresellSaleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * 预售活动查询
 */
@FeignClient(value = "${application.goods.name}", contextId = "PresellSaleQueryProvider")
public interface PresellSaleQueryProvider {

    /**
     * 根据预售活动id查询预售活动详情
     *
     * @param request 包含预售活动id查询请求结构 {@link PresellSaleByIdRequest}
     * @return 预售活动信息 {@link PresellSaleResponse}
     */
    @PostMapping("/goods/${application.goods.version}/id")
    BaseResponse<PresellSaleResponse> presellSaleById(@RequestBody @Valid PresellSaleByIdRequest request);


    /**
     * 根据多个条件查询
     *
     * @param request 包含预售活动查询条件 {@link PresellSalePageRequest}
     * @return 预售活动信息 {@link PresellSalePageResponse}
     */
    @PostMapping("/goods/${application.goods.version}/presell_sale/page")
    BaseResponse page(@RequestBody  PresellSalePageRequest request);



}
