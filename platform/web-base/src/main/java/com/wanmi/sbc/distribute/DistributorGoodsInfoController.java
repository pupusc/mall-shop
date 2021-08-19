package com.wanmi.sbc.distribute;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.goods.api.provider.distributor.goods.DistributorGoodsInfoProvider;
import com.wanmi.sbc.goods.api.request.distributor.goods.DistributorGoodsInfoAddRequest;
import com.wanmi.sbc.goods.api.request.distributor.goods.DistributorGoodsInfoByCustomerIdRequest;
import com.wanmi.sbc.goods.api.request.distributor.goods.DistributorGoodsInfoDeleteRequest;
import com.wanmi.sbc.goods.api.request.distributor.goods.DistributorGoodsInfoModifySequenceRequest;
import com.wanmi.sbc.goods.api.response.distributor.goods.DistributorGoodsInfoAddResponse;
import com.wanmi.sbc.goods.api.response.distributor.goods.DistributorGoodsInfoCountsResponse;
import com.wanmi.sbc.goods.api.response.distributor.goods.DistributorGoodsInfoDeleteResponse;
import com.wanmi.sbc.goods.bean.dto.DistributorGoodsInfoModifySequenceDTO;
import com.wanmi.sbc.marketing.api.provider.distribution.DistributionSettingQueryProvider;
import com.wanmi.sbc.marketing.api.request.distribution.DistributionStoreSettingListByStoreIdsRequest;
import com.wanmi.sbc.marketing.api.response.distribution.DistributionStoreSettingListByStoreIdsResponse;
import com.wanmi.sbc.marketing.bean.vo.DistributionStoreSettingVO;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 分销员商品
 */
@Api(description = "分销员商品API" ,tags ="DistributorGoodsInfoController")
@RestController
@RequestMapping("/distributor-goods")
public class DistributorGoodsInfoController {

    @Autowired
    private DistributorGoodsInfoProvider distributorGoodsInfoProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private DistributionSettingQueryProvider distributionSettingQueryProvider;

    /**
     * 新增分销商品
     * @return
     */
    @ApiOperation(value = "新增分销商品")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public BaseResponse<DistributorGoodsInfoAddResponse> add(@RequestBody DistributorGoodsInfoAddRequest request){
        request.setCustomerId(commonUtil.getOperatorId());
        return distributorGoodsInfoProvider.add(request);
    }

    /**
     * 删除分销商品
     * @return
     */
    @ApiOperation(value = "删除分销商品")
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public BaseResponse<DistributorGoodsInfoDeleteResponse> delete(@RequestBody DistributorGoodsInfoDeleteRequest request){
        request.setCustomerId(commonUtil.getOperatorId());
        return distributorGoodsInfoProvider.delete(request);
    }

    /**
     * 修改分销员商品排序
     * @return
     */
    @ApiOperation(value = "修改分销员商品排序")
    @RequestMapping(value = "/modify-sequence",method = RequestMethod.POST)
    public BaseResponse<DistributorGoodsInfoDeleteResponse> modifySequence(@RequestBody DistributorGoodsInfoModifySequenceRequest request){
        List<DistributorGoodsInfoModifySequenceDTO> distributorGoodsInfoModifySequenceDTOList = request.getDistributorGoodsInfoDTOList();
        List<String> storeIdList = distributorGoodsInfoModifySequenceDTOList.stream().map(d ->String.valueOf(d.getStoreId())).distinct().collect(Collectors.toList());
        BaseResponse<DistributionStoreSettingListByStoreIdsResponse>  baseResponse = distributionSettingQueryProvider.listByStoreIds(new DistributionStoreSettingListByStoreIdsRequest(storeIdList));
        List<DistributionStoreSettingVO> list = baseResponse.getContext().getList();
        Map<String,DefaultFlag> map =list.stream().collect(Collectors.toMap(DistributionStoreSettingVO::getStoreId,DistributionStoreSettingVO::getOpenFlag));
        distributorGoodsInfoModifySequenceDTOList.stream().forEach(distributorGoodsInfoModifySequenceDTO -> {
            String storeId = String.valueOf(distributorGoodsInfoModifySequenceDTO.getStoreId());
            distributorGoodsInfoModifySequenceDTO.setStatus(DefaultFlag.NO == map.get(storeId) ? NumberUtils.INTEGER_ONE : NumberUtils.INTEGER_ZERO);
            distributorGoodsInfoModifySequenceDTO.setCustomerId(commonUtil.getOperatorId());
            distributorGoodsInfoModifySequenceDTO.setCreateTime(LocalDateTime.now());
            distributorGoodsInfoModifySequenceDTO.setUpdateTime(LocalDateTime.now());
        });
        return distributorGoodsInfoProvider.modifySequence(request);
    }

    /**
     * 查询分销员下分销商品数
     *
     * @return
     */
    @ApiOperation(value = "查询分销员下分销商品数")
    @RequestMapping(value = "/count",method = RequestMethod.GET)
    public BaseResponse<DistributorGoodsInfoCountsResponse> count(){
        DistributorGoodsInfoByCustomerIdRequest request = new DistributorGoodsInfoByCustomerIdRequest(commonUtil.getOperatorId());
        return distributorGoodsInfoProvider.checkCountsByCustomerId(request);
    }

}
