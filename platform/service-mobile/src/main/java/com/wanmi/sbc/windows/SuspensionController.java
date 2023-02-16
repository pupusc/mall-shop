package com.wanmi.sbc.windows;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.goods.api.provider.SuspensionV2.SuspensionProvider;
import com.wanmi.sbc.goods.api.request.SuspensionV2.SuspensionByTypeRequest;
import com.wanmi.sbc.goods.api.response.SuspensionV2.SuspensionByTypeResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.setting.api.request.topicconfig.TopicStoreyContentRequest;
import com.wanmi.sbc.topic.response.GoodsOrBookResponse;
import com.wanmi.sbc.topic.response.NewBookPointResponse;
import com.wanmi.sbc.topic.response.ThreeGoodBookResponse;
import com.wanmi.sbc.topic.service.TopicService;
import com.wanmi.sbc.windows.request.ThreeGoodBookRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/windows/v2")
@Slf4j
public class SuspensionController {

    @Autowired
    private SuspensionProvider suspensionProvider;

    @Autowired
    private TopicService topicService;

    @PostMapping("/getSuspensionByType")
    public BaseResponse<SuspensionByTypeResponse> getByType(@RequestBody @Valid SuspensionByTypeRequest suspensionByTypeRequest) {
        return suspensionProvider.getByType(suspensionByTypeRequest);
    }

    @PostMapping("/test")
    public List<NewBookPointResponse> newBookPoint(@RequestBody @Valid BaseQueryRequest baseQueryRequest)  {
        CustomerGetByIdResponse customer=new CustomerGetByIdResponse();
        return topicService.newBookPoint(baseQueryRequest,customer);
    }

    @PostMapping("/test1")
    public List<ThreeGoodBookResponse> threeGoodBook(@RequestBody @Valid ThreeGoodBookRequest threeGoodBookRequest)  {
        return topicService.threeGoodBook(threeGoodBookRequest);
    }

    @PostMapping("/test2")
    public List<GoodsOrBookResponse> GoodsOrBook(@RequestBody @Valid  TopicStoreyContentRequest topicStoreyContentRequest)  {
        CustomerGetByIdResponse customer=new CustomerGetByIdResponse();
        return topicService.bookOrGoods(topicStoreyContentRequest,customer);
    }
}
