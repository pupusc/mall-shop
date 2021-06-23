package com.wanmi.sbc.customer.api.request.store;

import com.wanmi.sbc.common.annotation.CanEmpty;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.ValidateUtil;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.wanmi.sbc.common.util.ValidateUtil.*;

/**
 * @Author: songhanlin
 * @Date: Created In 下午3:51 2017/11/6
 * @Description: 驳回/通过 审核
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreAuditRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = 3302655499380865353L;

    /**
     * 店铺Id
     */
    @ApiModelProperty(value = "店铺Id")
    private Long storeId;

    /**
     * 驳回状态 0：待审核 1：已审核 2：审核未通过
     */
    @ApiModelProperty(value = "驳回状态")
    private CheckState auditState;

    /**
     * 驳回原因
     */
    @ApiModelProperty(value = "驳回原因")
    @CanEmpty
    private String auditReason;

    /**
     * 签约开始日期
     */
    @ApiModelProperty(value = "签约开始日期")
    private String contractStartDate;

    /**
     * 签约结束日期
     */
    @ApiModelProperty(value = "签约结束日期")
    private String contractEndDate;

    /**
     * 结算日
     */
    @ApiModelProperty(value = "结算日")
    private List<Long> days = new ArrayList<>();

    /**
     * 商家类型
     */
    @ApiModelProperty(value = "商家类型")
    private DefaultFlag companyType;

    /**
     * 结算日期字符串
     */
    @ApiModelProperty(value = "结算日期字符串")
    private String accountDay;


    @Override
    public void checkParam() {
        //店铺Id不能为空
        if (Objects.isNull(storeId)) {
            Validate.notNull(storeId, NULL_EX_MESSAGE, "storeId");
        }
        //驳回/通过状态 不能为空
        if (Objects.isNull(auditState)) {
            Validate.notNull(auditState, NULL_EX_MESSAGE, "auditState");
        } else if (auditState == auditState.WAIT_CHECK) { //如果是等待审核, 则操作有误
            Validate.validState(Boolean.FALSE);
        } else if (auditState == auditState.NOT_PASS) { //如果是驳回状态
            if (StringUtils.isBlank(auditReason)) { //驳回原因不能为空
                Validate.notBlank(auditReason, BLANK_EX_MESSAGE, "auditReason");
            } else if (!ValidateUtil.isBetweenLen(auditReason, 1, 100)) { //原因长度为1-100以内
                Validate.validState(Boolean.FALSE);
            }
        } else {
            if (StringUtils.isBlank(contractStartDate)) { //起始日期不能为空
                Validate.notBlank(contractStartDate, BLANK_EX_MESSAGE, "contractStartDate");
            } else if (StringUtils.isBlank(contractStartDate)) { //起始日期格式错误

            } else if (StringUtils.isBlank(contractEndDate)) { //结束日期不能为空
                Validate.notBlank(contractEndDate, BLANK_EX_MESSAGE, "contractEndDate");
            } else if (StringUtils.isBlank(contractEndDate)) { //结束日期格式错误

            } else if (CollectionUtils.isEmpty(days)) { //签约日期时间不能为空
                Validate.notEmpty(days, EMPTY_ARRAY_EX_MESSAGE, "days");
            } else if (Objects.isNull(companyType)) { //商家类型不能为空
                Validate.notNull(companyType, NULL_EX_MESSAGE, "companyType");
            } else {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime.parse(contractEndDate, formatter);
                    LocalDateTime.parse(contractStartDate, formatter);
                } catch (Exception e) {
                    Validate.validState(Boolean.FALSE);
                }
            }
        }
    }
}
