package com.wanmi.sbc.bookmeta.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/01/16:07
 * @Description:
 */
@Data
@Table(name = "authority")
public class Authority  implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "authority_id")
    private String authorityId;
    @Column(name = "system_type_cd")
    private int systemTypeCd;
    @Column(name = "function_id")
    private String functionId;
    @Column(name = "authority_title")
    private String authorityTitle;
    @Column(name = "authority_name")
    private String authorityName;
    @Column(name = "authority_url")
    private String authorityUrl;
    @Column(name = "request_type")
    private String requestType;
    @Column(name = "remark")
    private String remark;
    @Column(name = "sort")
    private int sort;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "del_flag")
    private int delFlag;
}
