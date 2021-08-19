package com.wanmi.ares.request.mq;

import com.wanmi.ares.base.BaseMqRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 商品品牌
 * Created by sunkun on 2017/9/21.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class GoodsBrandRequest extends BaseMqRequest {

    private static final long serialVersionUID = -7789617919432447998L;

    /**
     * 品牌名称
     */
    private String name;

    /**
     * 商家id
     */
    private String companyId;

}
