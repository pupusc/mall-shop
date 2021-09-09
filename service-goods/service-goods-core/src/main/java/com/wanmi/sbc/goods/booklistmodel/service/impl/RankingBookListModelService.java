package com.wanmi.sbc.goods.booklistmodel.service.impl;

import com.wanmi.sbc.goods.api.enums.BusinessTypeEnum;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelAndOrderNumProviderResponse;
import com.wanmi.sbc.goods.booklistgoodspublish.response.BookListGoodsPublishLinkModelResponse;
import com.wanmi.sbc.goods.booklistmodel.service.BusinessTypeBookListModelAbstract;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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

    public static final Integer MAX_SIZE = 2;

//    @Override

    /**
     * 书单排序
     * @param spuId
     * @return
     */
    @Override
    public List<BookListModelAndOrderNumProviderResponse> listBookListModelAndOrderNum(String spuId) {
        List<BookListGoodsPublishLinkModelResponse> bookListModelDTOList = super.listBookListModelBySpuId(Collections.singletonList(BusinessTypeEnum.RANKING_LIST.getCode()), spuId);
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
        return result;
    }


    public void getBookListGoodsPublishBySpuId(String spuId) {
        List<BookListModelAndOrderNumProviderResponse> bookListModelAndOrderNumProviderList = this.listBookListModelAndOrderNum(spuId);
        if (CollectionUtils.isEmpty(bookListModelAndOrderNumProviderList)) {
            return;
        }
        //获取最新的一条数据
        BookListModelAndOrderNumProviderResponse bookListModelAndOrderNumProviderModel = bookListModelAndOrderNumProviderList.get(0);

    }



}
