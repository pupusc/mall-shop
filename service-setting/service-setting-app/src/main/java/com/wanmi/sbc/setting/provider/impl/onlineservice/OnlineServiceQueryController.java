package com.wanmi.sbc.setting.provider.impl.onlineservice;

import com.wanmi.sbc.setting.bean.vo.OnlineServiceItemVO;
import com.wanmi.sbc.setting.onlineserviceitem.model.root.OnlineServiceItem;
import com.wanmi.sbc.setting.onlineserviceitem.service.OnlineServiceItemService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.onlineservice.OnlineServiceQueryProvider;
import com.wanmi.sbc.setting.api.request.onlineservice.OnlineServiceListRequest;
import com.wanmi.sbc.setting.api.response.onlineservice.OnlineServiceListResponse;
import com.wanmi.sbc.setting.api.request.onlineservice.OnlineServiceByIdRequest;
import com.wanmi.sbc.setting.api.response.onlineservice.OnlineServiceByIdResponse;
import com.wanmi.sbc.setting.onlineservice.service.OnlineServiceService;
import com.wanmi.sbc.setting.onlineservice.model.root.OnlineService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>onlineService查询服务接口实现</p>
 *
 * @author lq
 * @date 2019-11-05 16:10:28
 */
@RestController
@Validated
public class OnlineServiceQueryController implements OnlineServiceQueryProvider {

    @Autowired
    private OnlineServiceService onlineServiceService;

    @Autowired
    private OnlineServiceItemService onlineServiceItemService;

    @Override
    public BaseResponse<OnlineServiceListResponse> list(@RequestBody @Valid OnlineServiceListRequest onlineServiceListReq) {
        // 根据店铺id查询客服设置信息
        OnlineService onlineService = onlineServiceService.getByStoreId(onlineServiceListReq.getStoreId());

        // 店铺的座席列表
        List<OnlineServiceItem> onlineServiceItemList = onlineServiceItemService.list(onlineService.getOnlineServiceId());
        // 数据类型转换
        List<OnlineServiceItemVO> newList = onlineServiceItemList.stream().map(entity -> onlineServiceItemService.wrapperVo(entity)).collect(Collectors.toList());

        return BaseResponse.success(new OnlineServiceListResponse(onlineServiceService.wrapperVo(onlineService), newList));
    }

    @Override
    public BaseResponse<OnlineServiceByIdResponse> getById(@RequestBody @Valid OnlineServiceByIdRequest onlineServiceByIdRequest) {
        OnlineService onlineService = onlineServiceService.getByStoreId(onlineServiceByIdRequest.getStoreId());
        return BaseResponse.success(new OnlineServiceByIdResponse(onlineServiceService.wrapperVo(onlineService)));
    }

}

