package com.wanmi.sbc.crm.autotagstatistics;

import com.wanmi.sbc.crm.bean.enums.DimensionName;
import com.wanmi.sbc.crm.bean.enums.RelationType;
import com.wanmi.sbc.crm.bean.enums.TagDimensionFirstLastType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName statisticsDimensionInfo
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2020/3/17 10:21
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsDimensionInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标签维度名称
     * 访问 - access
     * 加购 - addCar
     * 收藏 - followGoods
     * 下单 - addOrder
     * 付款 - payOrder
     * 申请退单 - returnOrder
     * 评价商品 - goodsEvaluate
     * 评价店铺 - storeEvaluate
     * 关注店铺 - followStore
     * 分享商品 - shareGoods
     * 分享商城 - shareS2b
     * 分享店铺 - shareStore
     * 邀请好友 - invite
     * 签到 - signIn
     */
    private DimensionName dimensionName;

    /**
     *  首末次选项，0:非首末，1:首次，2：末次
     */
    private TagDimensionFirstLastType dimensionType;

    /**
     * 指标条件关联关系，且或关系，0：且，1：或
     */
    private RelationType relationType;

    /**
     * 指标结果数据，即分组的条件,
     * 如果是指标值标签，只有name，没有value值；
     * 如果是指标值范围标签则value有对应的值；
     * 如果是总和标签，paramResult为空；
     * 如果是偏好标签，value也有对应的值；
     */
    private StatisticsTagParamInfo paramResult;

    /**
     * 指标条件集合list
     */
    private List<StatisticsTagParamInfo> paramInfoList;

}
