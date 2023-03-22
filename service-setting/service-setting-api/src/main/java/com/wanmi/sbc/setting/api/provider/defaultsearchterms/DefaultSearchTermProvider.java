package com.wanmi.sbc.setting.api.provider.defaultsearchterms;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.setting.api.response.defaultsearchterms.SearchTermBo;
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
@FeignClient(value = "${application.setting.name}", contextId = "SearchTermProviderV2")
public interface DefaultSearchTermProvider {
    @PostMapping("/setting/${application.setting.name}/SearchTermV2/getTree")
    BaseResponse<List<SearchTermBo>> getSearchTermTree(@RequestBody SearchTermBo bo);
    @PostMapping("/setting/${application.setting.name}/SearchTermV2/delete")
    BaseResponse<Long> deleteSearchTerm(@RequestBody SearchTermBo bo);
    @PostMapping("/setting/${application.setting.name}/SearchTermV2/update")
    BaseResponse<Long> updateSearchTerm(@RequestBody SearchTermBo bo);
    @PostMapping("/setting/${application.setting.name}/SearchTermV2/add")
    BaseResponse<Integer> addSearchTerm(@RequestBody SearchTermBo bo);
    @PostMapping("/setting/${application.setting.name}/SearchTermV2/existName")
    BaseResponse<Boolean> existName(@RequestBody SearchTermBo bo);
    @PostMapping("/setting/${application.setting.name}/SearchTermV2/existId")
    BaseResponse<Boolean> existId(@RequestBody SearchTermBo bo);

}
