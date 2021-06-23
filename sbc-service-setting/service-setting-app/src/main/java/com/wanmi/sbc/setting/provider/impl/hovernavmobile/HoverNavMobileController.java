package com.wanmi.sbc.setting.provider.impl.hovernavmobile;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.hovernavmobile.HoverNavMobileProvider;
import com.wanmi.sbc.setting.api.request.hovernavmobile.HoverNavMobileModifyRequest;
import com.wanmi.sbc.setting.hovernavmobile.model.root.HoverNavMobile;
import com.wanmi.sbc.setting.hovernavmobile.service.HoverNavMobileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

/**
 * <p>移动端悬浮导航栏保存服务接口实现</p>
 *
 * @author dyt
 * @date 2020-04-29 14:28:21
 */
@RestController
@Validated
public class HoverNavMobileController implements HoverNavMobileProvider {
    @Autowired
    private HoverNavMobileService hoverNavMobileService;

    @Override
    public BaseResponse modify(@RequestBody @Valid HoverNavMobileModifyRequest hoverNavMobileModifyRequest) {
        HoverNavMobile hoverNavMobile = KsBeanUtil.convert(hoverNavMobileModifyRequest, HoverNavMobile.class);
        hoverNavMobileService.modify(hoverNavMobile);
        return BaseResponse.SUCCESSFUL();
    }
}

