package com.soybean.mall.order.api.response;

import com.soybean.mall.order.bean.vo.OrderCommitResultVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderCommitResponse implements Serializable {
    private static final long serialVersionUID = -1319558934158416265L;
    
    List<OrderCommitResultVO>
}
