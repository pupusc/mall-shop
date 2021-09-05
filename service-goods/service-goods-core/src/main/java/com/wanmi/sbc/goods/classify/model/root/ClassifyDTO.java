package com.wanmi.sbc.goods.classify.model.root;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/5 8:15 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
@Entity
@Table(name = "t_classify")
public class ClassifyDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 父订单
     */
    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 分类名
     */
    @Column(name = "classify_name")
    private String classifyName;

    /**
     *  层级
     */
    @Column(name = "level")
    private Integer level;

    /**
     * 顺序
     */
    @Column(name = "order_name")
    private Integer orderNum;

    /**
     * 广告图
     */
    @Column(name = "ad_img_url")
    private String adImgUrl;

    /**
     * 广告图跳转
     */
    @Column(name = "ad_img_href")
    private String adImgHref;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "del_flag")
    private Integer delFlag;
}
