package com.wanmi.sbc.bookmeta.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 数据字典(MetaDataDict)实体类
 *
 * @author Liang Jun
 * @since 2022-05-24 00:37:02
 */
@Data
public class MetaDataDictQueryByPageResVO implements Serializable {
    private static final long serialVersionUID = -57674373236498068L;
    
    private Integer id;
    /**
     * 业务名
     */
    private String name;
    /**
     * 业务值
     */
    private String value;
}

