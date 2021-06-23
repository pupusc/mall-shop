package com.wanmi.sbc.setting.api.response.storeresourcecate;

import com.wanmi.sbc.setting.bean.vo.StoreResourceCateVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>店铺资源资源分类表新增结果</p>
 * @author lq
 * @date 2019-11-05 16:13:19
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreResourceCateAddResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已新增的店铺资源资源分类表信息
     */
    @ApiModelProperty(value = "已新增的店铺资源资源分类表信息")
    private StoreResourceCateVO storeResourceCateVO;
}
