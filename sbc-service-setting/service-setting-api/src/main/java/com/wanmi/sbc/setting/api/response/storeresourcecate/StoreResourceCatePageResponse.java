package com.wanmi.sbc.setting.api.response.storeresourcecate;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.setting.bean.vo.StoreResourceCateVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>店铺资源资源分类表分页结果</p>
 * @author lq
 * @date 2019-11-05 16:13:19
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreResourceCatePageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 店铺资源资源分类表分页结果
     */
    @ApiModelProperty(value = "店铺资源资源分类表分页结果")
    private MicroServicePage<StoreResourceCateVO> storeResourceCateVOPage;
}
