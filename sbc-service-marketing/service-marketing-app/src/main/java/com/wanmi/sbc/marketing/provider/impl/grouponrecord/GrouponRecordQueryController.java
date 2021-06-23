package com.wanmi.sbc.marketing.provider.impl.grouponrecord;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.provider.grouponrecord.GrouponRecordQueryProvider;
import com.wanmi.sbc.marketing.api.request.grouponrecord.GrouponRecordByCustomerRequest;
import com.wanmi.sbc.marketing.api.request.grouponrecord.GrouponRecordListRequest;
import com.wanmi.sbc.marketing.api.request.grouponrecord.GrouponRecordQueryRequest;
import com.wanmi.sbc.marketing.api.response.grouponrecord.GrouponRecordByCustomerResponse;
import com.wanmi.sbc.marketing.api.response.grouponrecord.GrouponRecordListResponse;
import com.wanmi.sbc.marketing.bean.vo.GrouponRecordVO;
import com.wanmi.sbc.marketing.grouponrecord.model.root.GrouponRecord;
import com.wanmi.sbc.marketing.grouponrecord.service.GrouponRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>拼团活动参团信息表查询服务接口实现</p>
 *
 * @author groupon
 * @date 2019-05-17 16:17:44
 */
@RestController
@Validated
public class GrouponRecordQueryController implements GrouponRecordQueryProvider {
    @Autowired
    private GrouponRecordService grouponRecordService;

    /**
     * 单个查询C端用户参团拼团活动信息表API
     */
    @Override
    public BaseResponse<GrouponRecordByCustomerResponse> getByCustomerIdAndGoodsInfoId(@RequestBody @Valid
                                                                               GrouponRecordByCustomerRequest
                                                                               grouponRecordByIdRequest) {
        GrouponRecord grouponRecord = grouponRecordService.getByCustomerIdAndGoodsInfoId(grouponRecordByIdRequest
                        .getGrouponActivityId(), grouponRecordByIdRequest.getCustomerId(),
                grouponRecordByIdRequest.getGoodsInfoId());
        return BaseResponse.success(new GrouponRecordByCustomerResponse(grouponRecordService.wrapperVo(grouponRecord)));
    }



    @Override
    public BaseResponse<GrouponRecordListResponse> list(@RequestBody @Valid GrouponRecordListRequest grouponRecordListReq) {
        GrouponRecordQueryRequest queryReq = new GrouponRecordQueryRequest();
        KsBeanUtil.copyPropertiesThird(grouponRecordListReq, queryReq);
        List<GrouponRecord> grouponRecordList = grouponRecordService.list(queryReq);
        List<GrouponRecordVO> newList = grouponRecordList.stream().map(entity -> grouponRecordService.wrapperVo(entity)).collect(Collectors.toList());
        return BaseResponse.success(new GrouponRecordListResponse(newList));
    }

}

