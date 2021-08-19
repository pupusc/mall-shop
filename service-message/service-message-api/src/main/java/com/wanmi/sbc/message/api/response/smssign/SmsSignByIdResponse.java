package com.wanmi.sbc.message.api.response.smssign;

import com.wanmi.sbc.message.bean.vo.SmsSignVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）短信签名信息response</p>
 * @author lvzhenwei
 * @date 2019-12-03 15:49:24
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsSignByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 短信签名信息
     */
    @ApiModelProperty(value = "短信签名信息")
    private SmsSignVO smsSignVO;
}
