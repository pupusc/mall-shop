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
    private int id;
    @Column(name = "default_channel")
    private int defaultChannel;
    @Column(name = "default_search_keyword")
    private String defaultSearchKeyword;
    @Column(name = "related_landing_page")
    private String relatedLandingPage;
    @Column(name = "parent_id")
    private int parentId;
    @Column(name = "is_parent")
    private int isParent;
    @Column(name = "img_before")
    private String imgBefore;
    @Column(name = "img_after")
    private String imgAfter;
    @Column(name = "sort_number")
    private int sortNumber;
    @Column(name = "del_flag")
    private int delFlag;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "create_person")
    private String createPerson;
    @Column(name = "update_time")
    private Date updateTime;
    @Column(name = "update_person")
    private String updatePerson;

}
