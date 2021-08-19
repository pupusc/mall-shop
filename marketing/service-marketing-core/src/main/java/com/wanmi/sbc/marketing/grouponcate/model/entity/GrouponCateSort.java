package com.wanmi.sbc.marketing.grouponcate.model.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author: chenli
 * @Date: 2019/5/16
 * @Description: 拼团分类排序request
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrouponCateSort implements Serializable {

    private static final long serialVersionUID = -7747957628897929355L;

    /**
     * 拼团分类标识
     */
    @ApiModelProperty(value = "拼团分类Id")
    @NotBlank
    @Length(max = 32)
    private String grouponCateId;


    /**
     * 拼团分类排序顺序
     */
    @ApiModelProperty(value = "拼团分类排序顺序")
    @NotNull
    private Integer cateSort;
}
