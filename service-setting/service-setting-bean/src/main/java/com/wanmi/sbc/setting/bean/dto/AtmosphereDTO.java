package com.wanmi.sbc.setting.bean.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AtmosphereDTO {
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer atmosType;

    private String skuNo;

    private String goodsName;

    private String imageUrl;

    private String desc;

}
