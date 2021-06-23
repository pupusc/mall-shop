package com.wanmi.sbc.goods.api.response.bookingsale;

import com.wanmi.sbc.goods.bean.vo.BookingSaleVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>预售信息修改结果</p>
 * @author dany
 * @date 2020-06-05 10:47:21
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingSaleModifyResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已修改的预售信息信息
     */
    @ApiModelProperty(value = "已修改的预售信息信息")
    private BookingSaleVO bookingSaleVO;
}
