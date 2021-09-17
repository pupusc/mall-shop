package com.wanmi.sbc.goods.classify.service;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.goods.api.enums.DeleteFlagEnum;
import com.wanmi.sbc.goods.classify.request.BookListModelClassifyLinkPageRequest;
import com.wanmi.sbc.goods.classify.response.BookListModelClassifyLinkResponse;
import com.wanmi.sbc.goods.classify.model.root.BookListModelClassifyRelDTO;
import com.wanmi.sbc.goods.classify.model.root.ClassifyDTO;
import com.wanmi.sbc.goods.classify.repository.BookListModelClassifyRelRepository;
import com.wanmi.sbc.goods.classify.request.BookListModelClassifyRelPageRequest;
import com.wanmi.sbc.goods.classify.request.BookListModelClassifyRelRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/5 7:38 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
@Slf4j
public class BookListModelClassifyRelService {

    @Resource
    private BookListModelClassifyRelRepository bookListModelClassifyRelRepository;
    @Autowired
    private ClassifyService classifyService;
    @Resource
    private BookListModelClassifyRelService bookListModelClassifyRelService;


    /**
     * 更改商品对应分类
     * @param bookListModelClassifyRequest
     */
    @Transactional
    public void change(BookListModelClassifyRelRequest bookListModelClassifyRequest) {

        //获取商品分类，查看当前参数是否有效
        List<ClassifyDTO> classifyList = classifyService.listNoPage(bookListModelClassifyRequest.getClassifyIdList());
        log.info("--->> BookListModelClassifyRelService.add classify result: {}", JSON.toJSONString(classifyList));
        if (bookListModelClassifyRequest.getClassifyIdList().size() != classifyList.size()) {
            log.error("--->> BookListModelClassifyRelService.add 书单：{} 传递的参数为：{} 结果信息为:{}",
                    bookListModelClassifyRequest.getBookListModelId(),bookListModelClassifyRequest.getClassifyIdList(), JSON.toJSONString(classifyList));
            throw new SbcRuntimeException(String.format("书单：%s 获取的结果和传递的结果不同，请求参数有误", bookListModelClassifyRequest.getBookListModelId()));
        }
        Set<Integer> classifyIdSet = classifyList.stream().map(ClassifyDTO::getId).collect(Collectors.toSet());
        if (classifyList.size() != classifyIdSet.size()){
            throw new SbcRuntimeException(String.format("书单：%s 获取的结果和传递的结果中有重复的 classifyId", bookListModelClassifyRequest.getBookListModelId()));
        }

        //1删除当前 书单对一个的类目
        List<BookListModelClassifyRelDTO> bookListModelClassifyList = this.listNoPage(bookListModelClassifyRequest.getBookListModelId());
        if (!CollectionUtils.isEmpty(bookListModelClassifyList)) {
            bookListModelClassifyList.forEach(e -> {
                e.setDelFlag(DeleteFlagEnum.DELETE.getCode());
                e.setUpdateTime(new Date());
            });
            bookListModelClassifyRelRepository.saveAll(bookListModelClassifyList);
        }
        //2 新增 书单对应类目
        List<BookListModelClassifyRelDTO> addList = new ArrayList<>();
        for (Integer classifyIdParam : bookListModelClassifyRequest.getClassifyIdList()) {
            BookListModelClassifyRelDTO bookListModelClassifyDTO = new BookListModelClassifyRelDTO();
            bookListModelClassifyDTO.setBookListModelId(bookListModelClassifyRequest.getBookListModelId());
            bookListModelClassifyDTO.setClassifyId(classifyIdParam);
            bookListModelClassifyDTO.setCreateTime(new Date());
            bookListModelClassifyDTO.setUpdateTime(new Date());
            bookListModelClassifyDTO.setDelFlag(DeleteFlagEnum.NORMAL.getCode());
            addList.add(bookListModelClassifyDTO);
        }
        bookListModelClassifyRelRepository.saveAll(addList);
    }


