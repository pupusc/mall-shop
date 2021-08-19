package com.wanmi.sbc.setting.api.response.storewechatminiprogramconfig;

import com.wanmi.sbc.setting.bean.vo.StoreWechatMiniProgramConfigVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>根据id查询任意（包含已删除）门店微信小程序配置信息response</p>
 * @author tangLian
 * @date 2020-01-16 11:47:15
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreWechatMiniProgramConfigByCacheResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 门店微信小程序配置信息
     */
    @ApiModelProperty(value = "门店微信小程序配置信息")
    private StoreWechatMiniProgramConfigVO storeWechatMiniProgramConfigVO;
}
