package com.wanmi.sbc.customer.api.request.store;

import com.wanmi.sbc.common.annotation.CanEmpty;
import com.wanmi.sbc.common.util.ValidateUtil;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.Objects;

import static com.wanmi.sbc.common.util.ValidateUtil.BLANK_EX_MESSAGE;
import static com.wanmi.sbc.common.util.ValidateUtil.NULL_EX_MESSAGE;

/**
 * @Author: songhanlin
 * @Date: Created In 下午1:37 2017/11/3
 * @Description: 关店/开店 Request
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreSwitchRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = -4224503696108307692L;
    /**
     * 店铺Id
     */
    @ApiModelProperty(value = "店铺Id")
    private Long storeId;

    /**
     * 店铺状态 0:开启 1:关店
     */
    @ApiModelProperty(value = "店铺状态")
    private StoreState storeState;

    /**
     * 关店原因
     */
    @ApiModelProperty(value = "关店原因")
    @CanEmpty
    private String storeClosedReason;


    @Override
    public void checkParam() {
        //店铺Id不能为空
        if (Objects.isNull(storeId)) {
            Validate.notNull(storeId, NULL_EX_MESSAGE, "storeId");
        }
        //开/关 店状态不能为空
        if (Objects.isNull(storeState)) {
            Validate.notNull(storeState, NULL_EX_MESSAGE, "storeState");
        } else if (storeState.toValue() == StoreState.CLOSED.toValue()) { //如果是关店状态
            if (StringUtils.isBlank(storeClosedReason)) { //关店原因不能为空
                Validate.notBlank(storeClosedReason, BLANK_EX_MESSAGE, "storeCloseReason");
            } else if (!ValidateUtil.isBetweenLen(storeClosedReason, 1, 100)) { //原因长度为1-100以内
                Validate.validState(Boolean.FALSE);
            }
        }
    }
}
