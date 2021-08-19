package com.wanmi.sbc.setting.api.response.thirdplatformconfig;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.setting.bean.vo.StoreResourceVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>第三方平台配置结果</p>
 * @author lq
 * @date 2019-11-05 16:12:49
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThirdPlatformConfigResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 客户业务id
     */
    @ApiModelProperty(value="客户业务id 当configType=‘third_platform_linked_mall’时，才有")
    private String customerBizId;

    /**
     * 状态 0:未启用1:已启用
     */
    @ApiModelProperty(value="0:未启用1:已启用")
    private Integer status;


    /**
     * 第三方平台类型
     */
    @ApiModelProperty(value="0:未启用1:已启用")
    private ThirdPlatformType thirdPlatformType;
}
