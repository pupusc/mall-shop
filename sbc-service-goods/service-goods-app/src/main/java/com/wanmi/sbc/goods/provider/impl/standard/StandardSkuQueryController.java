package com.wanmi.sbc.goods.provider.impl.standard;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.standard.StandardSkuQueryProvider;
import com.wanmi.sbc.goods.api.request.standard.StandardSkuByIdRequest;
import com.wanmi.sbc.goods.api.request.standard.StandardPartColsListByGoodsIdsRequest;
import com.wanmi.sbc.goods.api.request.standard.StandardSkuByStandardIdRequest;
import com.wanmi.sbc.goods.api.response.standard.StandardSkuByIdResponse;
import com.wanmi.sbc.goods.api.response.standard.StandardPartColsListByGoodsIdsResponse;
import com.wanmi.sbc.goods.api.response.standard.StandardSkuByStandardIdResponse;
import com.wanmi.sbc.goods.bean.vo.*;
import com.wanmi.sbc.goods.standard.service.LmStandardService;
import com.wanmi.sbc.goods.standard.model.root.StandardSku;
import com.wanmi.sbc.goods.standard.request.StandardSkuQueryRequest;
import com.wanmi.sbc.goods.standard.response.StandardSkuEditResponse;
import com.wanmi.sbc.goods.standard.service.StandardSkuService;
import com.wanmi.sbc.goods.standardspec.service.StandardSpecService;
import com.wanmi.sbc.goods.util.mapper.StandardSkuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>对商品库查询接口</p>
 * Created by daiyitian on 2018-11-5-下午6:23.
 */
@RestController
@Validated
public class StandardSkuQueryController implements StandardSkuQueryProvider {

    @Autowired
    private StandardSkuService standardSkuService;

    @Autowired
    private StandardSpecService standardSpecService;

    @Autowired
    private StandardSkuMapper standardSkuMapper;

    @Autowired
    private LmStandardService lmStandardService;

    /**
     * 根据id获取商品库信息
     *
     * @param request 包含id的商品库信息查询结构 {@link StandardSkuByIdRequest}
     * @return 商品库信息 {@link StandardSkuByIdResponse}
     */
    @Override
    public BaseResponse<StandardSkuByIdResponse> getById(@RequestBody @Valid StandardSkuByIdRequest request){
        StandardSkuByIdResponse response = new StandardSkuByIdResponse();

        StandardSkuEditResponse editResponse = standardSkuService.findById(request.getStandardInfoId());
        StandardGoodsVO goodsVO = new StandardGoodsVO();
        KsBeanUtil.copyPropertiesThird(editResponse.getGoods(), goodsVO);

        StandardSkuVO skuVO = new StandardSkuVO();
        KsBeanUtil.copyPropertiesThird(editResponse.getGoodsInfo(), skuVO);

        List<StandardSpecDetailVO> specDetailVOs = editResponse.getGoodsSpecDetails().stream().map(specDetail -> {
            StandardSpecDetailVO detailVO = new StandardSpecDetailVO();
            KsBeanUtil.copyPropertiesThird(specDetail, detailVO);
            return detailVO;
        }).collect(Collectors.toList());

        List<StandardSpecVO> specVOs = editResponse.getGoodsSpecs().stream().map(spec -> {
            StandardSpecVO specVO = new StandardSpecVO();
            KsBeanUtil.copyPropertiesThird(spec, specVO);
            return specVO;
        }).collect(Collectors.toList());

        List<StandardImageVO> imageVOs = editResponse.getImages().stream().map(image -> {
            StandardImageVO imageVO = new StandardImageVO();
            KsBeanUtil.copyPropertiesThird(image, imageVO);
            return imageVO;
        }).collect(Collectors.toList());

        response.setGoods(goodsVO);
        response.setGoodsInfo(skuVO);
        response.setGoodsSpecDetails(specDetailVOs);
        response.setGoodsSpecs(specVOs);
        response.setImages(imageVOs);

//        KsBeanUtil.copyPropertiesThird(standardSkuService.findById(request.getStandardInfoId()), response);
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<StandardSkuByStandardIdResponse> listByStandardId(@RequestBody @Valid StandardSkuByStandardIdRequest request) {
        List<StandardSku> standardSkus = standardSkuService.findAll(StandardSkuQueryRequest.builder().goodsId(request.getStandardId()).delFlag(DeleteFlag.NO.toValue()).build());
        List<StandardSkuVO> standardSkuVOS = standardSkuMapper.listToVO(standardSkus);
        //填充规格
        standardSpecService.fillSpecDetail(standardSkuVOS);
        //填充Lm库存
        lmStandardService.fillLmStock(standardSkuVOS);
        StandardSkuByStandardIdResponse response = new StandardSkuByStandardIdResponse();
        response.setGoodsInfo(standardSkuVOS);
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<StandardPartColsListByGoodsIdsResponse> listPartColsByGoodsIds(@RequestBody @Valid StandardPartColsListByGoodsIdsRequest request) {
        StandardPartColsListByGoodsIdsResponse response = new StandardPartColsListByGoodsIdsResponse();
        StandardSkuQueryRequest queryRequest = new StandardSkuQueryRequest();
        queryRequest.setGoodsIds(request.getGoodsIds());
        queryRequest.setDelFlag(DeleteFlag.NO.toValue());
        response.setStandardSkuList(standardSkuMapper.listToVO(standardSkuService.listCols(queryRequest, request.getCols())));
        return BaseResponse.success(response);
    }
}
