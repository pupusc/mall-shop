package com.wanmi.sbc.setting.api.provider.weight;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.response.weight.SearchWeightResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "${application.setting.name}", contextId = "SearchWeightProvider")
public interface SearchWeightProvider {

    /**
     * 获取搜索权重列表
     * @param key
     * @return
     */
    @GetMapping("/setting/${application.setting.version}/search-weight/list/{key}")
    BaseResponse<List<SearchWeightResp>> list(@PathVariable("key") String key);

    /**
     * 删除搜索权重缓存
     * @param key
     * @return
     */
    @GetMapping("/setting/${application.setting.version}/search-weight/delete/{key}")
    BaseResponse<List<SearchWeightResp>> delete(String key);
}
