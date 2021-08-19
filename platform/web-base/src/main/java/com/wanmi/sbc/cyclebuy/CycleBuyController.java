package com.wanmi.sbc.cyclebuy;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.goods.api.provider.cyclebuy.CycleBuyQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.cyclebuy.CycleBuyByGoodsIdRequest;
import com.wanmi.sbc.goods.api.request.cyclebuy.CycleBuyByIdRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.goods.api.response.cyclebuy.CycleBuyByGoodsIdResponse;
import com.wanmi.sbc.goods.api.response.cyclebuy.CycleBuyGiftsResponse;
import com.wanmi.sbc.goods.bean.vo.CycleBuyGiftVO;
import com.wanmi.sbc.goods.bean.vo.CycleBuyVO;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 周期购
 */
@RestController
@RequestMapping("/goods/cyclebuy")
@Api(description = "周期购服务", tags = "CycleBuyController")
public class CycleBuyController {

    @Autowired
    private CycleBuyQueryProvider cycleBuyQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    /**
     * 查询周期购活动详情
     * @return
     */
    @ApiOperation(value = "查询周期购活动详情")
    @GetMapping("/{goodsId}")
    public BaseResponse<CycleBuyByGoodsIdResponse> detail(@PathVariable String goodsId){
        return cycleBuyQueryProvider.getByGoodsId(CycleBuyByGoodsIdRequest.builder().goodsId(goodsId).build());
    }

    @ApiOperation(value = "查询赠品")
    @GetMapping("/gifts/{goodsId}")
    public BaseResponse<CycleBuyGiftsResponse> getGiftsByGoodsId(@PathVariable String goodsId){
        CycleBuyGiftsResponse response = new CycleBuyGiftsResponse();
        response.setGiftList(Collections.emptyList());
        response.setGiftList(Collections.emptyList());
        //查询活动信息
        CycleBuyVO cycleBuyVO = cycleBuyQueryProvider.getByGoodsId(CycleBuyByGoodsIdRequest.builder().goodsId(goodsId).build()).getContext().getCycleBuyVO();
        if(Objects.isNull(cycleBuyVO)) {
            return BaseResponse.success(response);
        }

        //赠品详情
        List<String> skuIds = cycleBuyVO.getCycleBuyGiftVOList().stream().map(CycleBuyGiftVO::getGoodsInfoId).collect(Collectors.toList());
        response.setGiftList(goodsInfoQueryProvider.listViewByIds(GoodsInfoViewByIdsRequest.builder()
                .goodsInfoIds(skuIds).isHavSpecText(Constants.yes).build()).getContext().getGoodsInfos());
        response.setCycleBuyGiftVOList(cycleBuyVO.getCycleBuyGiftVOList());
        return BaseResponse.success(response);
    }
}
