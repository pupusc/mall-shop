package com.wanmi.sbc.order.api.request.trade;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SyncOrderDataRequest implements Serializable {

	private static final long serialVersionUID = -8476657009750522587L;
	
	private String id;

}
