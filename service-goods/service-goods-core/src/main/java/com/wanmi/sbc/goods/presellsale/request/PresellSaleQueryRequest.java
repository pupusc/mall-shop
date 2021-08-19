package com.wanmi.sbc.goods.presellsale.request;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.goods.bean.enums.PresellSaleStatus;
import com.wanmi.sbc.goods.presellsale.model.root.PresellSale;
import com.wanmi.sbc.goods.util.XssUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class PresellSaleQueryRequest extends BaseQueryRequest {

    /**
     * 预售活动id
     */
    private String presellSaleId;
    /**
     * 预售商品活动名称
     */
    @NotBlank
    private String presellSaleName;

    /**
     * 店铺ID
     */
    private Long storeId;


    /**
     * 活动支付类型  0： 定金  ，1：全款
     */
    @NotNull
    private Integer presellType;


    /**
     * 预售定金开始时间
     */

    private LocalDateTime handselStartTime;


    /**
     * 预售定金结束时间
     */
    private LocalDateTime handselEndTime;



    /**
     * 尾款支付开始时间
     */
    private LocalDateTime finalPaymentStartTime;

    /**
     * 尾款支付结束时间
     */
    private LocalDateTime finalPaymentEndTime;

    /**
     * 预售开始时间
     */
    private LocalDateTime presellStartTime;


    /**
     * 预售结束时间
     */
    private LocalDateTime presellEndTime;


    /**
     * 发货日期
     */
    private LocalDateTime deliverTime;


    /**
     * 发货开始时间
     */
    private LocalDateTime deliverTimeStart;

    /**
     * 发货结束时间
     */
    private LocalDateTime deliverTimeEnd;

    /**
     * 删除标记
     */
    private Integer delFlag;



    /**
     * 是否勾选全选按钮  0 勾选 1 未勾选
     */

    private Integer selectAll;



    /**
     * 是否平台等级 （1平台（自营店铺属于平台等级） 0店铺）
     */
    private DefaultFlag joinLevelType;


    /**
     * 查询类型，0：全部，1：进行中，2：暂停中，3：未开始，4：已结束
     */
    private PresellSaleStatus queryTab;

    /**
     * 店铺名称
     */
    private String storeName;


    /**
     * 封装公共条件
     *
     * @return
     */
    public Specification<PresellSale> getWhereCriteria() {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();

            //预售活动类型
            if (presellType != null) {
                predicates.add(cbuild.equal(root.get("presellType"), presellType));
            }

            //预售活动类型
            if (storeName != null) {
                predicates.add(cbuild.like(root.get("storeName"), storeName));
            }

            //店铺ID
            if (storeId != null) {
                predicates.add(cbuild.equal(root.get("storeId"), storeId));
            }
            //模糊查询预售活动名称
            if (StringUtils.isNotEmpty(presellSaleName)) {
                predicates.add(cbuild.like(root.get("presellSaleName"), StringUtil.SQL_LIKE_CHAR.concat(XssUtils.replaceLikeWildcard(presellSaleName.trim())).concat(StringUtil.SQL_LIKE_CHAR)));
            }

            //删除标记
            if (delFlag != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), delFlag));
            }


            //目标客户
            if (joinLevelType != null) {
                predicates.add(cbuild.equal(root.get("joinLevelType"), joinLevelType));
            }


            // 大于或等于 搜索条件:定金开始
            if (Objects.nonNull(handselStartTime)) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("handselStartTime"),
                        handselStartTime));
            }
            // 小于或等于 搜索条件:定金结束
            if (Objects.nonNull(handselEndTime)) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("handselEndTime"),
                        handselEndTime));
            }

            // 大于或等于 搜索条件:尾款开始
            if (Objects.nonNull(finalPaymentStartTime)) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("finalPaymentStartTime"),
                        finalPaymentStartTime));
            }
            // 小于或等于 搜索条件:尾款结束
            if (Objects.nonNull(finalPaymentEndTime)) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("finalPaymentEndTime"),
                        finalPaymentEndTime));
            }

            // 大于或等于 搜索条件:预售开始
            if (Objects.nonNull(presellStartTime)) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("presellStartTime"),
                        presellStartTime));
            }
            // 小于或等于 搜索条件:预售结束
            if (Objects.nonNull(finalPaymentEndTime)) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("finalPaymentEndTime"),
                        finalPaymentEndTime));
            }

            // 大于或等于 搜索条件:发货开始
            if (Objects.nonNull(deliverTimeStart)) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("deliverTime"),
                        deliverTimeStart));
            }
            // 小于或等于 搜索条件:发货结束
            if (Objects.nonNull(deliverTimeEnd)) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("deliverTime"),
                        deliverTimeEnd));
            }

            // 0:未开始，1 进行中 2 已结束 3. 已暂停
            switch (queryTab){
                case STARTED://进行中
                    predicates.add(cbuild.lessThan(root.get("startTime"), LocalDateTime.now()));
                    predicates.add(cbuild.greaterThan(root.get("endTime"), LocalDateTime.now()));
                    predicates.add(cbuild.equal(root.get("suspended"), Integer.valueOf(0)));
                    break;
                case PAUSED://暂停中
                    predicates.add(cbuild.equal(root.get("suspended"), Integer.valueOf(1)));
                    break;
                case NOT_START://未开始
                    predicates.add(cbuild.greaterThan(root.get("startTime"), LocalDateTime.now()));
                    predicates.add(cbuild.equal(root.get("suspended"), Integer.valueOf(0)));
                    break;
                case ENDED://已结束
                    predicates.add(cbuild.lessThan(root.get("endTime"), LocalDateTime.now()));
                    predicates.add(cbuild.equal(root.get("suspended"), Integer.valueOf(0)));
                    break;
                case S_NS://进行中&未开始
                    predicates.add(cbuild.equal(root.get("suspended"), Integer.valueOf(0)));
                    break;
                default:
                    break;
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
