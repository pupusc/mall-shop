package com.soybean.mall.shopcentersync.req;

import lombok.Data;

import java.io.Serializable;

@Data
public class SyncDataReq implements Serializable {

	/**
	 * 1001=库存变动，1004=成本价变动
	 */
	private Integer tag;

	private String requestId;
	/**
	 * 内容content
	 */
	private String data;
}
