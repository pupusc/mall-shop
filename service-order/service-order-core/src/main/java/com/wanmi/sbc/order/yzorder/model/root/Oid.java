package com.wanmi.sbc.order.yzorder.model.root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Oid implements Serializable {

    private static final long serialVersionUID = 4237518588491447330L;

    /**
     * 交易明细id
     */
    private String oid;
}
