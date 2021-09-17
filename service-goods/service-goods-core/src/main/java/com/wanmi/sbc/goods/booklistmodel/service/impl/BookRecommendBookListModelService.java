package com.wanmi.sbc.goods.booklistmodel.service.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.goods.api.enums.BusinessTypeEnum;
import com.wanmi.sbc.goods.api.enums.PublishStateEnum;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelAndOrderNumProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelIdAndClassifyIdProviderResponse;
import com.wanmi.sbc.goods.booklistgoodspublish.model.root.BookListGoodsPublishDTO;
import com.wanmi.sbc.goods.booklistgoodspublish.response.BookListGoodsPublishLinkModelResponse;
import com.wanmi.sbc.goods.booklistmodel.service.BusinessTypeBookListModelAbstract;
import com.wanmi.sbc.goods.classify.response.BookListModelClassifyLinkResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description: 书单&& 推荐
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/8 8:11 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
@Slf4j
public class BookRecommendBookListModelService extends BusinessTypeBookListModelAbstract {

    public static Integer MAX_SIZE = 2;

    /**
     * 推荐
     * @param spuId
     * @return
     */
    @Override
    public List<BookListModelAndOrderNumProviderResponse> listBookListModelAndOrderNum(String spuId, Integer size) {
        log.info("---> BookRecommendBookListModelService.listBookListModelAndOrderNum supId:{}", spuId);
        List<BookListGoodsPublishLinkModelResponse> bookListModelDTOList =
                super.listBookListModelBySpuId(Arrays.asList(BusinessTypeEnum.BOOK_LIST.getCode(),BusinessTypeEnum.BOOK_RECOMMEND.getCode()), spuId);
        if (size != null) {
            MAX_SIZE = size;
        }
        List<BookListGoodsPublishLinkModelResponse> resultParam;
        if (bookListModelDTOList.size() > MAX_SIZE) {
            resultParam = bookListModelDTOList.subList(0, MAX_SIZE);
        } else {
            resultParam = bookListModelDTOList;
        }

        List<BookListModelAndOrderNumProviderResponse> result = new ArrayList<>();
        //根据id列表获取图书信息
        for (BookListGoodsPublishLinkModelResponse bookListGoodPublishLinkModelParam : resultParam) {
            result.add(super.packageBookListModelAndOrderNumProviderResponse(bookListGoodPublishLinkModelParam));
        }

        int surplusCount = MAX_SIZE - result.size(); //需要剩余获取的书单
        if (surplusCount <= 0) {
            return result;
        }


        log.info("---> BookRecommendBookListModelService.listBookListModelAndOrderNum spuId:{} 从类目中获取", spuId);

        //获取不需要的书单
        List<Integer> notInBookListIdList =
                result.stream().map(BookListModelAndOrderNumProviderResponse::getBookListModelId).collect(Collectors.toList());

        //从类目中获取 书单列表
        List<BookListGoodsPublishLinkModelResponse> bookListGoodsPublishListByClassifyAndSpuIdList =
                super.listPublishGoodsAndBookListModelByClassifyAndSupId(
                        Arrays.asList(BusinessTypeEnum.BOOK_LIST.getCode(),BusinessTypeEnum.BOOK_RECOMMEND.getCode()),notInBookListIdList, spuId);
        if (CollectionUtils.isEmpty(bookListGoodsPublishListByClassifyAndSpuIdList)) {
            return result;
        }

        List<BookListModelAndOrderNumProviderResponse> resultOtherParam = new ArrayList<>();
        //获取店铺分类id列表
        for (int i = 0; i < surplusCount; i++) {
            BookListGoodsPublishLinkModelResponse bookListGoodPublishLinkModelParam = bookListGoodsPublishListByClassifyAndSpuIdList.get(i);
            resultOtherParam.add(super.packageBookListModelAndOrderNumProviderResponse(bookListGoodPublishLinkModelParam));
        }
        result.addAll(resultOtherParam);
        log.info("---> BookRecommendBookListModelService.listBookListModelAndOrderNum spuId:{} 从类目中获取 书单为：{}",
                spuId, JSON.toJSONString(resultOtherParam));

        return result;
    }

    @Override
    public List<BookListModelIdAndClassifyIdProviderResponse> listBookListModelMore(Integer bookListModelId, Integer size) {
        List<BookListModelClassifyLinkResponse> bookListModelClassifyLinkResponses = super.listParentAllChildClassifyByBookListModelId(bookListModelId,
                Collections.singletonList(BusinessTypeEnum.RANKING_LIST.getCode()),
                Collections.singletonList(PublishStateEnum.PUBLISH.getCode()), 1, size);
        List<BookListModelIdAndClassifyIdProviderResponse> result = new ArrayList<>();
        for (BookListModelClassifyLinkResponse param : bookListModelClassifyLinkResponses) {
            BookListModelIdAndClassifyIdProviderResponse bookListModelIdAndClassifyIdModel = new BookListModelIdAndClassifyIdProviderResponse();
            BeanUtils.copyProperties(param, bookListModelIdAndClassifyIdModel);
            result.add(bookListModelIdAndClassifyIdModel);
        }
        return result;
    }


}
