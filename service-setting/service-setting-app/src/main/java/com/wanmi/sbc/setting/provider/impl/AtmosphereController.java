package com.wanmi.sbc.setting.provider.impl;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.setting.api.provider.AtmosphereProvider;
import com.wanmi.sbc.setting.api.request.AtmosphereDeleteRequest;
import com.wanmi.sbc.setting.api.request.AtmosphereQueryRequest;
import com.wanmi.sbc.setting.atmosphere.AtmosphereService;
import com.wanmi.sbc.setting.bean.dto.AtmosphereDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AtmosphereController implements AtmosphereProvider {

    @Autowired
    private AtmosphereService atmosphereService;

    @Override
    public BaseResponse add(List<AtmosphereDTO> request) {
        atmosphereService.add(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<MicroServicePage<AtmosphereDTO>> page(AtmosphereQueryRequest request) {
        return BaseResponse.success(atmosphereService.page(request));
    }

    @Override
    public BaseResponse delete(AtmosphereDeleteRequest request) {
        atmosphereService.delete(request.getId());
        return BaseResponse.SUCCESSFUL();
    }
}
