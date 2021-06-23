package com.wanmi.sbc.pushsend;

import com.google.common.base.Splitter;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.crm.api.provider.customgroup.CustomGroupProvider;
import com.wanmi.sbc.crm.api.provider.rfmgroupstatistics.RfmGroupStatisticsQueryProvide;
import com.wanmi.sbc.crm.bean.vo.CustomGroupVo;
import com.wanmi.sbc.crm.bean.vo.RfmGroupDataVo;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerDetailListByConditionRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerDetailPageRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerListByConditionRequest;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelByIdsRequest;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailForPageVO;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailVO;
import com.wanmi.sbc.customer.bean.vo.CustomerLevelVO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.message.api.provider.pushsend.PushSendProvider;
import com.wanmi.sbc.message.api.provider.pushsend.PushSendQueryProvider;
import com.wanmi.sbc.message.api.request.messagesend.MessageSendDelByIdRequest;
import com.wanmi.sbc.message.api.request.pushsend.*;
import com.wanmi.sbc.message.api.response.pushsend.PushSendAddResponse;
import com.wanmi.sbc.message.api.response.pushsend.PushSendByIdResponse;
import com.wanmi.sbc.message.api.response.pushsend.PushSendModifyResponse;
import com.wanmi.sbc.message.api.response.pushsend.PushSendPageResponse;
import com.wanmi.sbc.message.bean.constant.ReceiveGroupType;
import com.wanmi.sbc.message.bean.enums.MessageSendType;
import com.wanmi.sbc.message.bean.enums.PushFlag;
import com.wanmi.sbc.message.bean.vo.PushSendVO;
import com.wanmi.sbc.message.service.MessageService;
import com.wanmi.sbc.pushsend.service.PushSendService;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;


@Api(description = "友盟消息推送管理API", tags = "PushSendController")
@RestController
@RequestMapping(value = "/pushsend")
public class PushSendController {

    @Autowired
    private PushSendQueryProvider pushSendQueryProvider;

    @Autowired
    private PushSendProvider pushSendProvider;

    @Autowired
    private MessageService messageService;

    @Autowired
    private PushSendService pushSendService;

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

    @ApiOperation(value = "分页查询友盟消息推送")
    @PostMapping("/page")
    public BaseResponse<PushSendPageResponse> getPage(@RequestBody @Valid PushSendPageRequest pageReq) {
        pageReq.setDelFlag(DeleteFlag.NO);
        pageReq.putSort("id", "desc");
        PushSendPageResponse PushSendPageResponse = pushSendQueryProvider.page(pageReq).getContext();
        fillReceive(PushSendPageResponse.getPushSendVOPage().getContent());
        return BaseResponse.success(PushSendPageResponse);
    }

    @ApiOperation(value = "根据id查询友盟消息推送")
    @GetMapping("/{id}")
    public BaseResponse<PushSendByIdResponse> getById(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        PushSendByIdRequest idReq = new PushSendByIdRequest();
        idReq.setId(id);
        PushSendByIdResponse response = pushSendQueryProvider.getById(idReq).getContext();
        if (Objects.nonNull(response.getPushSendVO())){
            fillReceive(Collections.singletonList(response.getPushSendVO()));
        }
        return BaseResponse.success(response);
    }

    @ApiOperation(value = "新增友盟消息推送")
    @PostMapping("/add")
    public BaseResponse<PushSendAddResponse> add(@RequestBody @Valid PushSendAddRequest addReq) {
        addReq.setCreatePerson(commonUtil.getOperatorId());
        addReq.setPushFlag(PushFlag.PUSH);
        return pushSendService.add(addReq);
    }

    @ApiOperation(value = "修改友盟消息推送")
    @PutMapping("/modify")
    public BaseResponse<PushSendModifyResponse> modify(@RequestBody @Valid PushSendModifyRequest modifyReq) {
        modifyReq.setUpdatePerson(commonUtil.getOperatorId());
        modifyReq.setPushFlag(PushFlag.PUSH);
        return pushSendService.modify(modifyReq);
    }

    @ApiOperation(value = "根据id删除友盟消息推送")
    @DeleteMapping("/{id}")
    public BaseResponse deleteById(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        PushSendDelByIdRequest delByIdReq = new PushSendDelByIdRequest();
        delByIdReq.setId(id);

        messageService.deleteById(new MessageSendDelByIdRequest(null, id.toString()));
        return pushSendProvider.deleteById(delByIdReq);
    }

