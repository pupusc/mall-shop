package com.soybean.mall.wx.mini.customerserver.controller;

import com.soybean.mall.wx.mini.customerserver.request.WxCustomerServerOnlineRequest;
import com.soybean.mall.wx.mini.customerserver.response.WxCustomerServerOnlineResponse;
import com.soybean.mall.wx.mini.service.WxService;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/4/13 2:30 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
public class WxCustomerServerApiControllerImpl implements WxCustomerServerApiController{

    @Autowired
    private WxService wxService;

    @Override
    public BaseResponse<WxCustomerServerOnlineResponse> listCustomerServerOnline(WxCustomerServerOnlineRequest customerServerOnlineRequest) {
        return BaseResponse.success(wxService.listCustomerServerOnline(customerServerOnlineRequest));
    }
}
