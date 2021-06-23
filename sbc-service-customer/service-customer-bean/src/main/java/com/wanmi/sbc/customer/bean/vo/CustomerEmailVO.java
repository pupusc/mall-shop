package com.wanmi.sbc.customer.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 邮箱服务器设置
 */
@ApiModel
@Data
public class CustomerEmailVO implements Serializable {

    private static final long serialVersionUID = -2127220754733920552L;

    /**
     * 邮箱配置Id，采用UUID
     */

    @ApiModelProperty(value = "邮箱配置Id")
    private String customerEmailId;

    /**
     * 邮箱所属客户Id
     */
    @ApiModelProperty(value = "邮箱所属客户Id")
    private String customerId;

    /**
     * 邮箱地址
     */
    @ApiModelProperty(value = "邮箱地址")
    private String emailAddress;

    /**
     * 删除标记  0未删除  1已删除
     */
    @ApiModelProperty(value = "删除标记")
    private DeleteFlag delFlag;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createPerson;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String updatePerson;

    /**
     * 删除时间
     */
    @ApiModelProperty(value = "删除时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime delTime;

    /**
     * 删除
     */
    @ApiModelProperty(value = "删除人")
    private String delPerson;

}
