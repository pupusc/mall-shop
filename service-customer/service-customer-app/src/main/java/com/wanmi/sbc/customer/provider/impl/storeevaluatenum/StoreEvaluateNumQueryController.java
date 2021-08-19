package com.wanmi.sbc.customer.provider.impl.storeevaluatenum;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.storeevaluatenum.StoreEvaluateNumQueryProvider;
import com.wanmi.sbc.customer.api.request.storeevaluatenum.StoreEvaluateNumByIdRequest;
import com.wanmi.sbc.customer.api.request.storeevaluatenum.StoreEvaluateNumListRequest;
import com.wanmi.sbc.customer.api.request.storeevaluatenum.StoreEvaluateNumPageRequest;
import com.wanmi.sbc.customer.api.request.storeevaluatenum.StoreEvaluateNumQueryRequest;
import com.wanmi.sbc.customer.api.response.storeevaluatenum.StoreEvaluateNumByIdResponse;
import com.wanmi.sbc.customer.api.response.storeevaluatenum.StoreEvaluateNumListResponse;
import com.wanmi.sbc.customer.api.response.storeevaluatenum.StoreEvaluateNumPageResponse;
import com.wanmi.sbc.customer.api.response.storeevaluatenum.StoreEvaluateNumResponse;
import com.wanmi.sbc.customer.bean.vo.StoreEvaluateNumVO;
import com.wanmi.sbc.customer.storeevaluatenum.model.root.StoreEvaluateNum;
import com.wanmi.sbc.customer.storeevaluatenum.service.StoreEvaluateNumService;
import com.wanmi.sbc.setting.api.provider.evaluateratio.EvaluateRatioQueryProvider;
import com.wanmi.sbc.setting.api.response.evaluateratio.EvaluateRatioByIdResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>店铺统计评分等级人数统计查询服务接口实现</p>
 * @author liutao
 * @date 2019-03-04 10:55:28
 */
@RestController
@Validated
public class StoreEvaluateNumQueryController implements StoreEvaluateNumQueryProvider {

	@Autowired
	private StoreEvaluateNumService storeEvaluateNumService;

	@Autowired
	private EvaluateRatioQueryProvider evaluateRatioQueryProvider;

	@Override
	public BaseResponse<StoreEvaluateNumPageResponse> page(@RequestBody @Valid StoreEvaluateNumPageRequest storeEvaluateNumPageReq) {
		StoreEvaluateNumQueryRequest queryReq = new StoreEvaluateNumQueryRequest();
		KsBeanUtil.copyPropertiesThird(storeEvaluateNumPageReq, queryReq);
		Page<StoreEvaluateNum> storeEvaluateNumPage = storeEvaluateNumService.page(queryReq);
		Page<StoreEvaluateNumVO> newPage = storeEvaluateNumPage.map(entity -> storeEvaluateNumService.wrapperVo(entity));
		MicroServicePage<StoreEvaluateNumVO> microPage = new MicroServicePage<>(newPage, storeEvaluateNumPageReq.getPageable());
		StoreEvaluateNumPageResponse finalRes = new StoreEvaluateNumPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<StoreEvaluateNumListResponse> list(@RequestBody @Valid StoreEvaluateNumListRequest storeEvaluateNumListReq) {
		StoreEvaluateNumQueryRequest queryReq = new StoreEvaluateNumQueryRequest();
		KsBeanUtil.copyPropertiesThird(storeEvaluateNumListReq, queryReq);
		List<StoreEvaluateNum> storeEvaluateNumList = storeEvaluateNumService.list(queryReq);
		List<StoreEvaluateNumVO> newList = storeEvaluateNumList.stream().map(entity -> storeEvaluateNumService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new StoreEvaluateNumListResponse(newList));
	}

	@Override
	public BaseResponse<StoreEvaluateNumByIdResponse> getById(@RequestBody @Valid StoreEvaluateNumByIdRequest storeEvaluateNumByIdRequest) {
		StoreEvaluateNum storeEvaluateNum = storeEvaluateNumService.getById(storeEvaluateNumByIdRequest.getNumId());
		return BaseResponse.success(new StoreEvaluateNumByIdResponse(storeEvaluateNumService.wrapperVo(storeEvaluateNum)));
	}

	@Override
	public BaseResponse<StoreEvaluateNumResponse> listByStoreIdAndScoreCycle(@RequestBody @Valid
																							 StoreEvaluateNumListRequest listRequest) {
		StoreEvaluateNumResponse response = new StoreEvaluateNumResponse();
		//获取评分列表
		List<StoreEvaluateNum> storeEvaluateNumList = storeEvaluateNumService.listByStoreIdAndScoreCycle(listRequest);
		List<StoreEvaluateNumVO> newList = storeEvaluateNumList.stream().map(entity -> storeEvaluateNumService.wrapperVo(entity)).collect(Collectors.toList());
		response.setStoreEvaluateNumVOList(newList);
		//获取其中一条数据 计算总人数
		Long count=0L;
		if(CollectionUtils.isNotEmpty(newList)){
			StoreEvaluateNumVO evaluateNumVO = newList.get(0);
			count = evaluateNumVO.getDifferenceNum()+evaluateNumVO.getExcellentNum()+evaluateNumVO.getMediumNum();
		}
		response.setEvaluateCount(count);

		//获取系数
		EvaluateRatioByIdResponse idResponse = evaluateRatioQueryProvider.findOne().getContext();
		//计算综合评分
		BigDecimal score = BigDecimal.ZERO;
		if(CollectionUtils.isNotEmpty(newList) && Objects.nonNull(idResponse.getEvaluateRatioVO())){
			for(StoreEvaluateNumVO evaluateNumVO : newList){
				//商品评分
				if(evaluateNumVO.getNumType()==0){
					score = score.add(idResponse.getEvaluateRatioVO().getGoodsRatio().multiply(evaluateNumVO
							.getSumCompositeScore()));
				}else if(evaluateNumVO.getNumType()==1){
					//服务评分
					score = score.add(idResponse.getEvaluateRatioVO().getServerRatio().multiply(evaluateNumVO
							.getSumCompositeScore()));
				}else if(evaluateNumVO.getNumType()==2){
					//物流评分
					score = score.add(idResponse.getEvaluateRatioVO().getLogisticsRatio().multiply(evaluateNumVO
							.getSumCompositeScore()));
				}
			}
		}

		response.setCompositeScore(score.setScale(2, BigDecimal.ROUND_HALF_UP));
		return BaseResponse.success(response);
	}

}

