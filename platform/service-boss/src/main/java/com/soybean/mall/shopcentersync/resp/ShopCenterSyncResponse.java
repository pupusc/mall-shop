package com.soybean.mall.shopcentersync.resp;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShopCenterSyncResponse<T> implements Serializable {
	private final static String SUCCESS_CODE = "0000";

	/**
	 * 响应码
	 */
	private String status;
	/**
	 * 响应消息
	 */
	private String msg;
	/**
	 * 响应数据
	 */
	private T data;

	public static<T> ShopCenterSyncResponse<T> success(){
		ShopCenterSyncResponse<T> response = new ShopCenterSyncResponse<>();
		response.setStatus(SUCCESS_CODE);
		return response;
	}

	public static<T> ShopCenterSyncResponse<T> success(T data){
		ShopCenterSyncResponse<T> response = new ShopCenterSyncResponse<>();
		response.setStatus(SUCCESS_CODE);
		response.setData(data);
		return response;
	}
}
