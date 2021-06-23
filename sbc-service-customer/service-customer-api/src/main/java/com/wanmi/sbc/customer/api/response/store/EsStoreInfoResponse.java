package com.wanmi.sbc.customer.api.response.store;

import com.wanmi.sbc.customer.bean.vo.EsStoreInfoVo;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EsStoreInfoResponse implements Serializable{
    private static final long serialVersionUID = 707232612689625808L;

    /**
     * 分页查询店铺信息
     */
    @ApiModelProperty(value = "分页查询店铺信息")
    private List<EsStoreInfoVo> lists;
}
