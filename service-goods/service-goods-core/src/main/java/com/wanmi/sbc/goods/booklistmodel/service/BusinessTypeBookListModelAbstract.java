package com.wanmi.sbc.goods.booklistmodel.service;

import com.wanmi.sbc.goods.api.enums.CategoryEnum;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelAndOrderNumProviderResponse;
import com.wanmi.sbc.goods.booklistgoodspublish.response.BookListGoodPublishLinkModelResponse;
import com.wanmi.sbc.goods.booklistgoodspublish.service.BookListGoodsPublishService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/8 4:03 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
public abstract class BusinessTypeBookListModelAbstract {

    @Resource
    protected BookListGoodsPublishService bookListGoodsPublishService;

    /**
     * 根据商品获取发布书单列表
     * @param businessTypeList
     * @param spuId
     * @return
     */
    protected List<BookListGoodPublishLinkModelResponse> listBookListModelBySupId(List<Integer> businessTypeList, String spuId) {
        //根据商品获取书单,此处可以获取
        List<BookListGoodPublishLinkModelResponse> bookListGoodPublishLinkModelList =
                bookListGoodsPublishService.listPublishGoodsAndBookListModel(businessTypeList, CategoryEnum.BOOK_LIST_MODEL.getCode(), spuId);


        return bookListGoodPublishLinkModelList;
    }

    /**
     * 书单排序列表
     * @param spuId
     * @return
     */
    public abstract List<BookListModelAndOrderNumProviderResponse> listBookListModelAndOrderNum(String spuId);


    /**
     * 书单和详情信息
     * @param spuId
     * @return
     */
    public abstract List<BookListModelAndOrderNumProviderResponse> listBookListModelAndGoodsDetail(String spuId);
}
