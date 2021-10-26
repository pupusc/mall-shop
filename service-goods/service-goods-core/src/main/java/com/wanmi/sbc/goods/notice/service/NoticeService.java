package com.wanmi.sbc.goods.notice.service;

import com.wanmi.sbc.goods.api.enums.DeleteFlagEnum;
import com.wanmi.sbc.goods.api.request.notice.NoticePageProviderRequest;
import com.wanmi.sbc.goods.api.request.notice.NoticeProviderRequest;
import com.wanmi.sbc.goods.notice.model.root.NoticeDTO;
import com.wanmi.sbc.goods.notice.repository.NoticeRepository;
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
import java.util.Collections;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/26 2:28 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class NoticeService {


    @Resource
    private NoticeRepository noticeRepository;

    public void add(NoticeProviderRequest noticeProviderRequest) {
        NoticeDTO noticeDTO = new NoticeDTO();
        noticeDTO.setContent(noticeProviderRequest.getContent());
        noticeDTO.setBeginTime(noticeProviderRequest.getBeginTime());
        noticeDTO.setEndTime(noticeProviderRequest.getEndTime());
        noticeDTO.setCreateTime(LocalDateTime.now());
        noticeDTO.setUpdateTime(LocalDateTime.now());
        noticeDTO.setDelFlag(DeleteFlagEnum.NORMAL.getCode());
        noticeRepository.save(noticeDTO);
    }


    public void update(NoticeProviderRequest noticeProviderRequest) {
        NoticePageProviderRequest request = new NoticePageProviderRequest();
        request.setNoticeIdColl(Collections.singleton(noticeProviderRequest.getId()));
        List<NoticeDTO> noticeDTOList = this.listNoPage(request);
        if (CollectionUtils.isEmpty(noticeDTOList)) {
            throw new IllegalStateException("K-000009");
        }

        NoticeDTO noticeDTO = noticeDTOList.get(0);
        if (!StringUtils.isEmpty(noticeProviderRequest.getContent())) {
            noticeDTO.setContent(noticeProviderRequest.getContent());
        }
        if (noticeProviderRequest.getBeginTime() != null) {
            noticeDTO.setBeginTime(noticeProviderRequest.getBeginTime());
        }
        if (noticeProviderRequest.getEndTime() != null) {
            noticeDTO.setEndTime(noticeProviderRequest.getEndTime());
        }
        noticeDTO.setUpdateTime(LocalDateTime.now());
        noticeRepository.save(noticeDTO);
    }

    private Sort packageSort() {
        return Sort.by(Sort.Direction.DESC, "updateTime");
    }

    /**
     * 获取列表
     * @return
     */
    public List<NoticeDTO> listNoPage(NoticePageProviderRequest request) {
        return noticeRepository.findAll(this.packageWhere(request), this.packageSort());
    }

    public Page<NoticeDTO> list(NoticePageProviderRequest request) {
        Sort sort = this.packageSort();
        Pageable pageable = PageRequest.of(request.getPageNum(), request.getPageSize(), sort);
        return noticeRepository.findAll(this.packageWhere(request), pageable);
    }


    private Specification<NoticeDTO> packageWhere(NoticePageProviderRequest request) {
        return new Specification<NoticeDTO>() {
            @Override
            public Predicate toPredicate(Root<NoticeDTO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                final List<Predicate> conditionList = new ArrayList<>();
                //只是获取有效的
                conditionList.add(criteriaBuilder.equal(root.get("delFlag"), DeleteFlagEnum.NORMAL.getCode()));
                if (!CollectionUtils.isEmpty(request.getNoticeIdColl())) {
                    conditionList.add(root.get("id").in(request.getNoticeIdColl()));
                }
                return criteriaBuilder.and(conditionList.toArray(new Predicate[0]));
            }
        };
    }
}
