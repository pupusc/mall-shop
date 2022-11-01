package com.soybean.mall.goods.service;

import com.soybean.common.resp.CommonPageResp;
import com.soybean.elastic.api.req.EsSpuNewQueryProviderReq;
import com.soybean.elastic.api.resp.EsSpuNewResp;
import com.soybean.mall.common.CommonUtil;
import com.soybean.mall.goods.response.SpuNewBookListResp;
import com.soybean.mall.goods.response.sputopic.HomeSpuTopicResp;
import com.soybean.mall.goods.response.sputopic.HomeTitleResp;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.enums.IndexModuleEnum;
import com.wanmi.sbc.goods.api.enums.StateEnum;
import com.wanmi.sbc.goods.api.provider.IndexCmsProvider;
import com.wanmi.sbc.goods.api.provider.index.NormalModuleProvider;
import com.wanmi.sbc.goods.api.request.index.NormalModuleSearchReq;
import com.wanmi.sbc.goods.api.request.index.NormalModuleSkuSearchReq;
import com.wanmi.sbc.goods.api.response.index.IndexModuleVo;
import com.wanmi.sbc.goods.api.response.index.NormalModuleResp;
import com.wanmi.sbc.goods.api.response.index.NormalModuleSkuResp;
import com.wanmi.sbc.goods.bean.enums.PublishState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: 栏目信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/13 4:48 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
@Slf4j
public class NormalModelService {


    @Autowired
    private NormalModuleProvider normalModuleProvider;

    @Autowired
    private IndexCmsProvider indexCmsProvider;

    @Autowired
    private EsSpuNewSearchService esSpuNewSearchService;

    @Autowired
    private SpuNewSearchService spuNewSearchService;

    @Autowired
    private CommonUtil commonUtil;


    /**
     * 获取主副标题
     * @return
     */
    private Map<String, HomeTitleResp> getHomeTitle() {
        BaseResponse<List<IndexModuleVo>> indexModuleVoResponse = indexCmsProvider.searchTitle();
        List<IndexModuleVo> context = indexModuleVoResponse.getContext();
        if (CollectionUtils.isEmpty(context)) {
            return new HashMap<>();
        }
        Map<String, HomeTitleResp> code2IndexModuleMap =
                context.stream()
                        .filter(ex -> Objects.equals(ex.getPublishState(), PublishState.ENABLE.toValue()))
                        .map(ex -> {
                            HomeTitleResp homeTitleResp = new HomeTitleResp();
                            BeanUtils.copyProperties(ex, homeTitleResp);
                            return homeTitleResp;
                        }).collect(Collectors.toMap(HomeTitleResp::getCode, Function.identity(), (k1, k2) -> k1));
        log.info("HomePageService 获取 主副标题的结果为：{}", code2IndexModuleMap);
        return code2IndexModuleMap;
    }


    /**
     * 获取栏目信息
     * @return
     */
    public HomeSpuTopicResp<HomeTitleResp, List<SpuNewBookListResp>> homeSpuTopic() {
        Map<String, HomeTitleResp> homeTopicMap = this.getHomeTitle();
        HomeTitleResp homeTitleResp = homeTopicMap.get(IndexModuleEnum.SPU_TOPIC.getCode());
        if (homeTitleResp == null) {
            return new HomeSpuTopicResp<>(homeTitleResp, new ArrayList<>());
        }
        NormalModuleSearchReq searchReq = new NormalModuleSearchReq();
//        searchReq.setChannelTypes(Collections.singletonList(commonUtil.getTerminal().getCode()));
        searchReq.setPageSize(1);
        searchReq.setStatus(StateEnum.RUNNING.getCode());
        searchReq.setPublishState(PublishState.ENABLE.toValue());
        CommonPageResp<List<NormalModuleResp>> context = normalModuleProvider.list(searchReq).getContext();
        if (CollectionUtils.isEmpty(context.getContent())) {
            return new HomeSpuTopicResp<>(homeTitleResp, new ArrayList<>());
        }
        NormalModuleResp normalModuleResp = context.getContent().get(0);
        //获取栏目下的商品列表
        NormalModuleSkuSearchReq skuSearchReq = new NormalModuleSkuSearchReq();
        skuSearchReq.setNormalModuleId(normalModuleResp.getId());
        List<NormalModuleSkuResp> normalModuleSkuResps = normalModuleProvider.listNormalModuleSku(skuSearchReq).getContext();
        if (CollectionUtils.isEmpty(normalModuleSkuResps)) {
            return new HomeSpuTopicResp<>(homeTitleResp, new ArrayList<>());
        }
        //封装结果对象信息
        List<String> spuIds = normalModuleSkuResps.stream().map(NormalModuleSkuResp::getSpuId).collect(Collectors.toList());
        EsSpuNewQueryProviderReq esSpuNewQueryProviderReq = new EsSpuNewQueryProviderReq();
        esSpuNewQueryProviderReq.setSpuIds(spuIds);
        CommonPageResp<List<EsSpuNewResp>> listCommonPageResp = esSpuNewSearchService.listNormalEsSpuNew(esSpuNewQueryProviderReq);
        List<SpuNewBookListResp> spuNewBookListResps = spuNewSearchService.listSpuNewSearch(listCommonPageResp.getContent(), new ArrayList<>());
        //排序展示
        Map<String, SpuNewBookListResp> spuId2RespMap =
                spuNewBookListResps.stream().collect(Collectors.toMap(SpuNewBookListResp::getSpuId, Function.identity(), (k1, k2) -> k1));

        List<SpuNewBookListResp> result = new ArrayList<>();
        Map<String, Boolean> spuId2ExistsMap = new HashMap<>();
        for (NormalModuleSkuResp normalModuleSkuResp : normalModuleSkuResps) {
            SpuNewBookListResp spuNewBookListResp = spuId2RespMap.get(normalModuleSkuResp.getSpuId());
            if (spuNewBookListResp == null) {
                continue;
            }
            Boolean isExists = spuId2ExistsMap.get(normalModuleSkuResp.getSpuId());
            if (isExists == null) {
                spuNewBookListResp.setSpuTag(normalModuleSkuResp.getSkuTag());
                result.add(spuNewBookListResp);
                spuId2ExistsMap.put(normalModuleSkuResp.getSpuId(), true);
            }
        }
        return new HomeSpuTopicResp<>(homeTitleResp, result);
    }
}
