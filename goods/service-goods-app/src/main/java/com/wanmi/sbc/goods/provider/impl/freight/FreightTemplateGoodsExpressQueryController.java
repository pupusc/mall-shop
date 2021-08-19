package com.wanmi.sbc.goods.provider.impl.freight;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.freight.FreightTemplateGoodsExpressQueryProvider;
import com.wanmi.sbc.goods.api.request.freight.FreightTemplateGoodsExpressByIdRequest;
import com.wanmi.sbc.goods.api.response.freight.FreightTemplateGoodsExpressByIdResponse;
import com.wanmi.sbc.goods.freight.model.root.FreightTemplateGoodsExpress;
import com.wanmi.sbc.goods.freight.service.FreightTemplateGoodsExpressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;


@RestController
@Validated
public class FreightTemplateGoodsExpressQueryController implements FreightTemplateGoodsExpressQueryProvider {

    @Autowired
    private FreightTemplateGoodsExpressService freightTemplateGoodsExpressService;

    /**
     * 根据单品运费模板id获取未删除和默认的单品运费模板快递
     *
     * @param freightTemplateGoodsExpressByIdRequest 运费模板id {@link FreightTemplateGoodsExpressByIdRequest}
     * @return 未删除和默认的单品运费模板快递 {@link FreightTemplateGoodsExpressByIdResponse}
     */

    @Override
    public  BaseResponse<FreightTemplateGoodsExpressByIdResponse> getById(@RequestBody @Valid FreightTemplateGoodsExpressByIdRequest freightTemplateGoodsExpressByIdRequest){
        FreightTemplateGoodsExpress freightTemplateGoodsExpress = freightTemplateGoodsExpressService.findByFreightTempIdAndDelFlag(freightTemplateGoodsExpressByIdRequest.getFreightTempId());
        if (Objects.isNull(freightTemplateGoodsExpress)){
            return BaseResponse.success(new FreightTemplateGoodsExpressByIdResponse());
        }
        FreightTemplateGoodsExpressByIdResponse freightTemplateGoodsExpressByIdResponse = new FreightTemplateGoodsExpressByIdResponse();
        KsBeanUtil.copyPropertiesThird(freightTemplateGoodsExpress,freightTemplateGoodsExpressByIdResponse);
        return BaseResponse.success(freightTemplateGoodsExpressByIdResponse);
    }

}
