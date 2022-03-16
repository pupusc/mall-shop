package com.wanmi.sbc.open.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.*;

@Data
public class TradeItemReqVO implements Serializable {
    @NotBlank
    private String skuId;
    /**
     * 购买数量
     */
    @NotNull
    private Long num;
}
