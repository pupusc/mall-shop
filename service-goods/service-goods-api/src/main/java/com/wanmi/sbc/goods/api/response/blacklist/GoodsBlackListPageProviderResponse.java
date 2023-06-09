package com.wanmi.sbc.goods.api.response.blacklist;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/11/21 1:35 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class GoodsBlackListPageProviderResponse implements Serializable {

    /**
     * 新书 黑名单
     */
    private BlackListCategoryProviderResponse newBooksBlackListModel;

    /**
     * 畅销书 黑名单
     */
    private BlackListCategoryProviderResponse sellWellBooksBlackListModel;

    /**
     * 特价书榜 黑名单
     */
    private BlackListCategoryProviderResponse specialOfferBooksBlackListModel;

    /**
     * 会员商品价格 黑名单
     */
    private BlackListCategoryProviderResponse unVipPriceBlackListModel;

    /**
     * 不能使用积分商品 黑名单
     */
    private BlackListCategoryProviderResponse pointNotSplitBlackListModel;

    /*
     * 虚拟库存码 黑名单
     */
    private BlackListCategoryProviderResponse wareHouseListModel;

    /*
     * 底部分类 黑名单
     */
    private BlackListCategoryProviderResponse classifyAtBottomBlackListModel;

    /*
     * 首页商品搜索H5和领阅不展示 黑名单
     */
    private BlackListCategoryProviderResponse goodsSearchAtIndexBlackListModel;

    /*
     * 首页商品搜索H5不展示 黑名单
     */
    private BlackListCategoryProviderResponse goodsSearchH5AtIndexBlackListModel;

    /*
     * 下单不使用优惠券
     */
    private BlackListCategoryProviderResponse unUseCouponBlackListModel;


    /*
     * 下单不返还积分客户
     */
    private BlackListCategoryProviderResponse unBackPointAfterPayBlackListModel;
}
