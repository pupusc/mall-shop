package com.wanmi.sbc.marketing.common;

import lombok.Data;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Map;

/**
 * 实体类基类
 * @author Administrator
 */
@Data
@MappedSuperclass
public class BaseBean implements Serializable {

    private static final long serialVersionUID = -4574239004317820550L;
    /**
     * 扩展属性
     */
    @Transient
    protected Map<String,Object> others;

}
