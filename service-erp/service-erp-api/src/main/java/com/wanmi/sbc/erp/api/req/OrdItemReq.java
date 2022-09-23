package com.wanmi.sbc.erp.api.req;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrdItemReq implements Serializable {

	private String platformOrderId;

	private String platformItemId;

	private String platformSkuId;
}
