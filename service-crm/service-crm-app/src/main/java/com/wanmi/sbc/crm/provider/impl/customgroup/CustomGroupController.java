package com.wanmi.sbc.crm.provider.impl.customgroup;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.constant.CustomGroupErrorCode;
import com.wanmi.sbc.crm.api.provider.customgroup.CustomGroupProvider;
import com.wanmi.sbc.crm.api.request.autotag.AutoTagQueryRequest;
import com.wanmi.sbc.crm.api.request.autotagpreference.AutoPreferenceListRequest;
import com.wanmi.sbc.crm.api.request.customerplan.CustomerPlanListRequest;
import com.wanmi.sbc.crm.api.request.customertag.CustomerTagQueryRequest;
import com.wanmi.sbc.crm.api.request.customgroup.CustomGroupCheckTagRequest;
import com.wanmi.sbc.crm.api.request.customgroup.CustomGroupListParamRequest;
import com.wanmi.sbc.crm.api.request.customgroup.CustomGroupListRequest;
import com.wanmi.sbc.crm.api.request.customgroup.CustomGroupRequest;
import com.wanmi.sbc.crm.api.response.customgroup.CustomGroupListForParamResponse;
import com.wanmi.sbc.crm.api.response.customgroup.CustomGroupListResponse;
import com.wanmi.sbc.crm.api.response.customgroup.CustomGroupQueryAllResponse;
import com.wanmi.sbc.crm.autotag.model.root.AutoTag;
import com.wanmi.sbc.crm.autotag.service.AutoTagService;
import com.wanmi.sbc.crm.autotagpreference.model.AutotagPreference;
import com.wanmi.sbc.crm.autotagpreference.service.AutotagPreferenceService;
import com.wanmi.sbc.crm.bean.dto.AutoTagDTO;
import com.wanmi.sbc.crm.bean.dto.RegionDTO;
import com.wanmi.sbc.crm.bean.enums.TagType;
import com.wanmi.sbc.crm.bean.vo.CustomGroupDetailVo;
import com.wanmi.sbc.crm.bean.vo.CustomGroupVo;
import com.wanmi.sbc.crm.constant.CustomGroupConstant;
import com.wanmi.sbc.crm.customerlevel.model.CustomerLevel;
import com.wanmi.sbc.crm.customerlevel.service.CustomerLevelService;
import com.wanmi.sbc.crm.customerplan.model.CustomerPlan;
import com.wanmi.sbc.crm.customerplan.service.CustomerPlanService;
import com.wanmi.sbc.crm.customertag.model.root.CustomerTag;
import com.wanmi.sbc.crm.customertag.service.CustomerTagService;
import com.wanmi.sbc.crm.customgroup.model.CustomGroup;
import com.wanmi.sbc.crm.customgroup.service.CustomGroupService;
import com.wanmi.sbc.crm.preferencetagdetail.service.PreferenceTagDetailService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-11-11
 * \* Time: 16:43
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@RestController
public class CustomGroupController implements CustomGroupProvider {

    public static final String LINE = "-";
    public static final String SLASH = "/";

    @Autowired
    private CustomGroupService customGroupService;
    @Autowired
    private CustomerTagService customerTagService;
    @Autowired
    private CustomerLevelService customerLevelService;

    @Autowired
    private CustomerPlanService customerPlanService;

    @Autowired
    private AutoTagService autoTagService;

    @Autowired
    private PreferenceTagDetailService preferenceTagDetailService;

    @Autowired
    private AutotagPreferenceService autotagPreferenceService;

    @Override
    public BaseResponse add(@RequestBody @Valid CustomGroupRequest request) {
        verify(request);
        CustomGroup customGroup = dataProcess(request);
        return BaseResponse.success(this.customGroupService.add(customGroup));
    }

    @Override
    public BaseResponse modify(@RequestBody CustomGroupRequest request) {
        verify(request);
        CustomGroup customGroup = dataProcess(request);
        return BaseResponse.success(this.customGroupService.modify(customGroup));
    }

