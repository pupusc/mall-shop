package com.wanmi.sbc.order.response;

import lombok.Data;

import java.io.*;

/**
 * @author Liang Jun
 * @date 2022-04-07 02:17:00
 */
@Data
public class TradeItemGuideResponse implements Serializable {
    private String spuId;
    private String skuId;
    private String spuName;
    private String skuName;
    private String guideText;
    private String guideImg;
}
