package com.wanmi.sbc.account.api.response.invoice;

import com.wanmi.sbc.account.bean.vo.InvoiceProjectSwitchVO;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;

/**
 * 开票项目开关修改响应
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class InvoiceProjectSwitchModifyResponse extends InvoiceProjectSwitchVO implements Serializable {

    private static final long serialVersionUID = 5107421287431094910L;
}
