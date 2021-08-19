package com.wanmi.sbc.marketing.api.response.groupon;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>拼团信息
 * @author groupon
 * @date 2019-05-15 14:02:38
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
public class GrouponCheckResponse implements Serializable {

    private static final long serialVersionUID = 1532370571828095348L;


}
