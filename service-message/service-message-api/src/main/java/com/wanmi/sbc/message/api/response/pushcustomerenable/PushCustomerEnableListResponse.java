package com.wanmi.sbc.message.api.response.pushcustomerenable;

import com.wanmi.sbc.message.bean.vo.PushCustomerEnableVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>会员推送通知开关列表结果</p>
 * @author Bob
 * @date 2020-01-07 15:31:47
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushCustomerEnableListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 会员推送通知开关列表结果
     */
    @ApiModelProperty(value = "会员推送通知开关列表结果")
    private List<PushCustomerEnableVO> pushCustomerEnableVOList;
}
