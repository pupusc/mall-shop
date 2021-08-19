package com.wanmi.sbc.elastic.api.provider.searchterms;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.request.searchterms.EsSearchAssociationalWordPageRequest;
import com.wanmi.sbc.elastic.api.request.searchterms.EsSearchAssociationalWordRequest;
import com.wanmi.sbc.elastic.api.response.searchterms.EsSearchAssociationalWordPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author houshuai
 * @date 2020/12/17 14:28
 * @description <p> </p>
 */
@FeignClient(value = "${application.elastic.name}", contextId = "EsSearchAssociationalWordProvider")
public interface EsSearchAssociationalWordProvider {

    /**
     * 搜索词列表查询
     *
     * @param request
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/search_associational_word/add")
    BaseResponse add(@RequestBody @Valid EsSearchAssociationalWordRequest request);

    /**
     *
     * 删除搜索词
     * @param id
     * @return
     */
    @GetMapping("/elastic/${application.elastic.version}/search_associational_word/delete-by-id/{id}")
    BaseResponse deleteById(@PathVariable("id") Long id);


    /**
     * 初始化搜索词列表数据
     * @param request
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/search_associational_word/init")
    BaseResponse init(@RequestBody @Valid EsSearchAssociationalWordPageRequest request);

}
