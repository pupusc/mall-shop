package com.wanmi.sbc.setting.atmosphere.model.root;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@Table(name = "atmosphere")
@Entity
public class Atmosphere {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "sku_no")
    private String skuNo;
    @Column(name = "sku_id")
    private String skuId;
    @Column(name = "start_time")
    private LocalDateTime startTime;
    @Column(name = "end_time")
    private LocalDateTime endTime;
    @Column(name = "create_time")
    private LocalDateTime createTime;
    @Column(name = "update_time")
    private LocalDateTime updateTime;
    @Column(name = "deleted")
    private Integer deleted;
    @Column(name = "element_desc")
    private String elementDesc;
    @Column(name = "atmos_type")
    private Integer atmosType;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "sync_status")
    private Integer syncStatus;
    @Column(name = "goods_info_name")
    private String goodsInfoName;
}