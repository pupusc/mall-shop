package com.wanmi.sbc.goods.chooserule.model.root;

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
 * Date       : 2021/9/1 8:03 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
@Table(name = "t_choose_rule")
@Entity
public class ChooseRuleDTO {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     *  书单模板id或者书单类目id
     */
    @Column(name = "book_list_id")
    private Integer bookListId;

    /**
     * 过滤规则 1 无库存展示 2 无库存不展示 3 无库存沉底
     */
    @Column(name = "filter_rule")
    private Integer filterRule;

    /**
     * 类 1书单模板 2类目
     */
    @Column(name = "category")
    private Integer category;

    /**
     * 类型 1按条件 2 按sql 3 制定商品 4 书单
     */
    @Column(name = "choose_type")
    private Integer chooseType;

    /**
     * 类型 内容 多个以 , 分割
     */
    @Column(name = "choose_condition")
    private String chooseCondition;

    @Version
    private Integer version;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "del_flag")
    private Integer delFlag;



//    private Integer id;
//
//    /**
//     *  书单模板id或者书单类目id
//     */
//    private Integer bookListId;
//
//    /**
//     * 过滤规则 1 无库存展示 2 无库存不展示 3 无库存沉底
//     */
//    private Integer filterRule;
//
//    /**
//     * 类 1书单模板 2类目
//     */
//    private Integer category;
//
//    /**
//     * 类型 1按条件 2 按sql 3 制定商品 4 书单
//     */
//    private Integer chooseType;
//
//    /**
//     * 类型 内容 多个以 , 分割
//     */
//    private String chooseCondition;
//
//    private Integer version;
//
//    private Date createTime;
//
//    private Date updateTime;
//
//    private Integer delFlag;

}
