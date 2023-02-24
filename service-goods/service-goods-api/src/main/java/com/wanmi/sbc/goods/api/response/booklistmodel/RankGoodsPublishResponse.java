package com.wanmi.sbc.goods.api.response.booklistmodel;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
public class RankGoodsPublishResponse {

    private Integer id;

    /**
     * 控件id
     */
    private Integer chooseRuleId;

    /**
     * 书单模板id或者书单类目id
     */
    private Integer bookListId;

    /**
     * 分类 1书单模板 2类目
     */
    private Integer category;

    /**
     *
     */
    private String spuId;

    private String spuNo;

    private String skuId;

    private String skuNo;

    private String erpGoodsNo;

    private String erpGoodsInfoNo;

    private Integer orderNum;

    private Integer version;

    private Date createTime;

    private Date updateTime;

    private Integer delFlag;

    private Integer saleNum;

    private String rankText;
}
