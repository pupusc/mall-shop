package com.wanmi.sbc.goods.api.response.goodscatethirdcaterel;

import com.wanmi.sbc.goods.bean.vo.GoodsCateThirdCateRelVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>平台类目和第三方平台类目映射新增结果</p>
 * @author 
 * @date 2020-08-18 19:51:55
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsCateThirdCateRelAddResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    ArrayList<String> updateEsGoodsIds;
    ArrayList<String> delEsGoodsIds;
}
