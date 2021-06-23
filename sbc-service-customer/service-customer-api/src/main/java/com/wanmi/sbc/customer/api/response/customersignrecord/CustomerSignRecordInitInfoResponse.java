package com.wanmi.sbc.customer.api.response.customersignrecord;

import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>签到页面初始化所需要的数据</p>
 * @author wangtao
 * @date 2019-10-05 16:13:04
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSignRecordInitInfoResponse implements Serializable {
    private static final long serialVersionUID = 2377965385048125989L;

    /**
     * 用户信息
     */
    @ApiModelProperty(value = "用户信息")
    private CustomerVO customerVO;

    /**
     * 签到可获得的积分值
     */
    @ApiModelProperty(value = "签到可获得的积分值")
    private Long signPoint;

    /**
     * 后台签到获取积分配置是否开启
     */
    @ApiModelProperty(value = "后台签到获取积分配置是否开启")
    private Boolean pointFlag;

    /**
     * 今日是否签到标志
     */
    @ApiModelProperty(value = "今日是否签到标志")
    private Boolean signFlag;

    /**
     * 后台签到获取成长值配置是否开启
     */
    @ApiModelProperty(value = "后台签到获取成长值配置是否开启")
    private Boolean growthFlag;

    /**
     * 签到可获得的成长值
     */
    @ApiModelProperty(value = "签到可获得的成长值")
    private Long growthValue;
}