    private void fillReceive(List<PushSendVO> voList){
        if(CollectionUtils.isEmpty(voList)){
            return;
        }

        List<PushSendVO> pushSendVOS =
                voList.stream().filter(vo -> MessageSendType.LEVEL.toValue() == vo.getMsgRecipient()).collect(Collectors.toList());

        List<Long> levelIds = new ArrayList<>();
        pushSendVOS.forEach(pushSendVO -> {
            List<Long> ids =
                    Splitter.on(",").splitToList(pushSendVO.getMsgRecipientDetail())
                            .stream().map(Long::parseLong).collect(Collectors.toList());
            levelIds.addAll(ids);
        });


        //会员类型
        if (CollectionUtils.isNotEmpty(levelIds)) {
            Map<Long, String> levelMap = customerLevelQueryProvider.listCustomerLevelByIds(CustomerLevelByIdsRequest
                    .builder().customerLevelIds(levelIds).build()).getContext().getCustomerLevelVOList().stream()
                    .collect(Collectors.toMap(CustomerLevelVO::getCustomerLevelId,CustomerLevelVO::getCustomerLevelName));
            if (MapUtils.isNotEmpty(levelMap)) {
                voList.stream().filter(vo -> MessageSendType.LEVEL.toValue() == (vo.getMsgRecipient())).forEach(vo -> {
                    List<String> ids = Splitter.on(",").splitToList(vo.getMsgRecipientDetail());
                    List<String> names = new ArrayList<>();
                    ids.forEach(id -> names.add(levelMap.get(Long.valueOf(id))));
                    vo.setMsgRecipientNames(names);
                });
            }
        }

        List<String> groupIds = voList.stream().filter(vo -> MessageSendType.GROUP.toValue() == vo.getMsgRecipient())
                .map(PushSendVO::getMsgRecipientDetail).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(groupIds)){
            //系统分组
            if(groupIds.stream().anyMatch(id -> id.contains(ReceiveGroupType.SYS.concat("_")))){
                Map<Long, String> groupMap = rfmGroupStatisticsQueryProvide.queryRfmGroupList().getContext()
                        .getGroupDataList().stream()
                        .collect(Collectors.toMap(RfmGroupDataVo::getId, RfmGroupDataVo::getGroupName));
                if (MapUtils.isNotEmpty(groupMap)) {
                    voList.stream().filter(vo -> MessageSendType.GROUP.toValue() == vo.getMsgRecipient()).forEach(vo -> {
                        List<String> ids = Splitter.on(",").splitToList(vo.getMsgRecipientDetail());
                        List<String> names = new ArrayList<>();
                        ids.stream()
                                .filter(id -> id.contains(ReceiveGroupType.SYS.concat("_")))
                                .forEach(id -> names.add(groupMap.get(NumberUtils.toLong(
                                        id.replaceAll(ReceiveGroupType.SYS.concat("_"), "")))));
                        vo.setMsgRecipientNames(names);
                    });
                }


            }

            //自定义分组
            if(groupIds.stream().anyMatch(id -> id.contains(ReceiveGroupType.CUSTOM.concat("_")))) {
                Map<Long, String> groupMap = customGroupProvider.queryAll().getContext().getCustomGroupVoList().stream()
                        .collect(Collectors.toMap(CustomGroupVo::getId, CustomGroupVo::getGroupName));
                voList.stream().filter(vo -> MessageSendType.GROUP.toValue() == vo.getMsgRecipient()).forEach(vo -> {
                    List<String> ids = Splitter.on(",").splitToList(vo.getMsgRecipientDetail());
                    ids.stream().filter(id -> id.contains(ReceiveGroupType.CUSTOM.concat("_")))
                            .forEach(id -> {
                                vo.getMsgRecipientNames().add(groupMap.get(NumberUtils.toLong(
                                        id.replaceAll(ReceiveGroupType.CUSTOM.concat("_"), ""))));
                            });
                });
            }
        }

        for (PushSendVO vo : voList) {
            if(MessageSendType.CUSTOMER.toValue() == vo.getMsgRecipient()){
                List<String> ids = Splitter.on(",").splitToList(vo.getMsgRecipientDetail());
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
                    List<String> names = new ArrayList<>();
                    List<String> accounts = new ArrayList<>();
                    ids.forEach(id -> {
                        detailResponseList.stream()
                                .filter(customerDetailForPageVO -> customerDetailForPageVO.getCustomerId().equals(id))
                                .findFirst().ifPresent(detailVO -> {
                            names.add(detailVO.getCustomerName());
                        });
                        customerVOList.stream()
                                .filter(customerVO -> customerVO.getCustomerId().equals(id))
                                .findFirst().ifPresent(customerVo -> {
                            accounts.add(customerVo.getCustomerAccount());
                        });
                    });
                    vo.setMsgRecipientNames(names);
                    vo.setAccountList(accounts);
                }
            }
        }
    }
}