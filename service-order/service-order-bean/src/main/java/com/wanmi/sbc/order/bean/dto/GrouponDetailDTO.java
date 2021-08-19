package com.wanmi.sbc.order.bean.dto;

import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GrouponGoodsInfoVO;
import com.wanmi.sbc.marketing.bean.enums.GrouponDetailOptType;
import com.wanmi.sbc.marketing.bean.vo.GrouponActivityVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: lq
 * @CreateTime:2019-05-18 14:32
 * @Description:todo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrouponDetailDTO {
    /**
     * 会员ID
     */
    @ApiModelProperty(value = "会员ID")
    private String customerId;


    /**
     * sku编号
     */
    @ApiModelProperty(value = "sku编号")
    private String goodsInfoId;


    /**
     * spu编号
     */
    @ApiModelProperty(value = "spu编号")
    private String goodsId;

    /**
     * skus编号
     */
    @ApiModelProperty(value = "sku编号")
    private String goodsInfoIds;

    /**
     * 是否团长
     */
    @ApiModelProperty(value = "是：开团 否：参团")
    private Boolean leader;


    /**
     * 团号
     */
    @ApiModelProperty(value = "团号")
    private String grouponNo;

    /**
     * 业务入口
     */
    private GrouponDetailOptType optType;



    /**
     * 团活动
     */
    private GrouponActivityVO grouponActivity;


    @ApiModelProperty(value = "商品信息")
    private List<GoodsInfoVO> goodsInfoList;


    @ApiModelProperty(value = "拼团商品信息")
    private List<GrouponGoodsInfoVO> grouponGoodsInfoVOList;


}
