package com.wanmi.sbc.system;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.setting.api.provider.SystemGrowthValueConfigProvider;
import com.wanmi.sbc.setting.api.provider.SystemGrowthValueConfigQueryProvider;
import com.wanmi.sbc.setting.api.request.SystemGrowthValueConfigModifyRequest;
import com.wanmi.sbc.setting.api.request.SystemGrowthValueStatusModifyRequest;
import com.wanmi.sbc.setting.api.response.SystemGrowthValueConfigQueryResponse;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 平台端-成长值设置
 */
@Api(tags = "SystemGrowthValueConfigController", description = "平台端-成长值设置API")
@RestController
@RequestMapping("/boss/growthValueConfig")
public class SystemGrowthValueConfigController {

    @Autowired
    private SystemGrowthValueConfigQueryProvider systemGrowthValueConfigQueryProvider;

    @Autowired
    private SystemGrowthValueConfigProvider systemGrowthValueConfigProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    /**
     * 查询成长值设置
     *
     * @return
     */
    @ApiOperation(value = "查询成长值设置")
    @RequestMapping(method = RequestMethod.GET)
    public BaseResponse<SystemGrowthValueConfigQueryResponse> query() {
        return systemGrowthValueConfigQueryProvider.querySystemGrowthValueConfig();
    }

    /**
     * 更新成长值设置
     *
     * @return BaseResponse
     */
    @ApiOperation(value = "更新成长值设置")
    @RequestMapping(method = RequestMethod.PUT)
    public BaseResponse modify(@Valid @RequestBody SystemGrowthValueConfigModifyRequest request) {
        return systemGrowthValueConfigProvider.modifySystemGrowthValueConfig(request);
    }

    /**
     * 开启成长值开关
     *
     * @return BaseResponse
     */
    @ApiOperation(value = "开启成长值开关")
    @RequestMapping(value = "/open", method = RequestMethod.POST)
    public BaseResponse openGrowthValue(@Valid @RequestBody SystemGrowthValueStatusModifyRequest request) {
        request.setStatus(EnableStatus.ENABLE);
        systemGrowthValueConfigProvider.modifySystemGrowthValueStatus(request);

        operateLogMQUtil.convertAndSend("设置", "修改成长值开关", "修改成长值开关：成长值设为开");
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 关闭成长值开关
     *
     * @return BaseResponse
     */
    @ApiOperation(value = "关闭成长值开关")
    @RequestMapping(value = "/close", method = RequestMethod.POST)
    public BaseResponse closeGrowthValue(@Valid @RequestBody SystemGrowthValueStatusModifyRequest request) {
        //判断是否有会员获取了成长值
        BaseResponse<Boolean> hasObtainedGrowthValue = customerQueryProvider.hasObtainedGrowthValue();
        if(hasObtainedGrowthValue.getContext().booleanValue()){
            throw new SbcRuntimeException("K-010301");
        }

        request.setStatus(EnableStatus.DISABLE);
        systemGrowthValueConfigProvider.modifySystemGrowthValueStatus(request);

        operateLogMQUtil.convertAndSend("设置", "修改成长值开关", "修改成长值开关：成长值设为关");
        return BaseResponse.SUCCESSFUL();
    }
}
