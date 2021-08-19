package com.wanmi.sbc.order.purchase.request;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.TerminalSource;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.customer.bean.vo.CustomerLevelVO;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.order.enums.FollowFlag;
import com.wanmi.sbc.order.purchase.Purchase;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.Enumerated;
import javax.persistence.criteria.Predicate;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 商品客户收藏请求
 * Created by daiyitian on 2017/5/17.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseRequest extends BaseQueryRequest {

    private static final long serialVersionUID = 3590025584368358090L;
    /**
     * 当前客户等级
     */
    CustomerLevelVO customerLevel;
    /**
     * 编号
     */
    private List<Long> followIds;
    /**
     * SKU编号
     */
    @NotBlank
    private String goodsInfoId;
    /**
     * 批量SKU编号
     */
    private List<String> goodsInfoIds;
    /**
     * 批量sku
     */
    private List<GoodsInfoDTO> goodsInfos;
    /**
     * 会员编号
     */
    private String customerId;
    /**
     * 购买数量
     */
    @Range(min = 1)
    private Long goodsNum;
    /**
     * 收藏标识
     */
    @Enumerated
    private FollowFlag followFlag;
    /**
     * 校验库存
     */
    @Default
    private Boolean verifyStock = true;
    /**
     * 是否赠品 true 是 false 否
     */
    @Default
    private Boolean isGift = false;

    /**
     * 购买数量是否覆盖
     */
    @Default
    private Boolean isCover = false;

    private LocalDateTime createTime;

    /**
     * 邀请人id-会员id
     */
    @ApiModelProperty(value = "邀请人id")
    String inviteeId;

    /**
     * 公司信息ID
     */
    @ApiModelProperty(value = "公司信息ID")
    private Long companyInfoId;
    /**
     * 门店id
     */
    @ApiModelProperty(value = "门店id")
    private Long storeId;



    /**
     * 终端来源
     */
    private TerminalSource terminalSource;

    /**
     * 是否是pc端访问或者社交分销关闭
     */
    @ApiModelProperty(value = "是否是pc端访问或者社交分销关闭")
    private Boolean pcAndNoOpenFlag;

    @ApiModelProperty("区的区域码")
    private Long AreaId;

    @ApiModelProperty("是否更新时间")
    private BoolFlag updateTimeFlag;

    /**
     * 封装公共条件
     *
     * @return
     */
    public Specification<Purchase> getWhereCriteria() {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();

            //客户编号
            if (StringUtils.isNotBlank(customerId)) {
                predicates.add(cbuild.equal(root.get("customerId"), customerId));
            }
            //SKU编号
            if (StringUtils.isNotBlank(goodsInfoId)) {
                predicates.add(cbuild.equal(root.get("goodsInfoId"), goodsInfoId));
            }

            //批量SKU编号
            if (CollectionUtils.isNotEmpty(goodsInfoIds)) {
                predicates.add(root.get("goodsInfoId").in(goodsInfoIds));
            }

            //分销员编号
            if (StringUtils.isNotBlank(inviteeId)) {
                predicates.add(cbuild.equal(root.get("inviteeId"), inviteeId));
            } else {
                predicates.add(cbuild.equal(root.get("inviteeId"), NumberUtils.INTEGER_ZERO));
            }

            //分销员编号
            if (Objects.nonNull(companyInfoId)) {
                predicates.add(cbuild.equal(root.get("companyInfoId"), companyInfoId));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }

    public Boolean getVerifyStock() {
        return this.verifyStock == null ? true : this.verifyStock;
    }

    public Boolean getIsGift() {
        return this.isGift == null ? false : this.isGift;
    }

    public Boolean getIsCover() {
        return this.isCover == null ? false : this.isCover;
    }
}
