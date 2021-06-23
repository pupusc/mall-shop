package com.wanmi.sbc.vas.api.response.iepsetting;

import com.wanmi.sbc.vas.bean.vo.IepSettingVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>查询第一个）企业购设置信息response</p>
 * @author 宋汉林
 * @date 2020-03-02 20:15:04
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IepSettingTopOneResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 企业购设置信息
     */
    @ApiModelProperty(value = "企业购设置信息")
    private IepSettingVO iepSettingVO;
}
