package com.wanmi.sbc.setting.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.setting.api.request.AtmosphereDeleteRequest;
import com.wanmi.sbc.setting.api.request.AtmosphereQueryRequest;
import com.wanmi.sbc.setting.api.request.ConfigStatusModifyByTypeAndKeyRequest;
import com.wanmi.sbc.setting.bean.dto.AtmosphereDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "${application.setting.name}", contextId = "AtmosphereProvider")
public interface AtmosphereProvider {

    /**
     * 根据type和key更新status，如果是商品审核关闭，会同步关闭自营商品审核开关
     *
     * @param request {@link ConfigStatusModifyByTypeAndKeyRequest}
     * @return BaseResponse
     */
    @PostMapping("/setting/${application.setting.version}/atmos/add")
    BaseResponse add(@RequestBody List<AtmosphereDTO> request);

    @PostMapping("/setting/${application.setting.version}/atmos/page")
    BaseResponse<MicroServicePage<AtmosphereDTO>> page(@RequestBody AtmosphereQueryRequest request);

    @PostMapping("/setting/${application.setting.version}/atmos/delete")
    BaseResponse delete(@RequestBody AtmosphereDeleteRequest request);
}
