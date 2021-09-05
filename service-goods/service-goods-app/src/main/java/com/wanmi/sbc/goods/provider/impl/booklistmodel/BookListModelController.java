package com.wanmi.sbc.goods.provider.impl.booklistmodel;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.goods.api.constant.BookListModelErrorCode;
import com.wanmi.sbc.goods.api.enums.PublishStateEnum;
import com.wanmi.sbc.goods.api.provider.booklistmodel.BookListModelProvider;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelPageProviderRequest;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelProviderRequest;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import com.wanmi.sbc.goods.booklistmodel.model.root.BookListModelDTO;
import com.wanmi.sbc.goods.booklistmodel.request.BookListModelPageRequest;
import com.wanmi.sbc.goods.booklistmodel.request.BookListModelRequest;
import com.wanmi.sbc.goods.booklistmodel.service.BookListModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;

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
     * @param bookListModelProviderRequest
     * @return
     */
    @Override
    public BaseResponse add(BookListModelProviderRequest bookListModelProviderRequest) {
        BookListModelRequest bookListModelRequest = new BookListModelRequest();
        BeanUtils.copyProperties(bookListModelProviderRequest, bookListModelRequest);
        bookListModelRequest.setPublishState(PublishStateEnum.UN_PUBLISH.getCode()); //默认草稿
        bookListModelService.add(bookListModelRequest, bookListModelProviderRequest.getOperator());
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 修改书单模版
     * @param bookListModelProviderRequest
     * @return
     */
    @Override
    public BaseResponse update(BookListModelProviderRequest bookListModelProviderRequest) {
        if (bookListModelProviderRequest.getPublishState() != null) {
            PublishStateEnum publishStateEnum = PublishStateEnum.getByCode(bookListModelProviderRequest.getPublishState());
            if (publishStateEnum == null) {
                log.error("BookListModelController update 更新书单发布类型 publicState: {} 有误",
                                    bookListModelProviderRequest.getPublishState());
                throw new SbcRuntimeException(BookListModelErrorCode.BOOK_LIST_MODEL_PUBLISH_STATE_UN_EXISTS,
                        BookListModelErrorCode.BOOK_LIST_MODEL_PUBLISH_STATE_UN_EXISTS_MESSAGE);
            }
            if (publishStateEnum == PublishStateEnum.UN_PUBLISH) {
                log.error("BookListModelController update 更新书单不能为草稿类型");
                throw new SbcRuntimeException(BookListModelErrorCode.BOOK_LIST_MODEL_PUBLISH_STATE_ERROR,
                        BookListModelErrorCode.BOOK_LIST_MODEL_PUBLISH_STATE_ERROR_MESSAGE);
            }
        }
        BookListModelRequest bookListModelRequest = new BookListModelRequest();
        BeanUtils.copyProperties(bookListModelProviderRequest, bookListModelRequest);
        bookListModelService.update(bookListModelRequest, bookListModelProviderRequest.getOperator());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 删除书单
     * @param bookListModelProviderRequest
     * @return
     */
    @Override
    public BaseResponse delete(BookListModelProviderRequest bookListModelProviderRequest) {
        bookListModelService.delete(bookListModelProviderRequest.getId(), bookListModelProviderRequest.getOperator());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 获取列表
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



}
