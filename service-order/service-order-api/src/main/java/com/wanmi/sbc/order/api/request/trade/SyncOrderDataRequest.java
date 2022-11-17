package com.wanmi.sbc.order.api.request.trade;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SyncOrderDataRequest implements Serializable {

	private static final long serialVersionUID = -8476657009750522587L;
	
	private String id;

	private String command;

	private Date bgnTime;

	private Date endTime;
}
