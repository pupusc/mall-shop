package com.wanmi.sbc.customer.levelrights.model.root;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>会员等级与权益关联表实体类</p>
 */
@Data
@Entity
@Table(name = "customer_level_rights_rel")
public class CustomerLevelRightsRel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 用户等级id
     */
    @Column(name = "customer_level_id")
    private Long customerLevelId;

    /**
     * 权益id
     */
    @Column(name = "rights_id")
    private Integer rightsId;

}