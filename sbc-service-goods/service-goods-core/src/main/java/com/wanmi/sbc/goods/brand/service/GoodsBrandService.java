package com.wanmi.sbc.goods.brand.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.constant.GoodsBrandErrorCode;
import com.wanmi.sbc.goods.ares.GoodsAresService;
import com.wanmi.sbc.goods.brand.entity.GoodsBrandBase;
import com.wanmi.sbc.goods.brand.model.root.GoodsBrand;
import com.wanmi.sbc.goods.brand.repository.GoodsBrandRepository;
import com.wanmi.sbc.goods.brand.request.GoodsBrandQueryRequest;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 商品品牌服务
 * Created by daiyitian on 2017/4/11.
 */
@Service
@Transactional(readOnly = true, timeout = 10)
public class GoodsBrandService {
    @Autowired
    GoodsAresService goodsAresService;

    @Autowired
    private GoodsBrandRepository goodsBrandRepository;

    @Autowired
    private ContractBrandService contractBrandService;


    /**
     * 条件查询商品品牌
     *
     * @param request 参数
     * @return list
     */
    public Page<GoodsBrand> page(GoodsBrandQueryRequest request) {
        return goodsBrandRepository.findAll(request.getWhereCriteria(), request.getPageRequest());
    }


    /**
     * 条件查询商品品牌
     *
     * @param request 参数
     * @return list
     */
    public List<GoodsBrand> query(GoodsBrandQueryRequest request) {
        List<GoodsBrand> list;
        Sort sort = request.getSort();
        if (Objects.nonNull(sort)) {
            list = goodsBrandRepository.findAll(request.getWhereCriteria(), sort);
        } else {
            list = goodsBrandRepository.findAll(request.getWhereCriteria());
        }
        return ListUtils.emptyIfNull(list);
    }

    /**
     * 条件查询商品品牌组件
     *
     * @param brandId 品牌ID
     * @return list
     */
    public GoodsBrand findById(Long brandId) {
        return goodsBrandRepository.findById(brandId).orElse(null);
    }

    /**
     * 批量获取商品品牌
     *
     * @param brandIds
     * @return
     */
    public List<GoodsBrand> findByIds(List<Long> brandIds) {
        return goodsBrandRepository.findByBrandIdIn(brandIds);
    }

    /**
     * 新增品牌
     *
     * @param goodsBrand 品牌信息
     * @throws SbcRuntimeException 业务异常
     */
    @Transactional
    public GoodsBrand add(GoodsBrand goodsBrand) throws SbcRuntimeException {
        GoodsBrandQueryRequest request = new GoodsBrandQueryRequest();
        request.setDelFlag(DeleteFlag.NO.toValue());
        //限制重复名称
        request.setBrandName(goodsBrand.getBrandName());
        if (goodsBrandRepository.count(request.getWhereCriteria()) > 0) {
            throw new SbcRuntimeException(GoodsBrandErrorCode.NAME_ALREADY_EXISTS);
        }
        //去除品牌个数限制
//        request.setBrandName(null);
//        //限制品牌数
//        if (goodsBrandRepository.count(request.getWhereCriteria()) >= Constants.BRAND_MAX_SIZE) {
//            throw new SbcRuntimeException(ResultCode.ERROR_030202, new Object[]{Constants.BRAND_MAX_SIZE});
//        }
        goodsBrand.setDelFlag(DeleteFlag.NO);
        goodsBrand.setCreateTime(LocalDateTime.now());
        goodsBrand.setUpdateTime(LocalDateTime.now());
        goodsBrand.setBrandId(goodsBrandRepository.save(goodsBrand).getBrandId());

        //ares埋点-商品-后台添加商品品牌
        goodsAresService.dispatchFunction("addGoodsBrand", goodsBrand);
        return goodsBrand;
    }

    /**
     * 编辑品牌
     *
     * @param newGoodsBrand 品牌信息
     * @throws SbcRuntimeException 业务异常
     */
    @Transactional
    public GoodsBrand edit(GoodsBrand newGoodsBrand) throws SbcRuntimeException {
        GoodsBrand oldGoodsBrand = goodsBrandRepository.findById(newGoodsBrand.getBrandId()).orElse(null);
        if (oldGoodsBrand == null || oldGoodsBrand.getDelFlag().compareTo(DeleteFlag.YES) == 0) {
            throw new SbcRuntimeException(GoodsBrandErrorCode.NOT_EXIST);
        }

        //限制重复名称
        if (goodsBrandRepository.count(GoodsBrandQueryRequest.builder()
                .notBrandId(newGoodsBrand.getBrandId())
                .brandName(newGoodsBrand.getBrandName()).delFlag(DeleteFlag.NO.toValue())
                .build().getWhereCriteria()) > 0) {
            throw new SbcRuntimeException(GoodsBrandErrorCode.NAME_ALREADY_EXISTS);
        }
        //更新品牌
        newGoodsBrand.setUpdateTime(LocalDateTime.now());
        KsBeanUtil.copyProperties(newGoodsBrand, oldGoodsBrand);
        goodsBrandRepository.save(oldGoodsBrand);

//        //持久化ES->CateBrand
//        Iterable<EsCateBrand> esCateBrands = esCateBrandService.queryCateBrandByBrandIds(Arrays.asList
//        (oldGoodsBrand.getBrandId()));
//        List<EsCateBrand> cateBrandList = new ArrayList<>();
//        esCateBrands.forEach(cateBrand -> {
//            cateBrand.setGoodsBrand(oldGoodsBrand);
//            cateBrandList.add(cateBrand);
//        });
//        if (CollectionUtils.isNotEmpty(cateBrandList)) {
//            esCateBrandService.save(cateBrandList);
//        }

        //ares埋点-商品-后台编辑商品品牌
        goodsAresService.dispatchFunction("editGoodsBrand", oldGoodsBrand);

        return oldGoodsBrand;
    }

    /**
     * 逻辑删除品牌
     *
     * @param brandId 品牌id
     * @throws SbcRuntimeException 业务异常
     */
    @Transactional
    public GoodsBrand delete(Long brandId) throws SbcRuntimeException {
        GoodsBrand goodsBrand = goodsBrandRepository.findById(brandId).orElse(null);
        if (goodsBrand == null || goodsBrand.getDelFlag().compareTo(DeleteFlag.YES) == 0) {
            throw new SbcRuntimeException(GoodsBrandErrorCode.NOT_EXIST);
        }
        //更新品牌
        goodsBrand.setDelFlag(DeleteFlag.YES);
        goodsBrandRepository.save(goodsBrand);
        //删除所有商家签约品牌
        contractBrandService.deleteByGoodsBrandId(brandId);

//        //持久化ES->CateBrand
//        Iterable<EsCateBrand> esCateBrands = esCateBrandService.queryCateBrandByBrandIds(Arrays.asList(brandId));
//        List<EsCateBrand> cateBrandList = new ArrayList<>();
//        esCateBrands.forEach(cateBrand -> {
//            cateBrand.getGoodsBrand().setBrandName(StringUtils.EMPTY);
//            cateBrandList.add(cateBrand);
//        });
//        if (CollectionUtils.isNotEmpty(cateBrandList)) {
//            esCateBrandService.save(cateBrandList);
//        }

        //ares埋点-商品-后台删除商品品牌
        goodsAresService.dispatchFunction("delGoodsBrand", brandId);

        return goodsBrand;
    }

    public List<GoodsBrandBase> findAllByBrandIdIn(List<Long> ids){
        return goodsBrandRepository.findAllByBrandIdIn(ids);
    }
}
