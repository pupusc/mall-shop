package com.wanmi.sbc.bookmeta.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/04/10:48
 * @Description:
 */
@Data
@Table(name = "meta_label_spu")
public class GoodsLabelSpu implements Serializable {
    @Column(name ="id" )
    private Integer id;
    @Column(name ="goods_id" )
    private String goodsId;
    @Column(name ="goods_name" )
    private String goodsName;
    @Column(name ="label_id" )
    private Integer labelId;
    @Column(name ="label_name" )
    private String labelName;
    @Column(name ="create_time" )
    private Date createTime;
    @Column(name ="update_time" )
    private Date updateTime;
    @Column(name ="first_id" )
    private Integer firstId;
    @Column(name ="second_id" )
    private Integer secondId;
    @Column(name ="order_num" )
    private Integer orderNum;

}
