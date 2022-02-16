package com.wanmi.sbc.goods.mini.service.goods;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.bean.request.wx.goods.WxGoodsCreateRequest;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.repository.GoodsInfoRepository;
import com.wanmi.sbc.goods.mini.enums.goods.WxGoodsEditStatus;
import com.wanmi.sbc.goods.mini.enums.goods.WxGoodsStatus;
import com.wanmi.sbc.goods.mini.model.goods.WxGoodsModel;
import com.wanmi.sbc.goods.mini.repository.goods.WxGoodsRepository;
import com.wanmi.sbc.goods.mini.wx.service.WxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class WxGoodsService {

    @Autowired
    private WxGoodsRepository wxGoodsRepository;
    @Autowired
    private GoodsInfoRepository goodsInfoRepository;
    @Autowired
    private WxService wxService;

    @Transactional
    public void addGoods(WxGoodsCreateRequest createRequest){
        if(goodsExist(createRequest)) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "商品已上架");
        WxGoodsModel wxGoodsModel = new WxGoodsModel();
        wxGoodsModel.setGoodsInfoId(createRequest.getGoodsId());
        wxGoodsModel.setGoodsInfoId(createRequest.getGoodsInfoId());
        wxGoodsModel.setWxCategory(createRequest.getWxCategory());
        wxGoodsModel.setStatus(WxGoodsStatus.WAIT_UPLOAD);
        wxGoodsModel.setEditStatus(WxGoodsEditStatus.WAIT_CHECK);
        LocalDateTime now = LocalDateTime.now();
        wxGoodsModel.setCreateTime(now);
        wxGoodsModel.setUpdateTime(now);
        wxGoodsModel.setDelFlag(DeleteFlag.NO);
        wxGoodsRepository.save(wxGoodsModel);

        if(!wxService.uploadGoodsToWx(createRequest.getGoodsId(), createRequest.getWxCategory())){
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "上传微信商品失败");
        }
    }

    @Transactional
    public void updateGoodsAndReaudit(WxGoodsCreateRequest createRequest){
        Optional<WxGoodsModel> opt = wxGoodsRepository.findById(createRequest.getId());
        if(opt.isPresent()){
            WxGoodsModel wxGoodsModel = opt.get();
            boolean updated = false;
            if(!createRequest.getWxCategory().equals(wxGoodsModel.getWxCategory())){
                wxGoodsModel.setWxCategory(createRequest.getWxCategory());
                updated = true;
            }
            if(updated){
                wxGoodsModel.setUpdateTime(LocalDateTime.now());
                wxGoodsModel.setEditStatus(WxGoodsEditStatus.WAIT_CHECK);
//                wxGoodsModel.setStatus(WxGoodsStatus.ON_SHELF);
                wxGoodsRepository.save(wxGoodsModel);
            }
            if(!wxService.uploadGoodsToWx(createRequest.getGoodsId(), createRequest.getWxCategory())){
                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "重审上传微信商品失败");
            }
        }
    }

    public void updateGoodsWithoutReaudit(WxGoodsCreateRequest createRequest){

    }

    private boolean goodsExist(WxGoodsCreateRequest createRequest){
        Integer onShelfCount = wxGoodsRepository.getOnShelfCount(createRequest.getGoodsId());
        return onShelfCount > 0;
    }
}
