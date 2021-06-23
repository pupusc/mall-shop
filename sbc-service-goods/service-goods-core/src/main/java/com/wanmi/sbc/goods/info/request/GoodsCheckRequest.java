package com.wanmi.sbc.goods.info.request;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.util.ValidateUtil;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

/**
 * 商品审核请求
 * Created by daiyitian on 2017/3/24.
 */
@Data
public class GoodsCheckRequest extends BaseRequest {

    /**
     * SpuId
     */
    @NotEmpty
    private List<String> goodsIds;

    /**
     * 审核状态
     */
    @NotNull
    private CheckStatus auditStatus;

    /**
     * 审核人名称
     */
    private String checker;

    /**
     * 审核驳回原因
     */
    @Length(max = 100)
    private String auditReason;

    /**
     * 是否处理商品库商品
     */
    private Boolean dealStandardGoodsFlag = Boolean.FALSE;

    @Override
    public void checkParam() {
        if((Objects.equals(auditStatus, CheckStatus.NOT_PASS) || Objects.equals(auditStatus, CheckStatus.FORBADE)) && StringUtils.isBlank(auditReason)){
            Validate.notBlank(auditReason, ValidateUtil.BLANK_EX_MESSAGE, "auditReason");
        }
    }
}
