package com.wanmi.sbc.message.api.response.smssignfileinfo;

import com.wanmi.sbc.message.bean.vo.SmsSignFileInfoVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）短信签名文件信息信息response</p>
 * @author lvzhenwei
 * @date 2019-12-04 14:19:35
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsSignFileInfoByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 短信签名文件信息信息
     */
    @ApiModelProperty(value = "短信签名文件信息信息")
    private SmsSignFileInfoVO smsSignFileInfoVO;
}
