package com.wanmi.sbc.customer.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.StoreType;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.customer.bean.enums.AccountState;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.StoreResponseState;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @ClassName EsStoreInfoVo
 * @Description
 * @Author yangzhen
 * @Date 2020/12/9 21:00
 * @Version 1.0
 */
@Data
@ApiModel
public class EsStoreInfoVo {

    /**
     * 公司信息ID
     */
    private Long companyInfoId;

    /**
     * 店铺ID
     */
    private Long storeId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 店铺名称
     */
    private String supplierName;

    /**
     * 商家编号
     */
    private String companyCode;

    /**
     * 商家账号
     */
    private String accountName;



    /**
     * 商家类型 0、平台自营 1、第三方商家
     */
    private Integer companyType;

    /**
     * 审核状态 0、待审核 1、已审核 2、审核未通过
     */
    private Integer auditState;

    /**
     * 审核未通过原因
     */
    private String auditReason;

    /**
     * 账号状态
     */
    private Integer accountState;

    /**
     * 账号禁用原因
     */
    private String accountDisableReason;

    /**
     * 店铺状态 0、开启 1、关店
     */
    private Integer storeState;

    /**
     * 账号关闭原因
     */
    private String storeClosedReason;

    /**
     * 商家类型0品牌商城，1商家
     */
    private Integer storeType;

    /**
     * 是否是主账号
     */
    private Integer isMasterAccount;

    /**
     * 店铺删除状态
     */
    private Integer storeDelFlag;

    /**
     * 公司删除状态
     */
    private Integer companyInfoDelFlag;

    /**
     * 员工删除状态
     */
    private Integer employeeDelFlag;

    /**
     * 是否确认打款 (-1:全部,0:否,1:是)
     */
    private Boolean remitAffirm;


    /**
     * 创建结束时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime applyEnterTime;


    /**
     * 创建结束时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime contractStartDate;


    /**
     * 创建结束时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime contractEndDate;

    /**
     * 从数据库实体转换为返回前端的用户信息
     * (字段顺序不可变)
     */
    public  EsStoreInfoVo convertFromNativeSQLResult(Object result) {
        Object[] results = StringUtil.cast(result, Object[].class);
        if (results == null || results.length < 1) {
            return this;
        }
        EsStoreInfoVo response = new EsStoreInfoVo();
        response.setCompanyInfoId(((BigInteger) ((Object[]) result)[0]).longValue());
        response.setStoreId(((BigInteger) ((Object[]) result)[1]).longValue());
        response.setStoreName((String)((Object[]) result)[2]);
        response.setSupplierName((String)((Object[]) result)[3]);
        response.setCompanyCode((String)((Object[]) result)[4]);
        response.setAccountName((String)((Object[]) result)[5]);

        Byte companyType = StringUtil.cast(results, 6, Byte.class);
        response.setCompanyType(companyType !=null ? companyType.intValue():null);


        Byte auditState = StringUtil.cast(results, 7, Byte.class);
        response.setAuditState(auditState !=null ? auditState.intValue():null);
        response.setAuditReason((String)((Object[]) result)[8]);

        Byte accountState = StringUtil.cast(results, 9, Byte.class);
        response.setAccountState(accountState !=null ? accountState.intValue():null);
        response.setAccountDisableReason((String)((Object[]) result)[10]);

        Byte storeState = StringUtil.cast(results, 11, Byte.class);
        response.setStoreState(storeState !=null ? storeState.intValue():null);
        response.setStoreClosedReason((String)((Object[]) result)[12]);

        Byte storeType = StringUtil.cast(results, 13, Byte.class);
        response.setStoreType(storeType !=null ? storeType.intValue():null);
        response.setIsMasterAccount(((Byte)((Object[]) result)[14]).intValue());

        Byte storeDelFlag = StringUtil.cast(results, 15, Byte.class);
        response.setStoreDelFlag(storeDelFlag !=null ? storeDelFlag.intValue():null);

        Byte companyInfoDelFlag = StringUtil.cast(results, 16, Byte.class);
        response.setCompanyInfoDelFlag(companyInfoDelFlag !=null ? companyInfoDelFlag.intValue():null);

        Byte employeeDelFlag = StringUtil.cast(results, 17, Byte.class);
        response.setEmployeeDelFlag(employeeDelFlag !=null ? employeeDelFlag.intValue():null);

        response.setRemitAffirm(((Boolean)((Object[]) result)[18]));


        Timestamp applyEnterTime = StringUtil.cast(results, 19, Timestamp.class);
        response.setApplyEnterTime(applyEnterTime !=null ? applyEnterTime.toLocalDateTime() : null);

        Timestamp contractStartDate = StringUtil.cast(results, 20, Timestamp.class);
        response.setContractStartDate(contractStartDate !=null ? contractStartDate.toLocalDateTime() : null);

        Timestamp contractEndDate = StringUtil.cast(results, 21, Timestamp.class);
        response.setContractEndDate(contractEndDate !=null ? contractEndDate.toLocalDateTime() : null);
        return response;
    }


}
