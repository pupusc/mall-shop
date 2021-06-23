package com.wanmi.sbc.elastic.api.response.storeInformation;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.elastic.bean.vo.storeInformation.EsCompanyInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

/**
 * @Author yangzhen
 * @Description // 商家结算账户查分页询
 * @Date 14:42 2020/12/9
 * @Param
 * @return
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class EsCompanyInfoResponse {

    /**
     * 索引SKU
     */
    @ApiModelProperty(value = "索引SKU")
    private MicroServicePage<EsCompanyInfoVO> esCompanyAccountPage = new MicroServicePage<>(new ArrayList<>());

}
