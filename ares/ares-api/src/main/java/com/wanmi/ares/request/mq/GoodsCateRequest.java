package com.wanmi.ares.request.mq;

import com.wanmi.ares.base.BaseMqRequest;
import lombok.*;

/**
 * 商品分类
 * Created by sunkun on 2017/9/21.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsCateRequest extends BaseMqRequest {

    private static final long serialVersionUID = -1827018468986375856L;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 父分类id
     */
    private Long parentId;

    /**
     * 所有上级名称字符串以"/"分割
     */
    private String parentNames;

    /**
     * 分类图片
     */
    private String img;

    /**
     * 分类层次
     */
    private Integer grade;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否默认
     */
    @Builder.Default
    private boolean isDefault = false;

    /**
     * 商家id
     */
    private String companyId;

}
