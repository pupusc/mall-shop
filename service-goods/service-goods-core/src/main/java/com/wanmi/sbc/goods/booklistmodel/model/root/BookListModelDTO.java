package com.wanmi.sbc.goods.booklistmodel.model.root;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/8/31 7:56 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
@Entity
@Table(name = "t_book_list_model")
public class BookListModelDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 模版名
     */
    @Column(name = "name")
    private String name;

    /**
     * 名家名人
     */
    @Column(name = "famous_name")
    private String famousName;

    /**
     * 描述
     */
    @Column(name = "description")
    private String desc;

    /**
     * 书单模板类型 1 排行榜 2 书单 3 编辑推荐 4 专题
     */
    @Column(name = "business_type")
    private Integer businessType;

    /**
     * 头图
     */
    @Column(name = "head_img_url")
    private String headImgUrl;

    /**
     * 头图方图
     */
    @Column(name = "head_square_img_url")
    private String headSquareImgUrl;

    /**
     * 头图跳转地址
     */
    @Column(name = "head_img_href")
    private String headImgHref;

    /**
     *  书单链接地址
     */
    @Column(name = "page_href")
    private String pageHref;

    /**
     * 是否置顶 0否 1 是
     */
    @Column(name = "has_top")
    private Integer hasTop;

    /**
     * 标签类型 1 新上 2 热门 3 自定义
     */
    @Column(name = "tag_type")
    private Integer tagType;

    /**
     * 标签名称
     */
    @Column(name = "tag_name")
    private Integer tagName;

    /**
     * 标签有效结束时间
     */
    @Column(name = "tag_valid_end_time")
    private LocalDateTime tagValidEndTime;

    /**
     * 标签有效开始时间
     */
    @Column(name = "tag_valid_begin_time")
    private LocalDateTime tagValidBeginTime;

    /**
     * 发布状态 0 草稿 1 已编辑未发布 2 已发布
     */
    @Column(name = "publish_state")
    private Integer publishState;

    /**
     * 版本号
     */
    @Column(name = "version")
    @Version
    private Integer version;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 已删除：1，未删除：0
     */
    @Column(name = "del_flag")
    private Integer delFlag;

}
