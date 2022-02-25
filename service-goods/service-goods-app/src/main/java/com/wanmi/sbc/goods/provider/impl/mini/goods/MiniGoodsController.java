package com.wanmi.sbc.goods.provider.impl.mini.goods;

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

@RestController
public class MiniGoodsController implements WxMiniGoodsProvider {

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
            for (WxGoodsModel wxGoodsModel : content) {
                WxGoodsVo wxGoodsVo = new WxGoodsVo();
                BeanUtils.copyProperties(wxGoodsModel, wxGoodsVo);
//                wxGoodsVo.setAuditStatus(g.getAuditStatus().toValue());
//                wxGoodsVo.setStatus(g.getStatus().toValue());
                wxGoodsVo.setUploadTime(wxGoodsModel.getUploadTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                if(collect != null){
                    Goods goods = collect.get(wxGoodsModel.getGoodsId()).get(0);
                    wxGoodsVo.setGoodsName(goods.getGoodsName());
                    wxGoodsVo.setGoodsImg(goods.getGoodsUnBackImg());
                    wxGoodsVo.setMarketPrice(goods.getMarketPrice().toString());
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
    public BaseResponse delete(WxDeleteProductRequest wxDeleteProductRequest) {
        wxGoodsService.deleteGoods(wxDeleteProductRequest);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse auditCallback(Map<String, Object> paramMap) {
        wxGoodsService.auditCallback(paramMap);
        return BaseResponse.SUCCESSFUL();
    }


}
