package com.wanmi.sbc.goods.api.response.enterprise;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: sbc-micro-service
 * @description:
 * @create: 2019-03-11 17:18
 **/
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnterpriseCheckResponse implements Serializable {

    /**
     * 检查的标识
     */
    private Boolean checkFlag;
}
