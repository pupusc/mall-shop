package com.soybean.mall.goods.controller;
import com.soybean.elastic.api.enums.SearchBookListCategoryEnum;
import com.soybean.elastic.api.enums.SearchSpuNewCategoryEnum;
import com.soybean.elastic.api.enums.SearchSpuNewSortTypeEnum;

import com.soybean.elastic.api.provider.booklistmodel.EsBookListModelProvider;
import com.soybean.elastic.api.provider.spu.EsSpuNewProvider;
import com.soybean.elastic.api.resp.EsBookListModelResp;
import com.soybean.common.resp.CommonPageResp;
import com.soybean.elastic.api.resp.EsSpuNewResp;
import com.soybean.mall.goods.req.KeyWordBookListQueryReq;
import com.soybean.mall.goods.req.KeyWordQueryReq;
import com.soybean.mall.goods.req.KeyWordSpuQueryReq;
import com.soybean.mall.goods.response.BookListSpuResp;
import com.soybean.mall.goods.response.SearchHomeResp;
import com.soybean.mall.goods.response.SpuNewBookListResp;
import com.soybean.mall.goods.service.BookListSearchService;
import com.soybean.mall.goods.service.SpuNewSearchService;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/9 2:19 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/

@RequestMapping("/search")
@RestController
public class SearchController {

    @Autowired
    private EsBookListModelProvider esBookListModelProvider;

    @Autowired
    private EsSpuNewProvider esSpuNewProvider;

    @Autowired
    private BookListSearchService bookListSearchService;

    @Autowired
    private SpuNewSearchService spuNewSearchService;

    /**
     * 前端 关键词搜索
     *  @menu 搜索功能
     * @return
     */
    @PostMapping("/keyword")
    public BaseResponse<SearchHomeResp> keywordSearch(@RequestBody KeyWordQueryReq keyWordQueryReq) {
        SearchHomeResp searchHomeResp = new SearchHomeResp();
        //图书
        KeyWordSpuQueryReq spuKeyWordQueryReq = new KeyWordSpuQueryReq();
        spuKeyWordQueryReq.setKeyword(keyWordQueryReq.getKeyword());
        spuKeyWordQueryReq.setSearchSpuNewCategory(SearchSpuNewCategoryEnum.BOOK.getCode());
        spuKeyWordQueryReq.setSpuSortType(SearchSpuNewSortTypeEnum.DEFAULT.getCode());
        CommonPageResp<List<SpuNewBookListResp>> bookPage = this.keywordSpuSearch(spuKeyWordQueryReq).getContext();
        searchHomeResp.setBooks(new SearchHomeResp.SubSearchHomeResp<>("图书", bookPage));

        //商品
        spuKeyWordQueryReq = new KeyWordSpuQueryReq();
        spuKeyWordQueryReq.setKeyword(keyWordQueryReq.getKeyword());
        spuKeyWordQueryReq.setSearchSpuNewCategory(SearchSpuNewCategoryEnum.SPU.getCode());
        spuKeyWordQueryReq.setSpuSortType(SearchSpuNewSortTypeEnum.DEFAULT.getCode());
        CommonPageResp<List<SpuNewBookListResp>> spuPage = this.keywordSpuSearch(spuKeyWordQueryReq).getContext();
        searchHomeResp.setBooks(new SearchHomeResp.SubSearchHomeResp<>("商品", spuPage));

        //查询榜单
        KeyWordBookListQueryReq bookListKeyWordQueryReq = new KeyWordBookListQueryReq();
        bookListKeyWordQueryReq.setSpuNum(3);
        bookListKeyWordQueryReq.setSearchBookListCategory(SearchBookListCategoryEnum.RANKING_LIST.getCode());
        bookListKeyWordQueryReq.setKeyword(keyWordQueryReq.getKeyword());
        CommonPageResp<List<BookListSpuResp>> rankingListPage = this.keywordBookListSearch(bookListKeyWordQueryReq).getContext();
        searchHomeResp.setBooks(new SearchHomeResp.SubSearchHomeResp<>("榜单", rankingListPage));


        //查询书单
        bookListKeyWordQueryReq = new KeyWordBookListQueryReq();
        bookListKeyWordQueryReq.setSpuNum(5);
        bookListKeyWordQueryReq.setSearchBookListCategory(SearchBookListCategoryEnum.BOOK_LIST.getCode());
        bookListKeyWordQueryReq.setKeyword(keyWordQueryReq.getKeyword());
        CommonPageResp<List<BookListSpuResp>> bookListPage = this.keywordBookListSearch(bookListKeyWordQueryReq).getContext();
        searchHomeResp.setBooks(new SearchHomeResp.SubSearchHomeResp<>("书单", bookListPage));

        return BaseResponse.success(searchHomeResp);
    }

    /**
     * 前端 获取书单/榜单
     * @menu 搜索功能
     * @param request
     * @return
     */
    @PostMapping("/keyword/keywordBookListSearch")
    public BaseResponse<CommonPageResp<List<BookListSpuResp>>> keywordBookListSearch(@Validated @RequestBody KeyWordBookListQueryReq request) {

        CommonPageResp<List<EsBookListModelResp>> context = esBookListModelProvider.listKeyWorldEsBookListModel(request).getContext();
        List<BookListSpuResp> bookListSpuResps = bookListSearchService.listBookListSearch(context.getContent(), request.getSpuNum());
        CommonPageResp<List<BookListSpuResp>> commonPageResp = new CommonPageResp<>(context.getTotal(), bookListSpuResps);
        return BaseResponse.success(commonPageResp);
    }


    /**
     * 搜索 获取商品/图书
     * @menu 搜索功能
     * @param request
     * @return
     */
    @PostMapping("/keyword/keywordSpuSearch")
    public BaseResponse<CommonPageResp<List<SpuNewBookListResp>>> keywordSpuSearch(@Validated @RequestBody KeyWordSpuQueryReq request) {
        CommonPageResp<List<EsSpuNewResp>> context = esSpuNewProvider.listKeyWorldEsSpu(request).getContext();
        List<SpuNewBookListResp> spuNewBookListResps = spuNewSearchService.listSpuNewSearch(context.getContent());
        return BaseResponse.success(new CommonPageResp<>(context.getTotal(), spuNewBookListResps));
    }

}
