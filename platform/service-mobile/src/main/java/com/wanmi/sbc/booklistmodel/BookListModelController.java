package com.wanmi.sbc.booklistmodel;

import com.wanmi.sbc.booklistmodel.request.BookListMixRequest;
import com.wanmi.sbc.booklistmodel.request.BookListModelPageRequest;
import com.wanmi.sbc.booklistmodel.request.BookListModelRequest;
import com.wanmi.sbc.booklistmodel.response.BookListMixResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.api.enums.CategoryEnum;
import com.wanmi.sbc.goods.api.provider.booklistmodel.BookListModelProvider;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListMixProviderRequest;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelPageProviderRequest;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelProviderRequest;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListMixProviderResponse;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import com.wanmi.sbc.util.CommonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/6 1:09 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RestController
@RequestMapping("/bookListModel")
public class BookListModelController {

    @Autowired
    private BookListModelProvider bookListModelProvider;

    @Resource
    private CommonUtil commonUtil;

    /**
     * 新增书单
     * @param bookListMixRequest
     * @menu 商城书单
     * @status undone
     * @return
     */
    @PostMapping("/add")
    public BaseResponse add(@Validated @RequestBody BookListMixRequest bookListMixRequest) {
        BookListMixProviderRequest request = new BookListMixProviderRequest();
        BeanUtils.copyProperties(bookListMixRequest, request);
        request.setOperator(commonUtil.getOperatorId());
        request.getChooseRuleGoodsListModel().setCategory(CategoryEnum.BOOK_LIST_MODEL.getCode());
        bookListModelProvider.add(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     *  修改书单
     * @param bookListMixRequest
     * @menu 商城书单
     * @status undone
     * @return
     */
    @PostMapping("/update")
    public BaseResponse update(@Validated @RequestBody BookListMixRequest bookListMixRequest) {
        BookListMixProviderRequest request = new BookListMixProviderRequest();
        BeanUtils.copyProperties(bookListMixRequest, request);
        request.setOperator(commonUtil.getOperatorId());
        bookListModelProvider.update(request);
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 删除书单
     * @param id
     * @menu 商城书单
     * @status undone
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseResponse delete(@PathVariable Integer id) {

        BookListModelProviderRequest request = new BookListModelProviderRequest();
        request.setId(id);
        request.setOperator(commonUtil.getOperatorId());
        bookListModelProvider.delete(request);
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 书单列表书单
     * @param bookListModelPageRequest
     * @menu 商城书单
     * @status undone
     * @return
     */
    @PostMapping("/listByPage")
    public MicroServicePage<BookListModelProviderResponse> listByPage(
            @RequestBody BookListModelPageRequest bookListModelPageRequest){
        BookListModelPageProviderRequest request = new BookListModelPageProviderRequest();
        BeanUtils.copyProperties(bookListModelPageRequest, request);
        return bookListModelProvider.listByPage(request);
    }


    /**
     * 发布书单
     * @param id
     * @menu 商城书单
     * @status undone
     * @return
     */
    @GetMapping("/publish/{id}")
    public BaseResponse publish(@PathVariable("id") Integer id){
        BookListModelProviderRequest request = new BookListModelProviderRequest();
        request.setId(id);
        request.setOperator(commonUtil.getOperatorId());
        return bookListModelProvider.publish(request);
    }


    /**
     * 根据id获取书单
     * @menu 商城书单
     * @status undone
     * @return
     */
    @GetMapping("/findById/{id}")
    public BaseResponse<BookListMixResponse> findById(@PathVariable("id") Integer id){
        BookListModelProviderRequest bookListModelProviderRequest = new BookListModelProviderRequest();
        bookListModelProviderRequest.setId(id);
        BaseResponse<BookListMixProviderResponse> bookListModelProviderByIdResponse = bookListModelProvider.findById(bookListModelProviderRequest);
        BookListMixResponse bookListMixResponse = new BookListMixResponse();
        BeanUtils.copyProperties(bookListModelProviderByIdResponse.getContext(), bookListMixResponse);
        return BaseResponse.success(bookListMixResponse);
    }

}
