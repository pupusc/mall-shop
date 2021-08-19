package com.wanmi.sbc.setting.api.response.syssms;

import com.wanmi.sbc.setting.bean.vo.SysSmsVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>系统短信配置列表结果</p>
 * @author lq
 * @date 2019-11-05 16:13:47
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysSmsListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 系统短信配置列表结果
     */
    @ApiModelProperty(value = "系统短信配置列表结果")
    private List<SysSmsVO> sysSmsVOList;

    @ApiModelProperty(value = "系统短信配置列表结果--前台需要的格式")
    private List<SmsSupplierRopResponse> smsSupplierRopResponses;
}
