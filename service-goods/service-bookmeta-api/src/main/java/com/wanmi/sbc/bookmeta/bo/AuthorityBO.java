package com.wanmi.sbc.bookmeta.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/01/15:12
 * @Description:
 */
@Data
public class AuthorityBO implements Serializable {

    private String authorityId;
    private int systemTypeCd;
    private String functionId;
    private String authorityTitle;
    private String authorityName;
    private String authorityUrl;
    private String requestType;
    private String remark;
    private int sort;
    private String createTime;
    private int delFlag;
}
