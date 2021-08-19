package com.wanmi.sbc.order.bean.dto.yzorder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OidDTO implements Serializable {

    private static final long serialVersionUID = 4237518588491447330L;

    /**
     * 交易明细id
     */
    private String oid;
}
