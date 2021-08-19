package com.wanmi.sbc.order.api.request.returnorder;

import com.wanmi.sbc.order.bean.vo.ReturnOrderVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-12-03 15:41
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class ReturnExportRequest implements Serializable {

    /**
     * 订单视图对象
     */
    @ApiModelProperty(value = "订单视图对象")
    private List<ReturnOrderVO> returnOrderList;

    /**
     *输出流
     */
    @ApiModelProperty(value = "输出流")
    private OutputStream outputStream;

    /**
     * 存在商家
     */
    @ApiModelProperty(value = "存在商家",dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    boolean existSupplier;

}
