package com.wanmi.sbc.setting.api.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.setting.bean.enums.GrowthValueRule;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>查询成长值设置信息response</p>
 * @author yxz
 * @date 2019-03-28 16:24:21
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemGrowthValueConfigQueryResponse implements Serializable {
    private static final long serialVersionUID = 1083626714258774374L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String growthValueConfigId;

    /**
     * 成长值获取规则
     */
    @ApiModelProperty(value = "成长值获取规则")
    private GrowthValueRule rule;

    /**
     * 成长值说明
     */
    @ApiModelProperty(value = "成长值说明")
    private String remark;

    /**
     * 是否启用标志 0：停用，1：启用
     */
    @ApiModelProperty(value = "是否启用标志 0：停用，1：启用")
    private EnableStatus status;

    /**
     * 是否删除标志 0：否，1：是
     */
    @ApiModelProperty(value = "是否删除标志 0：否，1：是")
    private DeleteFlag delFlag;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

}
