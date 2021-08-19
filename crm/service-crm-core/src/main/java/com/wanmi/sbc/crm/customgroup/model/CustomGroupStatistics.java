package com.wanmi.sbc.crm.customgroup.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * custom_group_statistics
 * @author zgl
 */
@Data
public class CustomGroupStatistics implements Serializable {
    private Long id;

    /**
     * 统计时间
     */
    private Date statDate;

    /**
     * 自定义人群id
     */
    private Long groupId;

    /**
     * 会员数量
     */
    private Long customerCount;

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
        CustomGroupStatistics other = (CustomGroupStatistics) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getStatDate() == null ? other.getStatDate() == null : this.getStatDate().equals(other.getStatDate()))
            && (this.getGroupId() == null ? other.getGroupId() == null : this.getGroupId().equals(other.getGroupId()))
            && (this.getCustomerCount() == null ? other.getCustomerCount() == null : this.getCustomerCount().equals(other.getCustomerCount()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getStatDate() == null) ? 0 : getStatDate().hashCode());
        result = prime * result + ((getGroupId() == null) ? 0 : getGroupId().hashCode());
        result = prime * result + ((getCustomerCount() == null) ? 0 : getCustomerCount().hashCode());
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
        sb.append(", statDate=").append(statDate);
        sb.append(", groupId=").append(groupId);
        sb.append(", customerCount=").append(customerCount);
        sb.append(", createTime=").append(createTime);
        sb.append("]");
        return sb.toString();
    }
}