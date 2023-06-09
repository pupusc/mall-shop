package com.wanmi.sbc.bookmeta.entity;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/16/13:52
 * @Description:
 */
@Data
public class SearchTerm implements Serializable {

    @Column(name = "id")
    private Integer id;
    @Column(name = "default_channel")
    private Integer defaultChannel;
    @Column(name = "default_search_keyword")
    private String defaultSearchKeyword;
    @Column(name = "related_landing_page")
    private String relatedLandingPage;
    @Column(name = "parent_id")
    private Integer parentId;
    @Column(name = "is_parent")
    private Integer isParent;
    @Column(name = "img_before")
    private String imgBefore;
    @Column(name = "img_after")
    private String imgAfter;
    @Column(name = "sort_number")
    private Integer sortNumber;
    @Column(name = "del_flag")
    private Integer delFlag;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "create_person")
    private String createPerson;
    @Column(name = "update_time")
    private Date updateTime;
    @Column(name = "update_person")
    private String updatePerson;

}
