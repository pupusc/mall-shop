package com.wanmi.sbc.customer.provider.impl.storesharerecord;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.storesharerecord.StoreShareRecordSaveProvider;
import com.wanmi.sbc.customer.api.request.storesharerecord.StoreShareRecordAddRequest;
import com.wanmi.sbc.customer.api.request.storesharerecord.StoreShareRecordDelByIdListRequest;
import com.wanmi.sbc.customer.api.request.storesharerecord.StoreShareRecordDelByIdRequest;
import com.wanmi.sbc.customer.api.request.storesharerecord.StoreShareRecordModifyRequest;
import com.wanmi.sbc.customer.api.response.storesharerecord.StoreShareRecordAddResponse;
import com.wanmi.sbc.customer.api.response.storesharerecord.StoreShareRecordModifyResponse;
import com.wanmi.sbc.customer.store.model.root.Store;
import com.wanmi.sbc.customer.store.service.StoreService;
import com.wanmi.sbc.customer.storesharerecord.model.root.StoreShareRecord;
import com.wanmi.sbc.customer.storesharerecord.service.StoreShareRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

/**
 * <p>商城分享保存服务接口实现</p>
 *
 * @author zhangwenchang
 * @date 2020-03-06 13:48:42
 */
@RestController
@Validated
public class StoreShareRecordSaveController implements StoreShareRecordSaveProvider {
    @Autowired
    private StoreShareRecordService storeShareRecordService;

    @Autowired
    private StoreService storeService;

    @Override
    public BaseResponse<StoreShareRecordAddResponse> add(@RequestBody @Valid StoreShareRecordAddRequest storeShareRecordAddRequest) {
        StoreShareRecord storeShareRecord = new StoreShareRecord();
        KsBeanUtil.copyPropertiesThird(storeShareRecordAddRequest, storeShareRecord);
        if (Objects.isNull(storeShareRecordAddRequest.getCompanyInfoId()) && Objects.nonNull(storeShareRecordAddRequest.getStoreId())) {
            Store store = storeService.find(storeShareRecordAddRequest.getStoreId());
            storeShareRecord.setCompanyInfoId(store.getCompanyInfo().getCompanyInfoId());
        }
        return BaseResponse.success(new StoreShareRecordAddResponse(
                storeShareRecordService.wrapperVo(storeShareRecordService.add(storeShareRecord))));
    }

    @Override
    public BaseResponse<StoreShareRecordModifyResponse> modify(@RequestBody @Valid StoreShareRecordModifyRequest storeShareRecordModifyRequest) {
        StoreShareRecord storeShareRecord = new StoreShareRecord();
        KsBeanUtil.copyPropertiesThird(storeShareRecordModifyRequest, storeShareRecord);
        return BaseResponse.success(new StoreShareRecordModifyResponse(
                storeShareRecordService.wrapperVo(storeShareRecordService.modify(storeShareRecord))));
    }

    @Override
    public BaseResponse deleteById(@RequestBody @Valid StoreShareRecordDelByIdRequest storeShareRecordDelByIdRequest) {
        storeShareRecordService.deleteById(storeShareRecordDelByIdRequest.getShareId());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteByIdList(@RequestBody @Valid StoreShareRecordDelByIdListRequest storeShareRecordDelByIdListRequest) {
        storeShareRecordService.deleteByIdList(storeShareRecordDelByIdListRequest.getShareIdList());
        return BaseResponse.SUCCESSFUL();
    }

}

