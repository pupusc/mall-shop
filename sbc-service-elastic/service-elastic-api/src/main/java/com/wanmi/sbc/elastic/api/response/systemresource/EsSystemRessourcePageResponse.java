package com.wanmi.sbc.elastic.api.response.systemresource;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.elastic.bean.vo.systemresource.EsSystemResourceVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author houshuai
 * @date 2020/12/14 10:21
 * @description <p> </p>
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ApiModel
public class EsSystemRessourcePageResponse implements Serializable {

    private static final long serialVersionUID = 1993093789359036003L;
    /**
     * 平台素材资源分页结果
     */
    @ApiModelProperty(value = "平台素材资源分页结果")
    private MicroServicePage<EsSystemResourceVO> systemResourceVOPage;

}
