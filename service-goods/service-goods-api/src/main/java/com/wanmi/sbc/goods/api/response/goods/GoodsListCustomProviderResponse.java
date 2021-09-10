package com.wanmi.sbc.goods.api.response.goods;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/10 3:56 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class GoodsListCustomProviderResponse {

    private String goodsId;

    private String goodsNo;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品副标题
     */
    private String goodsSubName;

    /**
     * 上架状态 0 未上架  1 已经上架 2 部分上架 {@link com.wanmi.sbc.goods.bean.enums.AddedFlag}
     */
//    private Integer addedFlag;
    private Integer forSaleStatus;

//    /**
//     * 上架时间
//     */
//    private LocalDateTime forSaleTime;

    /**
     * 0：待审核 1：已审核 2：审核失败 3：禁售中 {@link com.wanmi.sbc.goods.bean.enums.CheckStatus}
     */
//    private Integer checkStatus;
    private Integer goodsStatus;

    /**
     * 积分
     */
    private Integer buyPoint;

    /**
     * 知识顾问专享 0:不是 ，1：是
     */
    private Integer cpsSpecial;

    /**
     * 商品创建时间
     */
    private LocalDateTime createTime;

//    /**
//     * 禁售状态,0:未禁售 1:禁售中
//     */
//    private Integer forbidStatus;

    /**
     * 排序的价格
     */
    private BigDecimal esSortPrice;

    /**
     * 划线价格
     */
    private BigDecimal linePrice;

}
