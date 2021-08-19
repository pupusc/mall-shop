package com.wanmi.sbc.crm.customgroup.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * custom_group_rel
 * @author zgl
 */
@Data
public class CustomGroupRel implements Serializable {
    private Long id;

    /**
     * 人群id
     */
    private Integer groupId;

    private String groupName;

    private String definition;

    /**
     * 会员id
     */
    private String customerId;

    /**
     * 创建时间
     */
    private Date createTime;



    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        CustomGroupRel other = (CustomGroupRel) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getGroupId() == null ? other.getGroupId() == null : this.getGroupId().equals(other.getGroupId()))
            && (this.getCustomerId() == null ? other.getCustomerId() == null : this.getCustomerId().equals(other.getCustomerId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getGroupId() == null) ? 0 : getGroupId().hashCode());
        result = prime * result + ((getCustomerId() == null) ? 0 : getCustomerId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", groupId=").append(groupId);
        sb.append(", customerId=").append(customerId);
        sb.append(", createTime=").append(createTime);
        sb.append("]");
        return sb.toString();
    }
}