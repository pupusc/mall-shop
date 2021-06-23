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
 * <p>用户签到记录新增结果</p>
 * @author wangtao
 * @date 2019-10-05 16:13:04
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSignRecordAddResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已新增的用户签到记录信息
     */
    @ApiModelProperty(value = "已新增的用户签到记录信息")
    private CustomerSignRecordVO customerSignRecordVO;
}
