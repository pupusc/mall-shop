package com.wanmi.sbc.customer.api.response.levelrights;

import com.wanmi.sbc.customer.bean.vo.CustomerLevelRightsRelVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>会员等级与权益关联表列表结果</p>
 *
 * @author yang
 * @since 2019/2/27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerLevelRightsRelListResponse implements Serializable {

    private static final long serialVersionUID = 925010576405302179L;

    /**
     * 会员等级与权益关联表信息列表
     */
    protected List<CustomerLevelRightsRelVO> customerLevelRightsRelVOList;
}
