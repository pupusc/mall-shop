package com.wanmi.sbc.goods.provider.impl.goodssharerecord;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.goodssharerecord.GoodsShareRecordQueryProvider;
import com.wanmi.sbc.goods.api.request.goodssharerecord.GoodsShareRecordPageRequest;
import com.wanmi.sbc.goods.api.request.goodssharerecord.GoodsShareRecordQueryRequest;
import com.wanmi.sbc.goods.api.response.goodssharerecord.GoodsShareRecordPageResponse;
import com.wanmi.sbc.goods.api.request.goodssharerecord.GoodsShareRecordListRequest;
import com.wanmi.sbc.goods.api.response.goodssharerecord.GoodsShareRecordListResponse;
import com.wanmi.sbc.goods.api.request.goodssharerecord.GoodsShareRecordByIdRequest;
import com.wanmi.sbc.goods.api.response.goodssharerecord.GoodsShareRecordByIdResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsShareRecordVO;
import com.wanmi.sbc.goods.goodssharerecord.service.GoodsShareRecordService;
import com.wanmi.sbc.goods.goodssharerecord.model.root.GoodsShareRecord;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>商品分享查询服务接口实现</p>
 * @author zhangwenchang
 * @date 2020-03-06 13:46:24
 */
@RestController
@Validated
public class GoodsShareRecordQueryController implements GoodsShareRecordQueryProvider {
	@Autowired
	private GoodsShareRecordService goodsShareRecordService;

	@Override
	public BaseResponse<GoodsShareRecordPageResponse> page(@RequestBody @Valid GoodsShareRecordPageRequest goodsShareRecordPageReq) {
		GoodsShareRecordQueryRequest queryReq = new GoodsShareRecordQueryRequest();
		KsBeanUtil.copyPropertiesThird(goodsShareRecordPageReq, queryReq);
		Page<GoodsShareRecord> goodsShareRecordPage = goodsShareRecordService.page(queryReq);
		Page<GoodsShareRecordVO> newPage = goodsShareRecordPage.map(entity -> goodsShareRecordService.wrapperVo(entity));
		MicroServicePage<GoodsShareRecordVO> microPage = new MicroServicePage<>(newPage, goodsShareRecordPageReq.getPageable());
		GoodsShareRecordPageResponse finalRes = new GoodsShareRecordPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<GoodsShareRecordListResponse> list(@RequestBody @Valid GoodsShareRecordListRequest goodsShareRecordListReq) {
		GoodsShareRecordQueryRequest queryReq = new GoodsShareRecordQueryRequest();
		KsBeanUtil.copyPropertiesThird(goodsShareRecordListReq, queryReq);
		List<GoodsShareRecord> goodsShareRecordList = goodsShareRecordService.list(queryReq);
		List<GoodsShareRecordVO> newList = goodsShareRecordList.stream().map(entity -> goodsShareRecordService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new GoodsShareRecordListResponse(newList));
	}

	@Override
	public BaseResponse<GoodsShareRecordByIdResponse> getById(@RequestBody @Valid GoodsShareRecordByIdRequest goodsShareRecordByIdRequest) {
		GoodsShareRecord goodsShareRecord = goodsShareRecordService.getById(goodsShareRecordByIdRequest.getShareId());
		return BaseResponse.success(new GoodsShareRecordByIdResponse(goodsShareRecordService.wrapperVo(goodsShareRecord)));
	}

}

