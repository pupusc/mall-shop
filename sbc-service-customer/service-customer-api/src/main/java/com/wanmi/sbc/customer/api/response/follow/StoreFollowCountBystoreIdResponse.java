package com.wanmi.sbc.customer.api.response.follow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: sbc-micro-service
 * @description: 店铺被关注总数
 * @create: 2019-04-03 10:14
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreFollowCountBystoreIdResponse implements Serializable {

    private static final long serialVersionUID = -7458098307037191167L;

    //总数
    private long count;
}