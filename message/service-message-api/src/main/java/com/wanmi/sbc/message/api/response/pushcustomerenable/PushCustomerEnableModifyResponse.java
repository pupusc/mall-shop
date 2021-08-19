package com.wanmi.sbc.message.api.response.pushcustomerenable;

import com.wanmi.sbc.message.bean.vo.PushCustomerEnableVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>会员推送通知开关修改结果</p>
 * @author Bob
 * @date 2020-01-07 15:31:47
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushCustomerEnableModifyResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已修改的会员推送通知开关信息
     */
    @ApiModelProperty(value = "已修改的会员推送通知开关信息")
    private PushCustomerEnableVO pushCustomerEnableVO;
}
