package com.wanmi.sbc.bookmeta.vo;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/02/14:05
 * @Description:
 */
@Data
public class AuthorityAddReqVO {

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
