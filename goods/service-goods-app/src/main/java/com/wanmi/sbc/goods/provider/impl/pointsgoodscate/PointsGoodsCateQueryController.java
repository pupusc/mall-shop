package com.wanmi.sbc.goods.provider.impl.pointsgoodscate;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.pointsgoodscate.PointsGoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.request.pointsgoodscate.PointsGoodsCatePageRequest;
import com.wanmi.sbc.goods.api.request.pointsgoodscate.PointsGoodsCateQueryRequest;
import com.wanmi.sbc.goods.api.response.pointsgoodscate.PointsGoodsCatePageResponse;
import com.wanmi.sbc.goods.api.request.pointsgoodscate.PointsGoodsCateListRequest;
import com.wanmi.sbc.goods.api.response.pointsgoodscate.PointsGoodsCateListResponse;
import com.wanmi.sbc.goods.api.request.pointsgoodscate.PointsGoodsCateByIdRequest;
import com.wanmi.sbc.goods.api.response.pointsgoodscate.PointsGoodsCateByIdResponse;
import com.wanmi.sbc.goods.bean.vo.PointsGoodsCateVO;
import com.wanmi.sbc.goods.pointsgoodscate.service.PointsGoodsCateService;
import com.wanmi.sbc.goods.pointsgoodscate.model.root.PointsGoodsCate;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>积分商品分类表查询服务接口实现</p>
 * @author yang
 * @date 2019-05-13 09:50:07
 */
@RestController
@Validated
public class PointsGoodsCateQueryController implements PointsGoodsCateQueryProvider {
	@Autowired
	private PointsGoodsCateService pointsGoodsCateService;

	@Override
	public BaseResponse<PointsGoodsCatePageResponse> page(@RequestBody @Valid PointsGoodsCatePageRequest pointsGoodsCatePageReq) {
		PointsGoodsCateQueryRequest queryReq = new PointsGoodsCateQueryRequest();
		KsBeanUtil.copyPropertiesThird(pointsGoodsCatePageReq, queryReq);
		Page<PointsGoodsCate> pointsGoodsCatePage = pointsGoodsCateService.page(queryReq);
		Page<PointsGoodsCateVO> newPage = pointsGoodsCatePage.map(entity -> pointsGoodsCateService.wrapperVo(entity));
		MicroServicePage<PointsGoodsCateVO> microPage = new MicroServicePage<>(newPage, pointsGoodsCatePageReq.getPageable());
		PointsGoodsCatePageResponse finalRes = new PointsGoodsCatePageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<PointsGoodsCateListResponse> list(@RequestBody @Valid PointsGoodsCateListRequest pointsGoodsCateListReq) {
		PointsGoodsCateQueryRequest queryReq = new PointsGoodsCateQueryRequest();
		KsBeanUtil.copyPropertiesThird(pointsGoodsCateListReq, queryReq);
		List<PointsGoodsCate> pointsGoodsCateList = pointsGoodsCateService.list(queryReq);
		List<PointsGoodsCateVO> newList = pointsGoodsCateList.stream().map(entity -> pointsGoodsCateService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new PointsGoodsCateListResponse(newList));
	}

	@Override
	public BaseResponse<PointsGoodsCateByIdResponse> getById(@RequestBody @Valid PointsGoodsCateByIdRequest pointsGoodsCateByIdRequest) {
		PointsGoodsCate pointsGoodsCate = pointsGoodsCateService.getById(pointsGoodsCateByIdRequest.getCateId());
		return BaseResponse.success(new PointsGoodsCateByIdResponse(pointsGoodsCateService.wrapperVo(pointsGoodsCate)));
	}

}

