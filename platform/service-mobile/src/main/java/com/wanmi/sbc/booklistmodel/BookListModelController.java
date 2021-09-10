package com.wanmi.sbc.booklistmodel;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.enums.BusinessTypeEnum;
import com.wanmi.sbc.goods.api.provider.booklistmodel.BookListModelProvider;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelAndOrderNumProviderResponse;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/10 1:40 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Api(tags = "BookListModelController", description = "mobile 查询书单信息")
@RestController
@RequestMapping("/mobile/booklistmodel")
public class BookListModelController {

    @Autowired
    private BookListModelProvider bookListModelProvider;


    /**
     * 获取榜单
     *
     * @menu 商城书单和类目
     * @status undone
     * @param spuId
     * @return
     */
    @GetMapping("/ranking-book-list-model/{spuId}")
    public BaseResponse<List<BookListModelAndOrderNumProviderResponse>> RankingBookListModel(@PathVariable("spuId") String spuId){
        BaseResponse<List<BookListModelAndOrderNumProviderResponse>> listBaseResponse =
                bookListModelProvider.listBusinessTypeBookListModel(BusinessTypeEnum.RANKING_LIST.getCode(), spuId);
        return BaseResponse.success(listBaseResponse.getContext());
    }

    /**
     *
     * 获取专题
     *
     * @menu 商城书单和类目
     * @status undone
     * @param spuId
     * @return
     */
    @GetMapping("/special-book-list-model/{spuId}")
    public BaseResponse<List<BookListModelAndOrderNumProviderResponse>> SpecialBookListModel(@PathVariable("spuId") String spuId){
        BaseResponse<List<BookListModelAndOrderNumProviderResponse>> listBaseResponse =
                bookListModelProvider.listBusinessTypeBookListModel(BusinessTypeEnum.SPECIAL_SUBJECT.getCode(), spuId);
        return BaseResponse.success(listBaseResponse.getContext());
    }

    /**
     *
     * 获取推荐
     *
     * @menu 商城书单和类目
     * @status undone
     * @param spuId
     * @return
     */
    @GetMapping("/recommend-book-list-model/{spuId}")
    public BaseResponse<List<BookListModelAndOrderNumProviderResponse>> RecommendBookListModel(@PathVariable("spuId") String spuId){
        BaseResponse<List<BookListModelAndOrderNumProviderResponse>> listBaseResponse =
                bookListModelProvider.listBusinessTypeBookListModel(BusinessTypeEnum.BOOK_RECOMMEND.getCode(), spuId);
        //根据书单列表 获取商品列表信息，
        //根据商品列表id 获取商品详情
        return BaseResponse.success(listBaseResponse.getContext());
    }

    public void test() {
        String spuId = "";
        //获取排行榜
        BaseResponse<List<BookListModelAndOrderNumProviderResponse>> listBaseResponse =
                bookListModelProvider.listBusinessTypeBookListModel(BusinessTypeEnum.RANKING_LIST.getCode(), spuId);
        List<BookListModelAndOrderNumProviderResponse> context = listBaseResponse.getContext();
        if (!CollectionUtils.isEmpty(context)) {
            BookListModelAndOrderNumProviderResponse bookListModelAndOrderNumProviderResponse = context.get(0); //获取第一个排行榜书单
            //根据书单获取发布商品列表
        }


    }
}
