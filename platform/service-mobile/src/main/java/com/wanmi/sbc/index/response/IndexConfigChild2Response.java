package com.wanmi.sbc.index.response;

import lombok.Data;

import java.io.Serializable;


@Data
public class IndexConfigChild2Response extends IndexConfigChild1Response implements Serializable {
    private static final long serialVersionUID = 4826417031104953905L;
    /**
     * 标题
     */
    private String title;


}