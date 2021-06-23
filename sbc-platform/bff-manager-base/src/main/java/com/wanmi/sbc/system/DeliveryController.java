package com.wanmi.sbc.system;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.setting.api.request.ConfigQueryRequest;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.DeliveryQueryProvider;
import com.wanmi.sbc.setting.api.request.DeliveryQueryRequest;
import com.wanmi.sbc.setting.api.response.systemconfig.LogisticsRopResponse;
import com.wanmi.sbc.setting.api.provider.systemconfig.SystemConfigQueryProvider;
import com.wanmi.sbc.setting.api.provider.systemconfig.SystemConfigSaveProvider;
import com.wanmi.sbc.setting.api.request.systemconfig.LogisticsSaveRopRequest;
import com.wanmi.sbc.setting.bean.vo.ConfigVO;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by CHENLI on 2017/5/24.
 */
@Api(tags = "DeliveryController", description = "发货信息 Api")
@RestController
public class DeliveryController {

    @Autowired
    private DeliveryQueryProvider deliveryQueryProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private SystemConfigQueryProvider systemConfigQueryProvider;

    @Autowired
    private SystemConfigSaveProvider systemConfigSaveProvider;

    /**
     * 根据快递公司及快递单号查询物流详情
     * @param queryRequest
     * @return
     */
    @ApiOperation(value = "根据快递公司及快递单号查询物流详情", notes = "物流信息")
    @RequestMapping(value = "/deliveryInfos", method = RequestMethod.POST)
    public ResponseEntity<List<Map<Object, Object>>> queryExpressInfoUrl(@RequestBody DeliveryQueryRequest queryRequest){
        return ResponseEntity.ok(deliveryQueryProvider.queryExpressInfoUrl(queryRequest).getContext().getOrderList());
    }

    /**
     * 查询快递100信息
     * @return
     */
    @ApiOperation(value = "查询快递100信息")
    @RequestMapping(value = "/deliveryInfo", method = RequestMethod.GET)
    public ResponseEntity<LogisticsRopResponse> queryDelivery(){
        ConfigQueryRequest request = new ConfigQueryRequest();
        request.setConfigType(ConfigType.KUAIDI100.toValue());
        request.setDelFlag(0);
        LogisticsRopResponse response = systemConfigQueryProvider.findKuaiDiConfig(request).getContext();

//        CompositeResponse<LogisticsRopResponse> response = sdkClient.buildClientRequest().get(LogisticsRopResponse.class, "logistics.find", "1.0.0");
         return ResponseEntity.ok(response);
    }

    /**
     * 保存快递100信息
     * @param saveRopRequest
     * @return
     */
    @ApiOperation(value = "保存快递100信息")
    @RequestMapping(value = "/deliveryInfo", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> saveDelivery(@RequestBody LogisticsSaveRopRequest saveRopRequest){
//        CompositeResponse<LogisticsRopResponse> response = sdkClient.buildClientRequest().post( saveRopRequest,LogisticsRopResponse.class, "logistics.save", "1.0.0");
//        if ( !response.isSuccessful()) {
//            return ResponseEntity.ok( BaseResponse.FAILED());
//        }
        operateLogMQUtil.convertAndSend("设置", "保存物流接口", "保存物流接口");
        return ResponseEntity.ok(systemConfigSaveProvider.saveKuaidi(saveRopRequest));

    }

    /**
     * 修改快递100key
     * @param saveRopRequest
     * @return
     */
    @ApiOperation(value = "修改快递100key")
    @RequestMapping(value = "/deliveryInfo", method = RequestMethod.PUT)
    public ResponseEntity<BaseResponse> updateDelivery(@RequestBody LogisticsSaveRopRequest saveRopRequest){
//        CompositeResponse<LogisticsRopResponse> response = sdkClient.buildClientRequest().post( saveRopRequest, LogisticsRopResponse.class, "logistics.modify", "1.0.0");
//        if ( !response.isSuccessful()) {
//            return ResponseEntity.ok( BaseResponse.FAILED());
//        }
        operateLogMQUtil.convertAndSend("设置", "编辑物流接口", "编辑物流接口");
        return ResponseEntity.ok(systemConfigSaveProvider.saveKuaidi(saveRopRequest));
        //return ResponseEntity.ok( BaseResponse.SUCCESSFUL());
    }
}
