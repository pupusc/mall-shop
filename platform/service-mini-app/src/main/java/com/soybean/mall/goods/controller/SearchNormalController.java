package com.soybean.mall.goods.controller;

import com.soybean.common.resp.CommonPageResp;
import com.soybean.elastic.api.provider.spu.EsSpuNewProvider;
import com.soybean.elastic.api.req.EsSpuNewQueryProviderReq;
import com.soybean.elastic.api.resp.EsSpuNewAggResp;
import com.soybean.elastic.api.resp.EsSpuNewResp;
import com.soybean.mall.goods.req.KeyWordSpuQueryReq;
import com.soybean.mall.goods.response.SpuNewBookListResp;
import com.soybean.mall.goods.service.BookListSearchService;
import com.soybean.mall.goods.service.SpuNewSearchService;
import com.wanmi.sbc.common.base.BaseResponse;
import lombok.extern.slf4j.Slf4j;
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
 * Date       : 2022/10/20 9:06 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RequestMapping("/search/normal")
@RestController
@Slf4j
public class SearchNormalController {

    @Autowired
    private EsSpuNewProvider esSpuNewProvider;

    @Autowired
    private BookListSearchService bookListSearchService;

    @Autowired
    private SpuNewSearchService spuNewSearchService;

    /**
     * 搜索 普通获取商品/图书
     * @menu 搜索功能
     * @param request
     * @return
     */
    @PostMapping("/spu")
    public BaseResponse<CommonPageResp<List<SpuNewBookListResp>>> keywordSpuSearch(@Validated @RequestBody EsSpuNewQueryProviderReq request) {
        CommonPageResp<List<EsSpuNewResp>> commonPageResp = esSpuNewProvider.listNormalEsSpuNew(request).getContext();
        List<SpuNewBookListResp> spuNewBookListResps = spuNewSearchService.listSpuNewSearch(commonPageResp.getContent());
        return BaseResponse.success(new CommonPageResp<>(commonPageResp.getTotal(), spuNewBookListResps));
    }

}
