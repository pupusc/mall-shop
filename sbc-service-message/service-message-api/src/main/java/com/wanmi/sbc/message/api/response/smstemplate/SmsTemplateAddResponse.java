package com.wanmi.sbc.message.api.response.smstemplate;

import com.wanmi.sbc.message.bean.vo.SmsTemplateVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>短信模板新增结果</p>
 * @author lvzhenwei
 * @date 2019-12-03 15:43:29
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsTemplateAddResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已新增的短信模板信息
     */
    @ApiModelProperty(value = "已新增的短信模板信息")
    private SmsTemplateVO smsTemplateVO;
}
