package com.wanmi.sbc.linkedmall.cate;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.linkedmall.model.v20180116.GetCategoryChainRequest;
import com.aliyuncs.linkedmall.model.v20180116.GetCategoryChainResponse;
import com.aliyuncs.linkedmall.model.v20180116.GetCategoryListRequest;
import com.aliyuncs.linkedmall.model.v20180116.GetCategoryListResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.linkedmall.bean.vo.LinkedMallGoodsCateVO;
import com.wanmi.sbc.linkedmall.util.LinkedMallUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * linkedmall类目
 */
@Slf4j
@Service
public class LinkedMallCateService {
    @Autowired
    private IAcsClient iAcsClient;

    @Autowired
    private LinkedMallUtil linkedMallUtil;

    /**
     * 查询linkedmall全量类目
     */
    public ArrayList<LinkedMallGoodsCateVO> getAllCategory() {
        ArrayList<LinkedMallGoodsCateVO> linkedMallGoodsCateVOS = new ArrayList<>();
        //一级类目
        List<LinkedMallGoodsCateVO> oneCates = getCategoryById(0L);
        recursionCate(oneCates, 1, linkedMallGoodsCateVOS, 0L, "0");
        return linkedMallGoodsCateVOS;
    }

    /**
     * 根据分类id查询linkedmall类目
     */
    public List<LinkedMallGoodsCateVO> getCategoryById(long cateId) {
        List<LinkedMallGoodsCateVO> linkedMallGoodsCateVOS = null;
        GetCategoryListRequest getCategoryListRequest = new GetCategoryListRequest();
        getCategoryListRequest.setBizId(linkedMallUtil.getLinkedMallBizId());
        getCategoryListRequest.setCategoryId(cateId);
        List<GetCategoryListResponse.Category> categoryList = null;
        try {
            GetCategoryListResponse acsResponse = iAcsClient.getAcsResponse(getCategoryListRequest);
            categoryList = acsResponse.getCategoryList();
            if (categoryList != null && categoryList.size() > 0) {
                linkedMallGoodsCateVOS = categoryList.stream()
                        .map(v -> new LinkedMallGoodsCateVO(v.getCategoryId(), v.getName(), null, null, null, null))
                        .collect(Collectors.toList());
            }
        } catch (ClientException e) {
            log.error("查询linkedmall类目异常", e);
        }
        return linkedMallGoodsCateVOS;
    }

    /**
     * 根据providerGoodsId查询类目链
     *
     * @param providerGoodsId
     */
    public List<GetCategoryChainResponse.Category> getCategoryChainById(Long providerGoodsId) {
        GetCategoryChainRequest getCategoryChainRequest = new GetCategoryChainRequest();
        getCategoryChainRequest.setBizId(linkedMallUtil.getLinkedMallBizId());
        getCategoryChainRequest.setItemId(providerGoodsId);
        List<GetCategoryChainResponse.Category> categoryChain = null;
        try {
            GetCategoryChainResponse acsResponse = iAcsClient.getAcsResponse(getCategoryChainRequest);
            categoryChain = acsResponse.getCategoryList();
        } catch (ClientException e) {
            log.error("查询linkedmall商品类目链异常", e);
        }
        return categoryChain;
    }

    /**
     * 递归查询分类结构
     *
     * @param categoryList
     * @param grade
     * @param linkedMallGoodsCates
     * @param cateParentId
     * @param path
     */
    private void recursionCate(List<LinkedMallGoodsCateVO> categoryList, int grade, ArrayList<LinkedMallGoodsCateVO> linkedMallGoodsCates, long cateParentId, String path) {
        if (categoryList != null && categoryList.size() > 0) {
            for (LinkedMallGoodsCateVO category : categoryList) {
                LinkedMallGoodsCateVO linkedMallGoodsCate = new LinkedMallGoodsCateVO();
                linkedMallGoodsCate.setCateGrade(grade);
                linkedMallGoodsCate.setCateParentId(cateParentId);
                linkedMallGoodsCate.setCateId(category.getCateId());
                linkedMallGoodsCate.setCateName(category.getCateName());
                linkedMallGoodsCate.setCatePath(path);
                linkedMallGoodsCate.setThirdPlatformType(ThirdPlatformType.LINKED_MALL);
                linkedMallGoodsCates.add(linkedMallGoodsCate);
                List<LinkedMallGoodsCateVO> child = getCategoryById(category.getCateId());
                recursionCate(child, grade + 1, linkedMallGoodsCates, category.getCateId(), path + "|" + category.getCateId());
            }
        }

    }
}
