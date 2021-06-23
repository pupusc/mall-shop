package com.wanmi.sbc.customer.api.request.customer;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.customer.bean.enums.CustomerStatus;
import com.wanmi.sbc.customer.bean.enums.CustomerType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 会员详情查询参数
 * Created by baijz on 2017/4/19.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDetailListByPageFordrRequest extends BaseQueryRequest {

    private static final long serialVersionUID = -4582081187218350168L;

    /**
     * 账户
     */
    @ApiModelProperty(value = "账户")
    private String customerAccount;

    /**
     * 会员名称
     */
    @ApiModelProperty(value = "会员名称")
    private String customerName;


    /**
     * 删除标志 0未删除 1已删除
     */
    @ApiModelProperty(value = "删除标志", dataType = "com.wanmi.sbc.common.enums.DeleteFlag")
    private Integer delFlag;


}
