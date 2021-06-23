package com.wanmi.sbc.account.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 新增付款记录参数（Customer纬度）
 * Created by of628-wenzhi on 2017/4/27.
 */
@ApiModel
@Data
public class PaymentRecordRequest implements Serializable {

    /**
     * 关联的订单id
     */
    @ApiModelProperty(value = "关联的订单id")
    @NotBlank
    private String tid;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @NotBlank
    private String createTime;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 收款账号id
     */
    @ApiModelProperty(value = "收款账号id")
    @NotNull
    private Long accountId;

    /**
     * 附件
     */
    @ApiModelProperty(value = "附件")
    private String encloses;
}
