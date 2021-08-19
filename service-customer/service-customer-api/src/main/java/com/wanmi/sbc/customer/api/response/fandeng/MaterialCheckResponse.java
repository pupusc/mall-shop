package com.wanmi.sbc.customer.api.response.fandeng;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: sbc-background
 * @description: 素材审核返回实体
 * @author: Mr.Tian
 * @create: 2021-03-08 15:23
 **/
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaterialCheckResponse  implements Serializable {
    private static final long serialVersionUID = -7448100173848845872L;

    private String url;






}
