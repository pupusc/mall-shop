package com.wanmi.sbc.erp.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @program: sbc-background
 * @description: 管易云ERP公共返回对象
 * @author: 0F3685-wugongjiang
 * @create: 2021-01-26 17:34
 **/
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPBaseResponse implements Serializable {


    @JsonProperty("success")
    private boolean success;

    @JsonProperty("errorCode")
    private String errorCode;

    @JsonProperty("subErrorCode")
    private String subErrorCode;

    @JsonProperty("errorDesc")
    private String errorDesc;

    @JsonProperty("subErrorDesc")
    private String subErrorDesc;

    @JsonProperty("requestMethod")
    private String requestMethod;
}
