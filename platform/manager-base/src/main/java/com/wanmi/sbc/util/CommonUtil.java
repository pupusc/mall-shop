package com.wanmi.sbc.util;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.base.VASEntity;
import com.wanmi.sbc.common.constant.VASStatus;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.enums.VASConstants;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.redis.CacheKeyConstant;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.setting.bean.enums.ConfigKey;
import com.wanmi.sbc.vas.api.constants.iep.IepServiceErrorCode;
import com.wanmi.sbc.vas.api.provider.iepsetting.IepSettingQueryProvider;
import com.wanmi.sbc.vas.bean.vo.IepSettingVO;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * BFF公共工具类
 * Created by daiyitian on 15/12/29.
 */
@Slf4j
@Component
public final class CommonUtil {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisService redisService;

    @Autowired
    private IepSettingQueryProvider iepSettingQueryProvider;

    /**
     * 获取当前登录编号
     *
     * @return
     */
    public String getOperatorId() {
        return getOperator().getUserId();
    }

    /**
     * 获取当前登录的公司信息ID
     *
     * @return
     */
    public Long getCompanyInfoId() {
        String companyInfoId = this.getOperator().getAdminId().toString();
        if (StringUtils.isNotBlank(companyInfoId)) {
            return Long.valueOf(companyInfoId);
        }
        return null;
    }

    /**
     * 获取当前登录的店铺信息ID
     *
     * @return
     */
    public Long getStoreId() {
        String storeId = this.getOperator().getStoreId();
        if (StringUtils.isNotBlank(storeId)) {
            return Long.valueOf(storeId);
        }
        return null;
    }

    /**
     * 获取当前登录的店铺信息ID
     * 若没有设置默认值为boss对应的店铺ID
     *
     * @return
     */
    public Long getStoreIdWithDefault() {
        String storeId = this.getOperator().getStoreId();
        if (StringUtils.isNotBlank(storeId)) {
            return Long.valueOf(storeId);
        }
        return Constants.BOSS_DEFAULT_STORE_ID;
    }


    /**
     * 获取当前登录的商家类型
     *
     * @return
     */
    public BoolFlag getCompanyType() {
        return this.getOperator().getCompanyType();
    }


    /**
     * 获取当前登录的商家客户等级类别
     * 自营 平台等级
     * 店铺 店铺等级
     *
     * @return
     */
    public DefaultFlag getCustomerLevelType() {
        return this.getOperator().getCompanyType().equals(BoolFlag.YES) ? DefaultFlag.NO : DefaultFlag.YES;
    }

    /**
     * 获取当前登录账号(手机号)
     *
     * @return
     */
    public String getAccountName() {
        return this.getOperator().getName();
    }

    /**
     * 获取当前登录对象
     *
     * @return
     */
    public Operator getOperator() {
        Claims claims = (Claims) HttpUtil.getRequest().getAttribute("claims");
        if (claims == null) {
            throw new SbcRuntimeException(CommonErrorCode.PERMISSION_DENIED);
        }

        Object vasObject = claims.get(ConfigKey.VALUE_ADDED_SERVICES.toString());
        List<VASEntity> services = new ArrayList<>();
        if (Objects.nonNull(vasObject)) {
            String vasJson = vasObject.toString();
            Map<String, String> map = (Map<String, String>) JSONObject.parse(vasJson);
            services = map.entrySet().stream().map(m -> {
                VASEntity vasEntity = new VASEntity();
                vasEntity.setServiceName(VASConstants.fromValue(m.getKey()));
                vasEntity.setServiceStatus(StringUtils.equals(VASStatus.ENABLE.toValue(), m.getValue()));
                return vasEntity;
            }).collect(Collectors.toList());
        }

        return Operator.builder()
                .platform(Platform.forValue(ObjectUtils.toString(claims.get("platform"))))
                .adminId(ObjectUtils.toString(claims.get("adminId")))
                .ip(ObjectUtils.toString(claims.get("ip")))
                .account(ObjectUtils.toString(claims.get("EmployeeName")))
                .name(ObjectUtils.toString(claims.get("realEmployeeName")))
                .userId(ObjectUtils.toString(claims.get("employeeId")))
                .storeId(ObjectUtils.toString(claims.get("storeId")))
                .companyType(BoolFlag.fromValue(ObjectUtils.toString(claims.get("companyType"))))
                .companyInfoId(Long.valueOf(Objects.toString(claims.get("companyInfoId"), "0")))
                .services(services)
                .build();
    }

