package com.wanmi.sbc.goods.storecate.service;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.customer.api.constant.StoreCateErrorCode;
import com.wanmi.sbc.goods.api.request.storecate.*;
import com.wanmi.sbc.goods.ares.GoodsAresService;
import com.wanmi.sbc.goods.bean.enums.CateParentTop;
import com.wanmi.sbc.goods.storecate.entity.StoreCateBase;
import com.wanmi.sbc.goods.storecate.model.root.StoreCate;
import com.wanmi.sbc.goods.storecate.model.root.StoreCateGoodsRela;
import com.wanmi.sbc.goods.storecate.repository.StoreCateGoodsRelaRepository;
import com.wanmi.sbc.goods.storecate.repository.StoreCateRepository;
import com.wanmi.sbc.goods.storecate.request.StoreCateQueryRequest;
import com.wanmi.sbc.goods.storecate.request.StoreCateSaveRequest;
import com.wanmi.sbc.goods.storecate.request.StoreCateSortRequest;
import com.wanmi.sbc.goods.storecate.response.StoreCateResponse;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 商品分类服务
 * Created by bail on 2017/11/14.
 */
@Service
public class StoreCateService {

    private static final String SPLIT_CHAR = "|";

    @Autowired
    private StoreCateRepository storeCateRepository;

    @Autowired
    private StoreCateGoodsRelaRepository storeCateGoodsRelaRepository;

    @Autowired
    GoodsAresService goodsAresService;

    /**
     * 初始化分类，生成默认分类
     */
    @GlobalTransactional
    @Transactional
    public void initStoreDefaultCate(Long storeId) {
        StoreCateQueryRequest queryRequest = new StoreCateQueryRequest();
        queryRequest.setStoreId(storeId);
        queryRequest.setIsDefault(DefaultFlag.YES);
        StoreCate defCate = storeCateRepository.findOne(queryRequest.getWhereCriteria()).orElse(null);
        if (defCate == null) {
            StoreCate storeCate = new StoreCate();
            storeCate.setStoreId(storeId);
            storeCate.setCateName("默认分类");
            storeCate.setCateParentId((long) (CateParentTop.ZERO.toValue()));
            storeCate.setCatePath(String.valueOf(storeCate.getCateParentId()).concat(SPLIT_CHAR));
            storeCate.setCateGrade(1);
            storeCate.setCreateTime(LocalDateTime.now());
            storeCate.setUpdateTime(storeCate.getCreateTime());
            storeCate.setDelFlag(DeleteFlag.NO);
            storeCate.setSort(0);
            storeCate.setIsDefault(DefaultFlag.YES);
            storeCateRepository.save(storeCate);

            //ares埋点-商品-店铺审核通过,自动初始化默认店铺分类
            goodsAresService.dispatchFunction("addStoreCate", storeCate);
        }
    }

