package com.wanmi.sbc.account.bean.vo;

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


@ApiModel
@Data
public class InvoiceProjectVO implements Serializable {

    /**
     * 主键
     */
    @ApiModelProperty(value = "开票项目id")
    private String projectId;

    /**
     * 开票项目名称
     */
    @ApiModelProperty(value = "开票项目名称")
    private String projectName;

    /**
     * 删除标志
     */
    @ApiModelProperty(value = "删除标志")
    private DeleteFlag delFlag;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private String operatePerson;

    /**
     * 公司信息ID
     */
    @ApiModelProperty(value = "公司信息id")
    private Long companyInfoId;

}
