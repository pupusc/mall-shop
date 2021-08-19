package com.wanmi.sbc.goods.api.response.thirdgoodscate;

import com.wanmi.sbc.goods.bean.vo.ThirdGoodsCateVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>第三方平台类目修改结果</p>
 * @author 
 * @date 2020-08-29 13:35:42
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThirdGoodsCateModifyResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已修改的第三方平台类目信息
     */
    @ApiModelProperty(value = "已修改的第三方平台类目信息")
    private ThirdGoodsCateVO thirdGoodsCateVO;
}
