package com.wanmi.sbc.goods.bean.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * com.wanmi.sbc.goods.api.response.vo.GoodsCateListVO
 *
 * @author lipeng
 * @dateTime 2018/11/1 下午4:26
 */
@ApiModel
@Data
public class GoodsCateShenceBurialSiteVO implements Serializable {

    private static final long serialVersionUID = -1317380022650889726L;
    /**
     * 一级分类
     */
    private GoodsCateVO oneLevelGoodsCate;

    /**
     * 二级分类
     */
    private GoodsCateVO secondLevelGoodsCate;

    /**
     * 三级分类
     */
    private GoodsCateVO threeLevelGoodsCate;
}
