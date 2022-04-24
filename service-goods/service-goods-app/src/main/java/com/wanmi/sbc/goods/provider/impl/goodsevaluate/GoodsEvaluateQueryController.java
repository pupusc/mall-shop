package com.wanmi.sbc.goods.provider.impl.goodsevaluate;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.goodsevaluate.GoodsEvaluateQueryProvider;
import com.wanmi.sbc.goods.api.request.goodsevaluate.*;
import com.wanmi.sbc.goods.api.response.goodsevaluate.GoodsEvaluateByIdResponse;
import com.wanmi.sbc.goods.api.response.goodsevaluate.GoodsEvaluateCountResponse;
import com.wanmi.sbc.goods.api.response.goodsevaluate.GoodsEvaluateListResponse;
import com.wanmi.sbc.goods.api.response.goodsevaluate.GoodsEvaluatePageResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsEvaluateVO;
import com.wanmi.sbc.goods.goodsevaluate.model.root.GoodsEvaluate;
import com.wanmi.sbc.goods.goodsevaluate.service.GoodsEvaluateService;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.repository.GoodsInfoRepository;
import com.wanmi.sbc.goods.info.service.GoodsService;
import com.wanmi.sbc.goods.redis.RedisService;
import io.seata.common.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>商品评价查询服务接口实现</p>
 * @author liutao
 * @date 2019-02-25 15:14:16
 */
@RestController
@Validated
public class GoodsEvaluateQueryController implements GoodsEvaluateQueryProvider {
	@Autowired
	private GoodsEvaluateService goodsEvaluateService;

	@Autowired
	private GoodsInfoRepository goodsInfoRepository;

	@Autowired
	private GoodsService goodsService;

	@Autowired
	private RedisService redisService;

