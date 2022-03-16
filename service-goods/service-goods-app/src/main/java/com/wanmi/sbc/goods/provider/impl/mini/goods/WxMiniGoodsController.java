package com.wanmi.sbc.goods.provider.impl.mini.goods;

import com.alibaba.fastjson.JSONObject;
import com.soybean.mall.wx.mini.goods.bean.request.WxDeleteProductRequest;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.api.provider.mini.goods.WxMiniGoodsProvider;
import com.wanmi.sbc.goods.bean.wx.request.WxGoodsCreateRequest;
import com.wanmi.sbc.goods.bean.wx.request.WxGoodsSearchRequest;
import com.wanmi.sbc.goods.bean.wx.vo.WxGoodsVo;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.service.GoodsService;
import com.wanmi.sbc.goods.mini.model.goods.WxGoodsModel;
import com.wanmi.sbc.goods.mini.service.goods.WxGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class WxMiniGoodsController implements WxMiniGoodsProvider {

    @Autowired
    private WxGoodsService wxGoodsService;
    @Autowired
    private GoodsService goodsService;

    @Override
    public BaseResponse<MicroServicePage<WxGoodsVo>> list(WxGoodsSearchRequest wxGoodsSearchRequest) {
        Page<WxGoodsModel> goodsPage = wxGoodsService.listGoods(wxGoodsSearchRequest);
        List<WxGoodsModel> content = goodsPage.getContent();
        MicroServicePage<WxGoodsVo> microServicePage = new MicroServicePage<>();
        if(CollectionUtils.isNotEmpty(content)){
            Map<String, List<Goods>> collect = null;
            if(wxGoodsSearchRequest.getGoodsIds() == null){
                List<String> goodsIds = content.stream().map(WxGoodsModel::getGoodsId).collect(Collectors.toList());
                List<Goods> goods = goodsService.listByGoodsIds(goodsIds);
                collect = goods.stream().collect(Collectors.groupingBy(Goods::getGoodsId));
            }
            microServicePage.setTotal(goodsPage.getTotalElements());
            List<WxGoodsVo> voList = new ArrayList<>();
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (WxGoodsModel wxGoodsModel : content) {
                WxGoodsVo wxGoodsVo = new WxGoodsVo();
                BeanUtils.copyProperties(wxGoodsModel, wxGoodsVo);
                wxGoodsVo.setAuditStatus(wxGoodsModel.getAuditStatus().toValue());
                wxGoodsVo.setStatus(wxGoodsModel.getStatus().toValue());
                wxGoodsVo.setSaleStatus(wxGoodsModel.getPlatformProductId() == null ? 0 : 1);
                wxGoodsVo.setWxCategory(wxGoodsModel.getWxCategory() != null ? JSONObject.parseObject(wxGoodsModel.getWxCategory(), Map.class) : null);
                wxGoodsVo.setUploadTime(wxGoodsModel.getUploadTime() == null ? null : wxGoodsModel.getUploadTime().format(df));
                wxGoodsVo.setCreateTime(wxGoodsModel.getCreateTime().format(df));
                if(collect != null){
                    List<Goods> goodsList = collect.get(wxGoodsModel.getGoodsId());
                    if(goodsList != null){
                        Goods goods = goodsList.get(0);
                        wxGoodsVo.setGoodsNo(goods.getGoodsNo());
                        wxGoodsVo.setGoodsName(goods.getGoodsName());
                        wxGoodsVo.setGoodsImg(goods.getGoodsImg());
                        wxGoodsVo.setMarketPrice(goods.getSkuMinMarketPrice() != null ? goods.getSkuMinMarketPrice().toString() : "0");
                    }
                }
                voList.add(wxGoodsVo);
            }
            microServicePage.setContent(voList);
        }
        return BaseResponse.success(microServicePage);
    }

    @Override
    public BaseResponse add(WxGoodsCreateRequest wxGoodsCreateRequest) {
        wxGoodsService.addGoods(wxGoodsCreateRequest);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse toAudit(WxGoodsCreateRequest wxGoodsCreateRequest) {
        wxGoodsService.toAudit(wxGoodsCreateRequest);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse cancelAudit(String goodsId){
        wxGoodsService.cancelAudit(goodsId);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse update(WxGoodsCreateRequest wxGoodsCreateRequest) {
        wxGoodsService.update(wxGoodsCreateRequest);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse delete(WxDeleteProductRequest wxDeleteProductRequest) {
        wxGoodsService.deleteGoods(wxDeleteProductRequest);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<List<String>> findAllId(WxGoodsSearchRequest wxGoodsSearchRequest){
        List<WxGoodsModel> wxGoodsModels = wxGoodsService.findAll(wxGoodsSearchRequest);
        return BaseResponse.success(wxGoodsModels.stream().map(WxGoodsModel::getGoodsId).collect(Collectors.toList()));
    }

    @Override
    public BaseResponse<List<String>> findByGoodsIds(List<String> goodsIds){
        List<WxGoodsModel> wxGoodsModels = wxGoodsService.findByGoodsIds(goodsIds);
        return BaseResponse.success(wxGoodsModels.stream().map(WxGoodsModel::getGoodsId).collect(Collectors.toList()));
    }

    @Override
    public BaseResponse<Boolean> auditCallback(Map<String, Object> paramMap) {
        return BaseResponse.success(wxGoodsService.auditCallback(paramMap));
    }

}
