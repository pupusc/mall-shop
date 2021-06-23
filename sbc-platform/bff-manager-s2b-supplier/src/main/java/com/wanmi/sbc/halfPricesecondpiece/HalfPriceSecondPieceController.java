package com.wanmi.sbc.halfPricesecondpiece;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoRequest;
import com.wanmi.sbc.marketing.api.provider.halfpricesecondpiece.HalfPriceSecondPieceProvider;
import com.wanmi.sbc.marketing.api.request.halfpricesecondpiece.HalfPriceSecondPieceAddRequest;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 第二件半价服务API
 */
@Api(tags = "HalfPriceSecondPieceController", description = "第二件半价服务API")
@RestController
@RequestMapping("/half_price_second_piece")
@Validated
public class HalfPriceSecondPieceController {

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private HalfPriceSecondPieceProvider halfPriceSecondPieceProvider;


    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    /**
     * 新增一口价营销信息
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "新增第二件半价信息")
    @RequestMapping(method = RequestMethod.POST)
    public BaseResponse add(@Valid @RequestBody HalfPriceSecondPieceAddRequest request) {
        request.valid();

        request.setIsBoss(BoolFlag.NO);
        request.setStoreId(commonUtil.getStoreId());
        request.setCreatePerson(commonUtil.getOperatorId());

        halfPriceSecondPieceProvider.addHalfPriceSecondPiece(request);

        // 更新es
        esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().skuIds(request.getSkuIds()).build());

        operateLogMQUtil.convertAndSend("营销", "创建第二件半价活动", "创建第二件半价活动：" + request.getMarketingName());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 修改第二件半价信息
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "修改第二件半价信息")
    @RequestMapping(method = RequestMethod.PUT)
    public BaseResponse modify(@Valid @RequestBody HalfPriceSecondPieceAddRequest request) {
        request.valid();

        request.setUpdatePerson(commonUtil.getOperatorId());

        halfPriceSecondPieceProvider.modifyHalfPriceSecondPiece(request);

        // 更新es
        esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().skuIds(request.getSkuIds()).build());

        operateLogMQUtil.convertAndSend("第二件半价营销信息", "编辑第二件半价促销活动", "编辑第二件半价促销活动：" + request.getMarketingName());
        return BaseResponse.SUCCESSFUL();
    }


}
