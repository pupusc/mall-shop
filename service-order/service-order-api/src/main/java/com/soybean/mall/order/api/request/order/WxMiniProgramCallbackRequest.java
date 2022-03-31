package com.soybean.mall.order.api.request.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WxMiniProgramCallbackRequest {

    private Long id;

    private String param;

    private String content;

    /**
     * 状态 0-处理中 1-异常 2-已完成
     */
    private Integer status;
}
