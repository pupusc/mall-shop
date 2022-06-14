package com.wanmi.sbc.goods.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Description: 采集商品对象
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/14 12:51 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class CollectSpuVO {

    /**
     * 自增id
     */
    private Integer tmpId;

    /**
     * 商品id
     */
    private String goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品副标题
     */
    private String goodsSubtitle;

    /**
     * 商品渠道
     */
    private List<String> goodsChannelTypes;

    /**
     * 主播推荐
     */
    private List<String> anchorPushs;

    /**
     * 无背景图
     */
    private String goodsUnBackImg;

    /**
     * 最小价格图
     */
    private String minSalePriceImg;

    /**
     * 商品销量
     */
    private Long goodsSalesNum;

    /**
     * 商品最小售价
     */
    private BigDecimal miniSalesPrice;

    /**
     * 知识顾问专享 0：不是，1：是
     */
    private Integer cpsSpecial = 0;

    /**
     * 上下架状态
     */
    private Integer addedFlag;

    /**
     * 上下架时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime addedTime;

    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     * 删除标记
     */
    private DeleteFlag delFlag;

    /**
     * 审核状态
     */
    private CheckStatus auditStatus;

}
