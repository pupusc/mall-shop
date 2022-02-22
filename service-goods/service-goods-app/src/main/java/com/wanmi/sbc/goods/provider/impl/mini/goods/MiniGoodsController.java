package com.wanmi.sbc.goods.provider.impl.mini.goods;

import com.soybean.mall.wx.mini.goods.bean.request.WxDeleteProductRequest;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.api.provider.mini.goods.WxMiniGoodsProvider;
import com.wanmi.sbc.goods.bean.wx.request.WxGoodsCreateRequest;
import com.wanmi.sbc.goods.bean.wx.request.WxGoodsSearchRequest;
import com.wanmi.sbc.goods.bean.wx.vo.WxGoodsVo;
import com.wanmi.sbc.goods.mini.model.goods.WxGoodsModel;
import com.wanmi.sbc.goods.mini.service.goods.WxGoodsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class MiniGoodsController implements WxMiniGoodsProvider {

    @Autowired
    private WxGoodsService wxGoodsService;

    @Override
    public BaseResponse<MicroServicePage<WxGoodsVo>> list(WxGoodsSearchRequest wxGoodsSearchRequest) {
        Page<WxGoodsModel> goodsPage = wxGoodsService.listGoods(wxGoodsSearchRequest);
        List<WxGoodsModel> content = goodsPage.getContent();
        MicroServicePage<WxGoodsVo> microServicePage = new MicroServicePage<>();
        microServicePage.setTotal(goodsPage.getTotalElements());
        List<WxGoodsVo> voList = content.stream().map(g -> {
            WxGoodsVo wxGoodsVo = new WxGoodsVo();
            BeanUtils.copyProperties(g, wxGoodsVo);
            wxGoodsVo.setAuditStatus(g.getAuditStatus().toValue());
            wxGoodsVo.setStatus(g.getStatus().toValue());
            return wxGoodsVo;
        }).collect(Collectors.toList());
        microServicePage.setContent(voList);
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
