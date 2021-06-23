package com.wanmi.sbc.setting.api.response.expresscompany;

import com.wanmi.sbc.setting.bean.vo.ExpressCompanyVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>物流公司列表结果</p>
 * @author lq
 * @date 2019-11-05 16:10:00
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpressCompanyListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 物流公司列表结果
     */
    @ApiModelProperty(value = "物流公司列表结果")
    private List<ExpressCompanyVO> expressCompanyVOList;
}
