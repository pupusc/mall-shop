package com.wanmi.sbc.account.api.response.invoice;

import com.wanmi.sbc.account.bean.vo.InvoiceProjectVO;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;

/**
 * 开票项目新增响应
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class InvoiceProjectAddResponse extends InvoiceProjectVO implements Serializable{

    private static final long serialVersionUID = 6726161383227704718L;
}
