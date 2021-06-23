package com.wanmi.sbc.elastic.goods.service;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.elastic.api.request.goods.EsCateDeleteRequest;
import com.wanmi.sbc.elastic.goods.model.root.EsCateBrand;
import com.wanmi.sbc.elastic.goods.model.root.GoodsBrandNest;
import com.wanmi.sbc.elastic.goods.model.root.GoodsCateNest;
import com.wanmi.sbc.goods.api.provider.cate.GoodsCateQueryProvider;
import com.wanmi.sbc.goods.bean.vo.GoodsBrandVO;
import com.wanmi.sbc.goods.bean.vo.GoodsCateVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * ES商品信息数据源操作
 * Created by daiyitian on 2017/4/21.
 */
@Service
public class EsCateBrandService {

    @Autowired
    private GoodsCateQueryProvider goodsCateQueryProvider;

    @Autowired
    private EsGoodsInfoElasticService esGoodsInfoElasticService;

    /**
     * ES_cate_brand定义结构
     *
     * @param goodsCate  商品分类
     * @param goodsBrand 商品品牌
     * @return
     */
    public EsCateBrand putEsCateBrand(GoodsCateVO goodsCate, GoodsBrandVO goodsBrand) {
        GoodsCateNest cate = new GoodsCateNest();
        cate.setCateId(0L);
        if (goodsCate != null) {
            KsBeanUtil.copyPropertiesThird(goodsCate, cate);
            cate.setPinYin(ObjectUtils.toString(goodsCate.getCateName()));
        }

        GoodsBrandNest brand = new GoodsBrandNest();
        brand.setBrandId(0L);
        if (goodsBrand != null) {
            KsBeanUtil.copyPropertiesThird(goodsBrand, brand);
            brand.setPinYin(ObjectUtils.toString(goodsBrand.getBrandName()));
        }
        EsCateBrand esCateBrand = new EsCateBrand();
        esCateBrand.setId(String.valueOf(cate.getCateId()).concat("_").concat(String.valueOf(brand.getBrandId())));
        esCateBrand.setGoodsCate(cate);
        esCateBrand.setGoodsBrand(brand);
        return esCateBrand;
    }



    /**
     * 商品分类同步至es
     *
     * @param updateToEs
     */
    public void updateToEs(List<GoodsCateVO> updateToEs) {
        if (CollectionUtils.isNotEmpty(updateToEs)) {
            esGoodsInfoElasticService.updateCateName(updateToEs.get(0));
        }
    }

    /**
     * es模块-创建时迁移
     *
     * @param delRequest
     */
    public void deleteCateFromEs(EsCateDeleteRequest delRequest) {
//        GoodsCateNest goodsCateNest;
//        //查询默认分类
//        if (delRequest.isDefault()) {
//            //查询默认分类
//            GoodsCateListByConditionRequest request = new GoodsCateListByConditionRequest();
//            request.setIsDefault(DefaultFlag.YES.toValue());
//            List<GoodsCateVO> goodsCateList = null;
//            GoodsCateListByConditionResponse goodsCateListByConditionResponse =
//                    goodsCateQueryProvider.listByCondition(request).getContext();
//            if (Objects.nonNull(goodsCateListByConditionResponse)) {
//                goodsCateList = goodsCateListByConditionResponse.getGoodsCateVOList();
//            }
//            //如果默认分类不存在，不允许删除
//            if (CollectionUtils.isEmpty(goodsCateList)) {
//                throw new SbcRuntimeException(GoodsCateErrorCode.DEFAULT_CATE_NOT_EXIST);
//            } else {
//                goodsCateNest = KsBeanUtil.convert(goodsCateList.get(0), GoodsCateNest.class);
//            }
//        } else {
//            if (delRequest.getInsteadCate() != null) {//添加删除之后的替换分类
//                goodsCateNest = KsBeanUtil.convert(delRequest.getInsteadCate(), GoodsCateNest.class);
//            } else {
//                goodsCateNest = new GoodsCateNest();
//            }
//        }

//        Iterable<EsCateBrand> esCateBrands = this.queryCateBrandByCateIds(delRequest.getDeleteIds());
//        List<EsCateBrand> cateBrandList = new ArrayList<>();
//        esCateBrands.forEach(cateBrand -> {
//            cateBrand.setGoodsCate(goodsCateNest);
//            cateBrandList.add(cateBrand);
//        });
//        if (CollectionUtils.isNotEmpty(cateBrandList)) {
//            this.save(cateBrandList);
//        }

    }

    /**
     * es模块-创建时迁移
     *
     * @param isDelete   是否是删除品牌，false时表示更新品牌
     * @param goodsBrand 操作品牌实体
     */
    public void updateBrandFromEs(boolean isDelete, GoodsBrandVO goodsBrand) {
        if (Objects.nonNull(goodsBrand)) {
            if (isDelete) {
                goodsBrand = new GoodsBrandVO();
            }
            esGoodsInfoElasticService.updateBrandName(goodsBrand);
        }
    }
}
