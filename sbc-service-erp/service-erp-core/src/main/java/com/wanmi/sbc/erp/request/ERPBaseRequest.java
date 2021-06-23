package com.wanmi.sbc.erp.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: sbc-background
 * @description: 公共请求参数
 * @author: 0F3685-wugongjiang
 * @create: 2021-01-27 09:34
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class ERPBaseRequest implements Serializable {

    @JsonProperty("appkey")
    private String appkey;

    @JsonProperty("sessionkey")
    private String sessionkey;

    @JsonProperty("method")
    private String method;

    @JsonProperty("sign")
    private String sign;
}
