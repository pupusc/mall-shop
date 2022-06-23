package com.soybean.elastic.collect.service.spu.service;
import com.google.common.collect.Lists;

import com.soybean.elastic.api.enums.SearchSpuNewCategoryEnum;
import com.soybean.elastic.collect.service.spu.AbstractSpuCollect;
import com.soybean.elastic.spu.model.EsSpuNew;
import com.soybean.elastic.spu.model.sub.SubAwardNew;
import com.soybean.elastic.spu.model.sub.SubBookLabelNew;
import com.soybean.elastic.spu.model.sub.SubBookNew;
import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaBookResp;
import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaReq;
import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaResp;
import com.wanmi.sbc.bookmeta.provider.collect.CollectMetaBookProvider;
import com.wanmi.sbc.goods.api.provider.collect.CollectSpuPropProvider;
import com.wanmi.sbc.goods.api.request.collect.CollectClassifyProviderReq;
import com.wanmi.sbc.goods.api.request.collect.CollectSpuPropProviderReq;
import com.wanmi.sbc.goods.api.response.collect.CollectClassifyRelSpuResp;
import com.wanmi.sbc.goods.api.response.collect.CollectSpuPropResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: 采集图书信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/1 2:59 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class BookCollect extends AbstractSpuCollect {

    @Autowired
    protected CollectMetaBookProvider collectMetaBookProvider;

    /**
     * 采集商品id信息
     * @param beginTime
     * @param endTime
     * @return
     */
    protected CollectMetaResp collectBookByTime(LocalDateTime beginTime, LocalDateTime endTime, Integer fromId) {
        CollectMetaReq req = new CollectMetaReq();
        req.setFromId(fromId);
        req.setPageSize(MAX_PAGE_SIZE);
        req.setBeginTime(beginTime);
        req.setEndTime(endTime);
        return collectMetaBookProvider.collectMetaBookByTime(req).getContext();
    }


    @Override
    public Set<String> collectId(LocalDateTime lastCollectTime, LocalDateTime now) {
        CollectMetaResp collectMetaResp = this.collectBookByTime(lastCollectTime, now, 0);
        Set<String> result = new HashSet<>(super.collectSpuPropByIsbn(collectMetaResp));
        while (collectMetaResp.isHasMore()) {
            collectMetaResp = this.collectBookByTime(lastCollectTime, now, collectMetaResp.getLastBizId());
            result.addAll(super.collectSpuPropByIsbn(collectMetaResp));
        }
        return result;
    }



    @Override
    public <T> List<T> collect(List<T> list) {
        List<String> isbnList =
                list.stream().filter(t -> SearchSpuNewCategoryEnum.BOOK.getCode().equals(((EsSpuNew) t).getSpuCategory()))
                        .map(t -> ((EsSpuNew) t).getBook().getIsbn()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(isbnList)) {
            return list;
        }
        CollectMetaReq collectMetaReq = new CollectMetaReq();
        collectMetaReq.setIsbns(isbnList);
        List<CollectMetaBookResp> bookResps = collectMetaBookProvider.collectMetaBook(collectMetaReq).getContext();
        if (CollectionUtils.isEmpty(bookResps)) {
            return list;
        }
        Map<String, CollectMetaBookResp> isbn2BookMap = bookResps.stream().collect(Collectors.toMap(CollectMetaBookResp::getIsbn, Function.identity(), (k1, k2) -> k1));
        for (T t : list) {
            EsSpuNew esSpuNew = (EsSpuNew) t;
            SubBookNew book = esSpuNew.getBook();
            if (book == null || StringUtils.isEmpty(book.getIsbn())) {
//                表示非图书商品
                continue;
            }
            CollectMetaBookResp collectMetaBookResp = isbn2BookMap.get(book.getIsbn());
            if (collectMetaBookResp == null){
                continue;
            }
            book.setIsbn(collectMetaBookResp.getIsbn());
            book.setBookName(collectMetaBookResp.getBookName());
            book.setBookOriginName(collectMetaBookResp.getBookOriginName());
            book.setBookDesc(collectMetaBookResp.getBookDesc());
            book.setAuthorNames(collectMetaBookResp.getAuthorNames());
//            book.setScore(0.0D); //在商品部分添加
            book.setPublisher(collectMetaBookResp.getPublisher());
            book.setFixPrice(collectMetaBookResp.getFixPrice());
            book.setProducer(collectMetaBookResp.getProducer());
            book.setClumpName(collectMetaBookResp.getClumpName());
            book.setGroupName(collectMetaBookResp.getGroupName());
            book.setBindingName(collectMetaBookResp.getBindingName());
            List<CollectMetaBookResp.Award> awards = collectMetaBookResp.getAwards() == null ? new ArrayList<>() : collectMetaBookResp.getAwards();
            List<SubAwardNew> awardNews = new ArrayList<>();
            for (CollectMetaBookResp.Award award : awards) {
                SubAwardNew subAwardNew = new SubAwardNew();
                subAwardNew.setAwardCategory(award.getAwardCategory());
                subAwardNew.setAwardName(award.getAwardName());
                awardNews.add(subAwardNew);
            }
            book.setAwards(awardNews);

            List<SubBookLabelNew> bookLabelNews = new ArrayList<>();
            List<CollectMetaBookResp.Tag> tags = collectMetaBookResp.getTags() == null ? new ArrayList<>() : collectMetaBookResp.getTags();
            for (CollectMetaBookResp.Tag tag : tags) {
                SubBookLabelNew subBookLabelNew = new SubBookLabelNew();
                subBookLabelNew.setStagId(tag.getStagId());
                subBookLabelNew.setStagName(tag.getStagName());
                subBookLabelNew.setTagId(tag.getTagId());
                subBookLabelNew.setTagName(tag.getTagName());
                bookLabelNews.add(subBookLabelNew);
            }
            book.setTags(bookLabelNews);
        }
        return list;
    }
}
