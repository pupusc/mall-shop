package com.wanmi.sbc.goods.booklistmodel.service.impl;

import com.wanmi.sbc.goods.api.enums.BusinessTypeEnum;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelAndOrderNumProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelIdAndClassifyIdProviderResponse;
import com.wanmi.sbc.goods.booklistgoodspublish.response.BookListGoodsPublishLinkModelResponse;
import com.wanmi.sbc.goods.booklistmodel.service.BusinessTypeBookListModelAbstract;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Description: 专题
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/8 8:00 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class SpecialBookListModelService extends BusinessTypeBookListModelAbstract {

    /**
     * 专题
     * @param spuId
     * @return
     */
    @Override
    public List<BookListModelAndOrderNumProviderResponse> listBookListModelAndOrderNum(String spuId, Integer size) {
        List<BookListGoodsPublishLinkModelResponse> bookListModelDTOList = super.listBookListModelBySpuId(Collections.singletonList(BusinessTypeEnum.SPECIAL_SUBJECT.getCode()), spuId);
        if (CollectionUtils.isEmpty(bookListModelDTOList)) {
            return new ArrayList<>();
        }
        return Collections.singletonList(super.packageBookListModelAndOrderNumProviderResponse(bookListModelDTOList.get(0)));
    }

    @Override
    public List<BookListModelIdAndClassifyIdProviderResponse> listBookListModelMore(Integer bookListModelId, Integer size) {
        return new ArrayList<>();
    }

//    @Override
//    public List<BookListModelAndOrderNumProviderResponse> listBookListModelAndGoodsDetail(String spuId) {
//        return null;
//    }
}
