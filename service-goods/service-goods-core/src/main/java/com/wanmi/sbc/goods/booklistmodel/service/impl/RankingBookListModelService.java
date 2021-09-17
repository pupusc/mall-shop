package com.wanmi.sbc.goods.booklistmodel.service.impl;

import com.wanmi.sbc.goods.api.enums.BusinessTypeEnum;
import com.wanmi.sbc.goods.api.enums.CategoryEnum;
import com.wanmi.sbc.goods.api.enums.PublishStateEnum;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelAndOrderNumProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelIdAndClassifyIdProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import com.wanmi.sbc.goods.booklistgoodspublish.response.BookListGoodsPublishLinkModelResponse;
import com.wanmi.sbc.goods.booklistmodel.service.BusinessTypeBookListModelAbstract;
import com.wanmi.sbc.goods.classify.model.root.BookListModelClassifyRelDTO;
import com.wanmi.sbc.goods.classify.response.BookListModelClassifyLinkResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Description: 排行榜 书单
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/8 4:04 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class RankingBookListModelService extends BusinessTypeBookListModelAbstract {

    public static Integer MAX_SIZE = 2;

//    @Override

    /**
     * 书单排序
     * @param spuId
     * @return
     */
    @Override
    public List<BookListModelAndOrderNumProviderResponse> listBookListModelAndOrderNum(String spuId, Integer size) {
        List<BookListGoodsPublishLinkModelResponse> bookListModelDTOList = super.listBookListModelBySpuId(Collections.singletonList(BusinessTypeEnum.RANKING_LIST.getCode()), spuId);
        List<BookListGoodsPublishLinkModelResponse> resultParam = null;
        if (size != null) {
            MAX_SIZE = size;
        }
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
        return result;
    }

    @Override
    public List<BookListModelIdAndClassifyIdProviderResponse> listBookListModelMore(Integer bookListModelId, Integer size){
        List<BookListModelClassifyLinkResponse> bookListModelClassifyLinkResponses = super.listParentAllChildClassifyByBookListModelId(bookListModelId,
                                Collections.singletonList(BusinessTypeEnum.RANKING_LIST.getCode()), 0, size);
        List<BookListModelIdAndClassifyIdProviderResponse> result = new ArrayList<>();
        for (BookListModelClassifyLinkResponse param : bookListModelClassifyLinkResponses) {
            BookListModelIdAndClassifyIdProviderResponse bookListModelIdAndClassifyIdModel = new BookListModelIdAndClassifyIdProviderResponse();
            BeanUtils.copyProperties(param, bookListModelIdAndClassifyIdModel);
            result.add(bookListModelIdAndClassifyIdModel);
        }
        return result;
    }

}
