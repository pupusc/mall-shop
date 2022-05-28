package com.wanmi.sbc.bookmeta.vo;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;

/**
 * 数据字典(MetaDataDict)实体类
 *
 * @author Liang Jun
 * @since 2022-05-24 00:37:02
 */
@Data
public class MetaDataDictQueryByIdResVO implements Serializable {
    private static final long serialVersionUID = -42676091951672561L;
    
    private Integer id;
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
     * 业务名
     */
    private String name;
    /**
     * 分组：
book_cate=书籍分类；
book_language=书籍语言；
figure_cate=人物分类；
book_group_cate=书组分类；
book_bind=书籍装帧；
book_paper=书籍纸张；
figure_country=人物国籍；
     */
    private String cate;
    /**
     * 业务值
     */
    private String value;
}

