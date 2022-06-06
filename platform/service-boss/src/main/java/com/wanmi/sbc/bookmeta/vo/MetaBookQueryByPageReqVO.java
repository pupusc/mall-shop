package com.wanmi.sbc.bookmeta.vo;

import java.util.Date;
import java.io.Serializable;
import com.wanmi.sbc.common.base.Page;
import lombok.Data;

/**
 * 书籍(MetaBook)实体类
 *
 * @author Liang Jun
 * @since 2022-05-16 11:48:33
 */
@Data
public class MetaBookQueryByPageReqVO implements Serializable {
    private static final long serialVersionUID = 760240600516925033L;
    
    private String isbn;
    /**
     * 名称
     */
    private String name;
    /**
     * 作者
     */
    private String authorName;
    /**
     * 出版社
     */
    private String publisherName;
    /**
     * 分页参数
     */
    private Page page = new Page(1, 10);
}

