package com.wanmi.sbc.customer.api.response.store;

import com.wanmi.sbc.customer.bean.vo.StoreVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel
@Data
public class NoDeleteStoreByStoreNameResponse extends StoreVO {

    private static final long serialVersionUID = 4109858465309084660L;
}
