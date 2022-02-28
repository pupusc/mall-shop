package com.wanmi.sbc.mini.goods.controller;

import com.alipay.api.domain.GoodsInfo;
import com.soybean.mall.wx.mini.goods.bean.request.WxDeleteProductRequest;
import com.soybean.mall.wx.mini.goods.bean.response.WxAddProductResponse;
import com.soybean.mall.wx.mini.goods.controller.WxGoodsApiController;
import com.wanmi.ares.request.GoodsInfoQueryRequest;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
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
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController("wxGoodsController")
@RequestMapping("/wx/goods")
public class GoodsController {

    @Autowired
    private WxMiniGoodsProvider wxMiniGoodsProvider;
    @Autowired
    private EsGoodsInfoElasticQueryProvider esGoodsInfoElasticQueryProvider;
    @Autowired
    private WxGoodsApiController wxGoodsApiController;

    /**
     * @description 查询直播商品
     * @param wxGoodsSearchRequest
     * @menu 小程序
     * @status done
     */
    @PostMapping("/list")
    public BaseResponse<MicroServicePage<WxGoodsVo>> listGoods(@RequestBody WxGoodsSearchRequest wxGoodsSearchRequest){
        List<EsGoodsVO> esGoodsVOS = null;
        if(wxGoodsSearchRequest.getGoodsName() != null){
            EsGoodsInfoQueryRequest queryRequest = new EsGoodsInfoQueryRequest();
            queryRequest.setPageNum(0);
            queryRequest.setPageSize(50);
            queryRequest.setMatchGoodsName(wxGoodsSearchRequest.getGoodsName());
            queryRequest.setQueryGoods(true);
            queryRequest.setAddedFlag(AddedFlag.YES.toValue());
            queryRequest.setDelFlag(DeleteFlag.NO.toValue());
            queryRequest.setAuditStatus(CheckStatus.CHECKED.toValue());
            queryRequest.setStoreState(StoreState.OPENING.toValue());
            queryRequest.setVendibility(Constants.yes);
            esGoodsVOS = esGoodsInfoElasticQueryProvider.pageByGoods(queryRequest).getContext().getEsGoods().getContent();
            List<String> goodsIds = esGoodsVOS.stream().map(g -> g.getId()).collect(Collectors.toList());
            wxGoodsSearchRequest.setGoodsIds(goodsIds);
        }
        BaseResponse<MicroServicePage<WxGoodsVo>> wxGoodsVos = wxMiniGoodsProvider.list(wxGoodsSearchRequest);
        if(wxGoodsSearchRequest.getGoodsName() != null){
            Map<String, List<EsGoodsVO>> collect = esGoodsVOS.stream().collect(Collectors.groupingBy(EsGoodsVO::getId));
            for (WxGoodsVo wxGoodsVo : wxGoodsVos.getContext()) {
                EsGoodsVO esGoodsVO = collect.get(wxGoodsVo.getGoodsId()).get(0);
                wxGoodsVo.setGoodsName(esGoodsVO.getGoodsName());
                wxGoodsVo.setGoodsImg(esGoodsVO.getGoodsInfos().get(0).getGoodsInfoImg());
                wxGoodsVo.setMarketPrice(esGoodsVO.getEsSortPrice().toString());
            }
        }
        return wxGoodsVos;
    }

    /**
     * @description 添加直播商品
     * @param wxGoodsCreateRequest
     * @menu 小程序
     * @status done
     */
    @PostMapping("/add")
    public BaseResponse addGoods(@RequestBody WxGoodsCreateRequest wxGoodsCreateRequest){
        return wxMiniGoodsProvider.add(wxGoodsCreateRequest);
    }

    /**
     * @description 审核直播商品
     * @param wxGoodsCreateRequest
     * @menu 小程序
     * @status done
     */
    @PostMapping("/toAudit")
    public BaseResponse toAudit(@RequestBody WxGoodsCreateRequest wxGoodsCreateRequest){
        return wxMiniGoodsProvider.toAudit(wxGoodsCreateRequest);
    }

    /**
     * @description 更新直播商品
     * @param wxGoodsCreateRequest
     * @menu 小程序
     * @status done
     */
    @PostMapping("/update")
    public BaseResponse update(@RequestBody WxGoodsCreateRequest wxGoodsCreateRequest){
        return wxMiniGoodsProvider.update(wxGoodsCreateRequest);
    }

    /**
     * @description 删除直播商品
     * @param wxDeleteProductRequest
     * @menu 小程序
     * @status done
     */
    @PostMapping("/delete")
    public BaseResponse deleteGoods(@RequestBody WxDeleteProductRequest wxDeleteProductRequest){
        return wxMiniGoodsProvider.delete(wxDeleteProductRequest);
    }

}
