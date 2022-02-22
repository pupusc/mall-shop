package com.soybean.mall.goods.controller;

import com.soybean.mall.wx.mini.goods.bean.request.WxDeleteProductRequest;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoQueryRequest;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.goods.api.provider.mini.goods.WxMiniGoodsProvider;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.wx.request.WxGoodsCreateRequest;
import com.wanmi.sbc.goods.bean.wx.request.WxGoodsSearchRequest;
import com.wanmi.sbc.goods.bean.wx.vo.WxGoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/wx/goods")
public class GoodsController {

    @Autowired
    private WxMiniGoodsProvider wxMiniGoodsProvider;
    @Autowired
    private EsGoodsInfoElasticQueryProvider esGoodsInfoElasticQueryProvider;

    @PostMapping("/list")
    public BaseResponse<MicroServicePage<WxGoodsVo>> listGoods(WxGoodsSearchRequest wxGoodsSearchRequest){
        if(wxGoodsSearchRequest.getGoodsName() != null){
            EsGoodsInfoQueryRequest queryRequest = new EsGoodsInfoQueryRequest();
            queryRequest.setPageNum(0);
            queryRequest.setPageSize(99);
            queryRequest.setMatchGoodsName(wxGoodsSearchRequest.getGoodsName());
            queryRequest.setQueryGoods(true);
            queryRequest.setAddedFlag(AddedFlag.YES.toValue());
            queryRequest.setDelFlag(DeleteFlag.NO.toValue());
            queryRequest.setAuditStatus(CheckStatus.CHECKED.toValue());
            queryRequest.setStoreState(StoreState.OPENING.toValue());
            queryRequest.setVendibility(Constants.yes);
            List<EsGoodsVO> esGoodsVOS = esGoodsInfoElasticQueryProvider.pageByGoods(queryRequest).getContext().getEsGoods().getContent();
            List<String> goodsIds = esGoodsVOS.stream().map(g -> g.getId()).collect(Collectors.toList());
            wxGoodsSearchRequest.setGoodsIds(goodsIds);
        }
        return wxMiniGoodsProvider.list(wxGoodsSearchRequest);
    }

    @PostMapping("/add")
    public BaseResponse addGoods(WxGoodsCreateRequest wxGoodsCreateRequest){
        return wxMiniGoodsProvider.add(wxGoodsCreateRequest);
    }

    @PostMapping("/delete")
    public BaseResponse deleteGoods(WxDeleteProductRequest wxDeleteProductRequest){
        return wxMiniGoodsProvider.delete(wxDeleteProductRequest);
    }



}
