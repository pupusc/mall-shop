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
 * <p>短信签名文件信息修改结果</p>
 * @author lvzhenwei
 * @date 2019-12-04 14:19:35
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsSignFileInfoModifyResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已修改的短信签名文件信息信息
     */
    @ApiModelProperty(value = "已修改的短信签名文件信息信息")
    private SmsSignFileInfoVO smsSignFileInfoVO;
}
