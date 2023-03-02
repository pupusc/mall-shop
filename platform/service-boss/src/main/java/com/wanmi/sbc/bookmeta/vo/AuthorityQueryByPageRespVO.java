package com.wanmi.sbc.bookmeta.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/01/14:54
 * @Description:
 */
@Data
public class AuthorityQueryByPageRespVO  implements Serializable {
    private static final long serialVersionUID = -93577520761453533L;
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
