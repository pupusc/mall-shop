package com.wanmi.sbc.customer.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.StringUtil;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @ClassName EsStoreInfoVo
 * @Description
 * @Author yangzhen
 * @Date 2020/12/9 21:00
 * @Version 1.0
 */
@Data
@ApiModel
public class EsStoreCompanyInfoVo {



    /**
     * 商家编号
     */
    private Long companyInfoId;

    /**
     * 商家编号
     */
    private String companyCode;

    /**
     * 商家账号
     */
    private String accountName;



    /**
     * 账号状态
     */
    private Integer accountState;

    /**
     * 账号禁用原因
     */
    private String accountDisableReason;



    /**
     * 是否是主账号
     */
    private Integer isMasterAccount;


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
     * 从数据库实体转换为返回前端的用户信息
     * (字段顺序不可变)
     */
    public EsStoreCompanyInfoVo convertFromNativeSQLResult(Object result) {
        Object[] results = StringUtil.cast(result, Object[].class);
        if (results == null || results.length < 1) {
            return this;
        }
        EsStoreCompanyInfoVo response = new EsStoreCompanyInfoVo();
        response.setCompanyInfoId(((Integer) ((Object[]) result)[0]).longValue());
        response.setCompanyCode((String)((Object[]) result)[1]);
        response.setAccountName((String)((Object[]) result)[2]);




        Byte accountState = StringUtil.cast(results, 3, Byte.class);
        response.setAccountState(accountState !=null ? accountState.intValue():null);
        response.setAccountDisableReason((String)((Object[]) result)[4]);


        response.setIsMasterAccount(((Byte)((Object[]) result)[5]).intValue());


        Byte companyInfoDelFlag = StringUtil.cast(results, 6, Byte.class);
        response.setCompanyInfoDelFlag(companyInfoDelFlag !=null ? companyInfoDelFlag.intValue():null);

        Byte employeeDelFlag = StringUtil.cast(results, 7, Byte.class);
        response.setEmployeeDelFlag(employeeDelFlag !=null ? employeeDelFlag.intValue():null);
        response.setRemitAffirm(((Boolean)((Object[]) result)[8]));


        Timestamp applyEnterTime = StringUtil.cast(results, 9, Timestamp.class);
        response.setApplyEnterTime(applyEnterTime !=null ? applyEnterTime.toLocalDateTime() : null);

        return response;
    }


}
