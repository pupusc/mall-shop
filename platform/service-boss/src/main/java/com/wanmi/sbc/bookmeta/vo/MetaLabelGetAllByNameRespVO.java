package com.wanmi.sbc.bookmeta.vo;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/03/18:13
 * @Description:
 */
@Data
public class MetaLabelGetAllByNameRespVO implements Serializable {
    private Integer id;
    private String name;
    private Integer isStatic;
    private String goodsId;
    private String goodsName;
}
