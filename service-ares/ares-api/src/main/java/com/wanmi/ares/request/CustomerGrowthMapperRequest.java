package com.wanmi.ares.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户增长请求
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerGrowthMapperRequest implements Serializable{

    private Long start;

    private Long pageSize;

    private String startDate;

    private String endDate;

    private String sortField;

    private String sortType;
}
