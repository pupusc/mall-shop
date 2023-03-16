package com.wanmi.sbc.bookmeta.vo;

import com.wanmi.sbc.common.base.Page;
import com.wanmi.sbc.pay.bean.enums.PayGatewayEnum;
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
    private int labelId;
    private String goodsName;

}
