package com.wanmi.sbc.goods.freight.request;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.ValidateUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.Validate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by sunkun on 2018/5/4.
 */
@Getter
@Setter
public class FreightTemplateGoodsExpressSaveRequest extends BaseRequest {

    private static final long serialVersionUID = 8377961552042020183L;

    /**
     * 主键标识
     */
    private Long id;

    /**
     * 配送地id(逗号分隔)
     */
    private String[] destinationArea;

    /**
     * 配送地名称(逗号分隔)
     */
    private String[] destinationAreaName;

    /**
     * 首件/重/体积
     */
    @NotNull
    @Min(value = 0)
    private BigDecimal freightStartNum;

    /**
     * 对应于首件/重/体积的起步价
     */
    @NotNull
    @Min(value = 0)
    private BigDecimal freightStartPrice;

    /**
     * 续件/重/体积
     */
    @NotNull
    @Min(value = 0)
    private BigDecimal freightPlusNum;

    /**
     * 对应于续件/重/体积的价格
     */
    @NotNull
    @Min(value = 0)
    private BigDecimal freightPlusPrice;

    /**
     * 是否默认(0:否,1:是)
     */
    @NotNull
    private DefaultFlag defaultFlag;

    /**
     * 删除标识
     */
    private DeleteFlag delFlag;


    @Override
    public void checkParam() {
        if (this.defaultFlag == DefaultFlag.NO) {
            Validate.notNull(this.destinationArea, ValidateUtil.NULL_EX_MESSAGE, "destinationArea");
            Validate.notNull(this.destinationAreaName, ValidateUtil.NULL_EX_MESSAGE, "destinationAreaName");
            if (this.destinationArea.length != this.destinationAreaName.length) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }
        }
    }
}