	/**
	 * 分页查询商品评价
	 *
	 * @param goodsEvaluatePageReq 分页请求参数和筛选对象 {@link GoodsEvaluatePageRequest}
	 * @return
	 */
	@Override
	public BaseResponse<GoodsEvaluatePageResponse> page(@RequestBody @Valid GoodsEvaluatePageRequest goodsEvaluatePageReq) {
		GoodsEvaluateQueryRequest queryReq = new GoodsEvaluateQueryRequest();
		KsBeanUtil.copyPropertiesThird(goodsEvaluatePageReq, queryReq);
		Page<GoodsEvaluate> goodsEvaluatePage = goodsEvaluateService.page(queryReq);
		List<GoodsEvaluateVO> goodsEvaluateVOS = goodsEvaluateService.wrapperVo(goodsEvaluatePage.getContent());
		Page<GoodsEvaluateVO> newPage = new PageImpl(goodsEvaluateVOS, goodsEvaluatePage.getPageable(), goodsEvaluatePage.getTotalElements());
		MicroServicePage<GoodsEvaluateVO> microPage = new MicroServicePage<>(newPage, goodsEvaluatePageReq.getPageable());
		GoodsEvaluatePageResponse finalRes = new GoodsEvaluatePageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<GoodsEvaluateListResponse> list(@RequestBody @Valid GoodsEvaluateListRequest goodsEvaluateListReq) {
		GoodsEvaluateQueryRequest queryReq = new GoodsEvaluateQueryRequest();
		KsBeanUtil.copyPropertiesThird(goodsEvaluateListReq, queryReq);
		List<GoodsEvaluate> goodsEvaluateList = goodsEvaluateService.list(queryReq);
		List<GoodsEvaluateVO> newList = goodsEvaluateList.stream().map(entity -> goodsEvaluateService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new GoodsEvaluateListResponse(newList));
	}

	@Override
	public BaseResponse<GoodsEvaluateByIdResponse> getById(@RequestBody @Valid GoodsEvaluateByIdRequest goodsEvaluateByIdRequest) {
		GoodsEvaluate goodsEvaluate = goodsEvaluateService.getById(goodsEvaluateByIdRequest.getEvaluateId());
		return BaseResponse.success(new GoodsEvaluateByIdResponse(goodsEvaluateService.wrapperVo(goodsEvaluate)));
	}

	@Override
	public BaseResponse<Long> getGoodsEvaluateNum(@RequestBody  GoodsEvaluateQueryRequest queryReq){
		return BaseResponse.success(goodsEvaluateService.getGoodsEvaluateNum(queryReq));
	}

	/**
	 * @param request {@link GoodsEvaluateCountRequset}
	 * @Description: 该spu商品评价总数、晒单、好评率
	 * @Author: Bob
	 * @Date: 2019-04-04 16:17
	 */
	@Override
	public BaseResponse<GoodsEvaluateCountResponse> getGoodsEvaluateSum(@RequestBody @Valid GoodsEvaluateCountRequset request) {
		String goodsEvaluateStr = redisService.getString(RedisKeyConstant.KEY_GOODS_EVALUATE + request.getGoodsId());
		GoodsEvaluateCountResponse goodsEvaluateCountResponse = null;
		if (StringUtils.isNotBlank(goodsEvaluateStr)) {
			goodsEvaluateCountResponse = KsBeanUtil.convert(goodsEvaluateStr, GoodsEvaluateCountResponse.class);
			if (goodsEvaluateCountResponse != null) {
				return BaseResponse.success(goodsEvaluateCountResponse);
			}
		}
		//商品详情中的评价总数不包含隐藏的评论，好评率包含隐藏的评论
		GoodsEvaluateQueryRequest queryRequest =
				GoodsEvaluateQueryRequest.builder().goodsId(request.getGoodsId()).isShow(1).delFlag(0).build();
		Long count = goodsEvaluateService.getGoodsEvaluateNum(queryRequest);
		queryRequest.setIsUpload(1);
		Long uploadCount = goodsEvaluateService.getGoodsEvaluateNum(queryRequest);
		String praise = goodsEvaluateService.getGoodsPraise(request);
		goodsEvaluateCountResponse = new GoodsEvaluateCountResponse();
		goodsEvaluateCountResponse.setEvaluateConut(count);
		goodsEvaluateCountResponse.setPostOrderCount(uploadCount);
		goodsEvaluateCountResponse.setPraise(praise);
		redisService.setString(RedisKeyConstant.KEY_GOODS_EVALUATE + request.getGoodsId(), JSON.toJSONString(goodsEvaluateCountResponse), 5 * 60);
		return BaseResponse.success(goodsEvaluateCountResponse);
	}

	/**
	 * @param request {@link GoodsEvaluatePageRequest}
	 * @Description: 查询最新评价<排除系统评价>
	 * @Author: Bob
	 * @Date: 2019-05-29 17:49
	 */
	@Override
	public BaseResponse<GoodsEvaluateListResponse> getGoodsEvaluateTopData(@RequestBody @Valid GoodsEvaluatePageRequest request) {
		List<GoodsEvaluate> goodsEvaluateList = goodsEvaluateService.getTop(request);
		List<GoodsEvaluateVO> newList = goodsEvaluateList.stream().map(entity -> goodsEvaluateService.wrapperVo(entity)).collect(Collectors.toList());

		//书友说查询
		request.setPageSize(6);
		List<GoodsEvaluate> bookFriendEvaluate = goodsEvaluateService.getBookFriendEvaluate(request);
		List<GoodsEvaluateVO> bookFriendEvaluateVO = bookFriendEvaluate.stream().map(e -> {
			GoodsEvaluateVO eva = new GoodsEvaluateVO();
			BeanUtils.copyProperties(e, eva);
			return eva;
		}).collect(Collectors.toList());
		return BaseResponse.success(new GoodsEvaluateListResponse(newList, bookFriendEvaluateVO));
	}

	/**
	 * @param
	 * @return
	 * @discription 根据skuid查该spu商品评价总数、晒单、好评率
	 * @author yangzhen
	 * @date 2020/9/2 14:00
	 */
	@Override
	public BaseResponse<GoodsEvaluateCountResponse> getGoodsEvaluateSumByskuId(@Valid GoodsEvaluateCountBySkuIdRequset request) {

		String goodsEvaluateCountStr = redisService.getString(RedisKeyConstant.KEY_GOODS_INFO_EVALUATE + request.getSkuId());
		GoodsEvaluateCountResponse goodsEvaluateCountResponse = null;
		if (StringUtils.isNotBlank(goodsEvaluateCountStr)) {
			if (StringUtils.isNotBlank(goodsEvaluateCountStr)) {
				goodsEvaluateCountResponse = KsBeanUtil.convert(goodsEvaluateCountStr, GoodsEvaluateCountResponse.class);
				if (goodsEvaluateCountResponse != null) {
					return BaseResponse.success(goodsEvaluateCountResponse);
				}
			}
		}

		List<GoodsInfo> goodsInfos = goodsInfoRepository.findByGoodsInfoIds(Arrays.asList(request.getSkuId()));
		if (CollectionUtils.isEmpty(goodsInfos)) {
			return BaseResponse.FAILED();
		}
		GoodsEvaluateQueryRequest goodsEvaluateQueryRequest = GoodsEvaluateQueryRequest.builder().goodsId(goodsInfos.get(0).getGoodsId())
				.evaluateCatetory(0).delFlag(DeleteFlag.NO.toValue()).isShow(1).build();
		Long count = goodsEvaluateService.getGoodsEvaluateNum(goodsEvaluateQueryRequest);

		//商品详情中的评价总数
		GoodsEvaluateQueryRequest queryRequest =
				GoodsEvaluateQueryRequest.builder().goodsId(goodsInfos.get(0).getGoodsId()).delFlag(0).evaluateCatetory(0).build();
		queryRequest.setIsUpload(1);

		Long uploadCount = goodsEvaluateService.getGoodsEvaluateNum(queryRequest);
		String praise = goodsEvaluateService.getGoodsPraise(GoodsEvaluateCountRequset
				.builder().goodsId(goodsInfos.get(0).getGoodsId()).build());

		goodsEvaluateCountResponse = GoodsEvaluateCountResponse.builder().evaluateConut(count).postOrderCount(uploadCount)
				.praise(praise).goodsId(goodsInfos.get(0).getGoodsId()).build();
		redisService.setString(RedisKeyConstant.KEY_GOODS_INFO_EVALUATE + request.getSkuId(), JSON.toJSONString(goodsEvaluateCountResponse), 5 * 60);
		return BaseResponse.success(goodsEvaluateCountResponse);
	}
}

