package com.wanmi.sbc.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.provider.standard.EsStandardProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoRequest;
import com.wanmi.sbc.elastic.api.request.standard.EsStandardInitRequest;
import com.wanmi.sbc.goods.api.provider.ares.GoodsAresProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.info.*;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoByGoodsIdresponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoListByConditionResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdResponse;
import com.wanmi.sbc.goods.request.GoodsInfoByGoodsIdsRequest;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;

/**
 * 商品服务
 * Created by daiyitian on 17/4/12.
 */
@Api(tags = "GoodsInfoController", description = "商品服务 Api")
@RestController
public class GoodsInfoController {
    @Autowired
    private GoodsAresProvider goodsAresProvider;

    @Autowired
    private GoodsInfoProvider goodsInfoProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Autowired
    private EsStandardProvider esStandardProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    /**
     * 获取商品SKU详情信息
     *
     * @param goodsInfoId 商品编号
     * @return 商品详情
     */
    @ApiOperation(value = "获取商品SKU详情信息")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "goodsInfoId", value = "sku Id", required = true)
    @RequestMapping(value = "/goods/sku/{goodsInfoId}", method = RequestMethod.GET)
    public BaseResponse<GoodsInfoViewByIdResponse> info(@PathVariable String goodsInfoId) {
        return goodsInfoQueryProvider.getViewById(GoodsInfoViewByIdRequest.builder().goodsInfoId(goodsInfoId).build());
    }

    /**
     * 根据spuId获取商品SKU详情信息
     *
     * @param goodId 商品编号
     * @return 商品详情
     */
    @ApiOperation(value = "获取商品SPU详情信息")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "goodId", value = "spu Id", required = true)
    @RequestMapping(value = "/goods/skus/{goodId}", method = RequestMethod.GET)
    public BaseResponse<GoodsInfoByGoodsIdresponse> skusByGoodsId(@PathVariable String goodId) {
        return goodsInfoQueryProvider.getByGoodsId(DistributionGoodsChangeRequest.builder()
                .goodsId(goodId).showProviderInfoFlag(Boolean.TRUE).showVendibilityFlag(Boolean.TRUE).showSpecFlag(Boolean.TRUE).build());
    }

    /**
     * 根据spuId获取商品SKU详情信息
     *
     * @param request 商品编号
     * @return 商品SKU列表
     */
    @ApiOperation(value = "获取商品SPU详情信息")
    @RequestMapping(value = "/goods/skus/spuId", method = RequestMethod.POST)
    public BaseResponse<GoodsInfoListByConditionResponse> skusByGoodsIds(@RequestBody @Valid GoodsInfoByGoodsIdsRequest request) {
        return goodsInfoQueryProvider.listByCondition(GoodsInfoListByConditionRequest.builder()
                .goodsIds(request.getGoodsIds())
                .delFlag(DeleteFlag.NO.toValue())
                .showProviderInfoFlag(Boolean.TRUE)
                .showVendibilityFlag(Boolean.TRUE)
                .showSpecFlag(Boolean.TRUE).build());
    }

    /**
     * 编辑商品SKU
     */
    @ApiOperation(value = "编辑商品SKU")
    @RequestMapping(value = "/goods/sku", method = RequestMethod.PUT)
    public ResponseEntity<BaseResponse> edit(@RequestBody GoodsInfoModifyRequest saveRequest) {
        if (saveRequest.getGoodsInfo() == null || saveRequest.getGoodsInfo().getGoodsInfoId() == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        goodsInfoProvider.modify(saveRequest);

        //持化至ES
        esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder()
                .skuIds(Collections.singletonList(saveRequest.getGoodsInfo().getGoodsId())).build());

        //刷新商品库es
        esStandardProvider.init(EsStandardInitRequest.builder()
                .relGoodsIds(Collections.singletonList(saveRequest.getGoodsInfo().getGoodsId())).build());

        operateLogMQUtil.convertAndSend("商品", "编辑商品", "编辑商品：SKU编码" + saveRequest.getGoodsInfo().getGoodsInfoNo());

        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 编辑商品SKU
     */
    @ApiOperation(value = "编辑商品SKU")
    @RequestMapping(value = "/goods/sku/price", method = RequestMethod.PUT)
    public ResponseEntity<BaseResponse> price(@RequestBody GoodsInfoPriceModifyRequest saveRequest) {
        if (saveRequest.getGoodsInfo() == null || saveRequest.getGoodsInfo().getGoodsInfoId() == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        goodsInfoProvider.modifyPrice(saveRequest);

        //持化至ES
        esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder()
                .skuIds(Collections.singletonList(saveRequest.getGoodsInfo().getGoodsId())).build());

        operateLogMQUtil.convertAndSend("商品", "设价",
                "设价：SKU编码" + saveRequest.getGoodsInfo().getGoodsInfoNo());
        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }
}
