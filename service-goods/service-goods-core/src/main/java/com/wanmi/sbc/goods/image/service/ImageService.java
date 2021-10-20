package com.wanmi.sbc.goods.image.service;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.goods.api.enums.DeleteFlagEnum;
import com.wanmi.sbc.goods.api.enums.ImageTypeEnum;
import com.wanmi.sbc.goods.api.request.image.ImagePageProviderRequest;
import com.wanmi.sbc.goods.api.request.image.ImageProviderRequest;
import com.wanmi.sbc.goods.classify.model.root.ClassifyDTO;
import com.wanmi.sbc.goods.image.model.root.ImageDTO;
import com.wanmi.sbc.goods.image.repository.ImageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/20 1:37 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class ImageService {

    @Resource
    private ImageRepository imageRepository;

    /**
     * 新增
     * @param imageProviderRequest
     */
    public void add(ImageProviderRequest imageProviderRequest) {

        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setId(null);
        imageDTO.setName(imageProviderRequest.getName());
        imageDTO.setImgUrl(imageProviderRequest.getImgUrl());
        imageDTO.setImgHref(imageProviderRequest.getImgHref());
        imageDTO.setBeginTime(imageProviderRequest.getBeginTime());
        imageDTO.setEndTime(imageProviderRequest.getEndTime());
        imageDTO.setOrderNum(imageProviderRequest.getOrderNum());
        imageDTO.setImageType(imageProviderRequest.getImageType());
        imageDTO.setCreateTime(LocalDateTime.now());
        imageDTO.setUpdateTime(LocalDateTime.now());
        imageDTO.setDelFlag(DeleteFlagEnum.NORMAL.getCode());
        imageRepository.save(imageDTO);
    }


    public void update(ImageProviderRequest imageProviderRequest) {
        if (imageProviderRequest.getId() == null) {
            throw new SbcRuntimeException("K-000009");
        }
        ImagePageProviderRequest imagePageProviderRequest = new ImagePageProviderRequest();
        imagePageProviderRequest.setId(imageProviderRequest.getId());
        List<ImageDTO> imageDTOList = this.listNoPage(imagePageProviderRequest);
        if (CollectionUtils.isEmpty(imageDTOList)) {
            throw new SbcRuntimeException("K-000009");
        }
        ImageDTO imageDTO = imageDTOList.get(0);
        if (!StringUtils.isEmpty(imageProviderRequest.getName())) {
            imageDTO.setName(imageProviderRequest.getName());
        }

        if (imageProviderRequest.getBeginTime() != null) {
            imageDTO.setBeginTime(imageProviderRequest.getBeginTime());
        }

        if (imageProviderRequest.getEndTime() != null) {
            imageDTO.setEndTime(imageProviderRequest.getEndTime());
        }

        if (!StringUtils.isEmpty(imageProviderRequest.getImgUrl())) {
            imageDTO.setImgUrl(imageProviderRequest.getImgUrl());
        }

        if (!StringUtils.isEmpty(imageProviderRequest.getImgHref())) {
            imageDTO.setImgHref(imageProviderRequest.getImgHref());
        }
        imageRepository.save(imageDTO);
    }

    public void delete(ImageProviderRequest imageProviderRequest) {
        if (imageProviderRequest.getId() == null) {
            throw new SbcRuntimeException("K-000009");
        }

        ImagePageProviderRequest imagePageProviderRequest = new ImagePageProviderRequest();
        imagePageProviderRequest.setId(imageProviderRequest.getId());
        List<ImageDTO> imageDTOList = this.listNoPage(imagePageProviderRequest);
        if (CollectionUtils.isEmpty(imageDTOList)) {
            throw new SbcRuntimeException("K-000009");
        }
        ImageDTO imageDTO = imageDTOList.get(0);
        imageDTO.setDelFlag(DeleteFlagEnum.DELETE.getCode());
        imageDTO.setUpdateTime(LocalDateTime.now());
        imageRepository.save(imageDTO);
    }


    private Sort packageSort() {
        return Sort.by(Sort.Direction.ASC, "sortNum").and(Sort.by(Sort.Direction.DESC, "updateTime"));
    }

    /**
     * 获取图片列表
     * @param imagePageProviderRequest
     * @return
     */
    public Page<ImageDTO> list(ImagePageProviderRequest imagePageProviderRequest) {
        Sort sort = this.packageSort();
        Pageable pageable = PageRequest.of(imagePageProviderRequest.getPageNum(), imagePageProviderRequest.getPageSize(), sort);
        return imageRepository.findAll(this.packageWhere(imagePageProviderRequest), pageable);
    }

    /**
     * 获取图片列表
     * @param imagePageProviderRequest
     * @return
     */
    public List<ImageDTO> listNoPage(ImagePageProviderRequest imagePageProviderRequest) {
        Sort sort = this.packageSort();
        return imageRepository.findAll(this.packageWhere(imagePageProviderRequest), sort);
    }


    private Specification<ImageDTO> packageWhere(ImagePageProviderRequest imagePageProviderRequest) {
        return new Specification<ImageDTO>() {
            @Override
            public Predicate toPredicate(Root<ImageDTO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                final List<Predicate> conditionList = new ArrayList<>();
                //只是获取有效的
                conditionList.add(criteriaBuilder.equal(root.get("delFlag"), DeleteFlagEnum.NORMAL.getCode()));
                if (imagePageProviderRequest.getId() != null) {
                    conditionList.add(criteriaBuilder.equal(root.get("id"), imagePageProviderRequest.getId()));
                }
                if (!StringUtils.isEmpty(imagePageProviderRequest.getName())) {
                    conditionList.add(criteriaBuilder.equal(root.get("name"), imagePageProviderRequest.getName()));
                }
                if (imagePageProviderRequest.getPublishState() != null) {
                    conditionList.add(criteriaBuilder.equal(root.get("publishState"), imagePageProviderRequest.getPublishState()));
                }
                if (imagePageProviderRequest.getImageType() != null) {
                    conditionList.add(criteriaBuilder.equal(root.get("imageType"), imagePageProviderRequest.getImageType()));
                }
                if (imagePageProviderRequest.getStatus() != null) {
                    if (Objects.equals(imagePageProviderRequest.getStatus(), 0)) {
                        conditionList.add(criteriaBuilder.lessThan(root.get("beginTime"), LocalDateTime.now()));
                    } else if (Objects.equals(imagePageProviderRequest.getStatus(), 1)) {
                        LocalDateTime now = LocalDateTime.now();
                        conditionList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("beginTime"), now));
                        conditionList.add(criteriaBuilder.lessThan(root.get("endTime"), now));
                    } else {
                        conditionList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("endTime"), LocalDateTime.now()));
                    }
                }
                return criteriaBuilder.and(conditionList.toArray(new Predicate[0]));
            }
        };
    }
}