    @Override
    public BaseResponse deleteById(@RequestBody CustomGroupRequest request) {
        List<CustomerPlan> plans = customerPlanService.list(
                CustomerPlanListRequest.builder().delFlag(DeleteFlag.NO).notEndStatus(Boolean.TRUE)
                        .receiveValue("1_".concat(String.valueOf(request.getId()))).build());
        if (CollectionUtils.isNotEmpty(plans)) {
            throw new SbcRuntimeException(CustomGroupErrorCode.EXISTS_TO_PLAN);
        }
        return BaseResponse.success(this.customGroupService.deleteById(request.getId()));
    }

    @Override
    public BaseResponse queryById(@RequestBody CustomGroupRequest request) {
        CustomGroup customGroup = this.customGroupService.queryById(request.getId());
        CustomGroupVo customGroupVo = new CustomGroupVo();
        if (customGroup != null) {
            customGroupVo = getCustomGroupVoList(Arrays.asList(customGroup)).get(0);
        }

        return BaseResponse.success(customGroupVo);
    }

    @Override
    public BaseResponse list(@RequestBody CustomGroupListRequest request) {
        PageInfo<CustomGroup> pageInfo = this.customGroupService.queryList(request);
        List<CustomGroupVo> customGroupVos = new ArrayList<>();
        if (pageInfo.getSize() > 0) {
            List<CustomGroup> list = pageInfo.getList();
            customGroupVos = getCustomGroupVoList(list);
        }
        return BaseResponse.success(CustomGroupListResponse.builder().customGroupPageResponse(
                new MicroServicePage<CustomGroupVo>(customGroupVos,
                        request.getPageable(),
                        pageInfo.getTotal()
                )
        ).build());
    }

