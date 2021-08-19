package com.wanmi.sbc.customer.api.response.store;

import com.wanmi.sbc.customer.bean.vo.StoreVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel
@Data
public class StoreModifyResponse extends StoreVO {
    private static final long serialVersionUID = 2222951244101017271L;
}
