package com.wanmi.sbc.open.vo;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-02-15 18:02:00
 */
@Data
public class OrderCreateReqVO extends BaseReqVO implements Serializable {
    /**
     * 樊登读书用户id
     */
    @NotBlank
    private String fddsUserId;
    /**
     * 外部交易编号
     */
    @NotBlank
    private String outTradeNo;
    /**
     * 备注信息
     */
    @NotBlank
    private String buyerRemark;
    /**
     * 收货人信息
     */
    @Valid
    @NotNull
    private ConsigneeReqVO consignee;
    /**
     * 发货清单
     */
    @Valid
    @NotEmpty
    private List<TradeItemReqVO> tradeItems;
}
