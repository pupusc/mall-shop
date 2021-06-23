package com.wanmi.sbc.goods.api.request.pointsgoodscate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.Max;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>积分商品分类表修改参数</p>
 *
 * @author yang
 * @date 2019-05-13 09:50:07
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointsGoodsCateModifyRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 积分商品分类主键
     */
    @ApiModelProperty(value = "积分商品分类主键")
    private Integer cateId;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    @NotBlank
    private String cateName;

    /**
     * 排序 默认0
     */
    @ApiModelProperty(value = "排序 默认0")
    private Integer sort;

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
     * 删除标识,0: 未删除 1: 已删除
     */
    @ApiModelProperty(value = "删除标识,0: 未删除 1: 已删除")
    private DeleteFlag delFlag;

}