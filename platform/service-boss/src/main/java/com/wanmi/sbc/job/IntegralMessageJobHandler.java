package com.wanmi.sbc.job;

import com.google.common.collect.Lists;
import com.wanmi.sbc.common.base.MessageMQRequest;
import com.wanmi.sbc.common.enums.node.AccoutAssetsType;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.points.CustomerPointsDetailQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerIdsListRequest;
import com.wanmi.sbc.customer.api.response.points.CustomerPointsExpireResponse;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.message.bean.enums.NodeType;
import com.wanmi.sbc.mq.MessageSendProducer;
import com.wanmi.sbc.setting.api.provider.SystemPointsConfigQueryProvider;
import com.wanmi.sbc.setting.api.response.SystemPointsConfigQueryResponse;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * 积分过期再次提醒定时任务
 */
@Component
@Slf4j
@JobHandler(value="IntegralMessageJobHandler")
public class IntegralMessageJobHandler extends IJobHandler {

    @Autowired
    private CustomerPointsDetailQueryProvider customerPointsDetailQueryProvider;

    @Autowired
    private SystemPointsConfigQueryProvider systemPointsConfigQueryProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private MessageSendProducer messageSendProducer;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        SystemPointsConfigQueryResponse pointsConfig = systemPointsConfigQueryProvider.querySystemPointsConfig().getContext();
        Integer pointsExpireMonth = pointsConfig.getPointsExpireMonth();
        Integer pointsExpireDay = pointsConfig.getPointsExpireDay();
        if(pointsExpireMonth.equals(LocalDate.now().getMonthValue()) && pointsExpireDay.equals(LocalDate.now().getDayOfMonth())){
            // 2. 查询用户id列表
            List<String> customerIds = customerQueryProvider.listCustomerId().getContext().getCustomerIdList();
            // 2.1 创建线程池，暂时是每50个用户分配一个线程
            ExecutorService executor = Executors.newFixedThreadPool(50);
            Map<String, String> customerMobile = getCustomerAccountMap(customerIds);
            // 2.2 遍历用户列表
            customerIds.forEach(customerId -> {
                executor.submit(() -> {
                    // 3.查询用户即将过期积分
                    CustomerPointsExpireResponse response = customerPointsDetailQueryProvider
                            .queryWillExpirePointsForCronJob(new CustomerGetByIdRequest(customerId))
                            .getContext();
                    if (response.getWillExpirePoints() > 0) {
                        List<String> params = Lists.newArrayList(String.valueOf(response.getWillExpirePoints()));
                        this.sendMessage(NodeType.ACCOUNT_ASSETS, AccoutAssetsType.INTEGRAL_EXPIRED_AGAIN, params, null, customerId, customerMobile.get(customerId));
                    }
                });
            });
            executor.shutdown();
        }
        return SUCCESS;
    }

    /**
     * 发送消息
     * @param nodeType
     * @param nodeCode
     * @param params
     * @param routeParam
     * @param customerId
     */
    private void sendMessage(NodeType nodeType, AccoutAssetsType nodeCode, List<String> params, String routeParam, String customerId, String mobile){
        Map<String, Object> map = new HashMap<>();
        map.put("type", nodeType.toValue());
        map.put("node", nodeCode.toValue());

        MessageMQRequest messageMQRequest = new MessageMQRequest();
        messageMQRequest.setNodeCode(nodeCode.getType());
        messageMQRequest.setNodeType(nodeType.toValue());
        messageMQRequest.setParams(params);
        messageMQRequest.setRouteParam(map);
        messageMQRequest.setCustomerId(customerId);
        messageMQRequest.setMobile(mobile);
        messageSendProducer.sendMessage(messageMQRequest);
    }

    /**
     * 获取批量会员账号map
     * @param customerIds
     * @return
     */
    private Map<String, String> getCustomerAccountMap(List<String> customerIds){
        CustomerIdsListRequest listRequest = new CustomerIdsListRequest();
        listRequest.setCustomerIds(customerIds);
        List<CustomerVO> customerVOList = customerQueryProvider.getCustomerListByIds(listRequest).getContext().getCustomerVOList();
        Map<String, String> customerMobile = new HashMap<>();
        if(CollectionUtils.isNotEmpty(customerVOList)){
            customerMobile.putAll(customerVOList.stream().collect(Collectors.toMap(CustomerVO::getCustomerId, CustomerVO::getCustomerAccount)));
        }
        return customerMobile;
    }
}
