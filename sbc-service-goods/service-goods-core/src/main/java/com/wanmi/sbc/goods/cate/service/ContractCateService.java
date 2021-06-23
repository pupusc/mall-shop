package com.wanmi.sbc.goods.cate.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.customer.api.constant.SigningClassErrorCode;
import com.wanmi.sbc.goods.api.constant.ContractCateErrorCode;
import com.wanmi.sbc.goods.cate.model.root.ContractCate;
import com.wanmi.sbc.goods.cate.model.root.GoodsCate;
import com.wanmi.sbc.goods.cate.repository.ContractCateRepository;
import com.wanmi.sbc.goods.cate.repository.GoodsCateRepository;
import com.wanmi.sbc.goods.cate.request.ContractCateQueryRequest;
import com.wanmi.sbc.goods.cate.request.ContractCateSaveRequest;
import com.wanmi.sbc.goods.cate.response.ContractCateResponse;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.repository.GoodsRepository;
import com.wanmi.sbc.goods.info.request.GoodsQueryRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 签约分类服务
 * Created by sunkun on 2017/10/30.
 */
@Service
@Transactional
public class ContractCateService {

    @Resource
    private ContractCateRepository contractCateRepository;

    @Resource
    private GoodsCateService goodsCateService;

    @Resource
    private GoodsCateRepository goodsCateRepository;

    @Resource
    private GoodsRepository goodsRepository;

    /**
     * 新增
     *
     * @param request
     */
    public void add(ContractCateSaveRequest request) {
        //查询平台分类
        GoodsCate goodsCate = goodsCateService.findById(request.getCateId());
        if (goodsCate == null) {
            throw new SbcRuntimeException(SigningClassErrorCode.NOT_EXIST);
        }
        if(goodsCate.getDelFlag() == DeleteFlag.YES){
            throw new SbcRuntimeException(ContractCateErrorCode.NOT_EXIST,new Object[]{goodsCate.getCateName()});
        }
        //查询店铺下该平台分类是否存在
        ContractCateQueryRequest contractCateQueryRequest = new ContractCateQueryRequest();
        contractCateQueryRequest.setStoreId(request.getStoreId());
        contractCateQueryRequest.setCateId(request.getCateId());
        BeanUtils.copyProperties(request, contractCateQueryRequest);
        List<ContractCate> list = contractCateRepository.findAll(contractCateQueryRequest.getWhereCriteria());
        if (!list.isEmpty()) {
            //已签约
            throw new SbcRuntimeException(SigningClassErrorCode.HAS_SIGNED_CONTRACT);
        }
        //查询店铺签约分类总数
        request.setCateId(null);
        list = contractCateRepository.findAll(contractCateQueryRequest.getWhereCriteria());
        if (list.size() >= 200) {
            throw new SbcRuntimeException(SigningClassErrorCode.MOST_CONTRACT_NUMBER);
        }
        //保存分类
        ContractCate contractCate = new ContractCate();
        BeanUtils.copyProperties(request, contractCate);

        contractCate.setGoodsCate(goodsCate);
        contractCateRepository.save(contractCate);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Long id) {
        contractCateRepository.deleteById(id);
    }

    /**
     * 根据平台类目id和店铺id删除签约分类
     * @param ids
     * @param storeId
     */
    public void deleteByIds(List<Long> ids, Long storeId) {
        contractCateRepository.deleteByIdsAndStoreId(ids, storeId);
    }


