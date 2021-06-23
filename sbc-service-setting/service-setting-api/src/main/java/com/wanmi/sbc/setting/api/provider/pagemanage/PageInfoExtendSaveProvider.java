package com.wanmi.sbc.setting.api.provider.pagemanage;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.pagemanage.PageInfoExtendModifyRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>页面投放保存接口</p>
 * @author dyt
 * @date 2020-04-16
 */
@FeignClient(value = "${application.setting.name}", contextId = "PageInfoExtendSaveProvider")
public interface PageInfoExtendSaveProvider {

    /**
     * 查询预置搜索词
     */
    @PostMapping("/setting/${application.setting.version}/page-info-extend/modify")
    BaseResponse modify(@RequestBody PageInfoExtendModifyRequest request);

}
