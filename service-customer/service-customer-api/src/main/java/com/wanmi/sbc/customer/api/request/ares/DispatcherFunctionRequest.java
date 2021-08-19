package com.wanmi.sbc.customer.api.request.ares;

import com.wanmi.sbc.customer.bean.enums.AresFunctionType;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>会员业务埋点处理调度request</p>
 * Created by of628-wenzhi on 2018-09-20-下午3:28.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DispatcherFunctionRequest extends CustomerBaseRequest {
    private static final long serialVersionUID = -7689492353242413132L;

    /**
     * 方法类型
     */
    @ApiModelProperty(value = "方法类型")
    private AresFunctionType funcType;

    /**
     * 对应的参数Bean
     */
    @ApiModelProperty(value = "对应的参数Bean")
    private Object[] objs;

}
