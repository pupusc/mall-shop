package com.wanmi.sbc.account.api.response.invoice;

import com.wanmi.sbc.account.bean.vo.InvoiceProjectVO;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;

/**
 * 开票项目修改结果
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class InvoiceProjectModifyResponse extends InvoiceProjectVO implements Serializable{

    private static final long serialVersionUID = 3383760902633472221L;
}
