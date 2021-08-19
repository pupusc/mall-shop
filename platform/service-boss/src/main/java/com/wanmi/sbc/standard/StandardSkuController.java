package com.wanmi.sbc.standard;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.api.provider.standard.StandardSkuProvider;
import com.wanmi.sbc.goods.api.provider.standard.StandardSkuQueryProvider;
import com.wanmi.sbc.goods.api.request.standard.StandardSkuByIdRequest;
import com.wanmi.sbc.goods.api.request.standard.StandardSkuModifyRequest;
import com.wanmi.sbc.goods.api.response.standard.StandardSkuByIdResponse;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 商品库SKU服务
 * Created by daiyitian on 17/4/12.
 */
@RestController
@Api(description = "商品库SKU服务",tags ="StandardSkuController")
public class StandardSkuController {

    @Autowired
    private StandardSkuProvider standardSkuProvider;

    @Autowired
    private StandardSkuQueryProvider standardSkuQueryProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    /**
     * 获取商品库SKU详情信息
     *
     * @param standardInfoId 商品库SKU编号
     * @return 商品详情
     */
    @RequestMapping(value = "/standard/sku/{standardInfoId}", method = RequestMethod.GET)
    public BaseResponse<StandardSkuByIdResponse> info(@PathVariable String standardInfoId) {
        return standardSkuQueryProvider.getById(
                StandardSkuByIdRequest.builder().standardInfoId(standardInfoId).build()
        );
    }

    /**
     * 编辑商品库SKU
     */
    @RequestMapping(value = "/standard/sku", method = RequestMethod.PUT)
    public BaseResponse edit(@Valid @RequestBody StandardSkuModifyRequest saveRequest) {
        if (saveRequest.getGoodsInfo().getGoodsInfoId() == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        operateLogMQUtil.convertAndSend("商品", "编辑商品", "编辑商品：" + saveRequest.getGoodsInfo().getGoodsInfoName());
        return standardSkuProvider.modify(saveRequest);
    }


}
