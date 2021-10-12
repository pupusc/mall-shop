package com.wanmi.sbc.index.requst;


import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class KeyRequest implements Serializable {

    private static final long serialVersionUID = 3228778527828317959L;

    /**
     * key
     */
    private List<String> keys;



}
