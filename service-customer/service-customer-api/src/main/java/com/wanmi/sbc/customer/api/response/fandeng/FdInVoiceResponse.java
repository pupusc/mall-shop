package com.wanmi.sbc.customer.api.response.fandeng;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * 发票发起后的结果
 */

@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FdInVoiceResponse implements Serializable {

    private String key;
}
