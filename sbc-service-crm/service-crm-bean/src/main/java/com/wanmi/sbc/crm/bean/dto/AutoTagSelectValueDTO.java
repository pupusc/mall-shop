package com.wanmi.sbc.crm.bean.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName AutoTagSelectValueDTO
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2020/8/25 19:33
 **/
@ApiModel
@Data
public class AutoTagSelectValueDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long columnType;

    private List<?> dataSource;

    private Long selectedId;

    private List<?> value;

}
