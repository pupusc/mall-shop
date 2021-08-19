package com.wanmi.sbc.customer.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * <Description> <br>
 *
 * @author hejiawen<br>
 * @version 1.0<br>
 * @taskId <br>
 * @createTime 2018-09-12 10:33 <br>
 * @see com.wanmi.sbc.customer.api.response.store.vo <br>
 * @since V1.0<br>
 */
@ApiModel
@Data
public class StoreCustomerVO implements Serializable {


    private static final long serialVersionUID = -6972994338872127199L;

    /**
     * 会员ID
     */
    @ApiModelProperty(value = "会员ID")
    private String customerId;

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
     * 客户等级ID
     */
    @ApiModelProperty(value = "客户等级ID")
    private Long customerLevelId;

    /**
     * 客户等级名称
     */
    @ApiModelProperty(value = "客户等级名称")
    private String customerLevelName;

    /**
     * 从数据库实体转换为返回前端的用户信息
     * (字段顺序不可变)
     */
    public static StoreCustomerVO convertFromNativeSQLResult(Object result) {
        StoreCustomerVO response = new StoreCustomerVO();
        response.setCustomerId((String) ((Object[]) result)[0]);
        response.setCustomerAccount((String) ((Object[]) result)[1]);
        response.setCustomerName((String) ((Object[]) result)[2]);
        if (((Object[]) result)[3] != null) {
            response.setCustomerLevelId(((BigInteger) ((Object[]) result)[3]).longValue());
        }
        response.setCustomerLevelName((String) ((Object[]) result)[4]);
        return response;
    }
}
