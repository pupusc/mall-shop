package com.wanmi.sbc.goods.api.request.cate;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * com.wanmi.sbc.goods.api.request.goodscate.GoodsCateListRequest
 * 根据条件查询商品分类列表信息请求对象
 * @author lipeng
 * @dateTime 2018/11/1 下午3:25
 */
@ApiModel
@Data
public class GoodsCateListByConditionRequest extends BaseQueryRequest  {

    /**
     * 分类编号
     */
    @ApiModelProperty(value = "分类编号")
    private Long cateId;

    /**
     * 批量分类编号
     */
    @ApiModelProperty(value = "批量分类编号")
    private List<Long> cateIds;

    /**
     * 批量分类父编号
     */
    @ApiModelProperty(value = "批量分类父编号")
    private List<Long> cateParentIds;

    /**
     * 分类父编号
     */
    @ApiModelProperty(value = "分类父编号")
    private Long cateParentId;

    /**
     * 模糊查询，分类路径
     */
    @ApiModelProperty(value = "模糊查询，分类路径")
    private String likeCatePath;

    /**
     * 分类层级
     */
    @ApiModelProperty(value = "分类层级")
    private Integer cateGrade;

    /**
     * 删除标记
     */
    @ApiModelProperty(value = "删除标记", dataType = "com.wanmi.sbc.common.enums.DeleteFlag")
    private Integer delFlag;

    /**
     * 是否默认
     */
    @ApiModelProperty(value = "是否默认", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    private Integer isDefault;

    /**
     * 父类名称
     */
    @ApiModelProperty(value = "父类名称")
    private String cateName;

    /**
     * 非分类编号
     */
    @ApiModelProperty(value = "非分类编号")
    private Long notCateId;

    /**
     * 关键字查询，可能含空格
     */
    @ApiModelProperty(value = "关键字查询，可能含空格")
    private String keywords;

}
