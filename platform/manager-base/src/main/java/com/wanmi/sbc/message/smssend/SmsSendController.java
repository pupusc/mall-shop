package com.wanmi.sbc.message.smssend;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.crm.api.provider.customgroup.CustomGroupProvider;
import com.wanmi.sbc.crm.api.provider.rfmgroupstatistics.RfmGroupStatisticsQueryProvide;
import com.wanmi.sbc.crm.bean.vo.CustomGroupVo;
import com.wanmi.sbc.crm.bean.vo.RfmGroupDataVo;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelByIdsRequest;
import com.wanmi.sbc.customer.bean.vo.CustomerLevelVO;
import com.wanmi.sbc.message.api.provider.smssend.SmsSendQueryProvider;
import com.wanmi.sbc.message.api.provider.smssend.SmsSendSaveProvider;
import com.wanmi.sbc.message.api.request.smssend.*;
import com.wanmi.sbc.message.api.response.smssend.SmsSendAddResponse;
import com.wanmi.sbc.message.api.response.smssend.SmsSendByIdResponse;
import com.wanmi.sbc.message.api.response.smssend.SmsSendModifyResponse;
import com.wanmi.sbc.message.api.response.smssend.SmsSendPageResponse;
import com.wanmi.sbc.message.bean.constant.ReceiveGroupType;
import com.wanmi.sbc.message.bean.enums.ReceiveType;
import com.wanmi.sbc.message.bean.vo.SmsSendReceiveValueVO;
import com.wanmi.sbc.message.bean.vo.SmsSendVO;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Api(description = "短信发送任务管理API", tags = "SmsSendController")
@RestController
@RequestMapping(value = "/sms-send")
public class SmsSendController {

    @Autowired
    private SmsSendQueryProvider smsSendQueryProvider;

    @Autowired
    private SmsSendSaveProvider smsSendSaveProvider;

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;

    @Autowired
    private RfmGroupStatisticsQueryProvide rfmGroupStatisticsQueryProvide;

    @Autowired
    private CustomGroupProvider customGroupProvider;
    @Autowired
    private CommonUtil commonUtil;


    @ApiOperation(value = "分页查询短信发送任务")
    @PostMapping("/page")
    public BaseResponse<SmsSendPageResponse> getPage(@RequestBody @Valid SmsSendPageRequest pageReq) {
        pageReq.putSort("createTime", "desc");
        BaseResponse<SmsSendPageResponse> response = smsSendQueryProvider.page(pageReq);
        fillReceive(response.getContext().getSmsSendVOPage().getContent());
        return response;
    }

    @ApiOperation(value = "根据id查询短信发送任务")
    @GetMapping("/{id}")
    public BaseResponse<SmsSendByIdResponse> getById(@PathVariable Long id) {
        BaseResponse<SmsSendByIdResponse> response = smsSendQueryProvider.getById(SmsSendByIdRequest.builder().id(id).build());
        if(Objects.nonNull(response.getContext().getSmsSendVO()) ) {
            SmsSendVO vo = response.getContext().getSmsSendVO();
            fillReceive(Collections.singletonList(vo));
        }
        return response;
    }

    @ApiOperation(value = "新增短信发送任务")
    @PostMapping("/add")
    public BaseResponse<SmsSendAddResponse> add(@RequestBody @Valid SmsSendAddRequest addReq) {
        addReq.setCreatePerson(commonUtil.getOperatorId());
        addReq.setCreateTime(LocalDateTime.now());
        return smsSendSaveProvider.add(addReq);
    }

    @ApiOperation(value = "修改短信发送任务")
    @PutMapping("/modify")
    public BaseResponse<SmsSendModifyResponse> modify(@RequestBody @Valid SmsSendModifyRequest modifyReq) {
        modifyReq.setUpdatePerson(commonUtil.getOperatorId());
        modifyReq.setUpdateTime(LocalDateTime.now());
        return smsSendSaveProvider.modify(modifyReq);
    }

    @ApiOperation(value = "根据id删除短信发送任务")
    @DeleteMapping("/{id}")
    public BaseResponse deleteById(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        SmsSendDelByIdRequest delByIdReq = new SmsSendDelByIdRequest();
        delByIdReq.setId(id);
        return smsSendSaveProvider.deleteById(delByIdReq);
    }

