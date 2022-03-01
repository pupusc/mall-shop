package com.wanmi.sbc.goods.mini.service.assistant;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.bean.wx.request.WxGoodsSearchRequest;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantCreateRequest;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantGoodsCreateRequest;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantGoodsUpdateRequest;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantSearchRequest;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.service.GoodsInfoService;
import com.wanmi.sbc.goods.mini.model.goods.WxGoodsModel;
import com.wanmi.sbc.goods.mini.model.goods.WxLiveAssistantGoodsModel;
import com.wanmi.sbc.goods.mini.model.goods.WxLiveAssistantModel;
import com.wanmi.sbc.goods.mini.repository.goods.WxLiveAssistantGoodsRepository;
import com.wanmi.sbc.goods.mini.repository.goods.WxLiveAssistantRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class WxLiveAssistantService {

    @Autowired
    private WxLiveAssistantRepository wxLiveAssistantRepository;
    @Autowired
    private WxLiveAssistantGoodsRepository wxLiveAssistantGoodsRepository;
    @Autowired
    private GoodsInfoService goodsInfoService;

    public void addAssistant(WxLiveAssistantCreateRequest wxLiveAssistantCreateRequest){
        WxLiveAssistantModel wxLiveAssistantModel = WxLiveAssistantModel.create(wxLiveAssistantCreateRequest);
        //todo 发送延时消息
        wxLiveAssistantRepository.save(wxLiveAssistantModel);
    }

    public void deleteAssistant(Long id){
        Optional<WxLiveAssistantModel> opt = wxLiveAssistantRepository.findById(id);
        if(opt.isPresent()){
            WxLiveAssistantModel wxLiveAssistantModel = opt.get();
            wxLiveAssistantModel.setDelFlag(DeleteFlag.YES);
            wxLiveAssistantModel.setUpdateTime(LocalDateTime.now());
            wxLiveAssistantRepository.save(wxLiveAssistantModel);
        }
    }

    public void updateAssistant(WxLiveAssistantCreateRequest wxLiveAssistantCreateRequest){
        Optional<WxLiveAssistantModel> opt = wxLiveAssistantRepository.findById(wxLiveAssistantCreateRequest.getId());
        if(opt.isPresent()){
            WxLiveAssistantModel wxLiveAssistantModel = opt.get();
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            if(wxLiveAssistantCreateRequest.getTheme() != null){
                wxLiveAssistantModel.setTheme(wxLiveAssistantCreateRequest.getTheme());
            }
            if (wxLiveAssistantCreateRequest.getStartTime() != null){
                wxLiveAssistantModel.setStartTime(LocalDateTime.parse(wxLiveAssistantCreateRequest.getStartTime(), df));
            }
            if(wxLiveAssistantCreateRequest.getEndTime() != null){
                wxLiveAssistantModel.setEndTime(LocalDateTime.parse(wxLiveAssistantCreateRequest.getEndTime(), df));
            }
            wxLiveAssistantRepository.save(wxLiveAssistantModel);
        }
    }

    public Page<WxLiveAssistantModel> listAssistant(WxLiveAssistantSearchRequest wxLiveAssistantSearchRequest){
        return wxLiveAssistantRepository.findAll(PageRequest.of(wxLiveAssistantSearchRequest.getPageNum(), wxLiveAssistantSearchRequest.getPageSize(),
                Sort.by(Sort.Direction.DESC, "startTime")));
    }

    @Transactional
    public void addGoods(WxLiveAssistantGoodsCreateRequest wxLiveAssistantGoodsCreateRequest){
        Long assistantId = wxLiveAssistantGoodsCreateRequest.getAssistantId();
        if (assistantId == null) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播计划id不能为空");
        Optional<WxLiveAssistantModel> opt = wxLiveAssistantRepository.findById(assistantId);
        if(!opt.isPresent()) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播计划不存在");
        WxLiveAssistantModel wxLiveAssistantModel = opt.get();
        wxLiveAssistantModel.setGoodsCount(wxLiveAssistantModel.getGoodsCount() + wxLiveAssistantGoodsCreateRequest.getGoods().size());
        wxLiveAssistantRepository.save(wxLiveAssistantModel);

        List<WxLiveAssistantGoodsModel> assistantGoodsModels = WxLiveAssistantGoodsModel.create(wxLiveAssistantGoodsCreateRequest);
        wxLiveAssistantGoodsRepository.saveAll(assistantGoodsModels);
//        List<String> goodsIds = goods.stream().map(WxLiveAssistantCreateRequest.AssistantGoods::getGoodsId).collect(Collectors.toList());
//        List<GoodsInfo> allGoodsInfos = goodsInfoService.findByParams(GoodsInfoQueryRequest.builder().goodsIds(goodsIds).build());
//        Map<String, List<GoodsInfo>> goodsInfoGroup = allGoodsInfos.stream().collect(Collectors.groupingBy(GoodsInfo::getGoodsId));
//
//        for (WxLiveAssistantCreateRequest.AssistantGoods good : goods) {
//            WxLiveAssistantGoodsModel wxLiveAssistantGoodsModel = new WxLiveAssistantGoodsModel();
//            wxLiveAssistantGoodsModel.setAssistId(assistantId);
//            wxLiveAssistantGoodsModel.setGoodsId(good.getGoodsId());
//            List<GoodsInfo> goodsInfos = goodsInfoGroup.get(good.getGoodsId());
//            goodsInfos
//            wxLiveAssistantGoodsModel.set
//        }
    }

    public void deleteGoods(Long id){
        Optional<WxLiveAssistantGoodsModel> opt = wxLiveAssistantGoodsRepository.findById(id);
        if(opt.isPresent()){
            WxLiveAssistantGoodsModel wxLiveAssistantGoodsModel = opt.get();
            wxLiveAssistantGoodsModel.setDelFlag(DeleteFlag.YES);
            wxLiveAssistantGoodsModel.setUpdateTime(LocalDateTime.now());
            wxLiveAssistantGoodsRepository.save(wxLiveAssistantGoodsModel);
        }
    }

    public void updateGoods(WxLiveAssistantGoodsUpdateRequest wxLiveAssistantGoodsUpdateRequest){
        String goodsInfoId = wxLiveAssistantGoodsUpdateRequest.getGoodsInfoId();
        String price = wxLiveAssistantGoodsUpdateRequest.getPrice();
        Integer stock = wxLiveAssistantGoodsUpdateRequest.getStock();

        List<GoodsInfo> goodsInfos = goodsInfoService.findByIds(Collections.singletonList(goodsInfoId));

        //todo
    }

    public Page<WxLiveAssistantGoodsModel> listGoods(WxLiveAssistantSearchRequest wxLiveAssistantSearchRequest){
        return wxLiveAssistantGoodsRepository.findAll(wxLiveAssistantGoodsRepository.buildSearchCondition(wxLiveAssistantSearchRequest), PageRequest.of(wxLiveAssistantSearchRequest.getPageNum(), wxLiveAssistantSearchRequest.getPageNum()));
    }


}
