package com.wanmi.sbc.goods.bean.vo;

import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Enumerated;
import java.io.Serializable;

/**
 * 商品库关联VO
 *
 * @auther dyt
 * @create 2018/03/20 10:04
 */
@ApiModel
@Data
public class StandardGoodsRelVO  implements Serializable {

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号")
    private Long relId;

    /**
     * SPU标识
     */
    @ApiModelProperty(value = "SPU标识")
    private String goodsId;

    /**
     *商品库SPU编号
     */
    @ApiModelProperty(value = "商品库SPU编号")
    private String standardId;

    /**
     *店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    /**
     * 删除标记  二次导入可能用到
     */
    @ApiModelProperty(value = "删除标记")
    @Enumerated
    private DeleteFlag delFlag;

    /**
     * 是否需要同步 0：不需要同步 1：需要同步
     */
    @ApiModelProperty(value = "是否需要同步 0：不需要同步 1：需要同步")
    @Enumerated
    private BoolFlag needSynchronize;

}
