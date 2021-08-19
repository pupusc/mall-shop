package com.wanmi.sbc.order.api.response.trade;

import com.wanmi.sbc.common.base.MicroServicePage;
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
public class GrouponInstancePageResponse implements Serializable {


    private static final long serialVersionUID = -5429286589215088253L;
    /**
     * 分页数据
     */
    @ApiModelProperty(value = "分页数据")
    private MicroServicePage<GrouponInstanceVO> grouponInstanceVOS;

}
