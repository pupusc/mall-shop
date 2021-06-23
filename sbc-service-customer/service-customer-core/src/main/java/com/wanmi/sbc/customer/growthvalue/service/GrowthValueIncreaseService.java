package com.wanmi.sbc.customer.growthvalue.service;

import com.alibaba.fastjson.JSONObject;
import io.seata.spring.annotation.GlobalTransactional;
import com.wanmi.sbc.common.constant.MQConstant;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.request.growthvalue.CustomerGrowthValueAddRequest;
import com.wanmi.sbc.customer.bean.enums.GrowthValueServiceType;
import com.wanmi.sbc.customer.bean.enums.LevelRightsType;
import com.wanmi.sbc.customer.bean.enums.OperateType;
import com.wanmi.sbc.customer.growthvalue.builder.GrowthValueType;
import com.wanmi.sbc.customer.growthvalue.builder.GrowthValueTypeConfig;
import com.wanmi.sbc.customer.growthvalue.model.root.CustomerGrowthValue;
import com.wanmi.sbc.customer.growthvalue.repository.CustomerGrowthValueRepository;
import com.wanmi.sbc.customer.level.model.root.CustomerLevel;
import com.wanmi.sbc.customer.level.repository.CustomerLevelRepository;
import com.wanmi.sbc.customer.levelrights.model.root.CustomerLevelRights;
import com.wanmi.sbc.customer.levelrights.model.root.CustomerLevelRightsRel;
import com.wanmi.sbc.customer.levelrights.repository.CustomerLevelRightsRelRepository;
import com.wanmi.sbc.customer.levelrights.repository.CustomerLevelRightsRepository;
import com.wanmi.sbc.customer.model.root.Customer;
import com.wanmi.sbc.customer.repository.CustomerRepository;
import com.wanmi.sbc.setting.api.provider.GrowthValueBasicRuleQueryProvider;
import com.wanmi.sbc.setting.api.provider.SystemGrowthValueConfigQueryProvider;
import com.wanmi.sbc.setting.bean.vo.ConfigVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * <p>客户成长值明细表业务逻辑</p>
 */
@Service("GrowthValueIncreaseService")
@EnableBinding
public class GrowthValueIncreaseService implements InitializingBean {

    @Autowired
    private GrowthValueBasicRuleQueryProvider growthValueBasicRuleQueryProvider;

    @Autowired
    private SystemGrowthValueConfigQueryProvider systemGrowthValueConfigQueryProvider;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerLevelRepository customerLevelRepository;

    @Autowired
    private CustomerGrowthValueRepository customerGrowthValueRepository;

    @Autowired
    private CustomerLevelRightsRepository customerLevelRightsRepository;

    @Autowired
    private CustomerLevelRightsRelRepository customerLevelRightsRelRepository;

    @Autowired
    private BinderAwareChannelResolver resolver;

    @Autowired
    private List<GrowthValueType> growthValueTypeBuilders;

    private Map<GrowthValueServiceType, GrowthValueType> growthValueBuilderMap = new ConcurrentHashMap<>();

