package com.wanmi.sbc.customer.api.response.store;

import com.wanmi.sbc.customer.bean.vo.StoreSimpleInfo;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author : baijz
 * @Date : 2019/3/1 11 35
 * @Description : 门店信息——分销记录使用
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreListForDistributionResponse implements Serializable {

    private static final long serialVersionUID = -8576096693233067335L;

    /**
     * 简单门店信息——分销记录使用
     */
    private List<StoreSimpleInfo>  storeSimpleInfos;
}
