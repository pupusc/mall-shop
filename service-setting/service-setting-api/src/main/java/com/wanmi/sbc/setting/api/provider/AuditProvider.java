package com.wanmi.sbc.setting.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.*;
import com.wanmi.sbc.setting.api.response.InvoiceConfigModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${application.setting.name}", contextId = "AuditProvider")
public interface AuditProvider {

    /**
     * 根据type和key更新status，如果是商品审核关闭，会同步关闭自营商品审核开关
     *
     * @param request {@link ConfigStatusModifyByTypeAndKeyRequest}
     * @return BaseResponse
     */
    @PostMapping("/setting/${application.setting.version}/audit/modify-status-by-type-and-key")
    BaseResponse modifyStatusByTypeAndKey(@RequestBody ConfigStatusModifyByTypeAndKeyRequest request);

    /**
     * 保存增专资质配置
     *
     * @param request {@link InvoiceConfigModifyRequest}
     * @return 修改的增专资质配置记录数
     */
    @PostMapping("/setting/${application.setting.version}/audit/modify-invoice-config")
    BaseResponse<InvoiceConfigModifyResponse> modifyInvoiceConfig(@RequestBody InvoiceConfigModifyRequest request);

    /**
     * 修改设置
     *
     * @param request {@link TradeConfigsModifyRequest}
     * @return BaseResponse
     */
    @PostMapping("/setting/${application.setting.version}/audit/modify-trade-configs")
    BaseResponse modifyTradeConfigs(@RequestBody TradeConfigsModifyRequest request);

    /**
     * 修改小程序分享配置
     * @param request
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/audit/modify-share-little-program")
    BaseResponse modifyShareLittleProgram(@RequestBody ConfigContextModifyByTypeAndKeyRequest request);

    /**
     * 更新配置信息
     *
     * @param configListModifyRequest
     * @return
     */
    @PostMapping("/setting/${application.setting.version}/audit/modify-config-list")
    BaseResponse modifyConfigList(@RequestBody ConfigListModifyRequest configListModifyRequest);


}
