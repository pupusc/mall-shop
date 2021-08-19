package com.wanmi.sbc.setting.api.request;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 操作日志查询请求
 * Created by daiyitian on 2017/3/24.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OperationLogListRequest extends BaseQueryRequest {

    private static final long serialVersionUID = 2584160228306434248L;

    @ApiModelProperty(value = "token")
    private String token;

    /**
     * 员工编号
     */
    @ApiModelProperty(value = "员工编号")
    private String employeeId;

    /**
     * 店铺Id
     */
    @ApiModelProperty(value = "店铺Id")
    private Long storeId;

    /**
     * 公司Id
     */
    @ApiModelProperty(value = "公司Id")
    private Long companyInfoId;

    /**
     * 商家编号
     */
    @ApiModelProperty(value = "商家编号")
    private String thirdId;

    /**
     * 操作人账号
     */
    @ApiModelProperty(value = "操作人账号")
    private String opAccount;

    /**
     * 操作人名称
     */
    @ApiModelProperty(value = "操作人名称")
    private String opName;

    /**
     * 操作模块
     */
    @ApiModelProperty(value = "操作模块")
    private String opModule;

    /**
     * 操作类型
     */
    @ApiModelProperty(value = "操作类型")
    private String opCode;

    /**
     * 操作内容
     */
    @ApiModelProperty(value = "操作内容")
    private String opContext;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private String beginTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private String endTime;
}
