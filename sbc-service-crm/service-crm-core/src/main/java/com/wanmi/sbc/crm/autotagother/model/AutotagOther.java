package com.wanmi.sbc.crm.autotagother.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AutotagOther {
    // 标签id
    private String id;
    // 标签类型
    private String type;
    // 数量统计
    private int num;
    // 时间
    private Date pDate;
    // 标签名称
    private String detailName;
}
