package com.wanmi.sbc.freight.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class NotSupportAreaImportExcelRequest {

    private Long supplierId;
    private Map<String, List<String>> areas;

}
