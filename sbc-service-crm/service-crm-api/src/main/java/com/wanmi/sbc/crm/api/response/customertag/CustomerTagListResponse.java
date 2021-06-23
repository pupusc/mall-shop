package com.wanmi.sbc.crm.api.response.customertag;

import com.wanmi.sbc.crm.bean.vo.CustomerTagVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>会员标签列表结果</p>
 * @author zhanglingke
 * @date 2019-10-14 11:19:11
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerTagListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 会员标签列表结果
     */
    @ApiModelProperty(value = "会员标签列表结果")
    private List<CustomerTagVO> customerTagVOList;
}
