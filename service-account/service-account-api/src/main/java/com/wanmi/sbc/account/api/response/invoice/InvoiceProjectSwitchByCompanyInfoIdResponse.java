package com.wanmi.sbc.account.api.response.invoice;

import com.wanmi.sbc.account.bean.vo.InvoiceProjectSwitchVO;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;

/**
 * 根据公司信息Id返回的开票项目开关响应
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class InvoiceProjectSwitchByCompanyInfoIdResponse extends InvoiceProjectSwitchVO implements Serializable {


    private static final long serialVersionUID = -2654727847387229537L;
}
