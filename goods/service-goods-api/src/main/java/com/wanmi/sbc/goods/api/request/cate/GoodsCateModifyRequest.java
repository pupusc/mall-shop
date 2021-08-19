package com.wanmi.sbc.goods.api.request.cate;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * com.wanmi.sbc.goods.api.request.goodscate.GoodsCateModifyRequest
 * 修改商品分类信息请求对象
 * @author lipeng
 * @dateTime 2018/11/1 下午4:54
 */
@ApiModel
@Data
public class GoodsCateModifyRequest implements Serializable {

    private static final long serialVersionUID = 4362250574081024617L;

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
    @ApiModelProperty(value = "是否使用上级类目扣率 0 否   1 是")
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
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 删除标记
     */
    @ApiModelProperty(value = "删除标记")
    private DeleteFlag delFlag;

    /**
     * 默认标记
     */
    @ApiModelProperty(value = "默认标记")
    private DefaultFlag isDefault;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;
}
