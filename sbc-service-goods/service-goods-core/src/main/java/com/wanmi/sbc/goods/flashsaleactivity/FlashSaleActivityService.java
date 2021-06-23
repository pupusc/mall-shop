package com.wanmi.sbc.goods.flashsaleactivity;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.request.flashsaleactivity.FlashSaleActivityQueryRequest;
import com.wanmi.sbc.goods.api.response.flashsaleactivity.FlashSaleActivityResponse;
import com.wanmi.sbc.goods.bean.vo.FlashSaleActivityVO;
import com.wanmi.sbc.goods.flashsalegoods.repository.FlashSaleGoodsRepository;
import com.wanmi.sbc.setting.api.provider.flashsalesetting.FlashSaleSettingQueryProvider;
import com.wanmi.sbc.setting.api.request.flashsalesetting.FlashSaleSettingListRequest;
import com.wanmi.sbc.setting.api.request.flashsalesetting.FlashSaleSettingQueryRequest;
import com.wanmi.sbc.setting.bean.vo.FlashSaleSettingVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>抢购活动业务逻辑</p>
 * @author yxz
 * @date 2019-06-11 14:54:31
 */
@Service("FlashSaleActivityService")
public class FlashSaleActivityService {

	@Autowired
	private FlashSaleGoodsRepository flashSaleGoodsRepository;

	@Autowired
	private FlashSaleSettingQueryProvider flashSaleSettingQueryProvider;
	
	/** 
	 * 分页查询抢购活动
	 * @author yxz
	 */
	public Page<FlashSaleActivityResponse> page(FlashSaleActivityQueryRequest queryReq){
        String fullTimeBegin = null;
        String fullTimeEnd = null;
        if (Objects.nonNull(queryReq.getFullTimeBegin())) {
            fullTimeBegin = DateUtil.format(queryReq.getFullTimeBegin(), DateUtil.FMT_TIME_1);
        }
        if (Objects.nonNull(queryReq.getFullTimeEnd())) {
            fullTimeEnd = DateUtil.format(queryReq.getFullTimeEnd(), DateUtil.FMT_TIME_1);
        }
		Page<Object> objectPage = flashSaleGoodsRepository.queryGroupByFullTime(fullTimeBegin, fullTimeEnd,
                queryReq.getStoreId(), queryReq.getPageRequest());
		List<FlashSaleActivityResponse> responses = objectPage.getContent().stream()
				.map(o -> new FlashSaleActivityResponse().convertFromNativeSQLResult(o))
				.collect(Collectors.toList());
		return new PageImpl<>(responses, queryReq.getPageable(), objectPage.getTotalElements());
	}

	/**
	 * 列表查询抢购活动
	 * @author yxz
	 */
	public List<FlashSaleActivityResponse> list(FlashSaleActivityQueryRequest queryReq){
        String fullTimeBegin = null;
        String fullTimeEnd = null;
        if (Objects.nonNull(queryReq.getFullTimeBegin())) {
            fullTimeBegin = DateUtil.format(queryReq.getFullTimeBegin(), DateUtil.FMT_TIME_1);
        }
        if (Objects.nonNull(queryReq.getFullTimeEnd())) {
            fullTimeEnd = DateUtil.format(queryReq.getFullTimeEnd(), DateUtil.FMT_TIME_1);
        }
		List<Object> objectList = flashSaleGoodsRepository.queryGroupByFullTime(fullTimeBegin, fullTimeEnd,
                queryReq.getStoreId());
		List<FlashSaleActivityResponse> responses = objectList.stream()
				.map(o -> new FlashSaleActivityResponse().convertFromNativeSQLResult(o))
				.collect(Collectors.toList());
		return responses;
	}


	/**
	 * 列表查询抢购活动
	 * @author yxz
	 */
	public  List<FlashSaleActivityVO> listNew(FlashSaleActivityQueryRequest queryRequest){
		List<FlashSaleActivityResponse> flashSaleActivityVOList = this.list(queryRequest);

		//查询秒杀场次设置
		FlashSaleSettingListRequest request = new FlashSaleSettingListRequest();
		request.setDelFlag(DeleteFlag.NO);
		request.setStatus(EnableStatus.ENABLE);
		request.putSort("time", "asc");
		List<FlashSaleSettingVO> flashSaleSettingVOList = flashSaleSettingQueryProvider.list(request).getContext()
				.getFlashSaleSettingVOList();

		List<FlashSaleActivityVO> result = new ArrayList<>();
		FlashSaleActivityVO activityVO;
		for (int day = 0; day < 30; day++) {
			String date = DateUtil.getDate(LocalDateTime.now().plusDays(day));
			for (FlashSaleSettingVO flashSaleSettingVO : flashSaleSettingVOList) {
				LocalDateTime fullTime = LocalDateTime.parse(date + " " + flashSaleSettingVO.getTime(),
						DateTimeFormatter.ofPattern(DateUtil.FMT_TIME_2));
				if (fullTime.isBefore(queryRequest.getFullTimeBegin()) || fullTime.isAfter(queryRequest.getFullTimeEnd())) {
					continue;
				}
				activityVO = new FlashSaleActivityVO();
				activityVO.setActivityDate(date);
				activityVO.setActivityTime(flashSaleSettingVO.getTime());
				activityVO.setActivityFullTime(fullTime);
				activityVO.setActivityEndTime(fullTime.plusHours(Constants.FLASH_SALE_LAST_HOUR));
				activityVO.setStoreNum(0);
				activityVO.setGoodsNum(0);
				Optional<FlashSaleActivityResponse> flashSaleActivityVO = flashSaleActivityVOList.stream().filter(f -> f
						.getActivityDate().equals(date) &&
						f.getActivityTime()
								.equals(flashSaleSettingVO.getTime())).findFirst();
				if (flashSaleActivityVO.isPresent()) {
					activityVO.setGoodsNum(flashSaleActivityVO.get().getGoodsNum());
					activityVO.setStoreNum(flashSaleActivityVO.get().getStoreNum());
				}
				result.add(activityVO);
			}
		}
		return result;
	}

}
