package com.wanmi.sbc.order.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 退货原因
 * 0:商家发错货、1:货品与描述不符、2:货品少件/受损/污渍等、3:货品质量问题、4:其他
 *
 * 樊登定制 修改--  0： 我不想要了 1: 收货信息填写错误 2: 商品拍错   3: 没用优惠券 4: 其他
 * Created by jinwei on 20/4/2017.
 */
@ApiEnum
public enum ReturnReason {

    @ApiEnumProperty("0: 我不想要了")
    WRONGGOODS(0, "我不想要了"),
    @ApiEnumProperty("1: 收货信息填写错误")
    NOTDISCRIPTION(1, "收货信息填写错误"),
    @ApiEnumProperty("2: 商品拍错")
    ERRORGOODS(2, "商品拍错"),
    @ApiEnumProperty("3: 没用优惠券")
    BADGOODS(3, "没用优惠券"),
    @ApiEnumProperty("4: 其他")
    OTHER(4, "其他");

    private Integer type;

    private String desc;

    ReturnReason(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static Map<String, ReturnReason> map = new HashMap<>();

    static {
        Arrays.stream(ReturnReason.values()).forEach(
                t -> map.put(t.getType().toString(), t)
        );
    }

    @JsonCreator
    public static ReturnReason forValue(Map<String, String> param) {
        return map.get(param.keySet().iterator().next());
    }

    @JsonValue
    public Map<String, String> toValue() {
        Map<String, String> result = new HashMap<>();
        result.put(this.getType().toString(), this.getDesc());
        return result;

    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static Map<String, ReturnReason> getMap() {
        return map;
    }
}