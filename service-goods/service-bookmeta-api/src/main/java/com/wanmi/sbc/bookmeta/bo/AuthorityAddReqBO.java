package com.wanmi.sbc.bookmeta.bo;

import lombok.Data;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/02/14:09
 * @Description:
 */
@Data
public class AuthorityAddReqBO {
    private String authorityId;
    private int systemTypeCd;
    private String functionId;
    private String authorityTitle;
    private String authorityName;
    private String authorityUrl;
    private String requestType;
    private String remark;
    private int sort;
    private Date createTime;
    private int delFlag;
}

