package com.wanmi.sbc.setting.api.response.storewechatminiprogramconfig;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.setting.bean.vo.StoreWechatMiniProgramConfigVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>门店微信小程序配置分页结果</p>
 * @author tangLian
 * @date 2020-01-16 11:47:15
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreWechatMiniProgramConfigPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 门店微信小程序配置分页结果
     */
    @ApiModelProperty(value = "门店微信小程序配置分页结果")
    private MicroServicePage<StoreWechatMiniProgramConfigVO> storeWechatMiniProgramConfigVOPage;
}
