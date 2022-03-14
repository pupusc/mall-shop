//package com.wanmi.sbc.order;
//
//import com.wanmi.sbc.order.bean.enums.ReturnReason;
//import com.wanmi.sbc.order.bean.enums.ReturnWay;
//import com.wanmi.sbc.order.returnorder.model.root.ReturnOrder;
//import org.apache.commons.lang3.builder.DiffResult;
//import org.junit.Test;
//
///**
// * Created by jinwei on 27/4/2017.
// */
//public class ReturnOrderTest {
//
//    @Test
//    public void diffOneTest() {
//
//        ReturnOrder oldReturn = new ReturnOrder();
//        ReturnOrder newReturn = new ReturnOrder();
//
//        oldReturn.setReturnReason(ReturnReason.BADGOODS);
//        newReturn.setReturnReason(ReturnReason.ERRORGOODS);
//
//        DiffResult diff = oldReturn.diff(newReturn);
//        ReturnReason left = (ReturnReason) (diff.getDiffs().get(0).getLeft());
//        ReturnReason right = (ReturnReason) (diff.getDiffs().get(0).getRight());
//        System.out.println(String.format("退货原因由 %s 改为 %s", left.getDesc(), right.getDesc()));
//    }
//
//    @Test
//    public void diffAllTest() {
//        ReturnOrder oldReturn = new ReturnOrder();
//        ReturnOrder newReturn = new ReturnOrder();
//        //ReturnReason
//        oldReturn.setReturnReason(ReturnReason.BADGOODS);
//        newReturn.setReturnReason(ReturnReason.BADGOODS);
//        //Retu
//        oldReturn.setReturnWay(ReturnWay.EXPRESS);
//        newReturn.setReturnWay(ReturnWay.OTHER);
//        //ReturnPrice
////        oldReturn.setReturnPrice(new ReturnPrice(false, new BigDecimal(10), new BigDecimal(20), null, null));
////        newReturn.setReturnPrice(new ReturnPrice(false, new BigDecimal(10), new BigDecimal(40), null, null));
//        //ReturnItems
////        List<ReturnItem> oldItems = Arrays.asList(
////                new ReturnItem("P123456", "hello", null, null, new BigDecimal(10),null,null, 10, "", "", null),
////                new ReturnItem("P123457", null, null, null, null,null,null, 11, "", "", null)
////        );
////        List<ReturnItem> newItems = Arrays.asList(
////                new ReturnItem("P123456", null, null, null, null,null,null, 12, "", "", null),
////                new ReturnItem("P123457", null, null, null, null,null,null, 13, "", "", null)
////        );
////        oldReturn.setReturnItems(oldItems);
////        newReturn.setReturnItems(newItems);
//
//        DiffResult diff = oldReturn.diff(newReturn);
//        System.out.println(diff.getDiffs());
//
//        oldReturn.buildDiffStr(newReturn).forEach(System.out::println);
//    }
//
//}
