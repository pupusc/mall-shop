package com.wanmi.sbc.goods.bean.vo;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

@Data
public class GoodsAdAuditVO implements Serializable {


    private String goodsNo;
    
    private String imageUrl;



    private Integer status;


    private String errorMsg;

    private Date auditTime;
}
