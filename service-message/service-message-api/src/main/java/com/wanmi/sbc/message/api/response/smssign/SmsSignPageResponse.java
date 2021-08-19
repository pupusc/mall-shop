package com.wanmi.sbc.message.api.response.smssign;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.message.bean.vo.SmsSignVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>短信签名分页结果</p>
 * @author lvzhenwei
 * @date 2019-12-03 15:49:24
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsSignPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 短信签名分页结果
     */
    @ApiModelProperty(value = "短信签名分页结果")
    private MicroServicePage<SmsSignVO> smsSignVOPage;
}
