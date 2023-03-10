package com.wanmi.sbc.setting.service;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goodsPool.PoolFactory;
import com.wanmi.sbc.goodsPool.service.PoolService;
import com.wanmi.sbc.setting.api.provider.topic.TopicConfigProvider;
import com.wanmi.sbc.setting.api.request.topicconfig.ColumnContentQueryRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.MixedComponentContentRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.MixedComponentGoodsAddRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.MixedComponentTabQueryRequest;
import com.wanmi.sbc.setting.bean.dto.*;
import com.wanmi.sbc.setting.bean.enums.MixedComponentLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: todo-zh
 * @Author zh
 * @Date 2023/2/26 13:52
 */
@Service
public class TopicConfigService {

    @Autowired
    private TopicConfigProvider topicConfigProvider;

    @Autowired
    private PoolFactory poolFactory;


    public void addMixedComponentVideo(MixedComponentGoodsAddRequest request) {
        topicConfigProvider.addTopicStoreyColumnGoods(request);
    }

    public List<KeyWordDto> previewGoodsPool(MixedComponentContentRequest request) {
        //栏目信息
        Integer topicStoreyId = 194;
        MixedComponentTabQueryRequest requestTab = new MixedComponentTabQueryRequest();
        requestTab.setTopicStoreyId(topicStoreyId);
        requestTab.setPublishState(0);
        requestTab.setState(1);
        List<MixedComponentTabDto> mixedComponentTab = topicConfigProvider.listMixedComponentTab(requestTab).getContext();

        Integer tabId = request.getTabId();
        // 获取关键字
        List<KeyWordDto> keywords = new ArrayList<>();
        mixedComponentTab.stream().filter(c -> MixedComponentLevel.TWO.toValue().equals(c.getLevel()) && tabId.equals(c.getPId()))
                .map(c -> {return c.getKeywords();}).collect(Collectors.toList())
                .forEach(c -> {c.forEach(s -> {keywords.add(new KeyWordDto(s.getId(), s.getName()));});});
        // 获取规则
        List<String> rules = new ArrayList<>();
        mixedComponentTab.stream().filter(c -> MixedComponentLevel.THREE.toValue().equals(c.getLevel()) && tabId.equals(c.getPId()))
                .map(c -> {return c.getKeywords();}).collect(Collectors.toList())
                .forEach(c -> {c.forEach(s -> {rules.add(s.getName());});});
        //获取商品池
        List<MixedComponentTabDto> pools = mixedComponentTab.stream().filter(c -> MixedComponentLevel.FOUR.toValue().equals(c.getLevel()) && rules.contains(c.getDropName())).collect(Collectors.toList());
        String keyWord = request.getKeyWord();
        if (keyWord == null || "".equals(keyWord)) {
            keyWord = keywords.size() != 0 ? keywords.get(0).getName() : null;
        }
        String keyWordId = keywords.stream().filter(t -> keywords.equals(t.getName())).findFirst().get().getId();
        List<GoodsPoolDto> goodsPoolDtos = new ArrayList<>();
        for (MixedComponentTabDto pool : pools) {
            Integer id = pool.getId();
            ColumnContentQueryRequest columnContentQueryRequest = new ColumnContentQueryRequest();
            columnContentQueryRequest.setTopicStoreySearchId(id);
            List<ColumnContentDTO> columnContent = topicConfigProvider.ListTopicStoreyColumnContent(columnContentQueryRequest).getContext();
            PoolService poolService = poolFactory.getPoolService(pool.getBookType());
            poolService.getGoodsPool(goodsPoolDtos, columnContent, pool, keyWord);
        }
        //排序
        List<GoodsPoolDto> goodsPools = goodsPoolDtos.stream().sorted(Comparator.comparing(GoodsPoolDto::getSorting)
                        .thenComparing(Comparator.comparing(GoodsPoolDto::getType).reversed()))
                .collect(Collectors.toList());

        MicroServicePage goodsPoolPage = new MicroServicePage(goodsPools, Pageable.unpaged(), 10);
        if(keywords.size() != 0) {
            for (KeyWordDto s : keywords) {
                if (keyWord.equals(s.getName())) {
                    s.setGoodsPoolPage(goodsPoolPage);
                }
            }
        }
        return keywords;
    }

//    private MicroServicePage getPage(Param param, List<User> allList) {
//        Page page = new Page();
//        if (param.getCurrentPage() == null){
//            page.setCurrentPage(param.getCurrentPage());
//        } else {
//            page.setCurrentPage(param.getCurrentPage());
//        }
//        //设置每页数据为十条
//        page.setPageSize(param.getPageSize());
//        //每页的开始数
//        page.setStar((param.getCurrentPage() - 1) * param.getPageSize());
//        //list的大小
//        int count = allList.size();
//        //设置总页数
////        page.setTotalPage(count % 10 == 0 ? count / 10 : count / 10 + 1);
//        page.setTotals(allList.size());
//        //对list进行截取
//        page.setPageDatas(allList.subList(page.getStar(),count-page.getStar()>page.getPageSize()?page.getStar()+page.getPageSize():count));
//        return page;
//    }

}
