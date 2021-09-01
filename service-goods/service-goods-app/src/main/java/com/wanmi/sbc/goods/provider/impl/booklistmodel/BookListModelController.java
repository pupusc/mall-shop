package com.wanmi.sbc.goods.provider.impl.booklistmodel;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.api.provider.booklistmodel.BookListModelProvider;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelPageProviderRequest;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelProviderRequest;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import com.wanmi.sbc.goods.booklist.model.root.BookListModelDTO;
import com.wanmi.sbc.goods.booklist.request.BookListModelPageRequest;
import com.wanmi.sbc.goods.booklist.request.BookListModelRequest;
import com.wanmi.sbc.goods.booklist.service.BookListModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
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
    public BaseResponse add(@Validated(BookListModelProviderRequest.Add.class)
                                @RequestBody BookListModelProviderRequest bookListModelProviderRequest) {
        BookListModelRequest bookListModelRequest = new BookListModelRequest();
        BeanUtils.copyProperties(bookListModelProviderRequest, bookListModelRequest);
        bookListModelService.add(bookListModelRequest, bookListModelProviderRequest.getOperator());
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 修改书单模版
     * @param bookListModelProviderRequest
     * @return
     */
    @Override
    public BaseResponse update(@Validated(BookListModelProviderRequest.Update.class)
                            @RequestBody BookListModelProviderRequest bookListModelProviderRequest) {
        BookListModelRequest bookListModelRequest = new BookListModelRequest();
        BeanUtils.copyProperties(bookListModelProviderRequest, bookListModelRequest);
        bookListModelService.update(bookListModelRequest, bookListModelProviderRequest.getOperator());
        return BaseResponse.SUCCESSFUL();
    }


    @Override
    public MicroServicePage<BookListModelProviderResponse> listByPage(@RequestBody BookListModelPageProviderRequest bookListModelPageProviderRequest) {
        BookListModelPageRequest bookListModelPageRequest = new BookListModelPageRequest();
        BeanUtils.copyProperties(bookListModelPageProviderRequest, bookListModelPageRequest);
        Page<BookListModelDTO> pageBookListModel = bookListModelService.list(bookListModelPageRequest);
        List<BookListModelProviderResponse> bookListModelResponseList = pageBookListModel.getContent().stream().map(ex -> {
                                                        BookListModelProviderResponse bookListModelProviderResponse = new BookListModelProviderResponse();
                                                        BeanUtils.copyProperties(ex, bookListModelProviderResponse);
                                                        return bookListModelProviderResponse;
                                                    }).collect(Collectors.toList());

        MicroServicePage<BookListModelProviderResponse> microServicePage = new MicroServicePage<BookListModelProviderResponse>();
        microServicePage.setPageable(pageBookListModel.getPageable());
        microServicePage.setTotal(pageBookListModel.getTotalElements());
        microServicePage.setContent(bookListModelResponseList);
        return microServicePage;
    }
}
