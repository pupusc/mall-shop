package com.wanmi.sbc.order.trade.request;


import com.wanmi.sbc.account.bean.enums.PayType;
import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.util.ValidateUtil;
import com.wanmi.sbc.goods.bean.enums.DeliverWay;
import com.wanmi.sbc.order.trade.model.entity.value.GeneralInvoice;
import com.wanmi.sbc.order.trade.model.entity.value.SpecialInvoice;
import lombok.Data;
import org.apache.commons.lang3.Validate;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * <p>按店铺拆分订单提交信息</p>
 * Created by of628-wenzhi on 2017-11-24-下午3:43.
 */
@Data
public class StoreCommitInfo extends BaseRequest {

    private static final long serialVersionUID = -1980544654461057449L;
    /**
     * 店铺id
     */
    @NotNull
    private Long storeId;

    /**
     * 支付类型，默认在线支付
     */
    @NotNull
    private PayType payType = PayType.OFFLINE;

    /**
     * 开票类型，必传 0：普通发票 1：增值税发票 -1：无
     */
    @NotNull
    @Range(max = 1, min = -1)
    private Integer invoiceType;

    /**
     * 普通发票与增票参数，如果需要开票则至少一项必传
     */
    private GeneralInvoice generalInvoice;

    /**
     * 增值税发票，如果需要开票则与普票至少一项必传
     */
    private SpecialInvoice specialInvoice;

    /**
     * 开票项目id，如果需要开票则必传
     */
    private String invoiceProjectId;

    /**
     * 开票项目名称，如果需要开票则必传
     */
    private String invoiceProjectName;

    /**
     * 开票项修改时间
     */
    private String invoiceProjectUpdateTime;

    /**
     * 是否单独的收货地址,默认：否
     */
    private boolean specialInvoiceAddress;

    /**
     * 收货地址详细信息（包含省市区），如果需要开票,则必传
     */
    private String invoiceAddressDetail;

    /**
     * 发票的收货地址ID,如果需要开票,则必传
     */
    private String invoiceAddressId;

    /**
     * 发票收货地址修改时间，可空
     */
    private String invoiceAddressUpdateTime;

    /**
     * 订单备注
     */
    @Length(max = 100)
    private String buyerRemark;

    /**
     * 附件, 逗号隔开
     */
    private String encloses;

    /**
     * 配送方式，默认快递
     */
    private DeliverWay deliverWay = DeliverWay.EXPRESS;

    /**
     * 选择的店铺优惠券id
     */
    private String couponCodeId;

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
