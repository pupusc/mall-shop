package com.wanmi.sbc.store;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.StoreType;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.store.StorePageRequest;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 店铺信息查询bff
 * Created by bail on 2017/11/29.
 */
@RestController
@Api(tags = "StoreController", description = "mobile 店铺信息查询bff")
public class StoreController {
    @Autowired
//    private StoreService storeService;
    private StoreQueryProvider storeQueryProvider;

    /**
     * 店铺列表
     * @param queryRequest 搜索条件
     * @return 返回分页结果
     */
    @ApiOperation(value = "店铺列表")
    @RequestMapping(value = "/stores", method = RequestMethod.POST)
    public BaseResponse<Page<StoreVO>> list(@RequestBody StorePageRequest queryRequest) {
        queryRequest.setAuditState(CheckState.CHECKED);
        queryRequest.setStoreState(StoreState.OPENING);
        queryRequest.setGteContractStartDate(LocalDateTime.now());
        queryRequest.setLteContractEndDate(LocalDateTime.now());
        queryRequest.setStoreType(StoreType.SUPPLIER);
        MicroServicePage<StoreVO> storePage = storeQueryProvider.page(queryRequest).getContext().getStoreVOPage();
        return BaseResponse.success(storePage);
    }

}
