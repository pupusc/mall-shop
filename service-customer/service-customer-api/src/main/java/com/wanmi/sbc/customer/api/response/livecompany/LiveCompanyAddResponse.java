package com.wanmi.sbc.customer.api.response.livecompany;

import com.wanmi.sbc.customer.bean.vo.LiveCompanyVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>直播商家新增结果</p>
 * @author zwb
 * @date 2020-06-06 18:06:59
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiveCompanyAddResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已新增的直播商家信息
     */
    @ApiModelProperty(value = "已新增的直播商家信息")
    private LiveCompanyVO liveCompanyVO;
}
