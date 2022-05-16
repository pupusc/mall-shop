package com.wanmi.sbc.goods.vo;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;

/**
 * 行业数据文件(MetaBookIndustryFile)实体类
 *
 * @author Liang Jun
 * @since 2022-05-16 11:48:33
 */
@Data
public class MetaBookIndustryFileQueryByIdResVO implements Serializable {
    private static final long serialVersionUID = -58529389428494491L;
    
    private Integer id;
    /**
     * 文件地址
     */
    private String fileUrl;
    /**
     * 状态：1待同步；2同步中；3已同步；4同步失败；
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 删除标识
     */
    private Integer delFlag;
    /**
     * 描述信息，失败原因等
     */
    private String descr;
}

