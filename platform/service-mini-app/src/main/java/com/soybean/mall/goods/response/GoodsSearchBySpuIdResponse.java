package com.soybean.mall.goods.response;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/02/13:16
 * @Description:
 */
@Data
public class GoodsSearchBySpuIdResponse {
    private int id;
    private String name;
    private String spuId;
    private int status;
}
