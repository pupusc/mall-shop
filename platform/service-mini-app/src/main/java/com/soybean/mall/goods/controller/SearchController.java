package com.soybean.mall.goods.controller;

import com.soybean.elastic.api.provider.booklistmodel.EsBookListModelProvider;
import com.soybean.elastic.api.resp.EsBookListModelResp;
import com.soybean.elastic.api.resp.EsCommonPageResp;
import com.soybean.mall.goods.req.BookListKeyWordQueryReq;
import com.soybean.mall.goods.response.BookListSpuResp;
import com.soybean.mall.goods.service.BookListSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
    private BookListSearchService bookListSearchService;


    public void keywordBookListSearch(@Validated @RequestBody BookListKeyWordQueryReq request) {
        EsCommonPageResp<List<EsBookListModelResp>> context = esBookListModelProvider.listKeyWorldEsBookListModel(request).getContext();
        List<BookListSpuResp> bookListSpuResps = bookListSearchService.listBookListSearch(context.getContent(), request.getSpuNum());
    }
}
