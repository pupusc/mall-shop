package com.wanmi.sbc.goods.provider.impl.booklistmodel;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.api.enums.CategoryEnum;
import com.wanmi.sbc.goods.api.provider.booklistmodel.BookListModelProvider;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListMixProviderRequest;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelPageProviderRequest;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelProviderRequest;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListMixProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelAndOrderNumProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import com.wanmi.sbc.goods.booklistmodel.model.root.BookListModelDTO;
import com.wanmi.sbc.goods.booklistmodel.request.BookListModelPageRequest;
import com.wanmi.sbc.goods.booklistmodel.service.BookListModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/1 5:52 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/

@RestController
@Slf4j
public class BookListModelController implements BookListModelProvider {

    @Autowired
    private BookListModelService bookListModelService;

    /**
     * 新增书单模版
     * @param bookListMixProviderRequest
     * @return
     */
    @Override
    public BaseResponse add(BookListMixProviderRequest bookListMixProviderRequest) {
        bookListModelService.add(bookListMixProviderRequest);
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 修改书单模版
     * @param bookListMixProviderRequest
     * @return
     */
    @Override
    public BaseResponse update(BookListMixProviderRequest bookListMixProviderRequest) {
        bookListModelService.update(bookListMixProviderRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 删除书单
     * @param bookListModel
     * @return
     */
    @Override
    public BaseResponse delete(BookListModelProviderRequest bookListModel) {

        bookListModelService.delete(bookListModel.getId(), bookListModel.getOperator());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 发布书单
     * @param bookListModelProviderRequest
     * @return
     */
    @Override
    public BaseResponse publish(BookListModelProviderRequest bookListModelProviderRequest) {
        bookListModelService.publish(bookListModelProviderRequest.getId(), bookListModelProviderRequest.getOperator());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 获取详情 根据id获取 书单模版详细信息【这里是获取的书单不一定发布】
     * @param bookListModelProviderRequest
     * @return
     */
    @Override
    public BaseResponse<BookListMixProviderResponse> findById(BookListModelProviderRequest bookListModelProviderRequest) {
        BookListMixProviderResponse bookListMixProviderResponse = bookListModelService.findById(bookListModelProviderRequest.getId());
        return BaseResponse.success(bookListMixProviderResponse);
    }


    /**
     * 根据书单id列表获取 书单模版和排序规则 商品列表
     * @param bookListModelIdCollection
     * @return
     */
    @Override
    public BaseResponse<List<BookListMixProviderResponse>> listPublishGoodsByIds(Collection<Integer> bookListModelIdCollection){
        return BaseResponse.success(bookListModelService.listPublishGoodsByIds(bookListModelIdCollection, CategoryEnum.BOOK_LIST_MODEL));
    }

    /**
     * 获取书单模版列表
     * @param bookListModelPageProviderRequest
     * @return
     */
    @Override
    public MicroServicePage<BookListModelProviderResponse> listByPage(BookListModelPageProviderRequest bookListModelPageProviderRequest) {
        BookListModelPageRequest bookListModelPageRequest = new BookListModelPageRequest();
        BeanUtils.copyProperties(bookListModelPageProviderRequest, bookListModelPageRequest);
        Page<BookListModelDTO> pageBookListModel = bookListModelService.list(bookListModelPageRequest,
                bookListModelPageProviderRequest.getPageNum(), bookListModelPageProviderRequest.getPageSize());

        List<BookListModelProviderResponse> bookListModelResponseList = pageBookListModel.getContent().stream().map(ex -> {
                                                        BookListModelProviderResponse bookListModelProviderResponse = new BookListModelProviderResponse();
                                                        BeanUtils.copyProperties(ex, bookListModelProviderResponse);
                                                        return bookListModelProviderResponse;
                                                    }).collect(Collectors.toList());

        MicroServicePage<BookListModelProviderResponse> microServicePage = new MicroServicePage<>();
        microServicePage.setPageable(pageBookListModel.getPageable());
        microServicePage.setTotal(pageBookListModel.getTotalElements());
        microServicePage.setContent(bookListModelResponseList);
        return microServicePage;
    }


    /**
     * 获取榜单  书单  推荐 模版列表
     * @param businessTypeId
     * @param spuId
     * @return
     */
    @Override
    public BaseResponse<List<BookListModelAndOrderNumProviderResponse>> listBusinessTypeBookListModel(Integer businessTypeId, String spuId) {
        return BaseResponse.success(bookListModelService.listBusinessTypeBookListModel(spuId, businessTypeId));
    }



}
