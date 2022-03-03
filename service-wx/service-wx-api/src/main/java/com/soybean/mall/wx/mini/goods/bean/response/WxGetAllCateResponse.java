package com.soybean.mall.wx.mini.goods.bean.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class WxGetAllCateResponse extends WxResponseBase {

    @JSONField(name = "third_cat_list")
    private List<WxCate> thirdCatist;

    @Data
    public static class WxCate{

        @JSONField(name = "third_cat_id")
        private Integer thirdCatId;
        @JSONField(name = "third_cat_name")
        private String thirdCatName;
        @JSONField(name = "qualification")
        private String qualification;
        @JSONField(name = "qualification_type")
        private Integer qualificationType;
        @JSONField(name = "product_qualification")
        private String productQualification;
        @JSONField(name = "product_qualification_type")
        private Integer productQualificationType;
        @JSONField(name = "first_cat_id")
        private Integer firstCatId;
        @JSONField(name = "first_cat_name")
        private String firstCatName;
        @JSONField(name = "second_cat_id")
        private Integer secondCatId;
        @JSONField(name = "second_cat_name")
        private String secondCatName;
    }
}
