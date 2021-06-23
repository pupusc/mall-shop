package com.wanmi.sbc.goods.api.response.bookingsale;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.vo.BookingSaleVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>根据id查询任意（包含已删除）预售信息信息response</p>
 * @author dany
 * @date 2020-06-05 10:47:21
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingSaleByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 预售信息信息
     */
    @ApiModelProperty(value = "预售信息信息")
    private BookingSaleVO bookingSaleVO;
}
