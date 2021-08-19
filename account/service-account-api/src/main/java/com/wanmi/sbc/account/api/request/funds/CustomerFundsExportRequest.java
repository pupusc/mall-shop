package com.wanmi.sbc.account.api.request.funds;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 会员资金-导出查询对象
 * @author: of2975
 * @createDate: 2019/4/30 11:06
 * @version: 1.0
 */
@ApiModel
@Data
public class CustomerFundsExportRequest extends BaseQueryRequest implements Serializable {

    /**
     * jwt token
     */
    @ApiModelProperty(value = "token")
    private String token;

    /**
     * 批量查询-会员资金主键List
     */
    @ApiModelProperty(value = "批量查询-会员资金主键List")
    private List<String> customerFundsIdList;

    /**
     * 会员账号
     */
    @ApiModelProperty(value = "会员账号")
    private String customerAccount;

    /**
     * 会员名称
     */
    @ApiModelProperty(value = "会员名称")
    private String customerName;

    /**
     * 是否分销员，-1：全部，0：否，1：是
     */
    @ApiModelProperty(value = "是否分销员")
    private Integer distributor;

    /**
     * 账户余额开始
     */
    @ApiModelProperty(value = "账户余额开始")
    private BigDecimal startAccountBalance;

    /**
     * 账户余额结束
     */
    @ApiModelProperty(value = "账户余额结束")
    private BigDecimal endAccountBalance;

    /**
     * 冻结余额开始
     */
    @ApiModelProperty(value = "冻结余额开始")
    private BigDecimal startBlockedBalance;

    /**
     * 冻结余额结束
     */
    @ApiModelProperty(value = "冻结余额结束")
    private BigDecimal endBlockedBalance;

    /**
     * 可提现金额开始
     */
    @ApiModelProperty(value = "可提现金额开始")
    private BigDecimal startWithdrawAmount;

    /**
     * 可提现金额结束
     */
    @ApiModelProperty(value = "可提现金额结束")
    private BigDecimal endWithdrawAmount;


}
