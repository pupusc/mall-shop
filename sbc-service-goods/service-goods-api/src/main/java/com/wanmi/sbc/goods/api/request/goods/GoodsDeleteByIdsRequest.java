package com.wanmi.sbc.goods.api.request.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * com.wanmi.sbc.goods.api.request.goods.GoodsDeleteByIdsRequest
 * 批量删除商品信息请求对象
 * @author lipeng
 * @dateTime 2018/11/5 上午10:48
 */
@ApiModel
@Data
public class GoodsDeleteByIdsRequest implements Serializable {

    private static final long serialVersionUID = 5684270876833820644L;

    @ApiModelProperty(value = "商品Id")
    private List<String> goodsIds;

    /**
     * 删除原因
     */
    @ApiModelProperty(value = "deleteReason")
    private String deleteReason;

    @ApiModelProperty(value = "店铺ID", hidden = true)
    private Long storeId;

    @ApiModelProperty("更新人")
    private String updatePerson;
}
