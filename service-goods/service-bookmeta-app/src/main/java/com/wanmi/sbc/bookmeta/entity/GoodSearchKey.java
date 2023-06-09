package com.wanmi.sbc.bookmeta.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/02/11:39
 * @Description:
 */
@Data
public class GoodSearchKey {
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "goods_name")
    private String goodsName;
    @Column(name = "spu_id")
    private String spuId;
    @Column(name = "rel_spu_id")
    private String relSpuId;
    private String relSkuName;;
    @Column(name = "rel_sku_id")
    private String relSkuId;
    @Column(name = "type")
    private Integer type;
    @Column(name = "go_url")
    private String goUrl;
    @Column(name = "order_num")
    private Integer orderNum;
    @Column(name = "del_flag")
    private Integer delFlag;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "update_time")
    private Date updateTime;
    @Column(name = "status")
    private Integer status;

}