    /**
     * 查询
     *
     * @param request
     * @return
     */
    public List<ContractCateResponse> queryList(ContractCateQueryRequest request) {
        List<ContractCate> contractCates = contractCateRepository.findAll(request.getWhereCriteria());
        //根据path得到所有上级类目的编号列表
        List<Long> allGoodsCateIds = new ArrayList<>();
        contractCates.stream().map(ContractCate::getGoodsCate).map(GoodsCate::getCatePath).forEach(info -> {
            Arrays.asList(info.split(Constants.CATE_PATH_SPLITTER)).forEach(idStr -> {
                if (StringUtils.isNotBlank(idStr)) {
                    Long id = Long.parseLong(idStr);
                    if (id > 0) {
                        allGoodsCateIds.add(id);
                    }
                }
            });
        });
        //根据上级类目编号列表查询上级类目
        List<GoodsCate> tempList1 = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(allGoodsCateIds)) {
            tempList1 = goodsCateRepository.queryCates(allGoodsCateIds.stream().distinct().collect(Collectors.toList()), DeleteFlag.NO);
        }
        List<GoodsCate> goodsCates = tempList1;
        List<ContractCateResponse> contractCateResponseList = new ArrayList<>();
        //填充返回对象
        contractCates.forEach(info -> {
            ContractCateResponse contractCateResponse = new ContractCateResponse();
            BeanUtils.copyProperties(info, contractCateResponse);
            contractCateResponse.setCateId(info.getGoodsCate().getCateId());
            contractCateResponse.setCateName(info.getGoodsCate().getCateName());
            //筛选并填充上级分类名称
            Arrays.asList(info.getGoodsCate().getCatePath().split(Constants.CATE_PATH_SPLITTER)).forEach(id -> {
                List<GoodsCate> cates = goodsCates.stream().filter(cate -> cate.getCateId() == Long.parseLong(id)).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(cates)) {
                    GoodsCate goodsCate = cates.get(0);
                    contractCateResponse.setParentGoodCateNames(StringUtils.isBlank(contractCateResponse.getParentGoodCateNames()) ?
                            goodsCate.getCateName() :
                            contractCateResponse.getParentGoodCateNames().concat(Constants.STRING_SLASH_SPLITTER).concat(goodsCate.getCateName()));
                }
            });
            contractCateResponse.setPlatformCateRate(info.getGoodsCate().getCateRate());
            contractCateResponseList.add(contractCateResponse);
        });
        return contractCateResponseList;
    }

    /**
     * 查询店铺已签约的类目列表，包含上级类目
     *
     * @param storeId 店铺编号
     * @return 店铺已签约的类目列表，包含上级类目
     */
    public List<GoodsCate> listCate(Long storeId) {
        // 查询商家已签约的叶子类目
        ContractCateQueryRequest request = new ContractCateQueryRequest();
        request.setStoreId(storeId);
        List<ContractCate> list = contractCateRepository.findAll(request.getWhereCriteria());

        // 根据path得到所有上级类目的编号列表
        List<Long> cateIds = list.stream().map(contractCate -> {
            String catePath = contractCate.getGoodsCate().getCatePath();
            if (!StringUtils.isEmpty(catePath)) {
                return Arrays.asList(catePath.split(Constants.CATE_PATH_SPLITTER));
            }
            return null;
        }).filter(Objects::nonNull).flatMap(Collection::stream).filter(id -> !"0".equals(id)).map(Long::valueOf)
                .distinct().collect(Collectors.toList());

        // 根据类目编号列表查询类目信息
        List<GoodsCate> cateList = goodsCateRepository.queryCates(cateIds, DeleteFlag.NO);

        // 把查出来的父级一期返回出去
        cateList.addAll(list.stream().map(ContractCate::getGoodsCate).collect(Collectors.toList()));
        return cateList;
    }

    /**
     * 根据主键查询
     *
     * @return
     */
    public ContractCate queryById(Long id) {
        return contractCateRepository.findById(id).orElse(null);
    }


    /**
     * 根据平台类目主键和店铺主键查询签约分类
     *
     * @param cateId
     * @param storeId
     * @return
     */
    public void cateDelVerify(Long cateId, Long storeId) {
        //根据平台分类及店铺查询签约分类
        ContractCate contractCate = contractCateRepository.queryByCateIdAndStoreId(cateId, storeId);
        if (Objects.isNull(contractCate)) {
            //分类不存在
            throw new SbcRuntimeException(SigningClassErrorCode.CONTRACT_CATE_NOT_EXIST);
        }
        GoodsQueryRequest goodsQueryRequest = new GoodsQueryRequest();
        goodsQueryRequest.setCateId(contractCate.getGoodsCate().getCateId());
        goodsQueryRequest.setStoreId(storeId);
        goodsQueryRequest.setDelFlag(Integer.valueOf(0));
        List<Goods> goodsList = goodsRepository.findAll(goodsQueryRequest.getWhereCriteria());
        if (Objects.nonNull(goodsList) && goodsList.size() > 0) {
            throw new SbcRuntimeException(SigningClassErrorCode.RELATES_THE_ITEM);
        }
    }

    /**
     * 根据平台分类主键列表修改签约分类扣率
     *
     * @param cateIds
     * @param cateRate
     */
    public void updateCateRate(BigDecimal cateRate, List<Long> cateIds) {
        contractCateRepository.updateCateRate(cateRate, cateIds);
    }

    /**
     * 修改
     *
     * @param request
     */
    public void update(ContractCateSaveRequest request) {
        ContractCate contractCate = new ContractCate();
        BeanUtils.copyProperties(request, contractCate);
        GoodsCate goodsCate = goodsCateService.findById(request.getCateId());
        if(Objects.isNull(goodsCate)){
            throw new SbcRuntimeException(SigningClassErrorCode.NOT_EXIST);
        }
        if(goodsCate.getDelFlag() == DeleteFlag.YES){
            throw new SbcRuntimeException(ContractCateErrorCode.NOT_EXIST,new Object[]{goodsCate.getCateName()});
        }
        contractCate.setGoodsCate(goodsCate);
        contractCateRepository.saveAndFlush(contractCate);
    }

    /**
     * 根据分类Ids查询签约分类数量
     * @param ids
     * @return
     */
    public int findCountByIds(List<Long> ids) {
        return contractCateRepository.findCountByIds(ids);
    }


    /**
     * 根据分类Ids删除签约分类
     * @param ids
     * @return
     */
    public void deleteByIds(List<Long> ids) {
        contractCateRepository.deleteByIds(ids);
    }

    /**
     * 查询签约分类列表
     * @param request
     * @return
     */
    public List<ContractCate> queryContractCateList(ContractCateQueryRequest request) {
        return contractCateRepository.findAll(request.getWhereCriteria());
    }

}
