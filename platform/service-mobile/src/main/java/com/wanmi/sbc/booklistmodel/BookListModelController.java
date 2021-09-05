package com.wanmi.sbc.booklistmodel;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.api.provider.booklistmodel.BookListModelProvider;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListMixProviderRequest;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelPageProviderRequest;
import com.wanmi.sbc.goods.api.request.booklistmodel.BookListModelProviderRequest;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import com.wanmi.sbc.util.CommonUtil;
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
     * @param bookListMixProviderRequest
     * @menu 商城书单
     * @status undone
     * @return
     */
    @PostMapping("/add")
    public BaseResponse add(@Validated @RequestBody BookListMixProviderRequest bookListMixProviderRequest) {
        bookListMixProviderRequest.setOperator(commonUtil.getOperatorId());
        bookListModelProvider.add(bookListMixProviderRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     *  修改书单
     * @param bookListMixProviderRequest
     * @menu 商城书单
     * @status undone
     * @return
     */
    @PostMapping("/update")
    public BaseResponse update(@Validated @RequestBody BookListMixProviderRequest bookListMixProviderRequest) {
        bookListMixProviderRequest.setOperator(commonUtil.getOperatorId());
        bookListModelProvider.update(bookListMixProviderRequest);
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 删除书单
     * @param bookListModel
     * @menu 商城书单
     * @status undone
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse delete(@Validated @RequestBody BookListModelProviderRequest bookListModel) {
        bookListModel.setOperator(commonUtil.getOperatorId());
        bookListModelProvider.delete(bookListModel);
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 书单列表书单
     * @param bookListModelPageProviderRequest
     * @menu 商城书单
     * @status undone
     * @return
     */
    @PostMapping("/listByPage")
    public MicroServicePage<BookListModelProviderResponse> listByPage(
            @RequestBody BookListModelPageProviderRequest bookListModelPageProviderRequest){
        return bookListModelProvider.listByPage(bookListModelPageProviderRequest);
    }


    /**
     * 发布书单
     * @param bookListModelProviderRequest
     * @menu 商城书单
     * @status undone
     * @return
     */
    @PostMapping("/publish")
    public BaseResponse publish(
            @RequestBody BookListModelProviderRequest bookListModelProviderRequest){
        bookListModelProviderRequest.setOperator(commonUtil.getOperatorId());
        return bookListModelProvider.publish(bookListModelProviderRequest);
    }


    /**
     * 根据id获取书单
     * @menu 商城书单
     * @status undone
     * @return
     */
    @GetMapping("/findById/{id}")
    public BaseResponse findById(@PathVariable("id") Integer id){
        BookListModelProviderRequest bookListModelProviderRequest = new BookListModelProviderRequest();
        bookListModelProviderRequest.setId(id);
        return bookListModelProvider.findById(bookListModelProviderRequest);
    }

}
