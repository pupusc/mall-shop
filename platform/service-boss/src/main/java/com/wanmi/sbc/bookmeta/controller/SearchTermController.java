package com.wanmi.sbc.bookmeta.controller;

import com.wanmi.sbc.bookmeta.bo.SearchTermBo;
import com.wanmi.sbc.bookmeta.provider.SearchTermProvider;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/16/13:43
 * @Description:
 */
@RestController
@RequestMapping("searchTerm")
public class SearchTermController {
    @Resource
    private  SearchTermProvider searchTermProvider;

    @PostMapping("getSearchTermTree")
    public BusinessResponse<List<SearchTermBo>> getTree(@RequestBody SearchTermBo bo) {
        List<SearchTermBo> searchTermTree = searchTermProvider.getSearchTermTree(bo);
        return BusinessResponse.success(searchTermTree);
    }
    @PostMapping("delete")
    public BusinessResponse<Integer> delete(@RequestBody SearchTermBo bo) {
        int i = searchTermProvider.deleteSearchTerm(bo);
        return BusinessResponse.success(i);
    }
    @PostMapping("update")
    public BusinessResponse<Integer> update(@RequestBody SearchTermBo bo) {
        int i = searchTermProvider.updateSearchTerm(bo);
        return BusinessResponse.success(i);
    }
    @PostMapping("add")
    public BusinessResponse<Integer> add(@RequestBody SearchTermBo bo) {
        int i = searchTermProvider.updateSearchTerm(bo);
        return BusinessResponse.success(i);
    }


}
