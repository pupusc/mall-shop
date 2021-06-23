package com.wanmi.sbc.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.brand.GoodsBrandQueryProvider;
import com.wanmi.sbc.goods.api.request.brand.GoodsBrandByIdRequest;
import com.wanmi.sbc.goods.api.request.brand.GoodsBrandListRequest;
import com.wanmi.sbc.goods.api.response.brand.GoodsBrandByIdResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsBrandVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商品品牌controller
 * Created by sunkun on 2017/8/4.
 */
@RestController
@RequestMapping("/goods")
@Api(tags = "GoodsBrandBaseController", description = "S2B web公用-商品品牌信息API")
public class GoodsBrandBaseController {

    @Autowired
    private GoodsBrandQueryProvider goodsBrandQueryProvider;

    /**
     * 条件查询商品品牌列表
     *
     * @param queryRequest
     * @return
     */
    @ApiOperation(value = "条件查询商品品牌列表")
    @RequestMapping(value = {"/allGoodsBrands"}, method = RequestMethod.GET)
    public BaseResponse<List<GoodsBrandVO>> list(GoodsBrandListRequest queryRequest) {
        return BaseResponse.success(goodsBrandQueryProvider.list(queryRequest).getContext().getGoodsBrandVOList());
    }

    /**
     * 根据商品品牌id查询商品品牌信息
     * @param brandId
     * @return
     */
    @ApiOperation(value = "根据商品品牌id查询商品品牌信息")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "brandId", value = "商品品牌id", required = true)
    @RequestMapping(value = {"/goodsBrand/{brandId}"}, method = RequestMethod.GET)
    public BaseResponse<GoodsBrandByIdResponse> list(@PathVariable Long brandId) {
        return goodsBrandQueryProvider.getById(GoodsBrandByIdRequest.builder().brandId(brandId).build());
    }
}
