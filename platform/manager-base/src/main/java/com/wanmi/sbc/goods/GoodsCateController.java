package com.wanmi.sbc.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.cate.GoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.provider.prop.GoodsPropQueryProvider;
import com.wanmi.sbc.goods.api.request.prop.GoodsPropListAllByCateIdRequest;
import com.wanmi.sbc.goods.api.response.prop.GoodsPropListAllByCateIdResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsCateVO;
import com.wanmi.sbc.goods.bean.vo.GoodsPropVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 商品分类服务
 * Created by chenli on 17/10/30.
 */
@Api(tags = "GoodsCateController", description = "商品分类服务")
@RestController
@RequestMapping("/goods")
public class GoodsCateController {

    @Autowired
    private GoodsCateQueryProvider goodsCateQueryProvider;

    @Autowired
    private GoodsPropQueryProvider goodsPropQueryProvider;

    /**
     * 查询有效分类平台分类（有3级分类的）
     * @return
     */
    @ApiOperation(value = "查询有效分类平台分类（有3级分类的）")
    @RequestMapping(value = "/goodsCatesTree", method = RequestMethod.GET)
    public BaseResponse<List<GoodsCateVO>> queryGoodsCate(){
        return BaseResponse.success(goodsCateQueryProvider.list().getContext().getGoodsCateVOList());
    }

    /**
     * @description 查询分类下所有的属性信息
     * @param Long
     * @menu 商品
     * @status undone
     */
    @ApiOperation(value = "查询分类下所有的属性信息")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "cateId", value = "分类ID", required = true)
    @RequestMapping(value = "/goodsProp/{cateId}",method = RequestMethod.GET)
    public BaseResponse<List<GoodsPropVO>> list(@PathVariable Long cateId) {
        BaseResponse<GoodsPropListAllByCateIdResponse> baseResponse  = goodsPropQueryProvider.listAllByCateId(new GoodsPropListAllByCateIdRequest(cateId));
        GoodsPropListAllByCateIdResponse response = baseResponse.getContext();
        if (Objects.isNull(response)){
            return BaseResponse.success(Collections.emptyList());
        }
        return BaseResponse.success(response.getGoodsPropVOList());
    }
}
