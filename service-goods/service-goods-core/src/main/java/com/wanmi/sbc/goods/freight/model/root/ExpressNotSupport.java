package com.wanmi.sbc.goods.freight.model.root;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wanmi.sbc.common.enums.DeleteFlag;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_express_not_support")
public class ExpressNotSupport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    //二级供应商id
    @Column(name = "supplier_id")
    private Long supplierId;

    //不支持配送地区
    @Column(name = "not_support_area")
    private String notSupportArea;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "create_time")
    private LocalDateTime createTime;

    //更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    //是否删除
    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag;
}
