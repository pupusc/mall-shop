package com.wanmi.sbc.setting.provider.impl.pagemanage;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.pagemanage.PageInfoExtendSaveProvider;
import com.wanmi.sbc.setting.api.request.pagemanage.PageInfoExtendModifyRequest;
import com.wanmi.sbc.setting.pagemanage.service.PageInfoExtendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>页面投放保存接口</p>
 * @author dyt
 * @date 2020-04-16
 */
@RestController
public class PageInfoExtendSaveController implements PageInfoExtendSaveProvider {


    @Autowired
    private PageInfoExtendService pageInfoExtendService;

    @Override
    public BaseResponse modify(@RequestBody PageInfoExtendModifyRequest request) {
        pageInfoExtendService.modifyExtendById(request);
        return BaseResponse.SUCCESSFUL();
    }
}
