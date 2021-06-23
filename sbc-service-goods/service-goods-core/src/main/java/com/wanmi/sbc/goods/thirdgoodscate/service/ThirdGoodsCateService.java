package com.wanmi.sbc.goods.thirdgoodscate.service;

import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.goods.bean.dto.ThirdGoodsCateDTO;
import com.wanmi.sbc.goods.bean.dto.ThirdGoodsCateRelDTO;
import com.wanmi.sbc.goods.bean.vo.ThirdGoodsCateRelVO;
import com.wanmi.sbc.goods.bean.vo.ThirdGoodsCateVO;
import com.wanmi.sbc.goods.cate.model.root.GoodsCate;
import com.wanmi.sbc.goods.cate.service.GoodsCateService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.goods.thirdgoodscate.repository.ThirdGoodsCateRepository;
import com.wanmi.sbc.goods.thirdgoodscate.model.root.ThirdGoodsCate;
import com.wanmi.sbc.goods.api.request.thirdgoodscate.ThirdGoodsCateQueryRequest;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.enums.DeleteFlag;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>业务逻辑</p>
 *
 * @author
 * @date 2020-08-17 14:46:43
 */
@Service("ThirdGoodsCateService")
public class ThirdGoodsCateService {
    @Autowired
    private ThirdGoodsCateRepository thirdGoodsCateRepository;
    @Autowired
    private GoodsCateService goodsCateService;

    /**
     * 新增
     *
     * @author
     */
    @Transactional
    public ThirdGoodsCate add(ThirdGoodsCate entity) {
        thirdGoodsCateRepository.save(entity);
        return entity;
    }

    /**
     * 修改
     *
     * @author
     */
    @Transactional
    public ThirdGoodsCate modify(ThirdGoodsCate entity) {
        thirdGoodsCateRepository.save(entity);
        return entity;
    }

    /**
     * 单个删除
     *
     * @author
     */
    @Transactional
    public void deleteById(ThirdGoodsCate entity) {
        thirdGoodsCateRepository.save(entity);
    }

    /**
     * 批量删除
     *
     * @author
     */
    @Transactional
    public void deleteByIdList(List<ThirdGoodsCate> infos) {
        thirdGoodsCateRepository.saveAll(infos);
    }

    /**
     * 单个查询
     *
     * @author
     */
    public ThirdGoodsCate getOne(Long id) {
        return thirdGoodsCateRepository.findByCateIdAndDelFlag(id, DeleteFlag.NO)
                .orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SPECIFIED, "不存在"));
    }

    /**
     * 分页查询
     *
     * @author
     */
    public Page<ThirdGoodsCate> page(ThirdGoodsCateQueryRequest queryReq) {
        return thirdGoodsCateRepository.findAll(
                ThirdGoodsCateWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }

    /**
     * 列表查询
     *
     * @author
     */
    public List<ThirdGoodsCate> list(ThirdGoodsCateQueryRequest queryReq) {
        return thirdGoodsCateRepository.findAll(ThirdGoodsCateWhereCriteriaBuilder.build(queryReq));
    }

    /**
     * 根据三方类目父id关联查询平台类目
     *
     * @author
     */
    public List<ThirdGoodsCateRelDTO> getAllRel(ThirdPlatformType source, Long cateParentId) {
        return thirdGoodsCateRepository.getRelByParentId(source, cateParentId);
    }

    /**
     * 将实体包装成VO
     *
     * @author
     */
    public ThirdGoodsCateVO wrapperVo(ThirdGoodsCate thirdGoodsCate) {
        if (thirdGoodsCate != null) {
            ThirdGoodsCateVO thirdGoodsCateVO = KsBeanUtil.convert(thirdGoodsCate, ThirdGoodsCateVO.class);
            return thirdGoodsCateVO;
        }
        return null;
    }

    /**
     * 全量更新所有类目
     */
    @Transactional
    public void updateAll(List<ThirdGoodsCateDTO> thirdGoodsCateDTOS) {
        List<ThirdGoodsCate> thirdGoodsCates = KsBeanUtil.convert(thirdGoodsCateDTOS, ThirdGoodsCate.class);
        for (ThirdGoodsCate thirdGoodsCate : thirdGoodsCates) {
            thirdGoodsCate.setCreateTime(LocalDateTime.now());
            thirdGoodsCate.setUpdateTime(LocalDateTime.now());
            thirdGoodsCate.setDelFlag(DeleteFlag.NO);
        }
        ThirdPlatformType thirdPlatformType = thirdGoodsCateDTOS.get(0).getThirdPlatformType();
        if (thirdPlatformType == null) {
            throw new SbcRuntimeException("K-190001");
        }
        thirdGoodsCateRepository.delAllByThirdPlatformType(thirdPlatformType);
        thirdGoodsCateRepository.saveAll(thirdGoodsCates);
    }

    public List<ThirdGoodsCate> getByCateIds(ThirdPlatformType source, List<Long> cateIds) {
        return thirdGoodsCateRepository.getByCateIds(source, cateIds);
    }

    /**
     * 查询所有三方类目并关联平台类目
     *
     * @return
     */
    public List<ThirdGoodsCateRelVO> listRel(ThirdPlatformType thirdPlatformType) {
        List<ThirdGoodsCateRelDTO> allCate = thirdGoodsCateRepository.getRel(thirdPlatformType);
        List<ThirdGoodsCateRelVO> thirdGoodsCateRelVOS = KsBeanUtil.convertList(allCate, ThirdGoodsCateRelVO.class);
        for (ThirdGoodsCateRelVO thirdGoodsCateRelVO : thirdGoodsCateRelVOS) {
            thirdGoodsCateRelVO.setPath(catePath(thirdGoodsCateRelVO.getCateId()));
        }
		//一级类目
        List<ThirdGoodsCateRelVO> oneGrade = thirdGoodsCateRelVOS.stream().filter(v -> v.getThirdCateParentId() == 0).collect(Collectors.toList());
        for (ThirdGoodsCateRelVO thirdGoodsCateRelVO : oneGrade) {
            getChildrenCate(thirdGoodsCateRelVO, thirdGoodsCateRelVOS);
        }
        return oneGrade;
    }

    /**
     * 根据类目id查询名称路径
     *
     * @param cateId
     * @return
     */
    public String catePath(Long cateId) {
        if (cateId == null) {
            return null;
        } else {
            String path = "";
            GoodsCate goodsCate = goodsCateService.findById(cateId);
            String[] parentCateIds = goodsCate.getCatePath().split("\\|");
            for (int i = 1; i < parentCateIds.length; i++) {
                path += (goodsCateService.findById(Long.valueOf(parentCateIds[i])).getCateName() + " - ");
            }
            return path + goodsCate.getCateName();
        }
    }

    //递归子类目
    public void getChildrenCate(ThirdGoodsCateRelVO cateRelVO, List<ThirdGoodsCateRelVO> thirdGoodsCateRelVOS) {
        List<ThirdGoodsCateRelVO> children = thirdGoodsCateRelVOS.stream().filter(v -> v.getThirdCateParentId().equals(cateRelVO.getThirdCateId())).collect(Collectors.toList());
        if (children.size() > 0) {
            cateRelVO.setChildren(children);
            for (ThirdGoodsCateRelVO child : children) {
                getChildrenCate(child, thirdGoodsCateRelVOS);
            }
        }
    }

}

