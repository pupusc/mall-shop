package com.wanmi.sbc.bookmeta.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class MetaBookRecommentKeyBo implements Serializable {


    private static final long serialVersionUID = 6904740417636512845L;

    private String name;

    private String subName;

    private List<MetaBookRelationKeyBo> keyName=new ArrayList<>();

    @Data
    public static class MetaBookRelationKeyBo {
        /**
         * 名称
         */
        private String name;
        /**
         * 排序
         */
        private Integer orderNum;

    }
}
