package com.wanmi.sbc.customer.api.response.customersignrecord;

import com.wanmi.sbc.customer.bean.vo.CustomerSignRecordVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>用户签到记录列表结果</p>
 * @author wangtao
 * @date 2019-10-05 16:13:04
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSignRecordListResponse implements Serializable {
    private static final long serialVersionUID = 8221000419172101209L;

    /**
     * 用户签到记录列表结果
     */
    @ApiModelProperty(value = "用户签到记录列表结果")
    private List<CustomerSignRecordVO> customerSignRecordVOList;
}
