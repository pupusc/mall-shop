package com.wanmi.sbc.setting.util;

import com.wanmi.sbc.setting.api.request.ConfigQueryRequest;
import com.wanmi.sbc.setting.config.Config;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class SpecificationUtil {
    /**
     * 封装公共条件
     * @return
     */
    public static Specification<Config> getWhereCriteria(ConfigQueryRequest request){
        String configType = request.getConfigType();
        String configKey = request.getConfigKey();
        Integer status = request.getStatus();
        Integer delFlag = request.getDelFlag();

        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            //类型
            if(StringUtils.isNotEmpty(configType)){
                predicates.add(cbuild.equal(root.get("configType"), configType));
            }
            //类型
            if(StringUtils.isNotEmpty(configKey)){
                predicates.add(cbuild.equal(root.get("configKey"), configKey));
            }
            //状态
            if(status != null){
                predicates.add(cbuild.equal(root.get("status"), status));
            }
            //删除标记
            if(delFlag != null){
                predicates.add(cbuild.equal(root.get("delFlag"), delFlag));
            }
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }

//    // 没有发现使用的地方，暂时注释，测试没有问题后删除
//    /**
//     * 封装公共条件
//     * @return
//     */
//    public static Specification<OperationLog> getWhereCriteria(OperationLogListRequest request){
//        String employeeId = request.getEmployeeId();
//        String thirdId = request.getThirdId();
//        String likeOpCode = request.getLikeOpCode();
//
//        return (root, cquery, cbuild) -> {
//            List<Predicate> predicates = new ArrayList<>();
//            //员工编号
//            if(StringUtils.isNotBlank(employeeId)){
//                predicates.add(cbuild.equal(root.get("employeeId"), employeeId.trim()));
//            }
//            //商家编号
//            if(StringUtils.isNotBlank(thirdId)){
//                predicates.add(cbuild.equal(root.get("thirdId"), thirdId.trim()));
//            }
//            //模糊查询，分类路径做为前缀
//            if(StringUtils.isNotEmpty(likeOpCode)){
//                predicates.add(cbuild.like(root.get("opCode"),StringUtil.SQL_LIKE_CHAR.concat(XssUtils.replaceLikeWildcard(likeOpCode)).concat(StringUtil.SQL_LIKE_CHAR)));
//            }
//
//            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
//            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
//        };
//    }
}
