package com.wanmi.sbc.customer.api.response.store;

import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.customer.bean.vo.MiniCompanyInfoVO;
import com.wanmi.sbc.customer.bean.vo.MiniStoreVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>根据companyIds查询未删除店铺列表response</p>
 * Created by of628-wenzhi on 2018-09-12-下午5:23.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ListCompanyStoreByCompanyIdsResponse implements Serializable{

    private static final long serialVersionUID = -4895118700252748117L;

    /**
     * 店铺列表
     */
    @ApiModelProperty(value = "店铺列表")
    private List<MiniStoreVO> storeVOList;

    /**
     * 公司列表
     */
    @ApiModelProperty(value = "公司列表")
    private List<MiniCompanyInfoVO> companyInfoVOList;
}
