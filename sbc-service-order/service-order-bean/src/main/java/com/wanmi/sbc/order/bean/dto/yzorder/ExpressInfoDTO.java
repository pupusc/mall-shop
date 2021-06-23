package com.wanmi.sbc.order.bean.dto.yzorder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 包裹详情
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpressInfoDTO implements Serializable {

    private static final long serialVersionUID = 7304745612473444283L;

    /**
     * 快递单号
     */
    private String express_no;

    /**
     * 物流公司编号
     */
    private Integer express_id;
}
