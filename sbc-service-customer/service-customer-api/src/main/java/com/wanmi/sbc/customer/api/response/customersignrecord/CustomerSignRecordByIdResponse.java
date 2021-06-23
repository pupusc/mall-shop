package com.wanmi.sbc.customer.api.response.customersignrecord;

import com.wanmi.sbc.customer.bean.vo.CustomerSignRecordVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>根据id查询任意（包含已删除）用户签到记录信息response</p>
 * @author wangtao
 * @date 2019-10-05 16:13:04
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSignRecordByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户签到记录信息
     */
    @ApiModelProperty(value = "用户签到记录信息")
    private CustomerSignRecordVO customerSignRecordVO;
}
