package com.wanmi.sbc.marketing.api.response.distribution;

import com.wanmi.sbc.marketing.bean.vo.DistributionStoreSettingVO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


/**
 * <p>查询店铺分销设置响应</p>
 * @author gaomuwei
 * @date 2019-02-19 10:08:02
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistributionStoreSettingListByStoreIdsResponse implements Serializable {

    private List<DistributionStoreSettingVO>  list;
}