    /**
     * 成长值增长
     *
     * @param request 成长值增长参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void increaseGrowthValue(CustomerGrowthValueAddRequest request) {
        // 查询成长值体系开关
        Boolean isGrowthValueOpen = systemGrowthValueConfigQueryProvider.isGrowthValueOpen().getContext().isOpen();
        if (!isGrowthValueOpen) return;// 未启用成长值体系

        CustomerGrowthValue growthValue = new CustomerGrowthValue();
        KsBeanUtil.copyPropertiesThird(request, growthValue);
        if (Objects.isNull(growthValue.getOpTime())) {
            growthValue.setOpTime(LocalDateTime.now());
        }
        growthValue.setType(OperateType.GROWTH);
        growthValue.setDelFlag(DeleteFlag.NO);
        // 查询本次应增加的成长值，不为空表示是订单完成增加的成长值
        Long growthValueIncrease = growthValue.getGrowthValue();
        if (Objects.isNull(growthValueIncrease)) {
            // 查询该类型成长值基础规则
            ConfigVO config = growthValueBasicRuleQueryProvider.findGrowthValueByConfigType(
                    GrowthValueTypeConfig.CONFIG_TYPE_KEY_MAP.get(growthValue.getServiceType())).getContext();
            if (Objects.isNull(config) || config.getStatus() != 1) return;// 该成长值类型未开启

            JSONObject context = JSONObject.parseObject(config.getContext());
            if (StringUtils.isBlank(context.get("value").toString())) return;// 成长值规则为空

            // 根据成长值业务类型查询对应的实现类
            GrowthValueType type = growthValueBuilderMap.get(growthValue.getServiceType());
            // 未找到对应实现类
            if (type == null) return;
            // 在对应的实现类中判断本次应增长的成长值
            growthValueIncrease = type.getIncreaseGrowthValue(context, growthValue);
            // 超过该类型成长值获取的次数或限额
            if (growthValueIncrease.equals(0L)) return;
            growthValue.setGrowthValue(growthValueIncrease);
        }
        customerGrowthValueRepository.save(growthValue);

        updateCustomerLevel(growthValue);
    }

    /**
     * 用户升级
     *
     * @param growthValueDetail
     */
    private void updateCustomerLevel(CustomerGrowthValue growthValueDetail) {
        Customer customer = customerRepository.findById(growthValueDetail.getCustomerId()).orElse(null);
        if (Objects.isNull(customer)){
            return;
        }
        Long growthValueOrigin = customer.getGrowthValue() == null ? 0 : customer.getGrowthValue();
        Long growthValueTotal = growthValueOrigin + growthValueDetail.getGrowthValue();// 增长后会员总成长值
        customer.setGrowthValue(growthValueTotal);
        // 查询所有平台等级,按成长值降序
        List<CustomerLevel> allCustomerLevelList = customerLevelRepository.listLevelOrderByValueDesc();
        // 原成长值应处等级
        CustomerLevel originalRealLevel = allCustomerLevelList.stream()
                .filter(level -> growthValueOrigin >= level.getGrowthValue())
                .findFirst().get();
        // 成长值增长后应达到的等级
        CustomerLevel willReachLevel = allCustomerLevelList.stream()
                .filter(level -> growthValueTotal >= level.getGrowthValue())
                .findFirst().get();
        // 查询客户晋升的等级列表，用于发放权益
        List<CustomerLevel> levels = new ArrayList<>();
        if (customer.getCustomerLevelId() <= originalRealLevel.getCustomerLevelId()) {
            // 客户当前等级 <= 原成长值应处等级
            levels = increaseLevels(allCustomerLevelList, customer.getCustomerLevelId(), willReachLevel.getCustomerLevelId());
            customer.setCustomerLevelId(willReachLevel.getCustomerLevelId());
        } else if (customer.getCustomerLevelId() >= willReachLevel.getCustomerLevelId()) {
            // 客户当前等级 >= 成长值增长后应达到的等级
            levels = increaseLevels(allCustomerLevelList, originalRealLevel.getCustomerLevelId(), customer.getCustomerLevelId());
            customer.setCustomerLevelId(customer.getCustomerLevelId());
        } else if (customer.getCustomerLevelId() >= originalRealLevel.getCustomerLevelId()
                && customer.getCustomerLevelId() <= willReachLevel.getCustomerLevelId()) {
            // 原成长值应处等级 <= 客户当前等级 <=成长值增长后应达到的等级
            levels = increaseLevels(allCustomerLevelList, customer.getCustomerLevelId(), willReachLevel.getCustomerLevelId());
            customer.setCustomerLevelId(willReachLevel.getCustomerLevelId());
        }
        levels.forEach(level -> issueLevelRights(growthValueDetail.getCustomerId(), level));
        customerRepository.save(customer);
    }

    /**
     * 即将增长的等级
     *
     * @param allCustomerLevelList
     * @param lowerLevel
     * @param higherLevel
     * @return
     */
    private List<CustomerLevel> increaseLevels(List<CustomerLevel> allCustomerLevelList, Long lowerLevel, Long higherLevel) {
        return allCustomerLevelList.stream()
                .filter(level -> level.getCustomerLevelId() > lowerLevel && level.getCustomerLevelId() <= higherLevel)
                .collect(Collectors.toList());
    }

    /**
     * 发放等级券礼包
     *
     * @param customerId
     * @param level
     */
    private void issueLevelRights(String customerId, CustomerLevel level) {
        // 根据会员等级Id查询权益与等级的关联表 判断对应的等级权益是否有券礼包 查询达到等级发放的优惠券
        List<CustomerLevelRightsRel> rightsRels = customerLevelRightsRelRepository.findByCustomerLevelId(level.getCustomerLevelId());
        rightsRels.forEach(rightsRel -> {
            CustomerLevelRights rights = customerLevelRightsRepository.findByRightsId(rightsRel.getRightsId());
            // 1.权益为券礼包  2.优惠券活动id不为null  3.优惠券类型为达到等级即发放
            if (rights.getRightsType() == LevelRightsType.COUPON_GIFT
                    && rights.getActivityId() != null
                    && JSONObject.parseObject(rights.getRightsRule()).get("type").equals("issueOnce")) {
                Map<String, Object> map = new HashMap<>();
                map.put("customerId", customerId);
                map.put("activityId", rights.getActivityId());
                // mq通知marketing模块发放优惠券
                resolver.resolveDestination(MQConstant.ISSUE_COUPONS).send(new GenericMessage<>(JSONObject.toJSONString(map)));
            }
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        growthValueTypeBuilders.forEach(builder -> {
            List<GrowthValueServiceType> serviceTypeList = builder.supportGrowthValueType();
            serviceTypeList.forEach(serviceType -> growthValueBuilderMap.put(serviceType, builder));
        });
    }
}
