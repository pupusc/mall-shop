package com.wanmi.sbc.goods.provider.impl.mini.assistant;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.api.provider.mini.assistant.WxLiveAssistantProvider;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantSearchRequest;
import com.wanmi.sbc.goods.bean.wx.vo.assistant.WxLiveAssistantGoodsVo;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.request.GoodsInfoQueryRequest;
import com.wanmi.sbc.goods.info.service.GoodsInfoService;
import com.wanmi.sbc.goods.info.service.GoodsService;
import com.wanmi.sbc.goods.mini.model.goods.WxLiveAssistantGoodsModel;
import com.wanmi.sbc.goods.mini.service.assistant.WxLiveAssistantService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class WxLiveAssistantController implements WxLiveAssistantProvider {

    @Autowired
    private WxLiveAssistantService wxLiveAssistantService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsInfoService goodsInfoService;

    @Override
    public BaseResponse<MicroServicePage<WxLiveAssistantGoodsVo>> listGoods(WxLiveAssistantSearchRequest wxLiveAssistantSearchRequest) {
        Page<WxLiveAssistantGoodsModel> wxLiveAssistantGoodsPage = wxLiveAssistantService.listGoods(wxLiveAssistantSearchRequest);
        List<WxLiveAssistantGoodsModel> content = wxLiveAssistantGoodsPage.getContent();

        MicroServicePage<WxLiveAssistantGoodsVo> microServicePage = new MicroServicePage<>();
        if (CollectionUtils.isNotEmpty(content)) {
            List<String> goodsIds = content.stream().map(WxLiveAssistantGoodsModel::getGoodsId).collect(Collectors.toList());
            List<Goods> goodsList = goodsService.listByGoodsIds(goodsIds);
            Map<String, List<Goods>> goodsMap = goodsList.stream().collect(Collectors.groupingBy(Goods::getGoodsId));

            List<GoodsInfo> goodsInfoList = goodsInfoService.findByParams(GoodsInfoQueryRequest.builder().goodsIds(goodsIds).delFlag(DeleteFlag.NO.toValue()).build());
            Map<String, List<GoodsInfo>> goodsInfoMap = goodsInfoList.stream().collect(Collectors.groupingBy(GoodsInfo::getGoodsId));

            microServicePage.setTotal(wxLiveAssistantGoodsPage.getTotalElements());
            List<WxLiveAssistantGoodsVo> voList = new ArrayList<>();
            for (WxLiveAssistantGoodsModel wxLiveAssistantGoodsModel : content) {
                WxLiveAssistantGoodsVo wxLiveAssistantGoodsVo = new WxLiveAssistantGoodsVo();
                wxLiveAssistantGoodsVo.setAssistantGoodsId(wxLiveAssistantGoodsModel.getId());
                wxLiveAssistantGoodsVo.setGoodsId(wxLiveAssistantGoodsModel.getGoodsId());
                List<Goods> goodsGroup = goodsMap.get(wxLiveAssistantGoodsModel.getGoodsId());
                if (goodsGroup != null) {
                    Goods goods = goodsGroup.get(0);
                    wxLiveAssistantGoodsVo.setGoodsName(goods.getGoodsName());
                    wxLiveAssistantGoodsVo.setMarketPrice(goods.getSkuMinMarketPrice() != null ? goods.getSkuMinMarketPrice().toString() : "0");
                }
                List<GoodsInfo> goodsInfoGroup = goodsInfoMap.get(wxLiveAssistantGoodsModel.getGoodsId());
                List<WxLiveAssistantGoodsVo.WxLiveAssistantGoodsInfoVo> wxLiveAssistantGoodsInfoVos = new ArrayList<>();
                Long stockSum = 0L;
                if (goodsInfoGroup != null) {
                    for (GoodsInfo goodsInfo : goodsInfoGroup) {
                        WxLiveAssistantGoodsVo.WxLiveAssistantGoodsInfoVo wxLiveAssistantGoodsInfoVo = new WxLiveAssistantGoodsVo.WxLiveAssistantGoodsInfoVo();
                        wxLiveAssistantGoodsInfoVo.setGoodsInfoName(goodsInfo.getGoodsInfoName());
                        Long stock = goodsInfo.getStock();
                        if (stock != null){
                            stockSum += stock;
                            wxLiveAssistantGoodsInfoVo.setStock(stock.intValue());
                        }
                        wxLiveAssistantGoodsInfoVo.setMarketPrice(goodsInfo.getMarketPrice() != null ? goodsInfo.getMarketPrice().toString() : "0");
                        wxLiveAssistantGoodsInfoVos.add(wxLiveAssistantGoodsInfoVo);
                    }
                }
                wxLiveAssistantGoodsVo.setStock(stockSum.intValue());
                wxLiveAssistantGoodsVo.setGoodsInfos(wxLiveAssistantGoodsInfoVos);
                voList.add(wxLiveAssistantGoodsVo);
            }
            microServicePage.setContent(voList);
        }
        return BaseResponse.success(microServicePage);
    }

}
