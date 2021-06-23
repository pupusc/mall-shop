package com.wanmi.sbc.marketing.api.response.distribution;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.bean.enums.RecruitApplyType;
import com.wanmi.sbc.marketing.bean.vo.DistributionStoreSettingVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>查询店铺分销设置响应</p>
 * @author gaomuwei
 * @date 2019-02-19 10:08:02
 */
@ApiModel
@Data
public class DistributionSetting4StoreBagsResponse {

    /**
     * 是否开启社交分销 0：关闭，1：开启
     */
    @ApiModelProperty(value = "是否开启社交分销")
    private DefaultFlag openFlag;
    /**
     * 是否开启申请入口 0：关闭，1：开启
     */
    @ApiModelProperty(value = "是否开启申请入口")
    private DefaultFlag applyFlag;
    /**
     * 申请条件 0：购买商品，1：邀请注册
     */
    @ApiModelProperty(value = "申请条件")
    private RecruitApplyType applyType;

    /**
     * 招募海报
     */
    @ApiModelProperty(value = "招募海报")
    private String recruitImg;


    /**
     * 招募规则说明
     */
    @ApiModelProperty(value = "招募规则说明")
    private String recruitDesc;

    /**
     * 礼包商品列表
     */
    @ApiModelProperty(value = "礼包商品列表")
    private List<GoodsInfoVO> goodsInfos = new ArrayList<>();

}
