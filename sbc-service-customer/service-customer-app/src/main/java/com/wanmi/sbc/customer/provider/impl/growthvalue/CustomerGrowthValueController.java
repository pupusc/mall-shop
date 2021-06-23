package com.wanmi.sbc.customer.provider.impl.growthvalue;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.constant.MQConstant;
import com.wanmi.sbc.customer.api.provider.growthvalue.CustomerGrowthValueProvider;
import com.wanmi.sbc.customer.api.request.growthvalue.CustomerGrowthValueAddRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>客户成长值明细表服务接口实现</p>
 *
 * @author yang
 * @since 2019/2/22
 */
@RestController
@Validated
public class CustomerGrowthValueController implements CustomerGrowthValueProvider {

    @Autowired
    private BinderAwareChannelResolver resolver;

    /**
     * 新增客户成长值明细
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse increaseGrowthValue(@RequestBody @Valid CustomerGrowthValueAddRequest request) {
        resolver.resolveDestination(MQConstant.INCREASE_GROWTH_VALUE).send(new GenericMessage<>(JSONObject.toJSONString(request)));
        return BaseResponse.SUCCESSFUL();
    }
}
