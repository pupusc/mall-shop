package com.wanmi.sbc.customer.api.request.company;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.StoreType;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.Validate;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.wanmi.sbc.common.util.ValidateUtil.NULL_EX_MESSAGE;

/**
 * @Author: songhanlin
 * @Date: Created In 下午3:23 2017/11/7
 * @Description: 商家类型Request
 */
@ApiModel
@Data
public class CompanyTypeRequest extends BaseRequest {

    private static final long serialVersionUID = 6838306367156536511L;
    /**
     * 公司Id
     */
    @ApiModelProperty(value = "公司Id")
    private Long companyInfoId;

    /**
     * 商家类型
     */
    @ApiModelProperty(value = "商家类型")
    private DefaultFlag companyType;

    /**
     * 商家类型0品牌商城，1商家
     */
    @ApiModelProperty(value = "商家类型0品牌商城，1商家")
    private StoreType storeType = StoreType.SUPPLIER;

    /**
     * 商家入驻时间(第一次审核通过时间)
     */
    @ApiModelProperty(value = "商家入驻时间(第一次审核通过时间)")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime applyEnterTime;


    @Override
    public void checkParam() {
        if (Objects.isNull(companyInfoId)) { //商家Id不能为空
            Validate.notNull(companyInfoId, NULL_EX_MESSAGE, "companyInfoId");
        } else if (Objects.isNull(companyType)) { //商家类型不能为空
            Validate.notNull(companyType, NULL_EX_MESSAGE, "companyType");
        }
    }
}
