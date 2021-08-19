package com.wanmi.sbc.crm.api.response.customertagrel;

import com.wanmi.sbc.crm.bean.vo.CustomerTagRelVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）会员标签关联信息response</p>
 * @author dyt
 * @date 2019-11-12 14:49:08
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerTagRelByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 会员标签关联信息
     */
    @ApiModelProperty(value = "会员标签关联信息")
    private CustomerTagRelVO customerTagRelVO;
}