    public List<BookListModelClassifyRelDTO> listNoPage(Integer bookListModelId) {
        BookListModelClassifyRelPageRequest request = new BookListModelClassifyRelPageRequest();
        request.setBookListModelId(bookListModelId);
        return bookListModelClassifyRelRepository.findAll(this.packageWhere(request));
    }

    /**
     * 根据 发布状态 书单类型 和分类信息，关联表查询 书单列表
     * @param request
     */
    public List<BookListModelClassifyLinkResponse> listBookListModelClassifyLink(BookListModelClassifyLinkPageRequest request) {
        if (CollectionUtils.isEmpty(request.getBusinessTypeList())
            || CollectionUtils.isEmpty(request.getClassifyIdColl())
            || CollectionUtils.isEmpty(request.getPublishStateColl())) {
            return new ArrayList<>();
        }
        Pageable pageable = PageRequest.of(request.getPageNum(), request.getPageSize());
        Page<BookListModelClassifyLinkResponse> bookListModelClassifyLinkResponses =
                bookListModelClassifyRelRepository.listBookListModelClassifyLink
                        (request.getBusinessTypeList(), request.getClassifyIdColl(), request.getPublishStateColl(), pageable);
        return bookListModelClassifyLinkResponses.getContent();
    }

//    /**
//     * 分页获取书单 分类关系表信息
//     * @param request
//     * @return
//     */
//    public Page<BookListModelClassifyRelDTO> list(BookListModelClassifyRelPageRequest request) {
//        Pageable page = PageRequest.of(request.getPageNum(), request.getPageSize(), Sort.Direction.DESC, "updateTime");
//        return bookListModelClassifyRelRepository.findAll(this.packageWhere(request), page);
//    }

    /**
     * 根据 书单id 获取父分类下的所有子分类
     * @param bookListModelId
     * @return
     */
    public List<ClassifyDTO> listParentAllChildClassifyByBookListModelId(Integer bookListModelId) {
        //根据书单获取书单分类列表
        List<BookListModelClassifyRelDTO> bookListModelClassifyRelList = this.listNoPage(bookListModelId);
        if (CollectionUtils.isEmpty(bookListModelClassifyRelList)) {
            return new ArrayList<>();
        }
        //根据分类列表获取分类详情
        List<Integer> classifyIdList =
                bookListModelClassifyRelList.stream().map(BookListModelClassifyRelDTO::getClassifyId).collect(Collectors.toList());
        List<ClassifyDTO> classifyList = classifyService.listNoPage(classifyIdList);
        if (CollectionUtils.isEmpty(classifyList)) {
            return new ArrayList<>();
        }
        //获取父分类
        Set<Integer> parentClssifyList = classifyList.stream().map(ClassifyDTO::getParentId).collect(Collectors.toSet());
        //获取父分类的子分类列表
        List<ClassifyDTO> allChildClassifyList = classifyService.listChildClassifyNoPageByParentId(parentClssifyList);
        if (CollectionUtils.isEmpty(allChildClassifyList)) {
            return new ArrayList<>();
        }
        return allChildClassifyList;
    }


    private Specification<BookListModelClassifyRelDTO> packageWhere(BookListModelClassifyRelPageRequest request) {
        return new Specification<BookListModelClassifyRelDTO>() {
            @Override
            public Predicate toPredicate(Root<BookListModelClassifyRelDTO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                final List<Predicate> conditionList = new ArrayList<>();

                //只是获取有效的
                conditionList.add(criteriaBuilder.equal(root.get("delFlag"), DeleteFlagEnum.NORMAL.getCode()));
                if (request.getBookListModelId() != null) {
                    conditionList.add(criteriaBuilder.equal(root.get("bookListModelId"), request.getBookListModelId()));
                }
                if (!CollectionUtils.isEmpty(request.getClassifyIdColl())) {
                    conditionList.add(root.get("classifyId").in(request.getClassifyIdColl()));
                }
                return criteriaBuilder.and(conditionList.toArray(new Predicate[conditionList.size()]));
            }
        };
    }
}
