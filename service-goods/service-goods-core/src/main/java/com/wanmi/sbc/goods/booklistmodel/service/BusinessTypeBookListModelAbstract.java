package com.wanmi.sbc.goods.booklistmodel.service;

import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelAndOrderNumProviderResponse;
import com.wanmi.sbc.goods.booklistgoodspublish.response.BookListGoodsPublishLinkModelResponse;
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
     * 根据商品 获取发布书单列表
     * @param businessTypeList
     * @param spuId
     * @return
     */
    protected List<BookListGoodsPublishLinkModelResponse> listBookListModelBySpuId(List<Integer> businessTypeList, String spuId) {
        //根据商品获取书单,此处可以获取
        return bookListGoodsPublishService.listPublishGoodsAndBookListModelBySpuId(businessTypeList, spuId);
    }


    /**
     * 根据商品店铺分类 获取发布书单列表
     * @param spuId
     * @return
     */
    protected List<BookListGoodsPublishLinkModelResponse> listPublishGoodsAndBookListModelByClassifyAndSupId(List<Integer> businessTypeList, List<Integer> notInBookListIdList, String spuId) {
        return bookListGoodsPublishService.listPublishGoodsAndBookListModelByClassifyAndSupId(businessTypeList, notInBookListIdList, spuId);
    }


    /**
     * change BookListGoodPublishLinkModelResponse --> BookListModelAndOrderNumProviderResponse
     * @param bookListGoodPublishLinkModelParam
     * @return
     */
    protected BookListModelAndOrderNumProviderResponse packageBookListModelAndOrderNumProviderResponse(BookListGoodsPublishLinkModelResponse bookListGoodPublishLinkModelParam) {
        BookListModelAndOrderNumProviderResponse bookListModelAndOrderNumProviderResponse = new BookListModelAndOrderNumProviderResponse();
        bookListModelAndOrderNumProviderResponse.setBookListModelId(bookListGoodPublishLinkModelParam.getBookListModelId());
        bookListModelAndOrderNumProviderResponse.setBookListModelName(bookListGoodPublishLinkModelParam.getName());
        bookListModelAndOrderNumProviderResponse.setOrderNum(bookListGoodPublishLinkModelParam.getOrderNum());
        return bookListModelAndOrderNumProviderResponse;
    }



    /**
     * 书单排序列表
     * @param spuId
     * @return
     */
    public abstract List<BookListModelAndOrderNumProviderResponse> listBookListModelAndOrderNum(String spuId, Integer size);


//    /**
//     * 书单和详情信息
//     * @param spuId
//     * @return
//     */
//    public abstract List<BookListModelAndOrderNumProviderResponse> listBookListModelAndGoodsDetail(String spuId);
}
