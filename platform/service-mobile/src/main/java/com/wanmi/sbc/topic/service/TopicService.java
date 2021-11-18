package com.wanmi.sbc.topic.service;


import com.wanmi.sbc.booklistmodel.BookListModelAndGoodsService;
import com.wanmi.sbc.booklistmodel.response.GoodsCustomResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoQueryRequest;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsVO;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.setting.api.provider.topic.TopicConfigProvider;
import com.wanmi.sbc.setting.api.request.topicconfig.TopicQueryRequest;
import com.wanmi.sbc.setting.bean.dto.AtmosphereDTO;
import com.wanmi.sbc.setting.bean.dto.TopicStoreyContentDTO;
import com.wanmi.sbc.setting.bean.vo.TopicActivityVO;
import com.wanmi.sbc.topic.response.AtmosphereResponse;
import com.wanmi.sbc.topic.response.TopicResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TopicService {

    @Autowired
    private TopicConfigProvider topicConfigProvider;

    @Autowired
    private EsGoodsInfoElasticQueryProvider esGoodsInfoElasticQueryProvider;

    @Autowired
    private BookListModelAndGoodsService bookListModelAndGoodsService;

    @Autowired
    private AtmosphereService atmosphereService;

    public BaseResponse<TopicResponse> detail(@RequestBody TopicQueryRequest request){
        BaseResponse<TopicActivityVO> activityVO =  topicConfigProvider.detail(request);
        if(activityVO == null || activityVO.getContext() ==null){
            return BaseResponse.success(null);
        }
        TopicResponse response = KsBeanUtil.convert(activityVO.getContext(),TopicResponse.class);
        //如果配置有一行两个商品的配置信息，查询商品
        List<String> skuIds = new ArrayList<>();
        if(CollectionUtils.isEmpty(activityVO.getContext().getStoreyList())){
            return BaseResponse.success(response);
        }
        activityVO.getContext().getStoreyList().stream().filter(p->p.getStoreyType()!= null && p.getStoreyType().equals(3)).forEach(p->{
            if(CollectionUtils.isNotEmpty(p.getContents())) {
                skuIds.addAll(p.getContents().stream().filter(c -> c.getType() != null && c.getType().equals(1)).map(TopicStoreyContentDTO::getSkuId).collect(Collectors.toList()));
            } });
        if(CollectionUtils.isNotEmpty(skuIds)){
            List<GoodsCustomResponse> list = initGoods(skuIds);
            if(CollectionUtils.isNotEmpty(list)){
                response.getStoreyList().stream().filter(p->p.getStoreyType().equals(3)).forEach(p->{
                    p.getContents().stream().filter(g->g.getType().equals(1)).forEach(g->{
                        if(list.stream().anyMatch(l->l.getGoodsInfoId().equals(g.getSkuId()))){
                            g.setGoods(list.stream().filter(l->l.getGoodsInfoId().equals(g.getSkuId())).findFirst().get());
                        }
                    });
                });
            }
        }
        //氛围信息
        List<AtmosphereDTO> atmosphereList = atmosphereService.getAtmosphere();
        if(CollectionUtils.isEmpty(atmosphereList) ){
            return BaseResponse.success(response);
        }
        response.getStoreyList().forEach(storey->{
            if(CollectionUtils.isNotEmpty(storey.getContents()) &&  storey.getContents().stream().anyMatch(p->p.getType() == 1)){
                storey.getContents().stream().filter(p->p.getType() == 1).forEach(c->{
                    Optional<AtmosphereDTO> atmosphereDTOOptional = atmosphereList.stream().filter(atmos->atmos.getSkuId().equals(c.getSkuId())).findFirst();
                    if(atmosphereDTOOptional.isPresent()){
                        AtmosphereDTO atmosphereDTO = atmosphereDTOOptional.get();
                        c.setAtmosphere(KsBeanUtil.convert(atmosphereDTO, AtmosphereResponse.class));
                    }
                });
            }
        });
        return BaseResponse.success(response);
    }

    private List<GoodsCustomResponse> initGoods(List<String> goodIds) {
        List<GoodsCustomResponse> goodList = new ArrayList<>();
        //根据商品id列表 获取商品列表信息
        EsGoodsInfoQueryRequest queryRequest = new EsGoodsInfoQueryRequest();
        queryRequest.setPageNum(0);
        queryRequest.setPageSize(goodIds.size());
        queryRequest.setGoodsInfoIds(goodIds);
        queryRequest.setQueryGoods(true);
        queryRequest.setAddedFlag(AddedFlag.YES.toValue());
        queryRequest.setDelFlag(DeleteFlag.NO.toValue());
        queryRequest.setAuditStatus(CheckStatus.CHECKED.toValue());
        queryRequest.setStoreState(StoreState.OPENING.toValue());
        queryRequest.setVendibility(Constants.yes);
        List<EsGoodsVO> esGoodsVOS = esGoodsInfoElasticQueryProvider.pageByGoods(queryRequest).getContext().getEsGoods().getContent();
        List<GoodsVO> goodsVOList = bookListModelAndGoodsService.changeEsGoods2GoodsVo(esGoodsVOS);
        Map<String, GoodsVO> spuId2GoodsVoMap = goodsVOList.stream().collect(Collectors.toMap(GoodsVO::getGoodsId, Function.identity(), (k1, k2) -> k1));
        List<GoodsInfoVO> goodsInfoVOList = bookListModelAndGoodsService.packageGoodsInfoList(esGoodsVOS, null);
        for (EsGoodsVO goodsVo : esGoodsVOS) {
            GoodsCustomResponse goodsCustom = bookListModelAndGoodsService
                    .packageGoodsCustomResponse(spuId2GoodsVoMap.get(goodsVo.getId()), goodsVo, goodsInfoVOList);
            goodList.add(goodsCustom);
        }
        return goodList;
    }

}
