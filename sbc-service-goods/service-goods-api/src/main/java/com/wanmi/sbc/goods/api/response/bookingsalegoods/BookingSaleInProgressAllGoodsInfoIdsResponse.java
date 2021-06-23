package com.wanmi.sbc.goods.api.response.bookingsalegoods;

import com.wanmi.sbc.goods.bean.vo.AppointmentSaleSimplifyVO;
import com.wanmi.sbc.goods.bean.vo.BookingSaleSimplifyVO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingSaleInProgressAllGoodsInfoIdsResponse implements Serializable {


    private static final long serialVersionUID = 4236355895089819576L;

    /**
     * 预约商品列表
     */
    private List<AppointmentSaleSimplifyVO> appointmentSaleSimplifyVOList;

    /**
     * 预售商品列表
     */
    private List<BookingSaleSimplifyVO> bookingSaleSimplifyVOList;
}