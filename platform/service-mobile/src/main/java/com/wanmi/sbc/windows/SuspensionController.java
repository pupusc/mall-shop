package com.wanmi.sbc.windows;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.SuspensionV2.SuspensionProvider;
import com.wanmi.sbc.goods.api.request.SuspensionV2.SuspensionByIdRequest;
import com.wanmi.sbc.goods.api.response.SuspensionV2.SuspensionByIdResponse;
import com.wanmi.sbc.goods.bean.dto.SuspensionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/windows/V2")
@Slf4j
public class SuspensionController {

    @Autowired
    private SuspensionProvider suspensionProvider;

    @PostMapping("/getSuspensionById")
    public BaseResponse<SuspensionByIdResponse> getById(@RequestBody @Valid SuspensionByIdRequest suspensionByIdRequest) {
        return suspensionProvider.getById(suspensionByIdRequest);
    }
}
