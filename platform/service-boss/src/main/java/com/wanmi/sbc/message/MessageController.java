package com.wanmi.sbc.message;

import com.google.common.collect.Lists;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.provider.customgroup.CustomGroupProvider;
import com.wanmi.sbc.crm.api.provider.rfmgroupstatistics.RfmGroupStatisticsQueryProvide;
import com.wanmi.sbc.crm.bean.vo.CustomGroupVo;
import com.wanmi.sbc.crm.bean.vo.RfmGroupDataVo;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerDetailListByConditionRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerDetailPageRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerIdsListRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerListByConditionRequest;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelByIdsRequest;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailForPageVO;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailVO;
import com.wanmi.sbc.customer.bean.vo.CustomerLevelVO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.message.api.provider.messagesend.MessageSendQueryProvider;
import com.wanmi.sbc.message.api.request.messagesend.*;
import com.wanmi.sbc.message.api.response.messagesend.MessageSendByIdResponse;
import com.wanmi.sbc.message.api.response.messagesend.MessageSendPageResponse;
import com.wanmi.sbc.message.bean.constant.ReceiveGroupType;
import com.wanmi.sbc.message.bean.enums.MessageSendType;
import com.wanmi.sbc.message.bean.vo.MessageSendCustomerScopeVO;
import com.wanmi.sbc.message.bean.vo.MessageSendVO;
import com.wanmi.sbc.message.service.MessageService;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController("MessageController")
@RequestMapping("/messageSend")
@Api(tags = "MessageController", description = "站内信任务相关API")
public class MessageController {

    @Autowired
    private MessageSendQueryProvider messageSendQueryProvider;

    @Autowired
    private MessageService messageService;

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;

    @Autowired
    private RfmGroupStatisticsQueryProvide rfmGroupStatisticsQueryProvide;

    @Autowired
    private CustomGroupProvider customGroupProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加站内信任务")
    public BaseResponse add(@Valid @RequestBody MessageSendAddRequest request){
        request.setCreatePerson(commonUtil.getOperatorId());
        operateLogMQUtil.convertAndSend("消息", "创建站内信任务", "站内信活动：" + request.getName());
        return messageService.addMessage(request);
    }

