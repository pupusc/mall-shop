package com.wanmi.sbc.order.bean.dto;

import com.wanmi.sbc.marketing.bean.dto.TradeMarketingDTO;
import com.wanmi.sbc.order.bean.vo.CycleBuyInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>按商家，店铺分组的订单商品快照</p>
 * Created by of628-wenzhi on 2017-11-23-下午2:46.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class TradeItemGroupDTO {

    /**
     * 订单商品sku
     */
    @ApiModelProperty(value = "订单商品sku")
    private List<TradeItemDTO> tradeItems;

    /**
     * 商家与店铺信息
     */
    @ApiModelProperty(value = "商家与店铺信息")
    private SupplierDTO supplier;

    /**
     * 订单营销信息
     */
    @ApiModelProperty(value = "订单营销信息")
    private List<TradeMarketingDTO> tradeMarketingList;

    /**
     * 快照类型--秒杀活动抢购商品订单快照："FLASH_SALE"
     */
    private String snapshotType;

    /**
     * 周期购信息
     */
    @ApiModelProperty(value = "周期购信息")
    private CycleBuyInfoDTO cycleBuyInfo;

}
