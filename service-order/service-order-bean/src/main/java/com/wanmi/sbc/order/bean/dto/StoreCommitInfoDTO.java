package com.wanmi.sbc.order.bean.dto;


import com.wanmi.sbc.account.bean.enums.PayType;
import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.ValidateUtil;
import com.wanmi.sbc.goods.bean.enums.DeliverWay;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.Validate;

/**
 * <p>按店铺拆分订单提交信息</p>
 * Created by of628-wenzhi on 2017-11-24-下午3:43.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class StoreCommitInfoDTO extends BaseRequest {

    private static final long serialVersionUID = -1980544654461057449L;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    /**
     * 支付类型，默认在线支付
     */
    @ApiModelProperty(value = "支付类型")
    private PayType payType = PayType.OFFLINE;

    /**
     * 开票类型，必传 0：普通发票 1：增值税发票 -1：无
     */
    @ApiModelProperty(value = "开票类型", required = true)
    private Integer invoiceType;

    /**
     * 普通发票与增票参数，如果需要开票则至少一项必传
     */
    @ApiModelProperty(value = "普通发票与增票参数,如果需要开票则至少一项必传")
    private GeneralInvoiceDTO generalInvoice;

    /**
     * 增值税发票，如果需要开票则与普票至少一项必传
     */
    @ApiModelProperty(value = "增值税发票,如果需要开票则与普票至少一项必传")
    private SpecialInvoiceDTO specialInvoice;

    /**
     * 开票项目id，如果需要开票则必传
     */
    @ApiModelProperty(value = "开票项目id,如果需要开票则必传")
    private String invoiceProjectId;

    /**
     * 开票项目名称，如果需要开票则必传
     */
    @ApiModelProperty(value = "开票项目名称,如果需要开票则必传")
    private String invoiceProjectName;

    /**
     * 开票项修改时间
     */
    @ApiModelProperty(value = "开票项修改时间")
    private String invoiceProjectUpdateTime;

    /**
     * 是否单独的收货地址,默认：否
     */
    @ApiModelProperty(value = "是否单独的收货地址", dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    private boolean specialInvoiceAddress;

    /**
     * 收货地址详细信息（包含省市区），如果需要开票,则必传
     */
    @ApiModelProperty(value = "收货地址详细信息（包含省市区）,如果需要开票,则必传")
    private String invoiceAddressDetail;

    /**
     * 发票的收货地址ID,如果需要开票,则必传
     */
    @ApiModelProperty(value = "发票的收货地址ID,如果需要开票,则必传")
    private String invoiceAddressId;

    /**
     * 电子发票邮箱
     */
    private String invoiceEmail;

    /**
     * 发票收货地址修改时间，可空
     */
    @ApiModelProperty(value = "发票收货地址修改时间")
    private String invoiceAddressUpdateTime;

    /**
     * 订单备注
     */
    @ApiModelProperty(value = "订单备注")
    private String buyerRemark;

    /**
     * 附件, 逗号隔开
     */
    @ApiModelProperty(value = "附件,逗号隔开")
    private String encloses;

    /**
     * 配送方式，默认快递
     */
    @ApiModelProperty(value = "配送方式")
    private DeliverWay deliverWay = DeliverWay.EXPRESS;

    /**
     * 选择的店铺优惠券id
     */
    @ApiModelProperty(value = "选择的店铺优惠券id")
    private String couponCodeId;

    /**
     * 店铺分销设置开关状态
     */
    private DefaultFlag storeOpenFlag = DefaultFlag.NO;


    @Override
    public void checkParam() {
        if (invoiceType != -1) {
            if (invoiceType.equals(0)) {
                Validate.notNull(generalInvoice, ValidateUtil.NULL_EX_MESSAGE, "generalInvoice");
                Validate.notNull(generalInvoice.getFlag(), ValidateUtil.NULL_EX_MESSAGE, "generalInvoice.flag");
                Validate.inclusiveBetween(0L, 1L, generalInvoice.getFlag());
                if (generalInvoice.getFlag() == 1) {
                    Validate.notBlank(generalInvoice.getTitle(), ValidateUtil.BLANK_EX_MESSAGE, "generalInvoice.title");
                }

            } else if (invoiceType.equals(1)) {
                Validate.notNull(specialInvoice, ValidateUtil.NULL_EX_MESSAGE, "specialInvoice");
                Validate.notNull(specialInvoice.getId(), ValidateUtil.NULL_EX_MESSAGE, "specialInvoice.id");

            }
            Validate.notBlank(invoiceProjectId, ValidateUtil.BLANK_EX_MESSAGE, "invoiceProjectId");
            Validate.notBlank(invoiceProjectName, ValidateUtil.BLANK_EX_MESSAGE, "invoiceProjectName");

            Validate.notBlank(invoiceAddressDetail, ValidateUtil.BLANK_EX_MESSAGE, "invoiceAddressDetail");
            Validate.notBlank(invoiceAddressId, ValidateUtil.BLANK_EX_MESSAGE, "invoiceAddressId");
        }

    }
}
