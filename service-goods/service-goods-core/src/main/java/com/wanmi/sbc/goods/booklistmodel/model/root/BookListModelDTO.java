package com.wanmi.sbc.goods.booklistmodel.model.root;

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