    /**
     * 填充接受人
     * @param voList
     */
    private void fillReceive(List<SmsSendVO> voList){
        if(CollectionUtils.isEmpty(voList)){
            return;
        }
        List<Long> levelIds = voList.stream().filter(vo -> ReceiveType.LEVEL.equals(vo.getReceiveType()))
                .flatMap(vo -> Arrays.stream(StringUtils.split(vo.getReceiveValue(), ",")))
                .map(NumberUtils::toLong)
                .collect(Collectors.toList());
        //会员类型
        if (CollectionUtils.isNotEmpty(levelIds)) {
            Map<Long, String> levelMap = customerLevelQueryProvider.listCustomerLevelByIds(CustomerLevelByIdsRequest
                    .builder().customerLevelIds(levelIds).build()).getContext().getCustomerLevelVOList().stream()
                    .collect(Collectors.toMap(CustomerLevelVO::getCustomerLevelId,CustomerLevelVO::getCustomerLevelName));
            if (MapUtils.isNotEmpty(levelMap)) {
                voList.stream().filter(vo -> ReceiveType.LEVEL.equals(vo.getReceiveType())).forEach(vo -> {
                    List<SmsSendReceiveValueVO> valueList =  Arrays.stream(StringUtils.split(vo.getReceiveValue(), ","))
                            .map(NumberUtils::toLong).filter(levelMap::containsKey)
                            .map(id -> {
                                SmsSendReceiveValueVO valueVO = new SmsSendReceiveValueVO();
                                valueVO.setId(id);
                                valueVO.setName(levelMap.get(id));
                                return valueVO;
                            })
                            .collect(Collectors.toList());
                    vo.setReceiveValueList(valueList);
                    vo.setReceiveNames(valueList.stream().map(SmsSendReceiveValueVO::getName).collect(Collectors.toList()));
                });
            }
        }

        List<String> groupIds = voList.stream().filter(vo -> ReceiveType.GROUP.equals(vo.getReceiveType()))
                .flatMap(vo -> Arrays.stream(StringUtils.split(vo.getReceiveValue(), ",")))
                .collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(groupIds)){
            //系统分组
            if(groupIds.stream().anyMatch(id -> id.contains(ReceiveGroupType.SYS.concat("_")))){
                Map<Long, String> groupMap = rfmGroupStatisticsQueryProvide.queryRfmGroupList().getContext()
                        .getGroupDataList().stream()
                        .collect(Collectors.toMap(RfmGroupDataVo::getId, RfmGroupDataVo::getGroupName));
                voList.stream().filter(vo -> ReceiveType.GROUP.equals(vo.getReceiveType())).forEach(vo -> {
                    vo.setReceiveValueList(Arrays.stream(StringUtils.split(vo.getReceiveValue(), ","))
                            .filter(id -> id.contains(ReceiveGroupType.SYS.concat("_")))
                            .map(id -> NumberUtils.toLong(id.replaceAll(ReceiveGroupType.SYS.concat("_"), "")))
                            .filter(groupMap::containsKey)
                            .map(id -> {
                                SmsSendReceiveValueVO valueVO = new SmsSendReceiveValueVO();
                                valueVO.setId(id);
                                valueVO.setName(groupMap.get(id));
                                valueVO.setType(ReceiveGroupType.SYS);
                                return valueVO;
                            }).collect(Collectors.toList()));
                    vo.setReceiveNames(vo.getReceiveValueList().stream().map(SmsSendReceiveValueVO::getName).collect(Collectors.toList()));
                });
            }
            //自定义分组
            if(groupIds.stream().anyMatch(id -> id.contains(ReceiveGroupType.CUSTOM.concat("_")))){
                Map<Long, String> groupMap = customGroupProvider.queryAll().getContext().getCustomGroupVoList().stream()
                        .collect(Collectors.toMap(CustomGroupVo::getId, CustomGroupVo::getGroupName));
                voList.stream().filter(vo -> ReceiveType.GROUP.equals(vo.getReceiveType())).forEach(vo -> {
                    List<SmsSendReceiveValueVO> receiveValueList = Objects.isNull(vo.getReceiveValueList()) ? new ArrayList<>() :
                            vo .getReceiveValueList();
                    receiveValueList.addAll(Arrays.stream(StringUtils.split(vo.getReceiveValue(), ","))
                            .filter(id -> id.contains(ReceiveGroupType.CUSTOM.concat("_")))
                            .map(id -> NumberUtils.toLong(id.replaceAll(ReceiveGroupType.CUSTOM.concat("_"), "")))
                            .filter(groupMap::containsKey)
                            .map(id -> {
                                SmsSendReceiveValueVO valueVO = new SmsSendReceiveValueVO();
                                valueVO.setId(id);
                                valueVO.setName(groupMap.get(id));
                                valueVO.setType(ReceiveGroupType.CUSTOM);
                                return valueVO;
                            })
                            .collect(Collectors.toList()));
                    vo.setReceiveValueList(receiveValueList);
                    vo.setReceiveNames(vo.getReceiveValueList().stream().map(SmsSendReceiveValueVO::getName).collect(Collectors.toList()));
                });
            }
        }
        for(SmsSendVO vo : voList) {
            if (ReceiveType.CUSTOM.equals(vo.getReceiveType()) && StringUtils.isNotBlank(vo.getReceiveValue())) {
                String[] customers = vo.getReceiveValue().split(",");
                List<SmsSendReceiveValueVO> receiveValueList = new ArrayList<>();
                for (String customer : customers) {
                    if (StringUtils.isNotBlank(customer)) {
                        SmsSendReceiveValueVO smsSendReceiveValueVO = new SmsSendReceiveValueVO();
                        String[] info = customer.split(":");
                        smsSendReceiveValueVO.setAccount(info[0]);
                        if (info.length > 1) {
                            smsSendReceiveValueVO.setName(info[1]);
                        }
                        receiveValueList.add(smsSendReceiveValueVO);
                    }
                }
                vo.setReceiveValueList(receiveValueList);
            }
        }
        return;
    }
}
