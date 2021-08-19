package com.wanmi.sbc.customer.api.request.invoice;


import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangjin on 2017/5/4.
 */
@ApiModel
@Data
public class CustomerInvoiceRejectRequest extends CustomerBaseRequest implements Serializable {

    private static final long serialVersionUID = 2945115959411738875L;
    /**
     * 专票ids
     */
    @ApiModelProperty(value = "专票ids")
    private List<Long> customerInvoiceIds;

    /**
     * 审核状态
     */
    @ApiModelProperty(value = "增票资质审核状态")
    @NotNull
    private CheckState checkState;

    /**
     * 增票id
     */
    @ApiModelProperty(value = "增票id")
    @NotNull
    private Long customerInvoiceId;

    /**
     * 审核未通过原因
     */
    @ApiModelProperty(value = "审核未通过原因")
    @NotBlank
    private String rejectReason;
}