    /**
     * 查询指定增值服务是否购买
     *
     * @param constants
     * @return
     */
    public boolean findVASBuyOrNot(VASConstants constants) {
        boolean flag = false;
        List<VASEntity> list = this.getAllServices();
        VASEntity vasEntity =
                list.stream().filter(f -> StringUtils.equals(f.getServiceName().toValue(), constants.toValue()) && f.isServiceStatus()).findFirst().orElse(null);
        if (Objects.nonNull(vasEntity)) {
            flag = vasEntity.isServiceStatus();
        }
        return flag;
    }

    /**
     * 获取所有增值服务
     *
     * @return
     */
    public List<VASEntity> getAllServices() {
        return this.getOperator().getServices();
    }

    /**
     * 获取当前登录对象,可为空
     *
     * @return
     */
    public Operator getOperatorWithNull() {
        Claims claims = (Claims) HttpUtil.getRequest().getAttribute("claims");
        if (claims == null) {
            return null;
        }

        return Operator.builder()
                .platform(Platform.forValue(ObjectUtils.toString(claims.get("platform"))))
                .adminId(ObjectUtils.toString(claims.get("adminId")))
                .ip(ObjectUtils.toString(claims.get("ip")))
                .name(ObjectUtils.toString(claims.get("EmployeeName")))
                .account(ObjectUtils.toString(claims.get("EmployeeName")))
                .userId(ObjectUtils.toString(claims.get("employeeId")))
                .storeId(ObjectUtils.toString(claims.get("storeId")))
                .build();
    }

    /**
     * cookie中获取token，以获得用户信息
     *
     * @return
     */
    public Operator getOperatorFromCookie() {
        String token = "";
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        Claims claims = jwtUtil.validate(token);
        if (claims == null) {
            return null;
        }
        return Operator.builder()
                .platform(Platform.forValue(ObjectUtils.toString(claims.get("platform"))))
                .adminId(ObjectUtils.toString(claims.get("adminId")))
                .ip(ObjectUtils.toString(claims.get("ip")))
                .name(ObjectUtils.toString(claims.get("EmployeeName")))
                .account(ObjectUtils.toString(claims.get("EmployeeName")))
                .userId(ObjectUtils.toString(claims.get("employeeId")))
                .storeId(ObjectUtils.toString(claims.get("storeId")))
                .build();
    }

    /**
     * 获取企业购配置信息
     *
     * @return
     */
    public IepSettingVO getIepSettingInfo() {
        if (!this.findVASBuyOrNot(VASConstants.VAS_IEP_SETTING)) {
            throw new SbcRuntimeException(IepServiceErrorCode.DID_NOT_BUY_IEP_SERVICE);
        }
        if (redisService.hasKey(CacheKeyConstant.IEP_SETTING)) {
            IepSettingVO iepSettingVO = JSONObject.parseObject(redisService.getString(CacheKeyConstant.IEP_SETTING),
                    IepSettingVO.class);
            return iepSettingVO;
        } else {
            return iepSettingQueryProvider.cacheIepSetting().getContext().getIepSettingVO();
        }
    }

    /**
     * 从cookie中获取店铺Id
     *
     * @return
     */
    public Long getStoreIdFromCookieWithNotLoginDefault() {
        //先从cookie取
        Operator operator = this.getOperator();
        if (Objects.nonNull(operator)) {
            String storeId = operator.getStoreId();
            if (StringUtils.isNotBlank(storeId)) {
                return Long.valueOf(storeId);
            }
        }
        return Constants.BOSS_DEFAULT_STORE_ID;
    }




}
