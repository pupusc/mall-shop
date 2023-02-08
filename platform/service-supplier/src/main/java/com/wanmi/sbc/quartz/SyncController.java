package com.wanmi.sbc.quartz;


import com.wanmi.sbc.common.base.BaseResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 同步订单
 * @author cz
 */
@RestController
public class SyncController {

    @Autowired
    private SchedulTrade schedulTrade;

    @RequestMapping(value = "/wx/sync")
    public BaseResponse<Map> sync() {
        schedulTrade.doJob();
        BaseResponse<Map> base = new BaseResponse("K-000000");
        return base;
    }

}
