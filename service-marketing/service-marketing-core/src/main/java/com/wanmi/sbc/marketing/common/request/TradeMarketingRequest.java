package com.wanmi.sbc.marketing.common.request;

import com.wanmi.sbc.common.base.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>订单相关营销信息请求Bean</p>
 * Created by of628-wenzhi on 2018-02-27-下午4:30.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeMarketingRequest extends BaseRequest{
    private static final long serialVersionUID = -4198768506845115947L;

    /**
     * 营销活动Id
     */
    private Long marketingId;

    /**
     * 营销等级id
     */
    private Long marketingLevelId;

    /**
     * 该营销活动关联的订单商品id集合
     */
    private List<String> skuIds;

    /**
     * 如果是满赠，则填入用户选择的赠品id集合
     */
    @Builder.Default
    private List<String> giftSkuIds = new ArrayList<>();
    /**
     * 如果是加价购，则填入用户选择的换购商品id集合
     */
    @Builder.Default
    private  List<String> markupSkuIds = new ArrayList<>();

    public List<String> getGiftSkuIds() {
        if(giftSkuIds == null) {
            return new ArrayList<>();
        }
        return giftSkuIds;
    }
}
