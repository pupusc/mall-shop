package com.wanmi.sbc.goods.api.response.restrictedrecord;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.bean.vo.RestrictedRecordVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>限售分页结果</p>
 * @author 限售记录
 * @date 2020-04-11 15:59:01
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestrictedRecordPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 限售分页结果
     */
    @ApiModelProperty(value = "限售分页结果")
    private MicroServicePage<RestrictedRecordVO> restrictedRecordVOPage;
}
