package com.wanmi.sbc.goods.api.response.chooserulegoodslist;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/5 1:31 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class BookListGoodsProviderResponse implements Serializable {

    /**
     * bookListGoods
     */
    private Integer id;

    /**
     * 控件id
     */
    private Integer chooseRuleId;

    /**
     * 书单模板id或者书单类目id
     */
    private Integer bookListId;

    /**
     * 分类 1书单模板 2类目
     */
    private Integer category;

    /**
     *
     */
    private String spuId;

    private String spuNo;

    private String skuId;

    private String skuNo;

    private String erpGoodsNo;

    private String erpGoodsInfoNo;

//    /**
//     * 商品名称
//     */
//    private String goodsInfoName;
//
//    /**
//     * 规格
//     */
//    private String specText;
//
//    /**
//     * 定价
//     */
//    private BigDecimal marketPrice;
//
//    /**
//     * 积分
//     */
//    private Integer buyPoint;

    private Date createTime;

    private Date updateTime;

    private Integer saleNum;

    private String rankText;

}
