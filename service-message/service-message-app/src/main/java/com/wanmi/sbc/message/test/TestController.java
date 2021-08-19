package com.wanmi.sbc.message.test;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.message.bean.enums.NodeType;
import com.wanmi.sbc.message.pushsendnode.model.root.PushSendNode;
import com.wanmi.sbc.message.pushsendnode.service.PushSendNodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-11-14
 * \* Time: 18:34
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@RestController
@Slf4j
public class TestController {
////    @Autowired
////    private CustomGroupGenerateService customGroupGenerateService;
////
////    @GetMapping("/testJob/customGroupJob")
////    public BaseResponse customGroupJob(String param,String statDate){
////        LocalDate localDate = LocalDate.now();
////        if(StringUtils.isNotEmpty(statDate)){
////            localDate = DateUtil.parseDay(statDate).toLocalDate();
////        }
////        switch (param){
////            case "1":
////                this.customGroupGenerateService.generateCustomerBaseInfoStatistics();
////                log.info("会员基本信息统计完成！");
////                return BaseResponse.success("会员基本信息统计完成");
////            case "2":
////                this.customGroupGenerateService.generateCustomerRecentParamStatistics(localDate);
////                log.info("会员最近指标数据统计完成！");
////                return BaseResponse.success("会员最近指标数据统计完成");
////            case "3":
////                this.customGroupGenerateService.generateCustomerTradeStatistics(localDate);
////                log.info("会员订单数据统计完成！");
////                return BaseResponse.success("会员订单数据统计完成");
////            case "4":
////                this.customGroupGenerateService.generateCustomGroupCustomerRelStatistics();
////                log.info("自定义人群会员分组完成！");
////                return BaseResponse.success("自定义人群会员分组完成！");
////            case "5":
////                this.customGroupGenerateService.generateCustomGroupStatistics(localDate);
////                log.info("自定义人群人数统计完成！");
////                return BaseResponse.success("自定义人群人数统计完成！");
////
////        }
////        return BaseResponse.SUCCESSFUL();
//    }

    @Autowired
    private PushSendNodeService pushSendNodeService;

    @GetMapping("/test")
    public BaseResponse test(){
        JSONObject.toJSONString(new PushSendNode());
        PushSendNode one = pushSendNodeService.getOne(1L);
        PushSendNode node = pushSendNodeService.findByNodeTypeAndCode(NodeType.ORDER_PROGRESS_RATE.toValue(), "ORDER_COMMIT_SUCCESS");
        log.info("=======one:{},", one);
        log.info("=======node：{}", node);
        return BaseResponse.SUCCESSFUL();
    }
}
