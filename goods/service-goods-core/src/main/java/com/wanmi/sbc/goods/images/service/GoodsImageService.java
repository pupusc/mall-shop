package com.wanmi.sbc.goods.images.service;

import com.wanmi.sbc.goods.images.GoodsImage;
import com.wanmi.sbc.goods.images.GoodsImageRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.wanmi.sbc.goods.bean.vo.GoodsImageVO;
import com.wanmi.sbc.common.util.KsBeanUtil;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>商品图片业务逻辑</p>
 * @author liutao
 * @date 2019-02-26 10:35:57
 */
@Service("GoodsImageService")
public class GoodsImageService {
	@Autowired
	private GoodsImageRepository goodsImageRepository;
	
	/**
	 * 根据商品ID查询
	 * @author liutao
	 */
	public List<GoodsImageVO> findByGoodsId(String goodsId){
		List<GoodsImage> goodsImageList = goodsImageRepository.findByGoodsId(goodsId);
		if (CollectionUtils.isEmpty(goodsImageList)){
			return null;
		}
		List<GoodsImageVO> goodsImageVOList = goodsImageList.stream().map(goodsImage -> this.wrapperVo(goodsImage)).collect(Collectors.toList());
		return goodsImageVOList;
	}
	/**
	 * 根据商品ID集合批量查询
	 * @author liutao
	 */
	public List<GoodsImageVO> findByGoodsIds(List<String> goodsIds){
		List<GoodsImage> goodsImageList = goodsImageRepository.findByGoodsIds(goodsIds);
		if (CollectionUtils.isEmpty(goodsImageList)){
			return null;
		}
		List<GoodsImageVO> goodsImageVOList = goodsImageList.stream().map(goodsImage -> this.wrapperVo(goodsImage)).collect(Collectors.toList());
		return goodsImageVOList;
	}


	/**
	 * 将实体包装成VO
	 * @author liutao
	 */
	public GoodsImageVO wrapperVo(GoodsImage goodsImage) {
		if (goodsImage != null){
			GoodsImageVO goodsImageVO=new GoodsImageVO();
			KsBeanUtil.copyPropertiesThird(goodsImage,goodsImageVO);
			return goodsImageVO;
		}
		return null;
	}
}
