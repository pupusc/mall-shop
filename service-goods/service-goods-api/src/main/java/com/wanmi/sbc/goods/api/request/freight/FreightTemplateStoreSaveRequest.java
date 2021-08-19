package com.wanmi.sbc.goods.api.request.freight;

import com.wanmi.sbc.common.annotation.LengthIntensify;
import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.ValidateUtil;
import com.wanmi.sbc.goods.bean.enums.DeliverWay;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.Validate;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 店铺运费模板保存数据结构
 * Created by daiyitian on 2018/11/1.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class FreightTemplateStoreSaveRequest extends BaseRequest {

    private static final long serialVersionUID = -8107078231538944644L;

    /**
     * 运费模板id
     */
    @ApiModelProperty(value = "运费模板id")
    private Long freightTempId;

    /**
     * 运费模板名称
     */
    @ApiModelProperty(value = "运费模板名称")
    @NotBlank
    @LengthIntensify(min = 2, max = 20)
    private String freightTempName;

    /**
     * 配送地id(逗号分隔)
     */
    @ApiModelProperty(value = "配送地id", notes = "逗号分隔")
    @NotNull
    @Size(min = 1)
    private String[] destinationArea;

    /**
     * 配送地名称(逗号分隔)
     */
    @ApiModelProperty(value = "配送地名称", notes = "逗号分隔")
    @NotNull
    @Size(min = 1)
    private String[] destinationAreaName;

    /**
     * 运费计费规则(0:满金额包邮,1:固定运费)
     */
    @ApiModelProperty(value = "运费计费规则", notes = "0:满金额包邮,1:固定运费")
    @NotNull
    private DefaultFlag freightType;

    /**
     * 满多少金额包邮
     */
    @ApiModelProperty(value = "满多少金额包邮")
    @Min(value = 0)
    private BigDecimal satisfyPrice;

    /**
     * 不满金额的运费
     */
    @ApiModelProperty(value = "不满金额的运费")
    @Min(value = 0)
    private BigDecimal satisfyFreight;

    /**
     * 固定的运费
     */
    @ApiModelProperty(value = "固定的运费")
    @Min(value = 0)
    private BigDecimal fixedFreight;

    /**
     * 运送方式(1:快递配送) {@link DeliverWay}
     */
    @ApiModelProperty(value = "运送方式", notes = "0: 默认, 1: 快递")
    private DeliverWay deliverWay = DeliverWay.EXPRESS;

    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private Long companyInfoId;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    @Override
    public void checkParam() {
        if (freightType == DefaultFlag.NO) {
            Validate.notNull(this.satisfyPrice, ValidateUtil.NULL_EX_MESSAGE, "satisfyPrice");
            Validate.notNull(this.satisfyFreight, ValidateUtil.NULL_EX_MESSAGE, "satisfyFreight");
        } else {
            Validate.notNull(this.fixedFreight, ValidateUtil.NULL_EX_MESSAGE, "fixedFreight");
        }
        if (destinationArea.length != destinationAreaName.length) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
    }
}
