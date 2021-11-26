package com.wanmi.sbc.crm.autotagpreference.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AutotagPreference {
    // 标签id
    private String tagId;
    // 维度类型
    private String dimensionType;
    // 维度id
    private String dimensionId;
    // 数量统计
    private int num;
    // 时间
    private Date pDate;
    // 标签名称
    private String detailName;
}
