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
    @Column(name = "atmos_type")
    private Integer atmosType;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "sync_status")
    private Integer syncStatus;
    @Column(name = "goods_info_name")
    private String goodsInfoName;
    @Column(name = "element_one")
    private String elementOne;
    @Column(name = "element_two")
    private String elementTwo;
    @Column(name = "element_three")
    private String elementThree;
    @Column(name = "element_four")
    private String elementFour;
}