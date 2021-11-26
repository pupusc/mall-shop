package com.wanmi.sbc.crm.customgroup.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * custom_group
 *
 * @author
 */
@Data
public class CustomGroup implements Serializable {
    private static final long serialVersionUID = -1722118974051147076L;

    private Long id;

    /**
     * 分群名称
     */
    private String groupName;

    /**
     * 人群定义
     */
    private String definition;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createPerson;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private String updatePerson;

    /**
     * 分群信息
     */
    private String groupDetail;

    /**
     * 会员人数
     */
    private Long customerCount;

    /**
     * 手动标签
     */
    private String customerTags;

    /**
     * 自动标签
     */
    private String autoTags;

    /**
     * 偏好标签
     */
    private String preferenceTags;

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
        CustomGroup other = (CustomGroup) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getGroupName() == null ? other.getGroupName() == null :
                this.getGroupName().equals(other.getGroupName()))
                && (this.getDefinition() == null ? other.getDefinition() == null :
                this.getDefinition().equals(other.getDefinition()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null :
                this.getCreateTime().equals(other.getCreateTime()))
                && (this.getCreatePerson() == null ? other.getCreatePerson() == null :
                this.getCreatePerson().equals(other.getCreatePerson()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null :
                this.getUpdateTime().equals(other.getUpdateTime()))
                && (this.getUpdatePerson() == null ? other.getUpdatePerson() == null :
                this.getUpdatePerson().equals(other.getUpdatePerson()))
                && (this.getGroupDetail() == null ? other.getGroupDetail() == null :
                this.getGroupDetail().equals(other.getGroupDetail()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getGroupName() == null) ? 0 : getGroupName().hashCode());
        result = prime * result + ((getDefinition() == null) ? 0 : getDefinition().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreatePerson() == null) ? 0 : getCreatePerson().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdatePerson() == null) ? 0 : getUpdatePerson().hashCode());
        result = prime * result + ((getGroupDetail() == null) ? 0 : getGroupDetail().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", groupName=").append(groupName);
        sb.append(", definition=").append(definition);
        sb.append(", createTime=").append(createTime);
        sb.append(", createPerson=").append(createPerson);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", updatePerson=").append(updatePerson);
        sb.append(", groupDetail=").append(groupDetail);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}