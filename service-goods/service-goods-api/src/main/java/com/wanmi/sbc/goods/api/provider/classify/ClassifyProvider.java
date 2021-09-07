package com.wanmi.sbc.goods.api.provider.classify;


import com.wanmi.sbc.goods.api.response.classify.ClassifyProviderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(value = "${application.goods.name}", contextId = "ClassifyProvider")
public interface ClassifyProvider {


    @PostMapping("/goods/${application.goods.version}/classify/all")
    List<ClassifyProviderResponse> listClassify();
}
