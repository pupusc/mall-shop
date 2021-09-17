package com.wanmi.sbc.goods.booklistmodel.service;

import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelAndOrderNumProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import com.wanmi.sbc.goods.booklistgoodspublish.response.BookListGoodsPublishLinkModelResponse;
import com.wanmi.sbc.goods.booklistgoodspublish.service.BookListGoodsPublishService;
import com.wanmi.sbc.goods.classify.model.root.ClassifyDTO;
import com.wanmi.sbc.goods.classify.service.BookListModelClassifyRelService;
import org.apache.commons.collections4.CollectionUtils;

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

    @Resource
    protected BookListModelClassifyRelService bookListModelClassifyRelService;

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
     * 根据书单id 获取书单所在分类的书单列表
     */
    protected void listParentAllChildClassifyByBookListModelId(Integer bookListModelId) {
        List<ClassifyDTO> classifyList = bookListModelClassifyRelService.listParentAllChildClassifyByBookListModelId(bookListModelId);
        if (CollectionUtils.isEmpty(classifyList)) {
            return;
        }
        //根据分类获取书单关系表
        bookListGoodsPublishService.list
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


    /**
     * 根据booklistModelId 获取更多的书单
     * @param bookListModelId
     * @return
     */
    public abstract List<BookListModelProviderResponse> listBookListModelMore(Integer bookListModelId);

//    /**
//     * 书单和详情信息
//     * @param spuId
//     * @return
//     */
//    public abstract List<BookListModelAndOrderNumProviderResponse> listBookListModelAndGoodsDetail(String spuId);
}