    /**
     * 条件查询商品分类
     *
     * @param storeId 店铺标识
     * @return list
     */
    public List<StoreCateResponse> query(Long storeId) {
        if (storeId == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        StoreCateQueryRequest queryRequest = new StoreCateQueryRequest();
        queryRequest.setStoreId(storeId);
        queryRequest.setDelFlag(DeleteFlag.NO);
        queryRequest.putSort("isDefault", SortType.DESC.toValue());
        queryRequest.putSort("sort", SortType.ASC.toValue());
        queryRequest.putSort("createTime", SortType.DESC.toValue());
        List<StoreCate> cateList;
        Sort sort = queryRequest.getSort();
        if(Objects.nonNull(sort)) {
            cateList = storeCateRepository.findAll(queryRequest.getWhereCriteria(), sort);
        }else {
            cateList = storeCateRepository.findAll(queryRequest.getWhereCriteria());
        }
        return cateList.stream().map(storeCate -> {
            StoreCateResponse storeCateResponse = new StoreCateResponse();
            BeanUtils.copyProperties(storeCate, storeCateResponse);
            return storeCateResponse;
        }).collect(Collectors.toList());
    }

    /**
     * 条件查询非默认的店铺商品分类
     *
     * @param storeId 店铺标识
     * @return list
     */
    public List<StoreCateResponse> queryNoDefault(Long storeId) {
        if (storeId == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        StoreCateQueryRequest queryRequest = new StoreCateQueryRequest();
        queryRequest.setStoreId(storeId);
        queryRequest.setDelFlag(DeleteFlag.NO);
        queryRequest.setIsDefault(DefaultFlag.NO);
        queryRequest.putSort("sort", SortType.ASC.toValue());
        queryRequest.putSort("createTime", SortType.DESC.toValue());
        List<StoreCate> cateList;
        Sort sort = queryRequest.getSort();
        if(Objects.nonNull(sort)) {
            cateList = storeCateRepository.findAll(queryRequest.getWhereCriteria(), sort);
        }else {
            cateList = storeCateRepository.findAll(queryRequest.getWhereCriteria());
        }
        return cateList.stream().map(storeCate -> {
            StoreCateResponse storeCateResponse = new StoreCateResponse();
            BeanUtils.copyProperties(storeCate, storeCateResponse);
            return storeCateResponse;
        }).collect(Collectors.toList());
    }

    /**
     * 根据ID查询某个商品店铺分类
     *
     * @param storeCateId 分类ID
     * @return list
     */
    public StoreCateResponse findById(Long storeCateId) {
        if (storeCateId == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        StoreCate storeCate = storeCateRepository.findById(storeCateId).orElse(new StoreCate());
        StoreCateResponse response = new StoreCateResponse();
        BeanUtils.copyProperties(storeCate, response);
        return response;
    }

    /**
     * 验证分类名称是否重复
     */
    private void checkNameExist(StoreCate storeCate) {
        StoreCateQueryRequest queryRequest = new StoreCateQueryRequest();
        queryRequest.setStoreId(storeCate.getStoreId());
        queryRequest.setCateName(storeCate.getCateName());
        queryRequest.setDelFlag(DeleteFlag.NO);
        if (storeCate.getStoreCateId() == null) {
            //新增时,验证重复名称
            if (storeCateRepository.count(queryRequest.getWhereCriteria()) > 0) {
                throw new SbcRuntimeException(StoreCateErrorCode.NAME_EXIST);
            }
        } else {
            //编辑时,验证重复名称
            if (storeCateRepository.findAll(queryRequest.getWhereCriteria()).stream().filter(s -> !s.getStoreCateId().equals(storeCate.getStoreCateId())).count() > 0) {
                throw new SbcRuntimeException(StoreCateErrorCode.NAME_EXIST);
            }
        }
    }

    /**
     * 验证一级分类数量的限制
     */
    private void checkFirstGradeNum(StoreCate storeCate) {
        StoreCateQueryRequest queryRequest = new StoreCateQueryRequest();
        queryRequest.setStoreId(storeCate.getStoreId());
        queryRequest.setDelFlag(DeleteFlag.NO);
        queryRequest.setCateGrade(1);
        if (storeCateRepository.count(queryRequest.getWhereCriteria()) >= Constants.STORE_CATE_FIRST_NUM) {
            throw new SbcRuntimeException(StoreCateErrorCode.FIRST_GRADE_LIMIT, new Object[]{Constants.STORE_CATE_FIRST_NUM});
        }
    }

    /**
     * 验证分类的父级店铺分类信息
     *
     * @return 返回父分类
     */
    private StoreCate checkPareStoreCate(StoreCate storeCate) {
        //1.验证父分类是否存在
        StoreCate pareStoreCate = storeCateRepository.findById(storeCate.getCateParentId()).orElse(null);
        if (pareStoreCate == null || DeleteFlag.YES.equals(pareStoreCate.getDelFlag())) {
            throw new SbcRuntimeException(StoreCateErrorCode.PARENT_NOT_EXIST);
        }
        //2.验证分类层次是否超过2级(父分类的层次是否大于等于2级)
        if (pareStoreCate.getCateGrade() >= Constants.STORE_CATE_GRADE) {
            throw new SbcRuntimeException(StoreCateErrorCode.GRADE_NUM_LIMIT, new Object[]{Constants.STORE_CATE_GRADE});
        }
        //3.验证在该分类下的二级分类限制个数
        StoreCateQueryRequest queryRequest = new StoreCateQueryRequest();
        queryRequest.setStoreId(storeCate.getStoreId());
        queryRequest.setDelFlag(DeleteFlag.NO);
        queryRequest.setCateParentId(storeCate.getCateParentId());
        if (storeCateRepository.count(queryRequest.getWhereCriteria()) >= Constants.STORE_CATE_SECOND_NUM) {
            throw new SbcRuntimeException(StoreCateErrorCode.SECOND_GRADE_LIMIT, new Object[]{Constants.STORE_CATE_SECOND_NUM});
        }
        return pareStoreCate;
    }

    /**
     * 验证参数并初始化部分值
     */
    private void checkAndInit(StoreCate storeCate) {
        if (storeCate.getCateParentId() == null || storeCate.getCateParentId() == ((long) CateParentTop.ZERO.toValue())) {
            //如果添加的是一级分类,验证一级分类限制个数
            checkFirstGradeNum(storeCate);
            storeCate.setCateParentId((long) (CateParentTop.ZERO.toValue()));
            storeCate.setCateGrade(1);
            storeCate.setCatePath(String.valueOf(storeCate.getCateParentId()).concat(SPLIT_CHAR));
        } else {
            //验证父分类信息
            StoreCate pareStoreCate = checkPareStoreCate(storeCate);
            storeCate.setCateGrade(pareStoreCate.getCateGrade() + 1);
            storeCate.setCatePath(pareStoreCate.getCatePath().concat(String.valueOf(pareStoreCate.getStoreCateId())).concat(SPLIT_CHAR));
        }
        if (storeCate.getSort() == null) {
            storeCate.setSort(0);//默认排序号为0
        }
    }

    /**
     * 新增商品店铺分类
     *
     * @param saveRequest 商品店铺分类
     * @throws SbcRuntimeException
     */
    @Transactional
    public StoreCateResponse add(StoreCateAddRequest saveRequest) {
        if (saveRequest == null || saveRequest.getStoreId() == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        StoreCate storeCate = new StoreCate();
        BeanUtils.copyProperties(saveRequest, storeCate);

        /**1.参数验证,部分数据初始化*/
        //1.1.验证名称是否重复
//        checkNameExist(storeCate);
        //1.2.验证其他参数,并初始化部分值
        checkAndInit(storeCate);

        /**2.初始化数据并插入*/
        storeCate.setDelFlag(DeleteFlag.NO);
        storeCate.setIsDefault(DefaultFlag.NO);
        storeCate.setCreateTime(LocalDateTime.now());
        storeCate.setUpdateTime(LocalDateTime.now());
        //判断当前分类是否存在相同分类
        Long count = storeCateRepository.countStoreCateByConditions(storeCate);
        if (count > 0) {
            throw new SbcRuntimeException(StoreCateErrorCode.StoreCate_EXIST);
        }
        //查询该父分类下排序最大的子分类
        StoreCate maxCate = storeCateRepository.findTop1ByCateParentIdOrderBySortDesc(saveRequest.getCateParentId());
        if (Objects.nonNull(maxCate)) {
            //如果该分类下已有子分类，则新增的分类排序为：最大排序+1
            storeCate.setSort(maxCate.getSort() + 1);
        }

        storeCate.setStoreCateId(storeCateRepository.save(storeCate).getStoreCateId());

        StoreCateResponse response = new StoreCateResponse();
        BeanUtils.copyProperties(storeCate, response);

        //ares埋点-商品-添加店铺分类
        goodsAresService.dispatchFunction("addStoreCate", storeCate);
        return response;
    }

    /**
     * 编辑店铺商品分类
     *
     * @param saveRequest 店铺商品分类
     * @throws SbcRuntimeException
     */
    @Transactional
    public void edit(StoreCateModifyRequest saveRequest) {
        if (saveRequest == null || saveRequest.getStoreCateId() == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        StoreCate newStoreCate = new StoreCate();
        BeanUtils.copyProperties(saveRequest, newStoreCate);
        StoreCate oldStoreCate = storeCateRepository.findById(saveRequest.getStoreCateId()).orElse(null);

        /**1.参数验证*/
        if (oldStoreCate == null || DeleteFlag.YES.equals(oldStoreCate.getDelFlag()) || !Objects.equals(oldStoreCate.getStoreId(), newStoreCate.getStoreId()) || DefaultFlag.YES.equals(oldStoreCate.getIsDefault())) {
            //1.1.待修改的分类不存在 或 修改的店铺id不一致(数据安全问题) 或 默认分类不可修改
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        if (!oldStoreCate.getCateName().equals(newStoreCate.getCateName())) {
            //1.2.修改后的名称不一致时,才验证是否有重复名称
//            checkNameExist(newStoreCate);
        }
        //1.3.初始化部分值
        if (newStoreCate.getCateParentId() == null || newStoreCate.getCateParentId() == ((long) CateParentTop.ZERO.toValue())) {
            newStoreCate.setCateParentId((long) (CateParentTop.ZERO.toValue()));
            newStoreCate.setCateGrade(1);
            newStoreCate.setCatePath(String.valueOf(newStoreCate.getCateParentId()).concat(SPLIT_CHAR));
        } else {
            StoreCate pareStoreCate = storeCateRepository.findById(newStoreCate.getCateParentId()).orElse(new StoreCate());
            newStoreCate.setCateGrade(pareStoreCate.getCateGrade() + 1);
            newStoreCate.setCatePath(pareStoreCate.getCatePath().concat(String.valueOf(pareStoreCate.getStoreCateId())).concat(SPLIT_CHAR));
        }
//        if(newStoreCate.getSort() == null){
//            newStoreCate.setSort(0);//默认排序号为0
//        }

        /**2.更新分类*/
        oldStoreCate.convertBeforeEdit(newStoreCate);
        oldStoreCate.setUpdateTime(LocalDateTime.now());
        storeCateRepository.save(oldStoreCate);
        //判断当前分类是否存在相同分类
        Long count = storeCateRepository.countStoreCateByConditions(oldStoreCate);
        if (count > 1) {
            throw new SbcRuntimeException(StoreCateErrorCode.StoreCate_EXIST);
        }
        //ares埋点-商品-编辑店铺分类
        goodsAresService.dispatchFunction("editStoreCate", oldStoreCate);
    }

    /**
     * 删除店铺商品分类
     *
     * @param saveRequest 待删除的分类信息
     * @throws SbcRuntimeException
     */
    @Transactional
    public HashMap<String, Object> delete(StoreCateDeleteRequest saveRequest) {
        if (saveRequest == null || saveRequest.getStoreCateId() == null || saveRequest.getStoreId() == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        StoreCate storeCate = storeCateRepository.findById(saveRequest.getStoreCateId()).orElse(null);
        if (storeCate == null || DeleteFlag.YES.equals(storeCate.getDelFlag()) || !Objects.equals(storeCate.getStoreId(), saveRequest.getStoreId()) || DefaultFlag.YES.equals(storeCate.getIsDefault())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        StoreCateQueryRequest queryRequest = new StoreCateQueryRequest();
        queryRequest.setStoreId(storeCate.getStoreId());
        queryRequest.setIsDefault(DefaultFlag.YES);
        StoreCate defCate = storeCateRepository.findOne(queryRequest.getWhereCriteria()).orElse(null);//查询默认分类
        if (defCate == null) {
            //如果默认分类不存在，不允许删除
            throw new SbcRuntimeException(StoreCateErrorCode.DEFAULT_NOT_EXIST);
        }

        List<Long> allCate = new ArrayList<>();
        allCate.add(storeCate.getStoreCateId());

        String oldCatePath = storeCate.getCatePath().concat(String.valueOf(storeCate.getStoreCateId())).concat(SPLIT_CHAR);
        StoreCateQueryRequest cateReq = new StoreCateQueryRequest();
        cateReq.setStoreId(storeCate.getStoreId());
        cateReq.setDelFlag(DeleteFlag.NO);
        cateReq.setLikeCatePath(oldCatePath);
        List<StoreCate> childCateList = storeCateRepository.findAll(cateReq.getWhereCriteria());

        //删除子分类
        if (CollectionUtils.isNotEmpty(childCateList)) {
            childCateList.stream().forEach(cate -> {
                cate.setDelFlag(DeleteFlag.YES);
                allCate.add(cate.getStoreCateId());
            });
            storeCateRepository.saveAll(childCateList);
        }
        //删除当前分类
        storeCate.setDelFlag(DeleteFlag.YES);
        storeCateRepository.save(storeCate);
        //仅为了埋点查询一次需要变更的商品,必须放在下面代码的前面
        List<StoreCateGoodsRela> storeCateGoodsRelas = storeCateGoodsRelaRepository.selectByStoreCateIds(allCate);

        //依次删除多店铺分类的商品的店铺分类关系表信息(必须在前面)
        allCate.stream().forEach(storeCateId -> storeCateGoodsRelaRepository.deleteGoodsStoreCate(storeCateId));
        //迁移单店铺分类的商品至默认店铺分类(必须在后面)
        storeCateGoodsRelaRepository.updateGoodsStoreCate(defCate.getStoreCateId(), allCate);

        HashMap<String, Object> returnMap = new HashMap<>();
        returnMap.put("allCate", allCate);
        returnMap.put("storeCateGoodsRelas", storeCateGoodsRelas);
        return returnMap;

    }

    /**
     * 商家APP里店铺分类排序
     *
     * @param saveRequest
     */
    @Transactional
    public void batchSortCate(StoreCateSaveRequest saveRequest) {
        storeCateRepository.saveAll(saveRequest.getStoreCateList());
    }

    /**
     * 拖拽排序
     *
     * @param sortRequestList
     */
    @Transactional
    public void dragSort(List<StoreCateSortRequest> sortRequestList) {
        if (CollectionUtils.isEmpty(sortRequestList)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        if (CollectionUtils.isNotEmpty(sortRequestList) && sortRequestList.size() > Constants.STORE_CATE_FIRST_NUM) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        sortRequestList.forEach(cate ->
                storeCateRepository.updateCateSort(cate.getStoreCateId(),
                        cate.getCateSort()));
    }

    /**
     * 验证是否有子类
     *
     * @param request
     */
    public Integer checkHasChild(StoreCateQueryHasChildRequest request) {
        if (request == null || request.getStoreCateId() == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        StoreCate storeCate = storeCateRepository.findById(request.getStoreCateId()).orElse(null);
        if (storeCate == null || DeleteFlag.YES.equals(storeCate.getDelFlag())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        String oldCatePath = storeCate.getCatePath().concat(String.valueOf(storeCate.getStoreCateId())).concat(SPLIT_CHAR);
        StoreCateQueryRequest cateReq = new StoreCateQueryRequest();
        cateReq.setDelFlag(DeleteFlag.NO);
        cateReq.setLikeCatePath(oldCatePath);
        if (storeCateRepository.count(cateReq.getWhereCriteria()) > 0) {
            return Constants.yes;
        }
        return Constants.no;
    }

    /**
     * 验证分类下是否已经关联商品
     *
     * @param request
     */
    public Integer checkHasGoods(StoreCateQueryHasGoodsRequest request) {
        if (request == null || request.getStoreCateId() == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        StoreCate storeCate = storeCateRepository.findById(request.getStoreCateId()).orElse(null);
        if (storeCate == null || DeleteFlag.YES.equals(storeCate.getDelFlag())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        List<Long> allCate = new ArrayList<>();
        allCate.add(request.getStoreCateId());

        String oldCatePath = storeCate.getCatePath().concat(String.valueOf(storeCate.getStoreCateId())).concat(SPLIT_CHAR);
        StoreCateQueryRequest cateReq = new StoreCateQueryRequest();
        cateReq.setDelFlag(DeleteFlag.NO);
        cateReq.setLikeCatePath(oldCatePath);
        List<StoreCate> childCateList = storeCateRepository.findAll(cateReq.getWhereCriteria());

        if (CollectionUtils.isNotEmpty(childCateList)) {
            childCateList.stream().forEach(cate -> allCate.add(cate.getStoreCateId()));
        }

        //根据店铺分类List,查询关联的商品数量
        if (storeCateGoodsRelaRepository.count((root, cquery, cbuild) -> root.get("storeCateId").in(allCate)) > 0) {
            return Constants.yes;
        }
        return Constants.no;
    }

    /**
     * 生成树状结构的店铺分类
     */
    private List<StoreCate> generateTreeCates(Long storeId) {
        StoreCateQueryRequest queryRequest = new StoreCateQueryRequest();
        queryRequest.setStoreId(storeId);
        queryRequest.setDelFlag(DeleteFlag.NO);
        queryRequest.putSort("isDefault", SortType.DESC.toValue());
        queryRequest.putSort("sort", SortType.ASC.toValue());
        queryRequest.putSort("storeCateId", SortType.ASC.toValue());
        List<StoreCate> cateList;
        Sort sort = queryRequest.getSort();
        if(Objects.nonNull(sort)) {
            cateList = storeCateRepository.findAll(queryRequest.getWhereCriteria(), sort);
        }else {
            cateList = storeCateRepository.findAll(queryRequest.getWhereCriteria());
        }

        return this.recursiveTree(cateList, (long) (CateParentTop.ZERO.toValue()));
    }

    /**
     * 递归->树形结构
     */
    private List<StoreCate> recursiveTree(List<StoreCate> source, long pareId) {
        List<StoreCate> res = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(source)) {
            source.stream().filter(cate -> pareId == cate.getCateParentId().longValue()).forEach(cate -> {
                cate.setStoreCateList(recursiveTree(source, cate.getStoreCateId()));//递归找下一级分类
                res.add(cate);
            });
        }
        return res;
    }

    /**
     * 根据商品编号查询分类
     *
     * @param goodsIds
     * @return
     */
    public List<StoreCateGoodsRela> getStoreCateByGoods(List<String> goodsIds) {
        return storeCateGoodsRelaRepository.selectByGoodsId(goodsIds);
    }

    /**
     * 获取所有子分类
     *
     * @param storeCateId 分类ID
     * @param isHaveSelf  是否加入包含自身对象
     * @return
     */
    public List<StoreCate> findAllChlid(Long storeCateId, boolean isHaveSelf) {
        List<StoreCate> storeCateList = new ArrayList<>();
        StoreCate storeCate = storeCateRepository.findById(storeCateId).orElse(null);
        if (storeCate == null) {
            return storeCateList;
        }

        if (isHaveSelf) {
            storeCateList.add(storeCate);
        }

        String oldCatePath = storeCate.getCatePath().concat(String.valueOf(storeCate.getStoreCateId())).concat(SPLIT_CHAR);
        StoreCateQueryRequest cateReq = new StoreCateQueryRequest();
        cateReq.setDelFlag(DeleteFlag.NO);
        cateReq.setLikeCatePath(oldCatePath);
        storeCateList.addAll(storeCateRepository.findAll(cateReq.getWhereCriteria()));
        return storeCateList;
    }

    /**
     * 根据ID获取所有子分类->所有的商品
     *
     * @param storeCateId
     * @param isHaveSelf
     * @return
     */
    public List<StoreCateGoodsRela> findAllChildRela(Long storeCateId, boolean isHaveSelf) {
        List<Long> storeCateIds = this.findAllChlid(storeCateId, isHaveSelf)
                .stream()
                .map(StoreCate::getStoreCateId)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(storeCateIds)) {
            return new ArrayList<>();
        }
        return storeCateGoodsRelaRepository.findAll((root, cquery, cbuild) -> root.get("storeCateId").in(storeCateIds));
    }

    /**
     * 根据ID批量查询店铺商品分类
     *
     * @param cateIds 多个分类ID
     * @return list
     */
    public List<StoreCate> findByIds(List<Long> cateIds) {
        return storeCateRepository.findAll(StoreCateQueryRequest.builder().storeCateIds(cateIds).build().getWhereCriteria());
    }

    public List<StoreCateBase> findAllByStoreCateIdIn(List<Long> storeCateIds){
        return storeCateRepository.findAllByStoreCateIdIn(storeCateIds);
    }
}
