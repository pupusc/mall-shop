package com.wanmi.sbc.returnorder.request;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

/**
 * 线下退款
 * Created by wj on 24/5/2017.
 */
@ApiModel
@Data
@Validated
public class ReturnOfflineRefundRequest extends BaseRequest {

    /**
     * 退款单外键
     */
    @ApiModelProperty(value = "退款单外键")
    private String refundId;

    /**
     * 客户编号
     */
    @ApiModelProperty(value = "客户编号")
    private String customerId;

    /**
     * 客户的线下账户  0代表新增线下账户，使用 customerAccountName,customerBankName,customerAccountNo 进行新增
     */
    @ApiModelProperty(value = "客户的线下账户",
            notes = "0代表新增线下账户，使用 customerAccountName,customerBankName,customerAccountNo 进行新增")
    private String customerAccountId;

    /**
     * 客户账户名字
     */
    @ApiModelProperty(value = "客户账户名字")
    private String customerAccountName;

    /**
     * 客户银行账号
     */
    @ApiModelProperty(value = "客户银行账号")
    private String customerAccountNo;

    /**
     * 客户开户行
     */
    @ApiModelProperty(value = "客户开户行")
    private String customerBankName;

    /**
     * 线下账户
     */
    @ApiModelProperty(value = "线下账户")
    private Long offlineAccountId;

    /**
     * 退款评论
     */
    @ApiModelProperty(value = "退款评论")
    @Length(max = 100)
    private String refundComment;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    /**
     * 实付金额
     */
    @ApiModelProperty(value = "实付金额")
    @Min(value = 0)
    private BigDecimal actualReturnPrice;

    /**
     * 实退积分
     */
    @ApiModelProperty(value = "实退积分")
    @Min(value = 0)
    private Long actualReturnPoints;
}
