package com.wanmi.sbc.bookmeta.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wanmi.sbc.bookmeta.bo.MetaBookRelationBookAddBo;
import com.wanmi.sbc.bookmeta.bo.MetaBookRelationKeyAddBo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/06/17:59
 * @Description:
 */
@Data
public class MetaBookRelationAddReqVO implements Serializable {
    private int id;
    private int bookId;
    private String name;
    private String subName;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT-8")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT-8")
    private Date endTime;
    private int orderNum;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT-8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT-8")
    private Date updateTime;
    private int delFlag;
    private List<MetaBookRelationBookAddBo> metaBookRelationBook;
    private List<MetaBookRelationKeyAddBo> metaBookRelationKeyAddBo;
}
