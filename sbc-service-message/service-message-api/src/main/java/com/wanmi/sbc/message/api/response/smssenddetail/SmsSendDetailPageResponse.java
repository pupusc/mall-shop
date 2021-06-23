package com.wanmi.sbc.message.api.response.smssenddetail;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.message.bean.vo.SmsSendDetailVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>短信发送分页结果</p>
 * @author zgl
 * @date 2019-12-03 15:43:37
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsSendDetailPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 短信发送分页结果
     */
    @ApiModelProperty(value = "短信发送分页结果")
    private MicroServicePage<SmsSendDetailVO> smsSendDetailVOPage;
}
