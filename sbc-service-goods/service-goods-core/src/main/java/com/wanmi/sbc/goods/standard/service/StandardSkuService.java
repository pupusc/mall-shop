package com.wanmi.sbc.goods.standard.service;

import com.aliyuncs.linkedmall.model.v20180116.QueryItemInventoryResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.constant.StandardGoodsErrorCode;
import com.wanmi.sbc.goods.bean.enums.GoodsSource;
import com.wanmi.sbc.goods.standard.model.root.StandardGoods;
import com.wanmi.sbc.goods.standard.model.root.StandardSku;
import com.wanmi.sbc.goods.standard.repository.StandardGoodsRepository;
import com.wanmi.sbc.goods.standard.repository.StandardSkuRepository;
import com.wanmi.sbc.goods.standard.request.StandardSkuQueryRequest;
import com.wanmi.sbc.goods.standard.request.StandardSkuSaveRequest;
import com.wanmi.sbc.goods.standard.response.StandardSkuEditResponse;
import com.wanmi.sbc.goods.standardspec.model.root.StandardSkuSpecDetailRel;
import com.wanmi.sbc.goods.standardspec.model.root.StandardSpecDetail;
import com.wanmi.sbc.goods.standardspec.repository.StandardSkuSpecDetailRelRepository;
import com.wanmi.sbc.goods.standardspec.repository.StandardSpecDetailRepository;
import com.wanmi.sbc.goods.standardspec.repository.StandardSpecRepository;
import com.wanmi.sbc.linkedmall.api.provider.stock.LinkedMallStockQueryProvider;
import com.wanmi.sbc.linkedmall.api.request.stock.GoodsStockGetRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 商品服务
 * Created by daiyitian on 2017/4/11.
 */
@Service
public class StandardSkuService {

    @Autowired
    private StandardGoodsRepository standardGoodsRepository;

    @Autowired
    private StandardSkuRepository standardInfoRepository;

    @Autowired
    private StandardSpecRepository standardSpecRepository;

    @Autowired
    private StandardSpecDetailRepository standardSpecDetailRepository;

    @Autowired
    private StandardSkuSpecDetailRelRepository standardInfoSpecDetailRelRepository;

    @Autowired
    private LinkedMallStockQueryProvider linkedMallStockQueryProvider;

    @Autowired
    private EntityManager entityManager;

    /**
     * 根据ID查询商品SKU
     *
     * @param standardInfoId 商品SKU编号
     * @return list
     */
    @Transactional(readOnly = true, timeout = 10, propagation = Propagation.REQUIRES_NEW)
    public StandardSkuEditResponse findById(String standardInfoId) throws SbcRuntimeException {
        StandardSkuEditResponse response = new StandardSkuEditResponse();
        StandardSku standardSku = standardInfoRepository.findById(standardInfoId).orElse(null);
        if (standardSku == null || DeleteFlag.YES.toValue() == standardSku.getDelFlag().toValue()) {
            throw new SbcRuntimeException(StandardGoodsErrorCode.NOT_EXIST);
        }
        StandardGoods standard = standardGoodsRepository.findById(standardSku.getGoodsId()).orElse(null);
        if (standard == null) {
            throw new SbcRuntimeException(StandardGoodsErrorCode.NOT_EXIST);
        }

        //如果是多规格
        if (Constants.yes.equals(standard.getMoreSpecFlag())) {
            response.setGoodsSpecs(standardSpecRepository.findByGoodsId(standard.getGoodsId()));
            response.setGoodsSpecDetails(standardSpecDetailRepository.findByGoodsId(standard.getGoodsId()));

            //对每个规格填充规格值关系
            response.getGoodsSpecs().stream().forEach(standardSpec -> {
                standardSpec.setSpecDetailIds(response.getGoodsSpecDetails().stream().filter(specDetail -> specDetail.getSpecId().equals(standardSpec.getSpecId())).map(StandardSpecDetail::getSpecDetailId).collect(Collectors.toList()));
            });

            //对每个SKU填充规格和规格值关系
            List<StandardSkuSpecDetailRel> standardInfoSpecDetailRels = standardInfoSpecDetailRelRepository.findByGoodsId(standard.getGoodsId());
            standardSku.setMockSpecIds(standardInfoSpecDetailRels.stream().filter(detailRel -> detailRel.getGoodsInfoId().equals(standardSku.getGoodsInfoId())).map(StandardSkuSpecDetailRel::getSpecId).collect(Collectors.toList()));
            standardSku.setMockSpecDetailIds(standardInfoSpecDetailRels.stream().filter(detailRel -> detailRel.getGoodsInfoId().equals(standardSku.getGoodsInfoId())).map(StandardSkuSpecDetailRel::getSpecDetailId).collect(Collectors.toList()));
            standardSku.setSpecText(StringUtils.join(standardInfoSpecDetailRels.stream().filter(specDetailRel -> standardSku.getGoodsInfoId().equals(specDetailRel.getGoodsInfoId())).map(StandardSkuSpecDetailRel::getDetailName).collect(Collectors.toList()), " "));
        }
        //如果是linkedmall商品，实时查库存
        if (Integer.valueOf(GoodsSource.LINKED_MALL.toValue()).equals(standard.getGoodsSource())) {
            List<QueryItemInventoryResponse.Item> stocks = null;
            if (standard.getThirdPlatformSpuId() != null) {
                stocks = linkedMallStockQueryProvider.batchGoodsStockByDivisionCode(new GoodsStockGetRequest(Collections.singletonList(Long.valueOf(standard.getThirdPlatformSpuId())), "0", null)).getContext();
            }
            if (stocks != null) {
                Optional<QueryItemInventoryResponse.Item> optional = stocks.stream()
                        .filter(v -> v.getItemId().equals(Long.valueOf(standard.getThirdPlatformSpuId())))
                        .findFirst();
                if (optional.isPresent()) {
                    Long totalStock = optional.get().getSkuList().stream()
                            .map(v -> v.getInventory().getQuantity())
                            .reduce(0L, ((aLong, aLong2) -> aLong + aLong2));
                    standard.setStock(totalStock);
                }
                for (QueryItemInventoryResponse.Item spuStock : stocks) {
                    for (QueryItemInventoryResponse.Item.Sku sku : spuStock.getSkuList()) {
                        if (String.valueOf(spuStock.getItemId()).equals(standardSku.getThirdPlatformSpuId()) && String.valueOf(sku.getSkuId()).equals(standardSku.getThirdPlatformSkuId())) {
                            standardSku.setStock(sku.getInventory().getQuantity());
                        }
                    }
                }
            }
        }
        response.setGoodsInfo(standardSku);
        response.setGoods(standard);
        return response;
    }

