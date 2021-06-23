package com.wanmi.sbc.returnorder.request;

import com.wanmi.sbc.order.bean.enums.ReturnReason;
import com.wanmi.sbc.order.bean.enums.ReturnWay;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 修改退单请求
 * Created by jinwei on 25/4/2017.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemedyReturnRequest {

    /**
     * 退单id
     */
    @ApiModelProperty(value = "退单id")
    private String rid;

    /**
     * 退货原因
     */
    @ApiModelProperty(value = "退货原因")
    private ReturnReason returnReason;

    /**
     * 退货方式
     */
    @ApiModelProperty(value = "退货方式")
    private ReturnWay returnWay;

    /**
     * 退货说明
     */
    @ApiModelProperty(value = "退货说明")
    private String description;

    /**
     * 附件信息
     */
    @ApiModelProperty(value = "附件信息")
    private List<String> images;

    /**
     * 商品数量修改
     */
    @ApiModelProperty(value = "商品数量修改")
    private List<ReturnItemNum> returnItemNums;

    /**
     * 修改申请价格
     */
    @ApiModelProperty(value = "修改申请价格")
    private ReturnPriceRequest returnPriceRequest;

    /**
     * 修改申请积分
     */
    @ApiModelProperty(value = "修改积分")
    private ReturnPointsRequest returnPointsRequest;

}
