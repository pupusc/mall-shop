package com.wanmi.sbc.goods.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * com.wanmi.sbc.goods.api.response.vo.GoodsCateListVO
 *
 * @author lipeng
 * @dateTime 2018/11/1 下午4:26
 */
@ApiModel
@Data
public class GoodsCateVO implements Serializable {

    private static final long serialVersionUID = -1350115299914313788L;

    /**
     * 分类编号
     */
    @ApiModelProperty(value = "分类编号")
    private Long cateId;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    private String cateName;

    /**
     * 父类编号
     */
    @ApiModelProperty(value = "父类编号")
    private Long cateParentId;

    /**
     * 分类图片
     */
    @ApiModelProperty(value = "分类图片")
    private String cateImg;

    /**
     * 分类路径
     */
    @ApiModelProperty(value = "分类路径")
    private String catePath;

    /**
     * 分类层次
     */
    @ApiModelProperty(value = "分类层次")
    private Integer cateGrade;

    /**
     * 分类扣率
     */
    @ApiModelProperty(value = "分类扣率")
    private BigDecimal cateRate;

    /**
     * 是否使用上级类目扣率 0 否   1 是
     */
    @ApiModelProperty(value = "是否使用上级类目扣率", notes = "0否 1是")
    private DefaultFlag isParentCateRate;

    /**
     * 成长值获取比例
     */
    @ApiModelProperty(value = "成长值获取比例")
    private BigDecimal growthValueRate;

    /**
     * 是否使用上级类目成长值获取比例 0 否   1 是
     */
    @ApiModelProperty(value = "是否使用上级类目成长值获取比例", notes = "0否 1是")
    private DefaultFlag isParentGrowthValueRate;

    /**
     * 积分获取比例
     */
    @ApiModelProperty(value = "积分获取比例")
    private BigDecimal pointsRate;

    /**
     * 是否使用上级类目积分获取比例 0 否   1 是
     */
    @ApiModelProperty(value = "是否使用上级类目积分获取比例", notes = "0否 1是")
    private DefaultFlag isParentPointsRate;

    /**
     * 拼音
     */
    @ApiModelProperty(value = "拼音")
    private String pinYin;

    /**
     * 简拼
     */
    @ApiModelProperty(value = "简拼")
    private String sPinYin;

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
     * 删除标记
     */
    @ApiModelProperty(value = "删除标记", notes = "0: 否, 1: 是")
    private DeleteFlag delFlag;

    /**
     * 默认标记
     */
    @ApiModelProperty(value = "默认标记", notes = "0: 否, 1: 是")
    private DefaultFlag isDefault;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;


    /**
     * 是否图书 0-否 1-是
     */
    private Integer bookFlag;

    /**
     * 一对多关系，子分类
     */
    @ApiModelProperty(value = "一对多关系，子分类")
    private List<GoodsCateVO> goodsCateList;

    /**
     * 一对多关系，属性
     */
    @ApiModelProperty(value = "一对多关系，属性")
    private List<GoodsCatePropVO> goodsProps;
}
