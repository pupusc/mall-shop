package com.wanmi.sbc.goods.api.request.info;

import com.wanmi.sbc.common.enums.DeleteFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 根据商品SKU编号查询请求
 * Created by daiyitian on 2017/3/24.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsInfoViewByIdsRequest implements Serializable {

    private static final long serialVersionUID = -2265501195719873212L;

    /**
     * 批量SKU编号
     */
    @ApiModelProperty(value = "批量SKU编号")
    private List<String> goodsInfoIds;


    /**
     * 批量isbn编号
     */
    @ApiModelProperty(value = "批量isbn编号")
    private List<String> isbnList;
    /**
     * 是否需要显示规格明细
     * 0:否,1:是
     */
    @ApiModelProperty(value = "是否需要显示规格明细", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    private Integer isHavSpecText;

    /**
     * 店铺ID
     */
    @ApiModelProperty("店铺ID")
    private Long storeId;

    /**
     * 是否删除
     */
    @ApiModelProperty(value = "是否删除")
    private DeleteFlag deleteFlag;

    @ApiModelProperty(value = "是否需要返回标签数据 true:需要，false或null:不需要")
    private Boolean showLabelFlag;

    @ApiModelProperty(value = "当showLabelFlag=true时，true:返回开启状态的标签，false或null:所有标签")
    private Boolean showSiteLabelFlag;

}
