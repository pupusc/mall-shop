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
 * <p>未结束的活动信息</p>
 *
 * @author zxd
 * @date 2020-05-21 10:32:23
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingSaleNotEndResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 预约抢购信息
     */
    @ApiModelProperty(value = "预售信息列表")
    private List<BookingSaleVO> bookingSaleVOList;
}
