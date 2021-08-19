package com.wanmi.sbc.crm.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @program: sbc-micro-service-A
 * @description:
 * @create: 2020-09-03 09:52
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AutoTagSelectValuesDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long selectedId;

    private Long columnType;

    private String name;

    private String columnName;

    private List<?> dataSource;

    private List<String> value;

}