    @RequestMapping(value = "/modify", method = RequestMethod.PUT)
    @ApiOperation(value = "修改站内信任务")
    public BaseResponse modify(@Valid @RequestBody MessageSendModifyRequest request){
        request.setUpdatePerson(commonUtil.getOperatorId());
        operateLogMQUtil.convertAndSend("消息", "修改站内信任务", "站内信活动：" + request.getName());
        return messageService.modify(request);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "站内信任务id", required = true)
    @ApiOperation(value = "查看站内信任务详情")
    public BaseResponse<MessageSendByIdResponse> getMessageById(@PathVariable("id") Long id){
        MessageSendByIdRequest request = new MessageSendByIdRequest(id);
        BaseResponse<MessageSendByIdResponse> response = messageSendQueryProvider.getById(request);
        fillReceive(Lists.newArrayList(response.getContext().getMessageSendVO()));
        return BaseResponse.success(response.getContext());
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    @ApiOperation(value = "分页查看站内信任务")
    public BaseResponse<MessageSendPageResponse> page(@Valid @RequestBody MessageSendPageRequest request){
        BaseResponse<MessageSendPageResponse> response = messageSendQueryProvider.page(request);
        fillReceive(response.getContext().getMessageSendVOPage().getContent());
        return BaseResponse.success(response.getContext());
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "站内信任务id", required = true)
    @ApiOperation(value = "删除站内信任务")
    public BaseResponse deleteById(@PathVariable("id") Long id){
        MessageSendDelByIdRequest request = new MessageSendDelByIdRequest(id, null);
        return messageService.deleteById(request);
    }

    /**
     * 填充接收人
     */
    public void fillReceive(List<MessageSendVO> voList){
        if(CollectionUtils.isEmpty(voList)){
            return;
        }

        List<Long> levelIds = voList.stream().filter(vo -> MessageSendType.LEVEL.equals(vo.getSendType()))
                .flatMap(vo -> vo.getScopeVOList().stream().map(MessageSendCustomerScopeVO::getJoinId))
                .map(NumberUtils::toLong)
                .collect(Collectors.toList());
        //会员类型
        if (CollectionUtils.isNotEmpty(levelIds)) {
            Map<Long, String> levelMap = customerLevelQueryProvider.listCustomerLevelByIds(CustomerLevelByIdsRequest
                    .builder().customerLevelIds(levelIds).build()).getContext().getCustomerLevelVOList().stream()
                    .collect(Collectors.toMap(CustomerLevelVO::getCustomerLevelId,CustomerLevelVO::getCustomerLevelName));
            if (MapUtils.isNotEmpty(levelMap)) {
                voList.stream().filter(vo -> MessageSendType.LEVEL.equals(vo.getSendType())).forEach(vo -> {
                    vo.getScopeVOList().stream().forEach(scopeVO -> {
                        String name = levelMap.get(Long.valueOf(scopeVO.getJoinId()));
                        scopeVO.setReceiveName(name);
                    });

                });
            }
        }


        List<String> groupIds = voList.stream().filter(vo -> MessageSendType.GROUP.equals(vo.getSendType()))
                .flatMap(vo -> vo.getScopeVOList().stream().map(MessageSendCustomerScopeVO::getJoinId))
                .collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(groupIds)){
            //系统分组
            if(groupIds.stream().anyMatch(id -> id.contains(ReceiveGroupType.SYS.concat("_")))){
                Map<Long, String> groupMap = rfmGroupStatisticsQueryProvide.queryRfmGroupList().getContext()
                        .getGroupDataList().stream()
                        .collect(Collectors.toMap(RfmGroupDataVo::getId, RfmGroupDataVo::getGroupName));
                if (MapUtils.isNotEmpty(groupMap)) {
                    voList.stream().filter(vo -> MessageSendType.GROUP.equals(vo.getSendType())).forEach(vo -> {
                        vo.getScopeVOList().stream()
                                .filter(scopeVO -> scopeVO.getJoinId().contains(ReceiveGroupType.SYS.concat("_")))
                                .forEach(scopeVO -> {
                            String name = groupMap.get(NumberUtils.toLong(
                                    scopeVO.getJoinId().replaceAll(ReceiveGroupType.SYS.concat("_"), "")));
                            scopeVO.setReceiveName(name);
                        });

                    });
                }


            }

            //自定义分组
            if(groupIds.stream().anyMatch(id -> id.contains(ReceiveGroupType.CUSTOM.concat("_")))){
                Map<Long, String> groupMap = customGroupProvider.queryAll().getContext().getCustomGroupVoList().stream()
                        .collect(Collectors.toMap(CustomGroupVo::getId, CustomGroupVo::getGroupName));
                voList.stream().filter(vo -> MessageSendType.GROUP.equals(vo.getSendType())).forEach(vo -> {
                    vo.getScopeVOList().stream()
                            .filter(scopeVO -> scopeVO.getJoinId().contains(ReceiveGroupType.CUSTOM.concat("_")))
                            .forEach(scopeVO -> {
                        String name = groupMap.get(NumberUtils.toLong(
                                scopeVO.getJoinId().replaceAll(ReceiveGroupType.CUSTOM.concat("_"), "")));
                        scopeVO.setReceiveName(name);
                    });
                });
            }
        }

        for (MessageSendVO messageSendVO : voList) {
            if(MessageSendType.CUSTOMER.equals(messageSendVO.getSendType())){
                List<String> ids = messageSendVO.getScopeVOList().stream()
                        .map(MessageSendCustomerScopeVO::getJoinId).collect(Collectors.toList());

                CustomerListByConditionRequest customerRequest = new CustomerListByConditionRequest();
                customerRequest.setCustomerIds(ids);
                List<CustomerVO> customerVOList = customerQueryProvider.listCustomerByCondition(customerRequest)
                        .getContext().getCustomerVOList();

                CustomerDetailListByConditionRequest detailListRequest = new CustomerDetailListByConditionRequest();
                detailListRequest.setCustomerIds(ids);
                List<CustomerDetailVO> detailResponseList = customerQueryProvider
                        .listCustomerDetailByCondition(detailListRequest)
                        .getContext().getDetailResponseList();

                if(CollectionUtils.isNotEmpty(detailResponseList)){
                    messageSendVO.getScopeVOList().stream().forEach(scopeVO -> {
                        CustomerDetailVO detailVO = detailResponseList.stream()
                                .filter(customerDetailVO -> customerDetailVO.getCustomerId().equals(scopeVO.getJoinId()))
                                .findFirst().orElse(null);
                        CustomerVO vo = customerVOList.stream()
                                .filter(customerVO -> customerVO.getCustomerId().equals(scopeVO.getJoinId()))
                                .findFirst().orElse(null);

                        if(detailVO != null){
                            scopeVO.setReceiveName(detailVO.getCustomerName());
                        }
                        if(vo != null){
                            scopeVO.setReceiveAccount(vo.getCustomerAccount());
                        }
                    });
                }

            }
        }
    }

}
