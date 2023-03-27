package com.wanmi.sbc.bookmeta.vo;

import lombok.Data;

import javax.persistence.Column;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/02/13:16
 * @Description:
 */
@Data
public class GoodsSearchBySpuIdRespVO {
    private int id;
    private String name;
    private String goodsName;
    private String spuId;
    private String relSpuId;
    private String relSkuId;
    private String relSkuName;
    private Integer type;
    private String goUrl;
    private Integer orderNum;
    private int status;
}
