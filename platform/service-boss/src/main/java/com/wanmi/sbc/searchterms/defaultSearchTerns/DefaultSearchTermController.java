package com.wanmi.sbc.searchterms.defaultSearchTerns;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.setting.api.provider.defaultsearchterms.DefaultSearchTermProvider;
import com.wanmi.sbc.setting.api.response.defaultsearchterms.SearchTermBo;
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
@RequestMapping("searchTermV2")
public class DefaultSearchTermController {
    @Resource
    private DefaultSearchTermProvider defaultSearchTermProvider;

    @PostMapping("getSearchTermTree")
    public BaseResponse<List<SearchTermBo>> getTree(@RequestBody SearchTermBo bo) {
        BaseResponse<List<SearchTermBo>> searchTermTree = defaultSearchTermProvider.getSearchTermTree(bo);
        return searchTermTree;
    }
    @PostMapping("delete")
    public BaseResponse<Long> delete(@RequestBody SearchTermBo bo) {
        BaseResponse<Long> longBaseResponse = defaultSearchTermProvider.deleteSearchTerm(bo);
        return longBaseResponse;
    }
    @PostMapping("update")
    public BaseResponse<Long> update(@RequestBody SearchTermBo bo) {
        BaseResponse<Long> longBaseResponse = defaultSearchTermProvider.updateSearchTerm(bo);
        return longBaseResponse;
    }
    @PostMapping("add")
    public BaseResponse<Integer> add(@RequestBody SearchTermBo bo) {
        BaseResponse<Integer> integerBaseResponse = defaultSearchTermProvider.addSearchTerm(bo);
        return integerBaseResponse;
    }

    @PostMapping("existName")
    public BaseResponse<Boolean> ExistName(@RequestBody SearchTermBo bo) {
        BaseResponse<Boolean> integerBaseResponse = defaultSearchTermProvider.existName(bo);
        return integerBaseResponse;
    }
    @PostMapping("existId")
    public BaseResponse<Boolean> ExistId(@RequestBody SearchTermBo bo) {
        BaseResponse<Boolean> integerBaseResponse = defaultSearchTermProvider.existId(bo);
        return integerBaseResponse;
    }
}
