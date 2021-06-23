package com.wanmi.sbc.goods.api.request.brand;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * 品牌分页查询请求
 * Created by daiyitian on 2017/3/24.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsBrandPageRequest extends BaseQueryRequest {

    private static final long serialVersionUID = -6942228033110682924L;

    /**
     * 批量品牌编号
     */
    @ApiModelProperty(value = "批量品牌编号")
    private List<Long> brandIds;

    /**
     * and 精准查询，品牌名称
     */
    @ApiModelProperty(value = "and 精准查询，品牌名称")
    private String brandName;

    /**
     * and 模糊查询，品牌名称
     */
    @ApiModelProperty(value = "and 模糊查询，品牌名称")
    private String likeBrandName;

    /**
     * and 精准查询，品牌昵称
     */
    @ApiModelProperty(value = "and 精准查询，品牌昵称")
    private String nickName;

    /**
     * and 模糊查询，品牌昵称
     */
    @ApiModelProperty(value = "and 模糊查询，品牌昵称")
    private String likeNickName;

    /**
     * 模糊查询，品牌拼音
     */
    @ApiModelProperty(value = "模糊查询，品牌拼音")
    private String likePinYin;

    /**
     * 删除标记
     */
    @ApiModelProperty(value = "删除标记", dataType = "com.wanmi.sbc.common.enums.DeleteFlag")
    private Integer delFlag;

    /**
     * 非品牌编号
     */
    @ApiModelProperty(value = "非品牌编号")
    private Long notBrandId;

    /**
     * 关键字查询，可能含空格
     */
    @ApiModelProperty(value = "关键字查询，可能含空格")
    private String keywords;
}
