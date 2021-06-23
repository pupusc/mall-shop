package com.wanmi.sbc.customer.provider.impl.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.distribution.DistributorLevelQueryProvider;
import com.wanmi.sbc.customer.api.request.distribution.DistributorLevelByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributorLevelByIdsRequest;
import com.wanmi.sbc.customer.api.response.distribution.DistributorLevelBaseResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributorLevelByCustomerIdResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributorLevelListAllResponse;
import com.wanmi.sbc.customer.bean.vo.DistributorLevelBaseVO;
import com.wanmi.sbc.customer.bean.vo.DistributorLevelVO;
import com.wanmi.sbc.customer.distribution.model.entity.DistributorLevelBase;
import com.wanmi.sbc.customer.distribution.model.root.DistributorLevel;
import com.wanmi.sbc.customer.distribution.service.DistributorLevelService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 分销员等级-查询接口
 *
 * @author: Geek Wang
 * @createDate: 2019/6/13 16:26
 * @version: 1.0
 */
@Validated
@RestController
public class DistributorLevelQueryController implements DistributorLevelQueryProvider {

    @Autowired
    private DistributorLevelService distributorLevelService;

    @Override
    public BaseResponse<DistributorLevelBaseResponse> listBaseInfo() {
        List<DistributorLevelBase> distributorLevelBaseList = distributorLevelService.listBaseDistributorLevel();
        if (CollectionUtils.isEmpty(distributorLevelBaseList)) {
            return BaseResponse.success(new DistributorLevelBaseResponse());
        }
        List<DistributorLevelBaseVO> list = KsBeanUtil.convert(distributorLevelBaseList, DistributorLevelBaseVO.class);
        return BaseResponse.success(new DistributorLevelBaseResponse(list));
    }

    @Override
    public BaseResponse<DistributorLevelListAllResponse> listAll() {
        List<DistributorLevel> list = distributorLevelService.findAll();
        return BaseResponse.success(
                new DistributorLevelListAllResponse(KsBeanUtil.convert(list, DistributorLevelVO.class)));
    }

    @Override
    public BaseResponse<DistributorLevelByCustomerIdResponse> getByCustomerId(@RequestBody @Valid DistributorLevelByCustomerIdRequest request) {
        DistributorLevelVO distributorLevelVO = distributorLevelService.getByCustomerId(request.getCustomerId());
        return BaseResponse.success(new DistributorLevelByCustomerIdResponse(distributorLevelVO));

    }

    @Override
    public BaseResponse<DistributorLevelBaseResponse> listByIds(DistributorLevelByIdsRequest request) {
        List<DistributorLevel> distributorLevelList = distributorLevelService.findByDistributorLevelIdIn(request.getIdList());
        List<DistributorLevelBaseVO> vos = Optional.ofNullable(distributorLevelList).orElse(Collections.emptyList()).stream()
                .map(entity -> {
                    DistributorLevelBaseVO distributorLevelBaseVO = new DistributorLevelBaseVO();
                    BeanUtils.copyProperties(entity, distributorLevelBaseVO);
                    return distributorLevelBaseVO;
                }).collect(Collectors.toList());
        DistributorLevelBaseResponse distributorLevelBaseResponse = new DistributorLevelBaseResponse(vos);
        return BaseResponse.success(distributorLevelBaseResponse);
    }
}
