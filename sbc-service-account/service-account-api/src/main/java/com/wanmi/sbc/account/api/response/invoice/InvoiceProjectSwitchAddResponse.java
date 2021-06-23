package com.wanmi.sbc.account.api.response.invoice;

import com.wanmi.sbc.account.bean.vo.InvoiceProjectSwitchVO;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;

/**
 * 项目开票开关新增后对象响应
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class InvoiceProjectSwitchAddResponse extends InvoiceProjectSwitchVO implements Serializable {


    private static final long serialVersionUID = -6185115492232838958L;
}
