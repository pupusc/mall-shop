package com.wanmi.sbc.setting.api.response.expresscompany;

import com.wanmi.sbc.setting.bean.vo.ExpressCompanyVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）物流公司信息response</p>
 * @author lq
 * @date 2019-11-05 16:10:00
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpressCompanyByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 物流公司信息
     */
    @ApiModelProperty(value = "物流公司信息")
    private ExpressCompanyVO expressCompanyVO;
}
