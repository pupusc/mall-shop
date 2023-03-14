package com.wanmi.sbc.goods.booklistgoods.model.root;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Date;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/2 1:49 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
@Entity
@Table(name = "t_book_list_goods")
public class BookListGoodsDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 控件id
     */
    @Column(name = "choose_rule_id")
    private Integer chooseRuleId;

    /**
     * 书单模板id或者书单类目id
     */
    @Column(name = "book_list_id")
    private Integer bookListId;

    /**
     * 分类 1书单模板 2类目
     */
    @Column(name = "category")
    private Integer category;

    /**
     *
     */
    @Column(name = "spu_id")
    private String spuId;

    @Column(name = "spu_no")
    private String spuNo;

    @Column(name = "sku_id")
    private String skuId;

    @Column(name = "sku_no")
    private String skuNo;

    @Column(name = "erp_goods_no")
    private String erpGoodsNo;

    @Column(name = "erp_goods_info_no")
    private String erpGoodsInfoNo;

    @Column(name = "order_num")
    private Integer orderNum;

    @Version
    @Column(name = "version")
    private Integer version;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "del_flag")
    private Integer delFlag;

    @Column(name = "sale_num")
    private Integer saleNum;

    @Column(name = "rank_text")
    private String rankText;

//    /**
//     * 控件id
//     */
//    private Integer chooseRuleId;
//
//    /**
//     * 书单模板id或者书单类目id
//     */
//    private Integer bookListId;
//
//    /**
//     * 分类 1书单模板 2类目
//     */
//    private Integer category;
//
//    private String spuId;
//
//    private String spuNo;
//
//    private String skuId;
//
//    private String skuNo;
//
//    private Integer orderNum;
}
