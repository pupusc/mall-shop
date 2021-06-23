package com.wanmi.sbc.goods.redis;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 适用于redis HIncr 结构的对象
 * Created by daiyitian on 2016/12/24.
 */
@AllArgsConstructor
@Data
public class RedisHIncrBean {

    private String field;

    private Long value;

}
