//package com.wanmi.sbc.goods.booklistmodelclick.model.root;
//
//import lombok.Data;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.Table;
//import javax.persistence.Version;
//import java.util.Date;
//
///**
// * Description:
// * Company    : 上海黄豆网络科技有限公司
// * Author     : duanlongshan@dushu365.com
// * Date       : 2021/9/7 8:54 下午
// * Modify     : 修改日期          修改人员        修改说明          JIRA编号
// ********************************************************************/
//@Data
//@Entity
//@Table(name = "t_book_list_model_click")
//public class BookListModelClickDTO {
//
//    @Id
//    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//
//    /**
//     * 书单id
//     */
//    @Column(name = "book_list_model_id")
//    private Integer bookListModelId;
//
//    /**
//     * 点击数量
//     */
//    @Column(name = "click_count")
//    private Integer clickCount;
//
//    /**
//     * 书单模板类型 1 排行榜 2 书单 3 编辑推荐 4 专题
//     */
//    @Column(name = "business_type")
//    private Integer businessType;
//
//    /**
//     * 版本
//     */
//    @Version
//    private Integer version;
//
//    /**
//     * 创建时间
//     */
//    @Column(name = "create_time")
//    private Date createTime;
//
//    /**
//     * 更新时间
//     */
//    @Column(name = "update_time")
//    private Date updateTime;
//
//    /**
//     * 已删除：1，未删除：0
//     */
//    @Column(name = "del_flag")
//    private Integer delFlag;
//}
