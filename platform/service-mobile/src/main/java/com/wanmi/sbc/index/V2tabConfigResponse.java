package com.wanmi.sbc.index;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class V2tabConfigResponse implements Serializable {

    private static final long serialVersionUID = 9141263828409602401L;


    private String id;

    private String name;

    private String mall_name;

    private String paramsId;

    private String price;
    private String link;
}