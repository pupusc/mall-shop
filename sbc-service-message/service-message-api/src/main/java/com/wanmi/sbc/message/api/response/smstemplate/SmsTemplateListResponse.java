package com.wanmi.sbc.message.api.response.smstemplate;

import com.wanmi.sbc.message.bean.vo.SmsTemplateVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>短信模板列表结果</p>
 * @author lvzhenwei
 * @date 2019-12-03 15:43:29
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsTemplateListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 短信模板列表结果
     */
    @ApiModelProperty(value = "短信模板列表结果")
    private List<SmsTemplateVO> smsTemplateVOList;
}
