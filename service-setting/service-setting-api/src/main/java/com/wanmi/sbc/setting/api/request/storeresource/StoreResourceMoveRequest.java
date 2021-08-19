package com.wanmi.sbc.setting.api.request.storeresource;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 批量移动素材资源请求
 * Created by yinxianzhi on 2018/10/12.
 */
@Getter
@Setter
public class StoreResourceMoveRequest {
    /**
     * 素材分类编号
     */
    @NotNull
    private Long cateId;

    /**
     * 批量素材资源ID
     */
    @NotEmpty
    private List<Long> resourceIds;

    /**
     * 店铺标识
     */
    private Long storeId;

}