    /**
     * 商品SKU更新
     *
     * @param saveRequest 参数
     * @throws SbcRuntimeException 业务异常
     */
    @Transactional
    public StandardSku edit(StandardSkuSaveRequest saveRequest) throws SbcRuntimeException {
        StandardSku newStandardSku = saveRequest.getGoodsInfo();
        StandardSku oldStandardSku = standardInfoRepository.findById(newStandardSku.getGoodsInfoId()).orElse(null);
        if (oldStandardSku == null || oldStandardSku.getDelFlag().compareTo(DeleteFlag.YES) == 0) {
            throw new SbcRuntimeException(StandardGoodsErrorCode.NOT_EXIST);
        }
        newStandardSku.setUpdateTime(LocalDateTime.now());
        KsBeanUtil.copyProperties(newStandardSku, oldStandardSku);
        standardInfoRepository.save(oldStandardSku);
        return oldStandardSku;
    }

    /**
     * 获取详情
     * @param skuId 商品库sku编号
     * @return 商品库信息
     */
    public StandardSku findOne(String skuId){
        return this.standardInfoRepository.findById(skuId).orElse(null);
    }

    /**
     * 批量查询
     * @param skuIds 编号
     * @return 商品库列表
     */
    public List<StandardSku> findAll(List<String> skuIds){
        return this.standardInfoRepository.findAllById(skuIds);
    }


    /**
     * 批量查询
     *
     * @param request 参数
     * @return 商品库列表
     */
    public List<StandardSku> findAll(StandardSkuQueryRequest request) {
        return this.standardInfoRepository.findAll(request.getWhereCriteria());
    }


    /**
     * 自定义字段的列表查询
     * @param request 参数
     * @param cols 列名
     * @return 列表
     */
    public List<StandardSku> listCols(StandardSkuQueryRequest request, List<String> cols) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<StandardSku> rt = cq.from(StandardSku.class);
        cq.multiselect(cols.stream().map(c -> rt.get(c).alias(c)).collect(Collectors.toList()));
        Specification<StandardSku> spec = request.getWhereCriteria();
        Predicate predicate = spec.toPredicate(rt, cq, cb);
        if (predicate != null) {
            cq.where(predicate);
        }
        Sort sort = request.getSort();
        if (sort.isSorted()) {
            cq.orderBy(QueryUtils.toOrders(sort, rt, cb));
        }
        cq.orderBy(QueryUtils.toOrders(request.getSort(), rt, cb));
        return this.converter(entityManager.createQuery(cq).getResultList(), cols);
    }


    /**
     * 查询对象转换
     * @param result
     * @return
     */
    private List<StandardSku> converter(List<Tuple> result, List<String> cols) {
        return result.stream().map(item -> {
            StandardSku sku = new StandardSku();
            sku.setGoodsId(toString(item, "goodsId", cols));
            sku.setGoodsInfoId(toString(item,"goodsInfoId", cols));
            sku.setGoodsInfoNo(toString(item,"goodsInfoNo", cols));
            sku.setMarketPrice(toBigDecimal(item,"marketPrice", cols));
            sku.setCostPrice(toBigDecimal(item,"costPrice", cols));
            sku.setStock(toLong(item,"stock", cols));
            sku.setThirdPlatformSpuId(toString(item,"thirdPlatformSpuId", cols));
            sku.setThirdPlatformSkuId(toString(item,"thirdPlatformSkuId", cols));
            sku.setSupplyPrice(toBigDecimal(item, "supplyPrice", cols));
            return sku;
        }).collect(Collectors.toList());
    }


    private String toString(Tuple tuple, String name, List<String> cols) {
        if(!cols.contains(name)){
            return null;
        }
        Object value = tuple.get(name);
        return value != null ? value.toString() : null;
    }

    private BigDecimal toBigDecimal(Tuple tuple, String name, List<String> cols) {
        if(!cols.contains(name)){
            return null;
        }
        Object value = tuple.get(name);
        return value != null ? new BigDecimal(value.toString()) : null;
    }

    private Long toLong(Tuple tuple, String name, List<String> cols) {
        if(!cols.contains(name)){
            return null;
        }
        Object value = tuple.get(name);
        return value != null ? NumberUtils.toLong(value.toString()) : null;
    }
}
