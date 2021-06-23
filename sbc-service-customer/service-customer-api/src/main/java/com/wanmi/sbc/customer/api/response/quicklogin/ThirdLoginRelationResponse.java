package com.wanmi.sbc.customer.api.response.quicklogin;

import com.wanmi.sbc.customer.bean.vo.ThirdLoginRelationVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 第三方关系响应
 * Created by sunkun on 2017/12/4.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ThirdLoginRelationResponse implements Serializable {

    private static final long serialVersionUID = -499117397630725286L;
    /**
     * 第三方关系
     */
    @ApiModelProperty(value = "第三方关系")
    private ThirdLoginRelationVO thirdLoginRelation;


}
