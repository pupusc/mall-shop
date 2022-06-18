package com.soybean.elastic.collect.service.booklistmodel.service;

import com.soybean.elastic.api.enums.SearchBookListCategoryEnum;
import com.soybean.elastic.booklistmodel.model.EsBookListModel;
import com.soybean.elastic.collect.service.booklistmodel.AbstractBookListModelCollect;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.api.enums.BusinessTypeEnum;
import com.wanmi.sbc.goods.api.provider.collect.CollectBookListModelProvider;
import com.wanmi.sbc.goods.api.request.collect.CollectBookListModelProviderReq;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/1 10:44 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class BookListModelCollect extends AbstractBookListModelCollect {

    @Autowired
    private CollectBookListModelProvider collectBookListModelProvider;

    private List<BookListModelProviderResponse> collectBookListId(LocalDateTime beginTime, LocalDateTime endtime, Integer fromId) {
        //获取商品列表
        CollectBookListModelProviderReq request = new CollectBookListModelProviderReq();
        request.setBeginTime(beginTime);
        request.setEndTime(endtime);
        request.setFromId(fromId);
        request.setBusinesstypes(Arrays.asList(BusinessTypeEnum.RANKING_LIST.getCode(), BusinessTypeEnum.BOOK_LIST.getCode(), BusinessTypeEnum.BOOK_RECOMMEND.getCode(), BusinessTypeEnum.FAMOUS_RECOMMEND.getCode()));
        request.setPageSize(MAX_PAGE_SIZE);
        return collectBookListModelProvider.collectBookListId(request).getContext();
    }

    @Override
    public Set<Long> collectId(LocalDateTime lastCollectTime, LocalDateTime now) {
        Set<Long> bookListModelIdSet = new HashSet<>();
        List<BookListModelProviderResponse> collectBookListIdList = this.collectBookListId(lastCollectTime, now, 0);
        for (BookListModelProviderResponse collectBookListParam : collectBookListIdList) {
            bookListModelIdSet.add(collectBookListParam.getId().longValue());
        }
        //可能没有获取完，再取一次
        while (collectBookListIdList.size() >= MAX_PAGE_SIZE) {
            Integer maxBookListId = collectBookListIdList.get(collectBookListIdList.size() - 1).getId();
            collectBookListIdList = this.collectBookListId(lastCollectTime, now, maxBookListId);
            for (BookListModelProviderResponse collectBookListParam : collectBookListIdList) {
                bookListModelIdSet.add(collectBookListParam.getId().longValue());
            }
        }
        return bookListModelIdSet;
    }

    @Override
    public <F> List<F> collect(List<F> list) {
        List<Integer> bookListModelIdList = list.stream().map(ex -> ((EsBookListModel)ex).getBookListId().intValue()).collect(Collectors.toList());

        //根据id获取书单信息
        CollectBookListModelProviderReq collectBookListModelProviderRequest = new CollectBookListModelProviderReq();
        collectBookListModelProviderRequest.setBookListModelIds(bookListModelIdList);
        List<BookListModelProviderResponse> context = collectBookListModelProvider.collectBookListByBookListIds(collectBookListModelProviderRequest).getContext();
        Map<Integer, BookListModelProviderResponse> bookListModelId2ModelMap =
                context.stream().collect(Collectors.toMap(BookListModelProviderResponse::getId, Function.identity(), (k1, k2) -> k1));
        for (F f : list) {
            EsBookListModel esBookListModel = (EsBookListModel) f;
            BookListModelProviderResponse bookListModel = bookListModelId2ModelMap.get(esBookListModel.getBookListId().intValue());
            if (bookListModel == null) {
                esBookListModel.setDelFlag(DeleteFlag.YES.toValue());
                continue;
            }
            esBookListModel.setBookListName(bookListModel.getName());
            esBookListModel.setBookListDesc(bookListModel.getDesc());
            if (Objects.equals(bookListModel.getBusinessType(), BusinessTypeEnum.RANKING_LIST.getCode())) {
                esBookListModel.setBookListCategory(SearchBookListCategoryEnum.RANKING_LIST.getCode());
            } else {
                esBookListModel.setBookListCategory(SearchBookListCategoryEnum.BOOK_LIST.getCode());
            }
            esBookListModel.setBookListBusinessType(bookListModel.getBusinessType());
            esBookListModel.setDelFlag(bookListModel.getDelFlag());
            esBookListModel.setHasTop(bookListModel.getHasTop());
            esBookListModel.setPublishState(bookListModel.getPublishState());
            esBookListModel.setCreateTime(LocalDateTime.ofInstant(bookListModel.getCreateTime().toInstant(), ZoneId.systemDefault()));
            esBookListModel.setUpdateTime(LocalDateTime.ofInstant(bookListModel.getUpdateTime().toInstant(), ZoneId.systemDefault()));
        }
        return list;
    }
}
