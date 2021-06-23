package com.wanmi.sbc.order.api.request.returnorder;

import com.wanmi.sbc.common.base.Operator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 关闭退单请求结构
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReturnOrderCloseRequest implements Serializable {

    private static final long serialVersionUID = -4937623443809948041L;
    /**
     * 退单id
     */
    @NotBlank
    private String rid;

    /**
     * 操作人信息
     */
    @NotNull
    private Operator operator;
}
