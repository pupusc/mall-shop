package com.wanmi.sbc.setting.api.provider.search;

import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;


@FeignClient(value = "${application.setting.name}", contextId = "SearchAggsProvider")
public interface SearchAggsProvider {



    /**
     * 获取搜索聚合列表
     * @return
     */
    @GetMapping("/setting/${application.setting.version}/search-aggs/list/{key}")
    BaseResponse<Map<String, List<String>>> list(@PathVariable("key") String key);

    /**
     * 删除搜索聚合缓存
     * @param key
     * @return
     */
    @GetMapping("/setting/${application.setting.version}/search-aggs/delete/{key}")
    BaseResponse delete(String key);
}
