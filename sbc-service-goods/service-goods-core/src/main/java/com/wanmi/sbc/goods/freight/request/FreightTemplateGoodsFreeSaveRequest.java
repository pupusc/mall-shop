package com.wanmi.sbc.goods.freight.request;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.ValidateUtil;
import com.wanmi.sbc.goods.bean.enums.ConditionType;
import com.wanmi.sbc.goods.bean.enums.DeliverWay;
import com.wanmi.sbc.goods.bean.enums.ValuationType;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.Validate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * Created by sunkun on 2018/5/4.
 */
@Getter
@Setter
public class FreightTemplateGoodsFreeSaveRequest extends BaseRequest {

    private static final long serialVersionUID = -1585424215325010949L;

    /**
     * 主键标识
     */
    private Long id;

    /**
     * 配送地id(逗号分隔)
     */
    @NotNull
    @Size(min = 1)
    private String[] destinationArea;

    /**
     * 配送地名称(逗号分隔)
     */
    @NotNull
    @Size(min = 1)
    private String[] destinationAreaName;

    /**
     * 运送方式(1:快递配送)
     */
    private DeliverWay deliverWay = DeliverWay.EXPRESS;

    /**
     * 计价方式(0:按件数,1:按重量,2:按体积)
     */
    @NotNull
    private ValuationType valuationType;

    /**
     * 包邮条件类别(0:件/重/体积计价方式,1:金额,2:计价方式+金额)
     */
    @NotNull
    private ConditionType conditionType;

    /**
     * 包邮条件1(件/重/体积)
     */
    @Min(value = 0)
    private BigDecimal conditionOne;

    /**
     * 包邮条件2(金额)
     */
    @Min(value = 0)
    private BigDecimal conditionTwo;

    /**
     * 删除标识
     */
    private DeleteFlag delFlag;

    @Override
    public void checkParam() {
        if (this.conditionType == ConditionType.VALUATION) {
            Validate.notNull(this.conditionOne, ValidateUtil.NULL_EX_MESSAGE, "conditionType");
        } else if (this.conditionType == ConditionType.MONEY) {
            Validate.notNull(this.conditionTwo, ValidateUtil.NULL_EX_MESSAGE, "conditionTwo");
        }else{
            Validate.notNull(this.conditionOne, ValidateUtil.NULL_EX_MESSAGE, "conditionType");
            Validate.notNull(this.conditionTwo, ValidateUtil.NULL_EX_MESSAGE, "conditionTwo");
        }
    }
}
