package com.wanmi.sbc.bookmeta.bo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/16/14:05
 * @Description:
 */
@Data
public class SearchTermBo {

    private int id;
    private int defaultChannel;
    private String defaultSearchKeyword;
    private String relatedLandingPage;
    private int parentId;
    private int isParent;
    private String imgBefore;
    private String imgAfter;
    private int sortNumber;
    private int delFlag;
    private Date createTime;
    private String createPerson;
    private Date updateTime;
    private String updatePerson;
    private List<SearchTermBo> childrenList;
}
