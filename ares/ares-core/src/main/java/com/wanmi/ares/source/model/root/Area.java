package com.wanmi.ares.source.model.root;

import com.wanmi.ares.source.model.root.base.BaseData;
import lombok.*;

/**
 * 区域基础信息
 * Created by sunkun on 2017/9/19.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class Area extends BaseData {

    private static final long serialVersionUID = 7963793799765358789L;

    /**
     * 名称
     */
    private String name;

    /**
     * 父id
     */
    private Long parentId;

}
