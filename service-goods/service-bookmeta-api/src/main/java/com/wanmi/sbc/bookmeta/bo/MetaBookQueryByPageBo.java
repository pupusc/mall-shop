package com.wanmi.sbc.bookmeta.bo;

import com.wanmi.sbc.common.base.Page;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Data
public class MetaBookQueryByPageBo implements Serializable {

    private static final long serialVersionUID = 6425886876018208788L;

    private String queryBookName;

    private String queryLabelName;

    private Integer pageNum;

    private Integer pageSize;

    private Integer totalCount;

    private Page page = new Page(0,10);

    private List<MetaBookQueryByPageBo.MetaBookQueryByPageVo> metaBookQueryByPageVos=new ArrayList<>();

    @Data
    public static class MetaBookQueryByPageVo {
        /**
         * 书籍id
         */
        private Integer bookId;
        /**
         * 目录id
         */
        private Integer labelId;


        /**
         * 书名
         */
        private String bookName;
        /**
         * 标签名
         */
        private String labelName;
    }
}
