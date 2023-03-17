package com.wanmi.sbc.bookmeta.controller;

import com.wanmi.sbc.bookmeta.bo.MetaBookRelationAddBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookRelationDelBO;
import com.wanmi.sbc.bookmeta.bo.RelationAddBO;
import com.wanmi.sbc.bookmeta.provider.MetaBookRelationProvider;
import com.wanmi.sbc.bookmeta.vo.*;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/06/16:25
 * @Description:
 */
@RestController
@RequestMapping("/metaBookRelation")
public class MetaBookRelationController {
    @Resource
    MetaBookRelationProvider metaBookRelationProvider;
    @PostMapping("insertMetaBookRelation")
    public BusinessResponse<Integer> insert(@RequestBody RelationAddVo reqVO) {
        List<MetaBookRelationAddReqVO> list = reqVO.getList();
        List<MetaBookRelationAddBO> list1 = KsBeanUtil.convertList(list, MetaBookRelationAddBO.class);
        RelationAddBO convert = new RelationAddBO();
        convert.setList(list1);
        convert.setBookId(reqVO.getBookId());
        Integer insert = metaBookRelationProvider.insert(convert);
            return BusinessResponse.success(insert);
    }

    @PostMapping("deleteAllMetaBookRelation")
    public BusinessResponse<Integer> delete(@RequestBody MetaBookRelationAddReqVO reqVO) {
        MetaBookRelationAddBO convert = KsBeanUtil.convert(reqVO, MetaBookRelationAddBO.class);
        Integer insert = metaBookRelationProvider.delete(convert);
        return BusinessResponse.success(insert);
    }

    @PostMapping("deleteRelationKey")
    public BusinessResponse<Integer> deleteRelationKey(@RequestBody MetaBookRelationDelReqVO reqVO) {
        MetaBookRelationDelBO convert = KsBeanUtil.convert(reqVO, MetaBookRelationDelBO.class);
        Integer deleteKey = metaBookRelationProvider.deleteKey(convert);
        return BusinessResponse.success(deleteKey);
    }

    @PostMapping("deleteRelationBook")
    public BusinessResponse<Integer> deleteRelationBook(@RequestBody MetaBookRelationDelReqVO reqVO) {
        MetaBookRelationDelBO convert = KsBeanUtil.convert(reqVO, MetaBookRelationDelBO.class);
        Integer deleteBook = metaBookRelationProvider.deleteBook(convert);
        return BusinessResponse.success(deleteBook);
    }

    @PostMapping("getRelationBook")
    public BusinessResponse<List<MetaBookRelationAddReqVO>> getRelations(@RequestBody MetaBookRelationDelBO reqVO) {
        MetaBookRelationDelBO convert = KsBeanUtil.convert(reqVO, MetaBookRelationDelBO.class);
        List<MetaBookRelationAddBO> metaBookRelationAddBO = metaBookRelationProvider.selectAll(convert);
        List<MetaBookRelationAddReqVO> convert1 = KsBeanUtil.convertList(metaBookRelationAddBO, MetaBookRelationAddReqVO.class);
        return BusinessResponse.success(convert1);
    }


    @PostMapping("updateRelation")
    public BusinessResponse<Integer> updateRelations(@RequestBody MetaBookRelationAddReqVO reqVO) {
        MetaBookRelationAddBO convert = KsBeanUtil.convert(reqVO, MetaBookRelationAddBO.class);
        Integer updateAll = metaBookRelationProvider.updateAll(convert);
        return BusinessResponse.success(updateAll);
    }
}
