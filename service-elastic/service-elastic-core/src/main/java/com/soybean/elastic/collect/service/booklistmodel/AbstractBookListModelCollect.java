//package com.soybean.elastic.collect.service.booklistmodel;
//
//
//
//import com.soybean.elastic.collect.service.AbstractCollect;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
///**
// * Description:
// * Company    : 上海黄豆网络科技有限公司
// * Author     : duanlongshan@dushu365.com
// * Date       : 2022/6/1 2:51 下午
// * Modify     : 修改日期          修改人员        修改说明          JIRA编号
// ********************************************************************/
//public abstract class AbstractBookListModelCollect extends AbstractCollect {
//
//
//
//    private LocalDateTime beforeCollect() {
//        System.out.println("AbstractBookListModelCollect.beforeCollect");
//        //获取redis中的数据
//        return LocalDateTime.now();
//    }
//
//
////    public abstract <F> List<F> collect(LocalDateTime lastCollectTime, LocalDateTime now);
//
//    /**
//     * 初始化数据
//     * @param <F>
//     * @return
//     */
//    public  <F> List<F> incrementalLoads() {
//        LocalDateTime now = beforeCollect();
////        LocalDateTime lastSynTime = LocalDateTime.of(2022,12,12,1,1,0);
////        List<F> result = collect(lastSynTime, now);
////        afterCollect(now);
////        return result;
//        return null;
//    }
//
//
//    private void afterCollect(LocalDateTime now){
//        //重制redis数据
//        System.out.println("AbstractBookListModelCollect.afterCollect" + now);
//    }
//
//}