    @Override
    public BaseResponse<CustomGroupListForParamResponse> listForParam(@RequestBody @Valid CustomGroupListParamRequest request) {
        List<CustomGroup> list = this.customGroupService.queryListForParam(request);
        List<CustomGroupVo> customGroupVos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            customGroupVos = getCustomGroupVoList(list);
        }
        return BaseResponse.success(CustomGroupListForParamResponse.builder().customGroupVos(customGroupVos).build());
    }

    @Override
    public BaseResponse checkCustomerTag(@RequestBody @Valid CustomGroupCheckTagRequest request) {
        return BaseResponse.success(this.customGroupService.checkCustomerTag(request.getTagId()));
    }

    /**
     * 做参数校验
     *
     * @param request
     */
    private void verify(CustomGroupRequest request) {
        if (CollectionUtils.isEmpty(request.getRegionList()) && request.getLastTradeTime() == null
                && request.getGtTradeCount() == null && request.getLtTradeCount() == null
                && request.getTradeAmountTime() == null && request.getGtTradeAmount() == null
                && request.getLtTradeAmount() == null
                && request.getAvgTradeAmountTime() == null && request.getGtAvgTradeAmount() == null
                && request.getLtAvgTradeAmount() == null
                && request.getGtCustomerGrowth() == null && request.getLtCustomerGrowth() == null
                && request.getGtPoint() == null && request.getLtPoint() == null
                && request.getGtBalance() == null && request.getLtBalance() == null
                && request.getRecentTradeTime() == null && request.getNoRecentTradeTime() == null
                && request.getRecentPayTradeTime() == null && request.getNoRecentPayTradeTime() == null
                && request.getRecentCartTime() == null && request.getNoRecentCartTime() == null
                && request.getRecentFlowTime() == null && request.getNoRecentFlowTime() == null
                && request.getRecentFavoriteTime() == null && request.getNoRecentFavoriteTime() == null
                && CollectionUtils.isEmpty(request.getCustomerLevel())
                && CollectionUtils.isEmpty(request.getCustomerTag())
                && request.getGtAdmissionTime() == null && request.getLtAdmissionTime() == null
                && request.getGtAge() == null && request.getLtAge() == null
                && request.getGender() == null && CollectionUtils.isEmpty(request.getAutoTags())
        ) {
            throw new SbcRuntimeException(CustomGroupErrorCode.PARAM_NOT_NULL);
        }
        if (StringUtils.isBlank(request.getGroupName())) {
            throw new SbcRuntimeException(CustomGroupErrorCode.NAME_NOT_NULL);
        }
        if (CollectionUtils.isEmpty(request.getRegionList())) {
            request.setRegionList(null);
        }
        if (CollectionUtils.isEmpty(request.getCustomerLevel())) {
            request.setCustomerLevel(null);
        } else {
            int dbLevelCount =
                    this.customerLevelService.queryCount(CustomerLevel.builder().customerLevelIdList(request.getCustomerLevel()).build());
            if (request.getCustomerLevel().size() != dbLevelCount) {
                throw new SbcRuntimeException(CustomGroupErrorCode.LEVEL_NOT_EXISTS);
            }
        }
        if (CollectionUtils.isEmpty(request.getCustomerTag())) {
            request.setCustomerTag(null);
        } else {
            int dbTagCount = this.customerTagService.findCountByIdListAndDelFlag(request.getCustomerTag());
            if (request.getCustomerTag().size() != dbTagCount) {
                throw new SbcRuntimeException(CustomGroupErrorCode.TAG_NOT_EXISTS);
            }
        }
        if (CollectionUtils.isEmpty(request.getAutoTags())) {
            request.setAutoTags(null);
        } else {
            checkAutoTags(request.getAutoTags());
        }
    }

    @Override
    public BaseResponse<CustomGroupQueryAllResponse> queryAll() {
        List<CustomGroupVo> voList = KsBeanUtil.convert(this.customGroupService.queryAll(), CustomGroupVo.class);
        return BaseResponse.success(CustomGroupQueryAllResponse.builder().customGroupVoList(voList).build());
    }

    private List<CustomGroupVo> getCustomGroupVoList(List<CustomGroup> list) {
        List<CustomGroupVo> customGroupVos = new ArrayList<>();
        List<Long> customerTagList = new ArrayList<>();
        List<Long> customerLevelList = new ArrayList<>();
        Map<Long, String> customerLevelMap = new HashMap<>();
        Map<Long, String> customerTagMap = new HashMap<>();
        List<Long> autoTagList = new ArrayList<>();
        List<Long> preferenceTagList = new ArrayList<>();
        for (CustomGroup customGroup : list) {
            CustomGroupVo customGroupVo = KsBeanUtil.convert(customGroup, CustomGroupVo.class);
            if (StringUtils.isNotEmpty(customGroup.getGroupDetail())) {
                CustomGroupDetailVo detailVo = JSONObject.parseObject(customGroup.getGroupDetail(),
                        CustomGroupDetailVo.class);
                customGroupVo.setCustomGroupDetail(detailVo);
                if (CollectionUtils.isNotEmpty(detailVo.getCustomerLevel())) {
                    customerLevelList.addAll(detailVo.getCustomerLevel());
                }
                if (CollectionUtils.isNotEmpty(detailVo.getCustomerTag())) {
                    customerTagList.addAll(detailVo.getCustomerTag());
                }
            }
            customGroupVos.add(customGroupVo);
        }
        if (CollectionUtils.isNotEmpty(customerLevelList)) {
            List<CustomerLevel> customerLevels =
                    this.customerLevelService.queryList(CustomerLevel.builder().customerLevelIdList(customerLevelList).build());
            customerLevelMap = customerLevels.stream().collect(Collectors.toMap(CustomerLevel::getCustomerLevelId,
                    CustomerLevel::getCustomerLevelName));
        }
        if (CollectionUtils.isNotEmpty(customerTagList)) {
            List<CustomerTag> customerTags =
                    this.customerTagService.list(CustomerTagQueryRequest.builder().idList(customerTagList).build());
            customerTagMap = customerTags.stream().collect(Collectors.toMap(CustomerTag::getId, CustomerTag::getName));
        }

        for (CustomGroupVo customGroupVo : customGroupVos) {
            if (CollectionUtils.isNotEmpty(customGroupVo.getCustomGroupDetail().getCustomerLevel())
                    && (CollectionUtils.isNotEmpty(customGroupVo.getCustomGroupDetail().getCustomerTag())
                    || CollectionUtils.isNotEmpty(customGroupVo.getCustomGroupDetail().getAutoTags()))) {
                List<String> customerLevelNames = getNames(customGroupVo.getCustomGroupDetail().getCustomerLevel(),
                        customerLevelMap);
                //自动标签名称，要显示在手动标签之前
                List<String> autoTagNames = getAutoTagNames(customGroupVo.getCustomGroupDetail().getAutoTags());
                //手动标签
                List<String> customerTagNames = getNames(customGroupVo.getCustomGroupDetail().getCustomerTag(),
                        customerTagMap);
                //自动标签要显示在手动标签之前
                if (CollectionUtils.isNotEmpty(customerTagNames)) {
                    autoTagNames.addAll(customerTagNames);
                }
                customGroupVo.setDefinition(String.format(customGroupVo.getDefinition(),
                        StringUtils.join(customerLevelNames, "、"),
                        StringUtils.join(autoTagNames, "、")));
            }
            if (CollectionUtils.isNotEmpty(customGroupVo.getCustomGroupDetail().getCustomerLevel())
                    && CollectionUtils.isEmpty(customGroupVo.getCustomGroupDetail().getCustomerTag())
                    && CollectionUtils.isEmpty(customGroupVo.getCustomGroupDetail().getAutoTags())) {
                List<String> customerLevelNames = getNames(customGroupVo.getCustomGroupDetail().getCustomerLevel(),
                        customerLevelMap);

                customGroupVo.setDefinition(String.format(customGroupVo.getDefinition(),
                        StringUtils.join(customerLevelNames, "、")
                ));
            }
            if (CollectionUtils.isEmpty(customGroupVo.getCustomGroupDetail().getCustomerLevel())
                    && (CollectionUtils.isNotEmpty(customGroupVo.getCustomGroupDetail().getCustomerTag())
                    || CollectionUtils.isNotEmpty(customGroupVo.getCustomGroupDetail().getAutoTags()))) {
                //自动标签名称，要显示在手动标签之前
                List<String> autoTagNames = getAutoTagNames(customGroupVo.getCustomGroupDetail().getAutoTags());
                //手动标签
                List<String> customerTagNames = getNames(customGroupVo.getCustomGroupDetail().getCustomerTag(),
                        customerTagMap);
                //自动标签要显示在手动标签之前
                if (CollectionUtils.isNotEmpty(customerTagNames)) {
                    autoTagNames.addAll(customerTagNames);
                }
                customGroupVo.setDefinition(String.format(customGroupVo.getDefinition(),
                        StringUtils.join(autoTagNames, "、")));
            }
        }
        return customGroupVos;
    }

    private List<String> getNames(List<Long> ids, Map<Long, String> map) {
        List<String> names = new ArrayList<>();
        if (CollectionUtils.isEmpty(ids)) {
            return names;
        }
        for (Long id : ids) {
            String name = map.get(id);
            if (StringUtils.isNotEmpty(name)) {
                names.add(name);
            }
        }
        return names;
    }

    /**
     * 解析获得自动标签名称字符串
     *
     * @param autoTagDTOS
     * @return
     */
    private List<String> getAutoTagNames(List<AutoTagDTO> autoTagDTOS) {
        List<String> names = new ArrayList<>();
        if (CollectionUtils.isEmpty(autoTagDTOS)) {
            return names;
        }
        //过滤出指标值范围和综合标签
        List<AutoTagDTO> autoTags =
                autoTagDTOS.stream().filter(autoTagDTO -> autoTagDTO.getType().equals(TagType.RANGE)
                        || autoTagDTO.getType().equals(TagType.MULTIPLE)).collect(Collectors.toList());
        //查询偏好标签组数据、指标值范围和综合标签(在同一个表里)
        List<Long> auto =
                autoTags.stream().map(AutoTagDTO::getChooseTags).flatMap(x -> x.stream().map(Long::parseLong))
                        .collect(Collectors.toList());
        List<AutoTag> autoTagList = autoTagService.list(AutoTagQueryRequest.builder().idList(auto).build());
        //开始拼接名称
        Map<Long, String> autoTagMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(autoTagList)) {
            autoTagMap = autoTagList.stream().collect(Collectors.toMap(AutoTag::getId, AutoTag::getTagName));
        }
        if (!autoTagMap.isEmpty()) {
            names.addAll(getNames(auto, autoTagMap));
        }
        return names;
    }

    private CustomGroup dataProcess(CustomGroupRequest request) {
        CustomGroup customGroup = new CustomGroup();
        customGroup = KsBeanUtil.convert(request, CustomGroup.class);
        CustomGroupDetailVo detailVo = KsBeanUtil.convert(request, CustomGroupDetailVo.class);

        String groupDetail = JSONObject.toJSONString(detailVo);
        customGroup.setGroupDetail(groupDetail);
        customGroup.setDefinition(getDefinition(detailVo));
        if (CollectionUtils.isNotEmpty(detailVo.getCustomerTag())) {
            customGroup.setCustomerTags(String.join(",", detailVo.getCustomerTag().stream().map(tag -> String.format(
                    "\"%s\"", tag)).collect(Collectors.toList())));
        }
        if (CollectionUtils.isNotEmpty(detailVo.getAutoTags())) {
            //指标值范围标签和综合标签，拼接方式为标签id逗号分隔
            List<String> autoTagIds = request.getAutoTags().stream().filter(autoTagDTO ->
                    autoTagDTO.getType().equals(TagType.MULTIPLE) || autoTagDTO.getType().equals(TagType.RANGE))
                    .map(AutoTagDTO::getChooseTags).reduce(new ArrayList<>(), (all, item) -> {
                        all.addAll(item);
                        return all;
                    });
            customGroup.setAutoTags(String.join(",",
                    autoTagIds.stream().map(tag -> String.format("\"%s\"", tag)).collect(Collectors.toList())));
            List<AutoTagDTO> preferenceTags = request.getAutoTags().stream().filter(autoTagDTO ->
                    autoTagDTO.getType().equals(TagType.PREFERENCE)).collect(Collectors.toList());
            //偏好标签，拼接方式为偏好标签组id-标签id，以逗号分隔
            List<String> preferenceTagIds = preferenceTags.stream().map(autoTagDTO -> {
                return autoTagDTO.getChooseTags().stream().map(a -> String.format("\"%s\"",
                        autoTagDTO.getTagId() + LINE + a)).collect(Collectors.toList());
            }).collect(Collectors.toList()).stream().reduce(new ArrayList<>(), (all, item) -> {
                all.addAll(item);
                return all;
            });
            customGroup.setPreferenceTags(String.join(",", preferenceTagIds));
        }
        return customGroup;
    }

    private String getDefinition(CustomGroupDetailVo detailVo) {
        StringBuilder definition = new StringBuilder("");
        if (CollectionUtils.isNotEmpty(detailVo.getRegionList())) {
            List<String> regionList = new ArrayList<>();
            for (RegionDTO region : detailVo.getRegionList()) {
                /*if(StringUtils.isNotEmpty(region.getProvinceName())) {
                    regionList.add(region.getProvinceName());
                }*/
                if (StringUtils.isNotEmpty(region.getRegionName())) {
                    regionList.add(region.getRegionName());
                }
            }
            if (CollectionUtils.isNotEmpty(regionList)) {
                definition.append(String.format(CustomGroupConstant.FM_REGIONS, StringUtils.join(regionList, "、")));
            }
        }

        if (detailVo.getLastTradeTime() != null) {
            definition.append(String.format(CustomGroupConstant.FM_LAST_TRADE, detailVo.getLastTradeTime()));
        }
        if (detailVo.getTradeCountTime() != null) {

            if (detailVo.getGtTradeCount() != null && detailVo.getLtTradeCount() != null) {
                definition.append(String.format(CustomGroupConstant.FM_TRADE_COUNT,
                        detailVo.getTradeCountTime(),
                        detailVo.getGtTradeCount() + "-" + detailVo.getLtTradeCount()));
            }
            if (detailVo.getGtTradeCount() == null && detailVo.getLtTradeCount() != null) {
                definition.append(String.format(CustomGroupConstant.FM_TRADE_COUNT,
                        detailVo.getTradeCountTime(),
                        "≤" + detailVo.getLtTradeCount()));
            }
            if (detailVo.getGtTradeCount() != null && detailVo.getLtTradeCount() == null) {
                definition.append(String.format(CustomGroupConstant.FM_TRADE_COUNT,
                        detailVo.getTradeCountTime(),
                        "≥" + detailVo.getGtTradeCount()));
            }
        }

        if (detailVo.getTradeAmountTime() != null) {

            if (detailVo.getGtTradeAmount() != null && detailVo.getLtTradeAmount() != null) {
                definition.append(String.format(CustomGroupConstant.FM_TRADE_AMOUNT,
                        detailVo.getTradeAmountTime(),
                        detailVo.getGtTradeAmount() + "-" + detailVo.getLtTradeAmount()));
            }
            if (detailVo.getGtTradeAmount() == null && detailVo.getLtTradeAmount() != null) {
                definition.append(String.format(CustomGroupConstant.FM_TRADE_AMOUNT,
                        detailVo.getTradeAmountTime(),
                        "≤" + detailVo.getLtTradeAmount()));
            }
            if (detailVo.getGtTradeAmount() != null && detailVo.getLtTradeAmount() == null) {
                definition.append(String.format(CustomGroupConstant.FM_TRADE_AMOUNT,
                        detailVo.getTradeAmountTime(),
                        "≥" + detailVo.getGtTradeAmount()));
            }
        }

        if (detailVo.getAvgTradeAmountTime() != null) {

            if (detailVo.getGtAvgTradeAmount() != null && detailVo.getLtAvgTradeAmount() != null) {
                definition.append(String.format(CustomGroupConstant.FM_AVG_TRADE_AMOUNT,
                        detailVo.getAvgTradeAmountTime(),
                        detailVo.getGtAvgTradeAmount() + "-" + detailVo.getLtAvgTradeAmount()));
            }
            if (detailVo.getGtAvgTradeAmount() == null && detailVo.getLtAvgTradeAmount() != null) {
                definition.append(String.format(CustomGroupConstant.FM_AVG_TRADE_AMOUNT,
                        detailVo.getAvgTradeAmountTime(),
                        "≤" + detailVo.getLtAvgTradeAmount()));
            }
            if (detailVo.getGtAvgTradeAmount() != null && detailVo.getLtAvgTradeAmount() == null) {
                definition.append(String.format(CustomGroupConstant.FM_AVG_TRADE_AMOUNT,
                        detailVo.getAvgTradeAmountTime(),
                        "≥" + detailVo.getGtAvgTradeAmount()));
            }
        }

        if (CollectionUtils.isNotEmpty(detailVo.getCustomerLevel())) {

            definition.append(CustomGroupConstant.FM_CUSTOMER_LEVEL);
        }


        if (detailVo.getGtCustomerGrowth() != null && detailVo.getLtCustomerGrowth() != null) {
            definition.append(String.format(CustomGroupConstant.FM_CUSTOMER_GROWTH,
                    detailVo.getGtCustomerGrowth() + "-" + detailVo.getLtCustomerGrowth()));
        }
        if (detailVo.getGtCustomerGrowth() == null && detailVo.getLtCustomerGrowth() != null) {
            definition.append(String.format(CustomGroupConstant.FM_CUSTOMER_GROWTH,
                    "≤" + detailVo.getLtCustomerGrowth()));
        }
        if (detailVo.getGtCustomerGrowth() != null && detailVo.getLtCustomerGrowth() == null) {
            definition.append(String.format(CustomGroupConstant.FM_CUSTOMER_GROWTH,
                    "≥" + detailVo.getGtCustomerGrowth()));
        }


        if (detailVo.getGtPoint() != null && detailVo.getLtPoint() != null) {
            definition.append(String.format(CustomGroupConstant.FM_CUSTOMER_POINTS,
                    detailVo.getGtPoint() + "-" + detailVo.getLtPoint()));
        }
        if (detailVo.getGtPoint() == null && detailVo.getLtPoint() != null) {
            definition.append(String.format(CustomGroupConstant.FM_CUSTOMER_POINTS,
                    "≤" + detailVo.getLtPoint()));
        }
        if (detailVo.getGtPoint() != null && detailVo.getLtPoint() == null) {
            definition.append(String.format(CustomGroupConstant.FM_CUSTOMER_POINTS,
                    "≥" + detailVo.getGtPoint()));
        }

        if (detailVo.getGtBalance() != null && detailVo.getLtBalance() != null) {
            definition.append(String.format(CustomGroupConstant.FM_CUSTOMER_BALANCE,
                    detailVo.getGtBalance() + "-" + detailVo.getLtBalance()));
        }
        if (detailVo.getGtBalance() == null && detailVo.getLtBalance() != null) {
            definition.append(String.format(CustomGroupConstant.FM_CUSTOMER_BALANCE,
                    "≤" + detailVo.getLtBalance()));
        }
        if (detailVo.getGtBalance() != null && detailVo.getLtBalance() == null) {
            definition.append(String.format(CustomGroupConstant.FM_CUSTOMER_BALANCE,
                    "≥" + detailVo.getGtBalance()));
        }

        if (detailVo.getRecentFlowTime() != null) {
            definition.append(String.format(CustomGroupConstant.FM_RECENT_FLOW, detailVo.getRecentFlowTime()));
        }
        if (detailVo.getNoRecentFlowTime() != null) {
            definition.append(String.format(CustomGroupConstant.FM_NO_RECENT_FLOW, detailVo.getNoRecentFlowTime()));
        }

        if (detailVo.getRecentFavoriteTime() != null) {
            definition.append(String.format(CustomGroupConstant.FM_RECENT_FAVORITE, detailVo.getRecentFavoriteTime()));
        }
        if (detailVo.getNoRecentFavoriteTime() != null) {
            definition.append(String.format(CustomGroupConstant.FM_NO_RECENT_FAVORITE,
                    detailVo.getNoRecentFavoriteTime()));
        }

        if (detailVo.getRecentCartTime() != null) {
            definition.append(String.format(CustomGroupConstant.FM_RECENT_CART, detailVo.getRecentCartTime()));
        }
        if (detailVo.getNoRecentCartTime() != null) {
            definition.append(String.format(CustomGroupConstant.FM_NO_RECENT_CART, detailVo.getNoRecentCartTime()));
        }

        if (detailVo.getRecentTradeTime() != null) {
            definition.append(String.format(CustomGroupConstant.FM_RECENT_TRADE, detailVo.getRecentTradeTime()));
        }
        if (detailVo.getNoRecentTradeTime() != null) {
            definition.append(String.format(CustomGroupConstant.FM_NO_RECENT_TRADE, detailVo.getNoRecentTradeTime()));
        }

        if (detailVo.getRecentPayTradeTime() != null) {
            definition.append(String.format(CustomGroupConstant.FM_RECENT_PAY_TRADE, detailVo.getRecentPayTradeTime()));
        }
        if (detailVo.getNoRecentPayTradeTime() != null) {
            definition.append(String.format(CustomGroupConstant.FM_NO_RECENT_PAY_TRADE,
                    detailVo.getNoRecentPayTradeTime()));
        }

        if (CollectionUtils.isNotEmpty(detailVo.getCustomerTag()) || CollectionUtils.isNotEmpty(detailVo.getAutoTags())) {
            definition.append(CustomGroupConstant.FM_CUSTOMER_TAG);
        }
        if (detailVo.getGender() != null && detailVo.getGender() == 0) {
            definition.append(CustomGroupConstant.FM_CUSTOMER_GENDER + ":女；");
        }
        if (detailVo.getGender() != null && detailVo.getGender() == 1) {
            definition.append(CustomGroupConstant.FM_CUSTOMER_GENDER + ":男；");
        }
        if (detailVo.getGtAge() != null && detailVo.getLtAge() != null) {
            definition.append(String.format(CustomGroupConstant.FM_CUSTOMER_AGE,
                    detailVo.getGtAge() + "-" + detailVo.getLtAge()));
        }
        if (detailVo.getGtAge() == null && detailVo.getLtAge() != null) {
            definition.append(String.format(CustomGroupConstant.FM_CUSTOMER_AGE,
                    "≤" + detailVo.getLtAge()));
        }
        if (detailVo.getGtAge() != null && detailVo.getLtAge() == null) {
            definition.append(String.format(CustomGroupConstant.FM_CUSTOMER_AGE,
                    "≥" + detailVo.getGtAge()));
        }
        if (detailVo.getGtAdmissionTime() != null && detailVo.getLtAdmissionTime() != null) {
            definition.append(String.format(CustomGroupConstant.FM_CUSTOMER_ADMISSIONTIME,
                    detailVo.getGtAdmissionTime() + "-" + detailVo.getLtAdmissionTime()));
        }
        if (detailVo.getGtAdmissionTime() == null && detailVo.getLtAdmissionTime() != null) {
            definition.append(String.format(CustomGroupConstant.FM_CUSTOMER_ADMISSIONTIME,
                    "≤" + detailVo.getLtAdmissionTime()));
        }
        if (detailVo.getGtAdmissionTime() != null && detailVo.getLtAdmissionTime() == null) {
            definition.append(String.format(CustomGroupConstant.FM_CUSTOMER_ADMISSIONTIME,
                    "≥" + detailVo.getGtAdmissionTime()));
        }

        if (detailVo.getAutoTags() != null && CollectionUtils.isNotEmpty(detailVo.getAutoTags())){
            detailVo.getAutoTags().forEach(item -> {
                if (item.getType().equals(TagType.PREFERENCE)){
                    definition.append(item.getName()).append("：");
                    List<AutotagPreference> autotagPreferences =
                            autotagPreferenceService.findByTagIdAndDimensionId(AutoPreferenceListRequest.builder()
                                    .tagId(item.getTagId()).dimensionIds(item.getChooseTags()).build());
                    autotagPreferences.forEach(preference -> {
                        definition.append(preference.getDetailName()).append("、");
                    });
                }
            });
        }
        return definition.toString();
    }


    private void checkAutoTags(List<AutoTagDTO> autoTags) {
        if (CollectionUtils.isEmpty(autoTags)) {
            throw new SbcRuntimeException(CustomGroupErrorCode.PARAM_NOT_NULL);
        }
        long count =
                autoTags.stream().filter(autoTagDTO -> CollectionUtils.isEmpty(autoTagDTO.getChooseTags())).count();
        if (count > 0) {
            throw new SbcRuntimeException(CustomGroupErrorCode.PARAM_NOT_NULL);
        }
        //校验指标值标签和综合类标签、偏好类标签在库里是否存在
        List<Long> autoTagIds =
                autoTags.stream().filter(autoTagDTO -> autoTagDTO.getType().equals(TagType.MULTIPLE)
                        || autoTagDTO.getType().equals(TagType.RANGE))
                        .map(AutoTagDTO::getChooseTags).flatMap(x -> x.stream().map(Long::parseLong))
                        .collect(Collectors.toList());

        autoTagIds.addAll(autoTags.stream().filter(autoTagDTO -> autoTagDTO.getType().equals(TagType.PREFERENCE))
                .map(AutoTagDTO::getTagId)
                .collect(Collectors.toList()));

        if (CollectionUtils.isNotEmpty(autoTagIds)) {
            int autoTagCount = autoTagService.countAllByIds(AutoTagQueryRequest.builder().idList(autoTagIds).build());
            if (autoTagCount != autoTagIds.size()) {
                throw new SbcRuntimeException(CustomGroupErrorCode.TAG_NOT_EXISTS);
            }
        }
    }
}
