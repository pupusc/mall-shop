package com.wanmi.sbc.goods.provider.impl.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.goods.GoodsAuditProvider;
import com.wanmi.sbc.goods.api.request.goods.GoodsAuditQueryRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import com.wanmi.sbc.goods.bean.vo.GoodsSyncVO;

@RestController
@Validated
@Slf4j
public class GoodsAuditController implements GoodsAuditProvider {

    @Override
    public BaseResponse<Page<GoodsSyncVO>> list(GoodsAuditQueryRequest request) {
        return BaseResponse.SUCCESSFUL();
    }
}
