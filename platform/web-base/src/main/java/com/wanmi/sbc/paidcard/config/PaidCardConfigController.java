package com.wanmi.sbc.paidcard.config;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.api.provider.paidcardcustomerrel.PaidCardCustomerRelQueryProvider;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelByIdRequest;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelListRequest;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCustomerIdPageRequest;
import com.wanmi.sbc.customer.api.response.paidcardcustomerrel.PaidCardCustomerIdPageResponse;
import com.wanmi.sbc.customer.bean.vo.PaidCardCustomerRelVO;
import com.wanmi.sbc.setting.api.provider.systemconfig.SystemConfigQueryProvider;
import com.wanmi.sbc.setting.api.provider.systemconfig.SystemConfigSaveProvider;
import com.wanmi.sbc.setting.api.request.ConfigContextModifyByTypeAndKeyRequest;
import com.wanmi.sbc.setting.api.request.ConfigQueryRequest;
import com.wanmi.sbc.setting.api.response.systemconfig.SystemConfigTypeResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigKey;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * @author xuhai
 * @desc 付费会员配置控制器
 */
@Slf4j
@RestController
@RequestMapping("/paidCardConfig")
@Api(tags = "PaidCardConfigController", description = "付费会员配置控制器")
public class PaidCardConfigController {

    @Autowired
    private SystemConfigQueryProvider systemConfigQueryProvider;

    /**
     * 查询付费会员基本信息
     * @return
     */
    @ApiOperation(value = "查询付费会员基本信息")
    @GetMapping("/get-info")
    public BaseResponse getInfo(){
        ConfigQueryRequest configQueryRequest = new ConfigQueryRequest();
        configQueryRequest.setConfigKey(ConfigKey.PAID_CARD.toValue());
        configQueryRequest.setConfigType(ConfigType.PAID_CARD_CONFIG.toValue());
        configQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
        log.info("查询付费会员基本信息，入参 ConfigQueryRequest：",configQueryRequest);
        BaseResponse<SystemConfigTypeResponse> response = systemConfigQueryProvider.findByConfigTypeAndDelFlag(configQueryRequest);
        return response;
    }


}