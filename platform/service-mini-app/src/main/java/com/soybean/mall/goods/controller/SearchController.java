package com.soybean.mall.goods.controller;

import com.soybean.elastic.api.provider.booklistmodel.EsBookListModelProvider;
import com.soybean.elastic.api.resp.EsBookListModelResp;
import com.soybean.common.resp.CommonPageResp;
import com.soybean.mall.goods.req.BookListKeyWordQueryReq;
import com.soybean.mall.goods.req.SpuKeyWordQueryReq;
import com.soybean.mall.goods.response.BookListSpuResp;
import com.soybean.mall.goods.response.SpuBookListResp;
import com.soybean.mall.goods.service.BookListSearchService;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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

    /**
     * 搜索 获取书单/榜单
     * @param request
     * @return
     */
    @PostMapping("/keyword/keywordBookListSearch")
    public BaseResponse<CommonPageResp<List<BookListSpuResp>>> keywordBookListSearch(@Validated @RequestBody BookListKeyWordQueryReq request) {
        CommonPageResp<List<EsBookListModelResp>> context = esBookListModelProvider.listKeyWorldEsBookListModel(request).getContext();
        List<BookListSpuResp> bookListSpuResps = bookListSearchService.listBookListSearch(context.getContent(), request.getSpuNum());
        CommonPageResp<List<BookListSpuResp>> commonPageResp = new CommonPageResp<>(context.getTotal(), bookListSpuResps);
        return BaseResponse.success(commonPageResp);
    }


    /**
     * 搜索 获取书单/榜单
     * @param request
     * @return
     */
    @PostMapping("/keyword/keywordSpuSearch")
    public BaseResponse<CommonPageResp<List<SpuBookListResp>>> keywordSpuSearch(@Validated @RequestBody SpuKeyWordQueryReq request) {

        CommonPageResp<List<SpuBookListResp>> commonPageResp = new CommonPageResp<>(10L, new ArrayList<>());
        return BaseResponse.success(commonPageResp);
    }

}
