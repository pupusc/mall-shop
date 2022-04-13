package com.soybean.mall.wx.mini.customerserver.controller;

import com.soybean.mall.wx.mini.customerserver.request.WxCustomerServerOnlineRequest;
import com.soybean.mall.wx.mini.customerserver.response.WxCustomerServerOnlineResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Description: 客服信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/4/13 2:31 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RequestMapping("/wx/mini")
@FeignClient(value = "${application.wx.name}", contextId = "WxMiniApiController")
public interface WxCustomerServerApiController {


    @PostMapping("/customer-server/online")
    BaseResponse<WxCustomerServerOnlineResponse> listCustomerServerOnline(@RequestBody WxCustomerServerOnlineRequest customerServerOnlineRequest);
}
