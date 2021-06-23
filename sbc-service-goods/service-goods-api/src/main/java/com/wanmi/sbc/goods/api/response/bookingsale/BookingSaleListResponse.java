package com.wanmi.sbc.goods.api.response.bookingsale;

import com.wanmi.sbc.goods.bean.vo.BookingSaleVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>预售信息列表结果</p>
 * @author dany
 * @date 2020-06-05 10:47:21
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingSaleListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 预售信息列表结果
     */
    @ApiModelProperty(value = "预售信息列表结果")
    private List<BookingSaleVO> bookingSaleVOList;
}
