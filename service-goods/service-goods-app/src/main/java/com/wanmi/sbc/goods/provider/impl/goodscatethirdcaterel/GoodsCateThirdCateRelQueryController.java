package com.wanmi.sbc.goods.provider.impl.goodscatethirdcaterel;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.goodscatethirdcaterel.GoodsCateThirdCateRelQueryProvider;
import com.wanmi.sbc.goods.api.request.goodscatethirdcaterel.GoodsCateThirdCateRelPageRequest;
import com.wanmi.sbc.goods.api.request.goodscatethirdcaterel.GoodsCateThirdCateRelQueryRequest;
import com.wanmi.sbc.goods.api.response.goodscatethirdcaterel.GoodsCateThirdCateRelPageResponse;
import com.wanmi.sbc.goods.api.request.goodscatethirdcaterel.GoodsCateThirdCateRelListRequest;
import com.wanmi.sbc.goods.api.response.goodscatethirdcaterel.GoodsCateThirdCateRelListResponse;
import com.wanmi.sbc.goods.api.request.goodscatethirdcaterel.GoodsCateThirdCateRelByIdRequest;
import com.wanmi.sbc.goods.api.response.goodscatethirdcaterel.GoodsCateThirdCateRelByIdResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsCateThirdCateRelVO;
import com.wanmi.sbc.goods.goodscatethirdcaterel.service.GoodsCateThirdCateRelService;
import com.wanmi.sbc.goods.goodscatethirdcaterel.model.root.GoodsCateThirdCateRel;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>平台类目和第三方平台类目映射查询服务接口实现</p>
 * @author 
 * @date 2020-08-18 19:51:55
 */
@RestController
@Validated
public class GoodsCateThirdCateRelQueryController implements GoodsCateThirdCateRelQueryProvider {
	@Autowired
	private GoodsCateThirdCateRelService goodsCateThirdCateRelService;

	@Override
	public BaseResponse<GoodsCateThirdCateRelPageResponse> page(@RequestBody @Valid GoodsCateThirdCateRelPageRequest goodsCateThirdCateRelPageReq) {
		GoodsCateThirdCateRelQueryRequest queryReq = KsBeanUtil.convert(goodsCateThirdCateRelPageReq, GoodsCateThirdCateRelQueryRequest.class);
		Page<GoodsCateThirdCateRel> goodsCateThirdCateRelPage = goodsCateThirdCateRelService.page(queryReq);
		Page<GoodsCateThirdCateRelVO> newPage = goodsCateThirdCateRelPage.map(entity -> goodsCateThirdCateRelService.wrapperVo(entity));
		MicroServicePage<GoodsCateThirdCateRelVO> microPage = new MicroServicePage<>(newPage, goodsCateThirdCateRelPageReq.getPageable());
		GoodsCateThirdCateRelPageResponse finalRes = new GoodsCateThirdCateRelPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<GoodsCateThirdCateRelListResponse> list(@RequestBody @Valid GoodsCateThirdCateRelListRequest goodsCateThirdCateRelListReq) {
		GoodsCateThirdCateRelQueryRequest queryReq = KsBeanUtil.convert(goodsCateThirdCateRelListReq, GoodsCateThirdCateRelQueryRequest.class);
		List<GoodsCateThirdCateRel> goodsCateThirdCateRelList = goodsCateThirdCateRelService.list(queryReq);
		List<GoodsCateThirdCateRelVO> newList = goodsCateThirdCateRelList.stream().map(entity -> goodsCateThirdCateRelService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new GoodsCateThirdCateRelListResponse(newList));
	}

	@Override
	public BaseResponse<GoodsCateThirdCateRelByIdResponse> getById(@RequestBody @Valid GoodsCateThirdCateRelByIdRequest goodsCateThirdCateRelByIdRequest) {
		GoodsCateThirdCateRel goodsCateThirdCateRel =
		goodsCateThirdCateRelService.getOne(goodsCateThirdCateRelByIdRequest.getId());
		return BaseResponse.success(new GoodsCateThirdCateRelByIdResponse(goodsCateThirdCateRelService.wrapperVo(goodsCateThirdCateRel)));
	}

}

