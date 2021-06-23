package com.wanmi.sbc.account.api.response.invoice;

import com.wanmi.sbc.account.bean.vo.InvoiceProjectVO;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;

/**
 * 开票项目获取结果
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class InvoiceProjectByIdResponse extends InvoiceProjectVO implements Serializable{

    private static final long serialVersionUID = -993498831281408938L;
}
