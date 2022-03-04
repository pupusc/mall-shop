package com.wanmi.sbc.mini.goods;

import com.soybean.mall.wx.mini.goods.bean.request.WxDeleteProductRequest;
import com.soybean.mall.wx.mini.goods.bean.response.WxCateNodeResponse;
import com.soybean.mall.wx.mini.goods.controller.WxGoodsApiController;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoQueryRequest;
import com.wanmi.sbc.elastic.api.response.goods.EsGoodsResponse;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.goods.api.provider.mini.goods.WxMiniGoodsProvider;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.wx.request.WxGoodsCreateRequest;
import com.wanmi.sbc.goods.bean.wx.request.WxGoodsSearchRequest;
import com.wanmi.sbc.goods.bean.wx.vo.WxGoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        if(StringUtils.isNotEmpty(wxGoodsSearchRequest.getGoodsName()) || wxGoodsSearchRequest.getSaleStatus() != null){
            EsGoodsInfoQueryRequest queryRequest = new EsGoodsInfoQueryRequest();
            queryRequest.setPageNum(0);
            queryRequest.setPageSize(50);
            if(StringUtils.isNotEmpty(wxGoodsSearchRequest.getGoodsName())) queryRequest.setMatchGoodsName(wxGoodsSearchRequest.getGoodsName());
            if(wxGoodsSearchRequest.getSaleStatus() != null) queryRequest.setSaleStatus(wxGoodsSearchRequest.getSaleStatus());
            queryRequest.setQueryGoods(true);
            queryRequest.setAddedFlag(AddedFlag.YES.toValue());
            queryRequest.setDelFlag(DeleteFlag.NO.toValue());
            queryRequest.setAuditStatus(CheckStatus.CHECKED.toValue());
            queryRequest.setStoreState(StoreState.OPENING.toValue());
            queryRequest.setVendibility(Constants.yes);
            EsGoodsResponse context = esGoodsInfoElasticQueryProvider.pageByGoods(queryRequest).getContext();
            if(context == null || context.getEsGoods() == null || CollectionUtils.isEmpty(context.getEsGoods().getContent())){
                MicroServicePage<WxGoodsVo> microServicePage = new MicroServicePage<>();
                microServicePage.setTotal(0);
                microServicePage.setContent(new ArrayList<>());
                return BaseResponse.success(microServicePage);
            }
            esGoodsVOS = context.getEsGoods().getContent();
            List<String> goodsIds = esGoodsVOS.stream().map(g -> g.getId()).collect(Collectors.toList());
            wxGoodsSearchRequest.setGoodsIds(goodsIds);
        }
        BaseResponse<MicroServicePage<WxGoodsVo>> wxGoodsVos = wxMiniGoodsProvider.list(wxGoodsSearchRequest);
        if(StringUtils.isNotEmpty(wxGoodsSearchRequest.getGoodsName())){
            Map<String, List<EsGoodsVO>> collect = esGoodsVOS.stream().collect(Collectors.groupingBy(EsGoodsVO::getId));
            for (WxGoodsVo wxGoodsVo : wxGoodsVos.getContext()) {
                List<EsGoodsVO> esGoodsVOSList = collect.get(wxGoodsVo.getGoodsId());
                if(esGoodsVOSList != null){
                    EsGoodsVO esGoodsVO = esGoodsVOSList.get(0);
                    try {
                        wxGoodsVo.setGoodsNo(esGoodsVO.getGoodsNo());
                        wxGoodsVo.setGoodsName(esGoodsVO.getGoodsName());
                        wxGoodsVo.setGoodsImg(esGoodsVO.getGoodsInfos().get(0).getGoodsInfoImg());
                        wxGoodsVo.setMarketPrice(esGoodsVO.getEsSortPrice().toString());
                    }catch (Exception e) {
                        log.error(esGoodsVO.getId() + "缺少字段值", e);
                    }
                }
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
     * @description 直播商品取消审核
     * @param goodsId
     * @menu 小程序
     * @status done
     */
    @PostMapping("/cancelAudit")
    public BaseResponse cancelAudit(@RequestParam(required = true) String goodsId){
        return wxMiniGoodsProvider.cancelAudit(goodsId);
    }

    /**
     * @description 获取所有类目
     * @menu 小程序
     * @status done
     */
    @PostMapping("/getAllCate")
    public BaseResponse<Set<WxCateNodeResponse>> getAllCate(){
        return wxGoodsApiController.getAllCate();
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
