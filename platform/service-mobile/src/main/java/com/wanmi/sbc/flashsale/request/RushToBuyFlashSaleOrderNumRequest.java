package com.wanmi.sbc.flashsale.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @ClassName RushToBuyFlashSaleOrderNumRequest
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2019/10/3 19:36
 **/
@ApiModel
@Data
public class RushToBuyFlashSaleOrderNumRequest implements Serializable {

    private static final long serialVersionUID = -7171344015361321798L;

    /**
     * 抢购商品Id
     */
    @ApiModelProperty(value = "抢购商品Id")
    @NotNull
    private Long flashSaleGoodsId;

    /**
     * 客户ID
     */
    @ApiModelProperty(value = "客户ID")
    private String customerId;

    /**
     * 秒杀商品会员抢购次数集合
     */
    @ApiModelProperty(value = "秒杀商品会员抢购次数集合")
    private List<Integer> flashSaleNumList;

}
