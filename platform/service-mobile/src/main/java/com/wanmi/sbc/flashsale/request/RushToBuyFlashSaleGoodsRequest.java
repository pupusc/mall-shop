package com.wanmi.sbc.flashsale.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName ImmediatelyFlashSaleGoodsRequest
 * @Description 秒杀抢购活动Request请求类
 * @Author lvzhenwei
 * @Date 2019/6/14 9:38
 **/
@ApiModel
@Data
public class RushToBuyFlashSaleGoodsRequest implements Serializable {

    private static final long serialVersionUID = -8936733533728487547L;

    /**
     * 抢购商品Id
     */
    @ApiModelProperty(value = "抢购商品Id")
    @NotNull
    private Long flashSaleGoodsId;

    /**
     * 抢购商品数量
     */
    @ApiModelProperty(value = "抢购商品数量")
    private Integer flashSaleGoodsNum;

    /**
     * 客户ID
     */
    @ApiModelProperty(value = "客户ID")
    private String customerId;

    /**
     * 秒杀商品会员抢购次数
     */
    @ApiModelProperty(value = "秒杀商品会员抢购次数")
    private Integer flashSaleNum;

    @ApiModelProperty(value = "终端登陆token")
    private String terminalToken;

}
