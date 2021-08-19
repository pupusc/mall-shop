package com.wanmi.sbc.goods.brand.request;

import com.wanmi.sbc.common.base.BaseRequest;
import lombok.Data;

/**
 * 签约品牌更新请求
 * Created by sunkun on 2017/10/31.
 */
@Data
public class ContractBrandSaveRequest extends BaseRequest {

    private static final long serialVersionUID = -8756169481298950247L;

    /**
     * 签约品牌分类
     */
    private Long contractBrandId;


    /**
     * 品牌名称
     */
    private String name;

    /**
     * 品牌别名
     */
    private String nickName;

    /**
     * 品牌logo
     */
    private String logo;

    /**
     * 店铺主键
     */
    private Long storeId;


    /**
     * 品牌主键
     */
    private Long brandId;

    /**
     * 待审核品牌主键
     */
    private Long checkBrandId;


    /**
     * 授权图片路径
     */
    private String authorizePic;
}
