package com.wanmi.sbc.goods.bean.dto;


import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilterDTO {

    private Integer status;

    private Integer prod;

    private String userIds;

    private String oldUrl;

    private String newUrl;

    private String loginUrl;

    private List<String> phoneList;
}
