package com.wanmi.sbc.goods.customergoodsevaluatepraise.service;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.request.customergoodsevaluatepraise.CustomerGoodsEvaluatePraiseQueryRequest;
import com.wanmi.sbc.goods.bean.vo.CustomerGoodsEvaluatePraiseVO;
import com.wanmi.sbc.goods.customergoodsevaluatepraise.model.root.CustomerGoodsEvaluatePraise;
import com.wanmi.sbc.goods.customergoodsevaluatepraise.repository.CustomerGoodsEvaluatePraiseRepository;
import com.wanmi.sbc.goods.goodsevaluate.repository.GoodsEvaluateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>会员商品评价点赞关联表业务逻辑</p>
 *
 * @author lvzhenwei
 * @date 2019-05-07 14:25:25
 */
@Service("CustomerGoodsEvaluatePraiseService")
public class CustomerGoodsEvaluatePraiseService {

    @Autowired
    private CustomerGoodsEvaluatePraiseRepository customerGoodsEvaluatePraiseRepository;

    @Autowired
    private GoodsEvaluateRepository goodsEvaluateRepository;

    /**
     * 新增会员商品评价点赞关联表
     *
     * @author lvzhenwei
     */
    @Transactional
    public CustomerGoodsEvaluatePraise add(CustomerGoodsEvaluatePraise entity) {
        customerGoodsEvaluatePraiseRepository.save(entity);
        //更新商品评价点赞数
        goodsEvaluateRepository.updateGoodsEvaluateGoodNum(entity.getGoodsEvaluateId());
        return entity;
    }

    /**
     * 修改会员商品评价点赞关联表
     *
     * @author lvzhenwei
     */
    @Transactional
    public CustomerGoodsEvaluatePraise modify(CustomerGoodsEvaluatePraise entity) {
        customerGoodsEvaluatePraiseRepository.save(entity);
        return entity;
    }

    /**
     * 单个删除会员商品评价点赞关联表
     *
     * @author lvzhenwei
     */
    @Transactional
    public void deleteById(String id) {
        customerGoodsEvaluatePraiseRepository.deleteById(id);
    }

    /**
     * 批量删除会员商品评价点赞关联表
     *
     * @author lvzhenwei
     */
    @Transactional
    public void deleteByIdList(List<String> ids) {
        ids.forEach(id -> customerGoodsEvaluatePraiseRepository.deleteById(id));
    }

    /**
     * 单个查询会员商品评价点赞关联表
     *
     * @author lvzhenwei
     */
    public CustomerGoodsEvaluatePraise getById(String id) {
        return customerGoodsEvaluatePraiseRepository.findById(id).orElse(null);
    }

    /**
     * 单个查询会员商品评价点赞关联表
     *
     * @author lvzhenwei
     */
    public CustomerGoodsEvaluatePraise getCustomerGoodsEvaluatePraise(CustomerGoodsEvaluatePraiseQueryRequest request) {
        return customerGoodsEvaluatePraiseRepository.findOne(CustomerGoodsEvaluatePraiseWhereCriteriaBuilder.build(request)).orElse(null);
    }

    /**
     * 分页查询会员商品评价点赞关联表
     *
     * @author lvzhenwei
     */
    public Page<CustomerGoodsEvaluatePraise> page(CustomerGoodsEvaluatePraiseQueryRequest queryReq) {
        return customerGoodsEvaluatePraiseRepository.findAll(
                CustomerGoodsEvaluatePraiseWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }

    /**
     * 列表查询会员商品评价点赞关联表
     *
     * @author lvzhenwei
     */
    public List<CustomerGoodsEvaluatePraise> list(CustomerGoodsEvaluatePraiseQueryRequest queryReq) {
        return customerGoodsEvaluatePraiseRepository.findAll(CustomerGoodsEvaluatePraiseWhereCriteriaBuilder.build(queryReq));
    }

    /**
     * 将实体包装成VO
     *
     * @author lvzhenwei
     */
    public CustomerGoodsEvaluatePraiseVO wrapperVo(CustomerGoodsEvaluatePraise customerGoodsEvaluatePraise) {
        if (customerGoodsEvaluatePraise != null) {
            CustomerGoodsEvaluatePraiseVO customerGoodsEvaluatePraiseVO = new CustomerGoodsEvaluatePraiseVO();
            KsBeanUtil.copyPropertiesThird(customerGoodsEvaluatePraise, customerGoodsEvaluatePraiseVO);
            return customerGoodsEvaluatePraiseVO;
        }
        return null;
    }

    /**
     * 取消点赞
     *
     * @author lvzhenwei
     */
    @Transactional
    public CustomerGoodsEvaluatePraise cancel(CustomerGoodsEvaluatePraise entity) {
        customerGoodsEvaluatePraiseRepository.deleteCustomerGoodsEvaluatePraiseByCustomerIdAndGoodsEvaluateId(entity.getCustomerId(), entity.getGoodsEvaluateId());
        //更新商品评价点赞数
        goodsEvaluateRepository.subGoodsEvaluateGoodNum(entity.getGoodsEvaluateId());
        return entity;
    }
}
