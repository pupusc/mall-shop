package com.wanmi.sbc.setting.log;

import lombok.Data;

import javax.persistence.*;

/**
 * IP归属信息
 * Created by yuanlinling on 2017/4/26.
 */
@Data
@Entity
@Table(name = "system_ip_info")
public class IpInfo {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * IP
     */
    @Column(name = "ip")
    private String ip;

    /**
     * 运营商
     */
    @Column(name = "isp")
    private String isp;

    /**
     * 国家
     */
    @Column(name = "country")
    private String country;

    /**
     * 省份
     */
    @Column(name = "province")
    private String province;

    /**
     * 城市
     */
    @Column(name = "city")
    private String city;
}
