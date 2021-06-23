package com.wanmi.sbc.customer.api.response.livecompany;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.customer.api.response.company.CompanyReponse;
import com.wanmi.sbc.customer.bean.vo.LiveCompanyVO;
import com.wanmi.sbc.customer.bean.vo.LiveRoomVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.domain.Page;

/**
 * <p>直播商家分页结果</p>
 * @author zwb
 * @date 2020-06-06 18:06:59
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiveCompanyPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 直播商家分页结果
     */
    @ApiModelProperty(value = "直播商家分页结果")
    private MicroServicePage<LiveCompanyVO> liveCompanyVOPage;

}
