package com.wanmi.sbc.customer.bean.dto;

import com.wanmi.sbc.customer.bean.enums.CustomerType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <Description> <br>
 *
 * @author hejiawen<br>
 * @version 1.0<br>
 * @taskId <br>
 * @createTime 2018-09-12 14:44 <br>
 * @see com.wanmi.sbc.customer.api.request.store.dto <br>
 * @since V1.0<br>
 */
@ApiModel
@Data
public class StoreCustomerRelaDTO implements Serializable {

    private static final long serialVersionUID = 2462269149545703301L;

    @ApiModelProperty(value = "主键")
    private String id;

    /**
     * 用户标识
     */
    @ApiModelProperty(value = "用户标识")
    private String customerId;

    /**
     * 店铺标识
     */
    @ApiModelProperty(value = "店铺标识")
    private Long storeId;

    /**
     * 商家标识
     */
    @ApiModelProperty(value = "商家标识")
    private Long companyInfoId;

    /**
     * 客户等级标识
     */
    @ApiModelProperty(value = "客户等级标识")
    private Long customerLevelId;

    /**
     * 店铺等级标识
     */
    @ApiModelProperty(value = "店铺等级标识")
    private Long storeLevelId;

    /**
     * 负责的业务员标识
     */
    @ApiModelProperty(value = "负责的业务员标识")
    private String employeeId;

    /**
     * 关系类型(0:店铺关联的客户,1:店铺发展的客户)
     */
    @ApiModelProperty(value = "关系类型")
    private CustomerType customerType;

}
