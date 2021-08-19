package com.wanmi.sbc.account.api.request.funds;

import com.wanmi.sbc.account.bean.enums.FundsStatus;
import com.wanmi.sbc.account.bean.enums.FundsType;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 会员资金明细-导出查询对象
 * @author: of2975
 * @createDate: 2019/4/30 11:06
 * @version: 1.0
 */
@ApiModel
@Data
public class CustomerFundsDetailExportRequest extends BaseQueryRequest implements Serializable {

    /**
     * jwt token
     */
    @ApiModelProperty(value = "token")
    private String token;

    /**
     * 批量查询-明细主键List
     */
    @ApiModelProperty(value = "批量查询-明细主键List")
    private List<String> customerFundsDetailIdList;

    /**
     * 会员ID
     */
    @ApiModelProperty(value = "会员ID")
    private String customerId;

    /**
     * Tab类型 0: 全部, 1: 收入, 2: 支出, 3:分销佣金&邀新记录
     */
    @ApiModelProperty(value = "Tab类型 0: 全部, 1: 收入, 2: 支出, 3:分销佣金&邀新记录")
    private Integer tabType;

    /**
     * 资金类型集合
     */
    @ApiModelProperty(value = "资金类型集合")
    private List<Integer> fundsTypeList;

    /**
     * 资金类型
     */
    @ApiModelProperty(value = "资金类型")
    private FundsType fundsType;

    /**
     * 资金状态
     */
    @ApiModelProperty(value = "资金状态")
    private FundsStatus fundsStatus;

    /**
     * 业务编号
     */
    @ApiModelProperty(value = "业务编号")
    private String businessId;

    /**
     * 入账开始时间
     */
    @ApiModelProperty(value = "入账开始时间")
    private String startTime;

    /**
     * 入账结束时间
     */
    @ApiModelProperty(value = "入账结束时间")
    private String endTime;

    /**
     * 佣金提现id
     */
    @ApiModelProperty(value = "佣金提现id")
    private String drawCashId;
}
