package com.wanmi.sbc.crm.customgroup.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * customer_base_info
 * @author 
 */
@Data
public class CustomerBaseInfo implements Serializable {
    /**
     * 会员id
     */
    private String customerId;

    /**
     * 省份
     */
    private Long provinceId;

    /**
     * 城市
     */
    private Long cityId;

    /**
     * 地区
     */
    private Long areaId;

    /**
     * 成长值
     */
    private Long growthValue;

    /**
     * 会员等级
     */
    private Long customerLevelId;

    /**
     * 积分
     */
    private Long points;

    /**
     * 账户余额
     */
    private BigDecimal accountBalance;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;



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
        CustomerBaseInfo other = (CustomerBaseInfo) that;
        return (this.getCustomerId() == null ? other.getCustomerId() == null : this.getCustomerId().equals(other.getCustomerId()))
            && (this.getProvinceId() == null ? other.getProvinceId() == null : this.getProvinceId().equals(other.getProvinceId()))
            && (this.getCityId() == null ? other.getCityId() == null : this.getCityId().equals(other.getCityId()))
            && (this.getAreaId() == null ? other.getAreaId() == null : this.getAreaId().equals(other.getAreaId()))
            && (this.getGrowthValue() == null ? other.getGrowthValue() == null : this.getGrowthValue().equals(other.getGrowthValue()))
            && (this.getCustomerLevelId() == null ? other.getCustomerLevelId() == null : this.getCustomerLevelId().equals(other.getCustomerLevelId()))
            && (this.getPoints() == null ? other.getPoints() == null : this.getPoints().equals(other.getPoints()))
            && (this.getAccountBalance() == null ? other.getAccountBalance() == null : this.getAccountBalance().equals(other.getAccountBalance()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCustomerId() == null) ? 0 : getCustomerId().hashCode());
        result = prime * result + ((getProvinceId() == null) ? 0 : getProvinceId().hashCode());
        result = prime * result + ((getCityId() == null) ? 0 : getCityId().hashCode());
        result = prime * result + ((getAreaId() == null) ? 0 : getAreaId().hashCode());
        result = prime * result + ((getGrowthValue() == null) ? 0 : getGrowthValue().hashCode());
        result = prime * result + ((getCustomerLevelId() == null) ? 0 : getCustomerLevelId().hashCode());
        result = prime * result + ((getPoints() == null) ? 0 : getPoints().hashCode());
        result = prime * result + ((getAccountBalance() == null) ? 0 : getAccountBalance().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", customerId=").append(customerId);
        sb.append(", provinceId=").append(provinceId);
        sb.append(", regionId=").append(cityId);
        sb.append(", areaId=").append(areaId);
        sb.append(", growthValue=").append(growthValue);
        sb.append(", customerLevelId=").append(customerLevelId);
        sb.append(", points=").append(points);
        sb.append(", accountBalance=").append(accountBalance);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}