package com.wanmi.sbc.bookmeta.provider;

import com.wanmi.sbc.bookmeta.bo.SearchTermBo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/16/13:47
 * @Description:
 */
@FeignClient(value = "${application.goods.name}", contextId = "SearchTermProvider")
public interface SearchTermProvider {
    @PostMapping("/goods/${application.goods.version}/SearchTerm/getTree")
    List<SearchTermBo> getSearchTermTree(@RequestBody SearchTermBo bo);

    @PostMapping("/goods/${application.goods.version}/SearchTerm/delete")
    int deleteSearchTerm(@RequestBody SearchTermBo bo);

    @PostMapping("/goods/${application.goods.version}/SearchTerm/update")
    int updateSearchTerm(@RequestBody SearchTermBo bo);
    @PostMapping("/goods/${application.goods.version}/SearchTerm/add")
    int addSearchTerm(@RequestBody SearchTermBo bo);
}
