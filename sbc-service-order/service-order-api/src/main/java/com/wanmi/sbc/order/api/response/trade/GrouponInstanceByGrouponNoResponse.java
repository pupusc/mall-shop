package com.wanmi.sbc.order.api.response.trade;

import com.wanmi.sbc.order.bean.vo.GrouponInstanceVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description:
 * @Date: 2018-12-04 11:02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class GrouponInstanceByGrouponNoResponse implements Serializable {


    private static final long serialVersionUID = -3447768748084136790L;
    /**
     * 团信息
     */
    @ApiModelProperty(value = "团信息")
    private GrouponInstanceVO grouponInstance;

}
