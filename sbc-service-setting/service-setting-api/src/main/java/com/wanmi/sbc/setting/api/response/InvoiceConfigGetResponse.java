package com.wanmi.sbc.setting.api.response;

import com.wanmi.sbc.setting.bean.vo.ConfigVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 增专资质配置
 */
@ApiModel
@Data
public class InvoiceConfigGetResponse extends ConfigVO {
    private static final long serialVersionUID = 5369660470433442712L;
}
