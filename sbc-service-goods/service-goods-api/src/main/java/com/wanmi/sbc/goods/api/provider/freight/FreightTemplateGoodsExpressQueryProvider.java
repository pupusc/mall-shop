package com.wanmi.sbc.goods.api.provider.freight;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.freight.FreightTemplateGoodsExpressByIdRequest;
import com.wanmi.sbc.goods.api.response.freight.FreightTemplateGoodsExpressByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;


@FeignClient(value = "${application.goods.name}", contextId = "FreightTemplateGoodsExpressQueryProvider")
public interface FreightTemplateGoodsExpressQueryProvider {

    /**
     * 根据单品运费模板id获取未删除和默认的单品运费模板快递
     *
     * @param freightTemplateGoodsExpressByIdRequest 运费模板id {@link FreightTemplateGoodsExpressByIdRequest}
     * @return 未删除和默认的单品运费模板快递 {@link FreightTemplateGoodsExpressByIdResponse}
     */
    @PostMapping("/goods/${application.goods.version}/freight/express/get-by-id")
    BaseResponse<FreightTemplateGoodsExpressByIdResponse> getById(@RequestBody @Valid FreightTemplateGoodsExpressByIdRequest freightTemplateGoodsExpressByIdRequest);

}
