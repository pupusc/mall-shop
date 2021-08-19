package com.wanmi.sbc.marketing.provider.impl.distributionrecord;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.provider.distributionrecord.DistributionRecordSaveProvider;
import com.wanmi.sbc.marketing.api.request.distributionrecord.*;
import com.wanmi.sbc.marketing.api.response.distributionrecord.DistributionRecordAddResponse;
import com.wanmi.sbc.marketing.bean.enums.CommissionReceived;
import com.wanmi.sbc.marketing.bean.vo.DistributionRecordVO;
import com.wanmi.sbc.marketing.distributionrecord.model.root.DistributionRecord;
import com.wanmi.sbc.marketing.distributionrecord.service.DistributionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>DistributionRecord保存服务接口实现</p>
 * @author baijz
 * @date 2019-02-27 18:56:40
 */
@RestController
@Validated
public class DistributionRecordSaveController implements DistributionRecordSaveProvider {
	@Autowired
	private DistributionRecordService distributionRecordService;

	@Override
	public BaseResponse<DistributionRecordAddResponse> add(@RequestBody @Valid DistributionRecordAddRequest distributionRecordAddRequest) {
		List<DistributionRecordAddInfo> addList = distributionRecordAddRequest.getDistributionRecordAddInfos();
		List<DistributionRecord> addDistributionRecords = new ArrayList<>();
		List<DistributionRecordVO> distributionRecordVOS = new ArrayList<>();
		if(!CollectionUtils.isEmpty(addList)){
			addList.forEach(addRecord->{
				DistributionRecord distributionRecord = new DistributionRecord();
				KsBeanUtil.copyPropertiesThird(addRecord,distributionRecord);
				distributionRecord.setCommissionState(CommissionReceived.UNRECEIVE);
				distributionRecord.setDeleteFlag(DeleteFlag.NO);
				addDistributionRecords.add(distributionRecord);
			});
		}
		List<DistributionRecord> distributionRecords = distributionRecordService.add(addDistributionRecords);
		distributionRecords.forEach(distributionRecord -> {
			distributionRecordVOS.add(KsBeanUtil.convert(distributionRecord,DistributionRecordVO.class));
		});
		return BaseResponse.success(new DistributionRecordAddResponse(distributionRecordVOS));
	}

	@Override
	public BaseResponse modify(@RequestBody @Valid DistributionRecordModifyRequest distributionRecordModifyRequest) {
		distributionRecordService.modify(distributionRecordModifyRequest);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByTradeIdAndGoodsInfoId(@RequestBody @Valid
																  DistributionRecordDelByTradeIdAndGoodsInfoIdRequest distributionRecordDelByTradeIdAndGoodsInfoIdRequest) {
		distributionRecordService.deleteByTradeIdAndGoodsInfoId(distributionRecordDelByTradeIdAndGoodsInfoIdRequest
				.getTradeId(), distributionRecordDelByTradeIdAndGoodsInfoIdRequest.getGoodsInfoId());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid DistributionRecordDelByIdListRequest distributionRecordDelByIdListRequest) {
		distributionRecordService.deleteByIdList(distributionRecordDelByIdListRequest.getRecordIdList());
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByTradeId(@RequestBody @Valid DistributionRecordByTradeIdRequest recordByTradeIdRequest){
		distributionRecordService.deleteByTradeId(recordByTradeIdRequest.getTradeId());
		return BaseResponse.SUCCESSFUL();
	}

}

