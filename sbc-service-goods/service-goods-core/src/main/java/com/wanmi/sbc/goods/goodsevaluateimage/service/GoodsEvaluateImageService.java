package com.wanmi.sbc.goods.goodsevaluateimage.service;

import io.seata.spring.annotation.GlobalTransactional;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.request.goodsevaluateimage.EvaluateImgUpdateIsShowReq;
import com.wanmi.sbc.goods.api.request.goodsevaluateimage.GoodsEvaluateImageDelByEvaluateIdRequest;
import com.wanmi.sbc.goods.api.request.goodsevaluateimage.GoodsEvaluateImageListRequest;
import com.wanmi.sbc.goods.api.request.goodsevaluateimage.GoodsEvaluateImageQueryRequest;
import com.wanmi.sbc.goods.bean.vo.GoodsEvaluateImageVO;
import com.wanmi.sbc.goods.bean.vo.GoodsEvaluateVO;
import com.wanmi.sbc.goods.goodsevaluateimage.model.root.GoodsEvaluateImage;
import com.wanmi.sbc.goods.goodsevaluateimage.repository.GoodsEvaluateImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>商品评价图片业务逻辑</p>
 * @author liutao
 * @date 2019-02-26 09:56:17
 */
@Service("GoodsEvaluateImageService")
public class GoodsEvaluateImageService {
	@Autowired
	private GoodsEvaluateImageRepository goodsEvaluateImageRepository;
	
	/** 
	 * 新增商品评价图片
	 * @author liutao
	 */
	@GlobalTransactional
	@Transactional(rollbackFor = Exception.class)
	public GoodsEvaluateImage add(GoodsEvaluateImage entity) {
		goodsEvaluateImageRepository.save(entity);
		return entity;
	}
	
	/** 
	 * 修改商品评价图片
	 * @author liutao
	 */
	@Transactional
	public GoodsEvaluateImage modify(GoodsEvaluateImage entity) {
		goodsEvaluateImageRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除商品评价图片
	 * @author liutao
	 */
	@Transactional
	public void deleteById(String id) {
		goodsEvaluateImageRepository.deleteById(id);
	}
	
	/** 
	 * 批量删除商品评价图片
	 * @author liutao
	 */
	@Transactional
	public void deleteByIdList(List<String> ids) {
		goodsEvaluateImageRepository.deleteAll(ids.stream().map(id -> {
			GoodsEvaluateImage entity = new GoodsEvaluateImage();
			entity.setImageId(id);
			return entity;
		}).collect(Collectors.toList()));
	}
	
	/** 
	 * 单个查询商品评价图片
	 * @author liutao
	 */
	public GoodsEvaluateImage getById(String id){
		return goodsEvaluateImageRepository.findById(id).orElse(null);
	}
	
	/** 
	 * 分页查询商品评价图片
	 * @author liutao
	 */
	public Page<GoodsEvaluateImage> page(GoodsEvaluateImageQueryRequest queryReq){
		return goodsEvaluateImageRepository.findAll(
				GoodsEvaluateImageWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}
	
	/** 
	 * 列表查询商品评价图片
	 * @author liutao
	 */
	public List<GoodsEvaluateImage> list(GoodsEvaluateImageQueryRequest queryReq){
		return goodsEvaluateImageRepository.findAll(GoodsEvaluateImageWhereCriteriaBuilder.build(queryReq));
	}

	/**
	 * 将实体包装成VO
	 * @author liutao
	 */
	public GoodsEvaluateImageVO wrapperVo(GoodsEvaluateImage goodsEvaluateImage) {
		if (goodsEvaluateImage != null){
			GoodsEvaluateImageVO goodsEvaluateImageVO=new GoodsEvaluateImageVO();
			KsBeanUtil.copyPropertiesThird(goodsEvaluateImage,goodsEvaluateImageVO);
			if (Objects.nonNull(goodsEvaluateImage.getGoodsEvaluate())) {
				GoodsEvaluateVO goodsEvaluateVO = new GoodsEvaluateVO();
				KsBeanUtil.copyPropertiesThird(goodsEvaluateImage.getGoodsEvaluate(), goodsEvaluateVO);
				goodsEvaluateImageVO.setGoodsEvaluate(goodsEvaluateVO);
			}
			return goodsEvaluateImageVO;
		}
		return null;
	}

	/**
	 * 单个查询商品评价图片
	 * @author liutao
	 */
	public GoodsEvaluateImage getByEvaluateId(String evaluateId){
		GoodsEvaluateImageListRequest goodsEvaluateImageListRequest = new GoodsEvaluateImageListRequest();
		goodsEvaluateImageListRequest.setEvaluateId(evaluateId);
		return goodsEvaluateImageRepository.findById(evaluateId).orElse(null);
	}

	/**
	 * 根据评价id 删除评价图片
	 * @param delByEvaluateIdRequest
	 */
	@Transactional
	public void deleteByEvaluateId(GoodsEvaluateImageDelByEvaluateIdRequest delByEvaluateIdRequest) {
		goodsEvaluateImageRepository.deleteByEvaluateId(delByEvaluateIdRequest.getEvaluateId());
	}

	/**
	 * @param req {@link EvaluateImgUpdateIsShowReq}
	 * @Description: 商品ID更新晒单是否显示
	 * @Author: Bob
	 * @Date: 2019-04-24 16:58
	 */
	@Transactional
	public int updateIsShowByEvaluateId(EvaluateImgUpdateIsShowReq req) {
		return goodsEvaluateImageRepository.updateIsShowByGoodsId(req.getIsShow(), req.getEvaluateId());
	}

}
