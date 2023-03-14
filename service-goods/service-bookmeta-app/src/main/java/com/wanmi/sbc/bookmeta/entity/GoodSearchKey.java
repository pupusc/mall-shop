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
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "goods_name")
    private String goodsName;
    @Column(name = "spu_id")
    private String spuId;
    @Column(name = "del_flag")
    private int delFlag;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "update_time")
    private Date updateTime;
    @Column(name = "status")
    private int status;

}
