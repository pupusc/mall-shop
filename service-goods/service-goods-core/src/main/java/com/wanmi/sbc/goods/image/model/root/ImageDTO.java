package com.wanmi.sbc.goods.image.model.root;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.time.LocalDateTime;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/20 1:26 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
@Entity
@Table(name = "t_image")
public class ImageDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Integer id;

    /**
     * 名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 图片地址
     */
    @Column(name = "img_url")
    private String imgUrl;

    /**
     * 图片跳转链接
     */
    @Column(name = "img_href")
    private String imgHref;

    /**
     * 开始时间
     */
    @Column(name = "begin_time")
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    @Column(name = "end_time")
    private LocalDateTime endTime;

    /**
     * 启用状态 0未启用 1启用
     */
    @Column(name = "publish_state")
    private Integer publishState;

    /**
     * 排序
     */
    @Column(name = "order_num")
    private Integer orderNum;

    /**
     * 图片类型 1首页轮播
     */
    @Column(name = "image_type")
    private Integer imageType;

    /**
     * 版本
     */
    @Column(name = "version")
    @Version
    private Integer version;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 删除状态 0 正常 1 已删除
     */
    @Column(name = "del_flag")
    private Integer delFlag;
}
