package com.wanmi.sbc.goods.provider.impl.thirdgoodscate;

import com.wanmi.sbc.goods.api.provider.thirdgoodscate.ThirdGoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.request.thirdgoodscate.*;
import com.wanmi.sbc.goods.bean.dto.ThirdGoodsCateRelDTO;
import com.wanmi.sbc.goods.bean.vo.ThirdGoodsCateRelVO;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.response.thirdgoodscate.ThirdGoodsCatePageResponse;
import com.wanmi.sbc.goods.api.response.thirdgoodscate.ThirdGoodsCateListResponse;
import com.wanmi.sbc.goods.api.response.thirdgoodscate.ThirdGoodsCateByIdResponse;
import com.wanmi.sbc.goods.bean.vo.ThirdGoodsCateVO;
import com.wanmi.sbc.goods.thirdgoodscate.service.ThirdGoodsCateService;
import com.wanmi.sbc.goods.thirdgoodscate.model.root.ThirdGoodsCate;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>查询服务接口实现</p>
 * @author 
 * @date 2020-08-17 14:46:43
 */
@RestController
@Validated
public class ThirdGoodsCateQueryController implements ThirdGoodsCateQueryProvider {
	@Autowired
	private ThirdGoodsCateService thirdGoodsCateService;

	@Override
	public BaseResponse<ThirdGoodsCatePageResponse> page(@RequestBody @Valid ThirdGoodsCatePageRequest thirdGoodsCatePageReq) {
		ThirdGoodsCateQueryRequest queryReq = KsBeanUtil.convert(thirdGoodsCatePageReq, ThirdGoodsCateQueryRequest.class);
		Page<ThirdGoodsCate> thirdGoodsCatePage = thirdGoodsCateService.page(queryReq);
		Page<ThirdGoodsCateVO> newPage = thirdGoodsCatePage.map(entity -> thirdGoodsCateService.wrapperVo(entity));
		MicroServicePage<ThirdGoodsCateVO> microPage = new MicroServicePage<>(newPage, thirdGoodsCatePageReq.getPageable());
		ThirdGoodsCatePageResponse finalRes = new ThirdGoodsCatePageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<ThirdGoodsCateListResponse> list(@RequestBody @Valid ThirdGoodsCateListRequest thirdGoodsCateListReq) {
		ThirdGoodsCateQueryRequest queryReq = KsBeanUtil.convert(thirdGoodsCateListReq, ThirdGoodsCateQueryRequest.class);
		List<ThirdGoodsCate> thirdGoodsCateList = thirdGoodsCateService.list(queryReq);
		List<ThirdGoodsCateVO> newList = thirdGoodsCateList.stream().map(entity -> thirdGoodsCateService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new ThirdGoodsCateListResponse(newList));
	}

	@Override
	public BaseResponse<List<ThirdGoodsCateRelVO>> listRel(@Valid CateRelRequest request) {

		return BaseResponse.success(thirdGoodsCateService.listRel(request.getThirdPlatformType()));
	}

	@Override
	public BaseResponse<ThirdGoodsCateByIdResponse> getById(@RequestBody @Valid ThirdGoodsCateByIdRequest thirdGoodsCateByIdRequest) {
		ThirdGoodsCate thirdGoodsCate =
		thirdGoodsCateService.getOne(thirdGoodsCateByIdRequest.getId());
		return BaseResponse.success(new ThirdGoodsCateByIdResponse(thirdGoodsCateService.wrapperVo(thirdGoodsCate)));
	}

	@Override
	public BaseResponse<List<ThirdGoodsCateRelDTO>> getRelByParentId(CateRelByParentIdRequest cateRelByParentIdRequest) {
		List<ThirdGoodsCateRelDTO> allRel = thirdGoodsCateService.getAllRel(cateRelByParentIdRequest.getSource(), cateRelByParentIdRequest.getCateParentId());
		return BaseResponse.success(allRel);
	}

}

