package com.wanmi.sbc.account.api.constant;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by sunkun on 2017/12/6.
 */
public class BaseBankConfiguration {

    public static List<BaseBank> bankList = Lists.newArrayList(
            BaseBank.builder().bankName("支付宝").bankCode("alipay").build(),
            BaseBank.builder().bankName("微信").bankCode("WeChat").build(),
            BaseBank.builder().bankName("安徽省农村信用社").bankCode("ARCU").build(),
            BaseBank.builder().bankName("鞍山银行").bankCode("ASCB").build(),
            BaseBank.builder().bankName("北京银行").bankCode("BJBANK").build(),
            BaseBank.builder().bankName("北京农商行").bankCode("BJRCB").build(),
            BaseBank.builder().bankName("渤海银行").bankCode("BOHAIB").build(),
            BaseBank.builder().bankName("包商银行").bankCode("BSB").build(),
            BaseBank.builder().bankName("保定银行").bankCode("BDCBANK").build(),
            BaseBank.builder().bankName("成都银行").bankCode("CDCB").build(),
            BaseBank.builder().bankName("常熟农商银行").bankCode("CSRCB").build(),
            BaseBank.builder().bankName("重庆三峡银行").bankCode("CCQTGB").build(),
            BaseBank.builder().bankName("长安银行").bankCode("CABANK").build(),
            BaseBank.builder().bankName("长沙银行").bankCode("CSCB").build(),
            BaseBank.builder().bankName("沧州银行").bankCode("BOCZ").build(),
            BaseBank.builder().bankName("朝阳银行").bankCode("BOCY").build(),
            BaseBank.builder().bankName("重庆农村商业银行").bankCode("CRCBANK").build(),
            BaseBank.builder().bankName("重庆银行").bankCode("CQBANK").build(),
            BaseBank.builder().bankName("承德银行").bankCode("BOCD").build(),
            BaseBank.builder().bankName("长春朝阳和润村镇银行").bankCode("CCHRCZYH").build(),
            BaseBank.builder().bankName("长城华西银行").bankCode("DYCB").build(),
            BaseBank.builder().bankName("大连银行").bankCode("DLB").build(),
            BaseBank.builder().bankName("德州银行").bankCode("DZBANK").build(),
            BaseBank.builder().bankName("东营银行").bankCode("DYCCB").build(),
            BaseBank.builder().bankName("东亚银行").bankCode("HKBEA").build(),
            BaseBank.builder().bankName("大连农村商业银行").bankCode("DLRCB").build(),
            BaseBank.builder().bankName("东莞农村商业银行").bankCode("DRCBCL").build(),
            BaseBank.builder().bankName("东莞银行").bankCode("BOD").build(),
            BaseBank.builder().bankName("鄂尔多斯银行").bankCode("ORBANK").build(),
            BaseBank.builder().bankName("富邦华一银行").bankCode("FBBANK").build(),
            BaseBank.builder().bankName("阜新银行").bankCode("FXCB").build(),
            BaseBank.builder().bankName("福建海峡银行").bankCode("FJHXBC").build(),
            BaseBank.builder().bankName("福建省农村信用社联合社").bankCode("FJNX").build(),
            BaseBank.builder().bankName("富滇银行").bankCode("FDB").build(),
            BaseBank.builder().bankName("广发银行").bankCode("GDB").build(),
            BaseBank.builder().bankName("贵阳银行").bankCode("GYCB").build(),
            BaseBank.builder().bankName("桂林银行").bankCode("GLBANK").build(),
            BaseBank.builder().bankName("贵州省农村信用社联合社").bankCode("GZRCU").build(),
            BaseBank.builder().bankName("广西北部湾银行").bankCode("BGB").build(),
            BaseBank.builder().bankName("广东华兴银行").bankCode("GHB").build(),
            BaseBank.builder().bankName("甘肃省农村信用社").bankCode("GSRCU").build(),
            BaseBank.builder().bankName("赣州银行").bankCode("GZB").build(),
            BaseBank.builder().bankName("广东南粤银行").bankCode("NYBANK").build(),
            BaseBank.builder().bankName("甘肃银行").bankCode("GSBANK").build(),
            BaseBank.builder().bankName("广东省农村信用社联合社").bankCode("GDRCC").build(),
            BaseBank.builder().bankName("广州农村商业银行").bankCode("GRCB").build(),
            BaseBank.builder().bankName("贵州银行").bankCode("ZYCBANK").build(),
            BaseBank.builder().bankName("广西壮族自治区农村信用社联合社").bankCode("GXRCU").build(),
            BaseBank.builder().bankName("广州银行").bankCode("GCB").build(),
            BaseBank.builder().bankName("华夏银行").bankCode("HXBANK").build(),
            BaseBank.builder().bankName("湖北省农村信用合作联社").bankCode("HURCB").build(),
            BaseBank.builder().bankName("华融湘江银行").bankCode("HRXJB").build(),
            BaseBank.builder().bankName("湖南省农村信用社").bankCode("HNRCC").build(),
            BaseBank.builder().bankName("河南省农村信用社").bankCode("HNRCU").build(),
            BaseBank.builder().bankName("海口联合农商银行").bankCode("UBCHN").build(),
            BaseBank.builder().bankName("河北银行").bankCode("BHB").build(),
            BaseBank.builder().bankName("河北省农村信用社").bankCode("HBRCU").build(),
            BaseBank.builder().bankName("邯郸银行").bankCode("HDBANK").build(),
            BaseBank.builder().bankName("海南省农村信用社").bankCode("BOHN").build(),
            BaseBank.builder().bankName("汉口银行").bankCode("HKB").build(),
            BaseBank.builder().bankName("湖州银行").bankCode("HZCCB").build(),
            BaseBank.builder().bankName("衡水市商业银行").bankCode("HSBK").build(),
            BaseBank.builder().bankName("黑龙江省农村信用社联合社").bankCode("HLJRCU").build(),
            BaseBank.builder().bankName("恒丰银行").bankCode("EGBANK").build(),
            BaseBank.builder().bankName("韩亚银行").bankCode("HANABANK").build(),
            BaseBank.builder().bankName("韩国企业银行").bankCode("QYBANK").build(),
            BaseBank.builder().bankName("徽商银行").bankCode("HSBANK").build(),
            BaseBank.builder().bankName("哈尔滨银行").bankCode("HRBANK").build(),
            BaseBank.builder().bankName("葫芦岛银行").bankCode("HLDB").build(),
            BaseBank.builder().bankName("杭州银行").bankCode("HZCB").build(),
            BaseBank.builder().bankName("湖商村镇银行").bankCode("HSCZB").build(),
            BaseBank.builder().bankName("湖北银行").bankCode("HBC").build(),
            BaseBank.builder().bankName("交通银行").bankCode("COMM").build(),
            BaseBank.builder().bankName("江苏银行").bankCode("JSBANK").build(),
            BaseBank.builder().bankName("江苏省农村信用社联合社").bankCode("JSRCU").build(),
            BaseBank.builder().bankName("江南农村商业银行").bankCode("CZRCB").build(),
            BaseBank.builder().bankName("吉林省农村信用社联合社").bankCode("JLRCU").build(),
            BaseBank.builder().bankName("江苏太仓农村商业银行").bankCode("TCRCB").build(),
            BaseBank.builder().bankName("晋商银行").bankCode("JSB").build(),
            BaseBank.builder().bankName("晋城银行").bankCode("JINCHB").build(),
            BaseBank.builder().bankName("九江银行").bankCode("JJBANK").build(),
            BaseBank.builder().bankName("嘉兴银行").bankCode("JXBANK").build(),
            BaseBank.builder().bankName("金华银行").bankCode("JHBANK").build(),
            BaseBank.builder().bankName("锦州银行").bankCode("BOJZ").build(),
            BaseBank.builder().bankName("济宁银行").bankCode("JNBANK").build(),
            BaseBank.builder().bankName("吉林银行").bankCode("JLBANK").build(),
            BaseBank.builder().bankName("焦作中旅银行").bankCode("JZCBANK").build(),
            BaseBank.builder().bankName("江西省农村信用社").bankCode("JXRCU").build(),
            BaseBank.builder().bankName("江西银行").bankCode("NCB").build(),
            BaseBank.builder().bankName("晋中银行").bankCode("JZBANK").build(),
            BaseBank.builder().bankName("江苏长江商业银行").bankCode("CJCCB").build(),
            BaseBank.builder().bankName("江苏江阴农村商业银行").bankCode("JRCB").build(),
            BaseBank.builder().bankName("昆山农村商业银行").bankCode("KSRB").build(),
            BaseBank.builder().bankName("昆仑银行").bankCode("KLB").build(),
            BaseBank.builder().bankName("泸州市商业银行").bankCode("LUZBANK").build(),
            BaseBank.builder().bankName("兰州银行").bankCode("LZYH").build(),
            BaseBank.builder().bankName("莱商银行").bankCode("LSBANK").build(),
            BaseBank.builder().bankName("龙江银行").bankCode("DAQINGB").build(),
            BaseBank.builder().bankName("漯河银行").bankCode("LHBANK").build(),
            BaseBank.builder().bankName("辽宁省农村信用社").bankCode("LNRCC").build(),
            BaseBank.builder().bankName("柳州银行").bankCode("LZCCB").build(),
            BaseBank.builder().bankName("临商银行").bankCode("LSBC").build(),
            BaseBank.builder().bankName("联合村镇银行").bankCode("URB").build(),
            BaseBank.builder().bankName("洛阳银行").bankCode("BOL").build(),
            BaseBank.builder().bankName("乐山市商业银行").bankCode("LSCCB").build(),
            BaseBank.builder().bankName("廊坊银行").bankCode("LANGFB").build(),
            BaseBank.builder().bankName("梅县客家村镇银行").bankCode("KJCZYH").build(),
            BaseBank.builder().bankName("绵阳市商业银行").bankCode("MYBANK").build(),
            BaseBank.builder().bankName("内蒙古农村信用社联合社").bankCode("NMGNXS").build(),
            BaseBank.builder().bankName("宁夏银行").bankCode("NXBANK").build(),
            BaseBank.builder().bankName("宁波银行").bankCode("NBBANK").build(),
            BaseBank.builder().bankName("南海农商银行").bankCode("NHB").build(),
            BaseBank.builder().bankName("宁波通商银行").bankCode("NBCBANK").build(),
            BaseBank.builder().bankName("南京银行").bankCode("NJCB").build(),
            BaseBank.builder().bankName("宁夏黄河农村商业银行").bankCode("NXRCU").build(),
            BaseBank.builder().bankName("四川天府银行").bankCode("CGNB").build(),
            BaseBank.builder().bankName("南阳市商业银行").bankCode("BNY").build(),
            BaseBank.builder().bankName("内蒙古银行").bankCode("H3CB").build(),
            BaseBank.builder().bankName("浦发银行").bankCode("SPDB").build(),
            BaseBank.builder().bankName("平安银行").bankCode("SPABANK").build(),
            BaseBank.builder().bankName("攀枝花市商业银行").bankCode("PZHCCB").build(),
            BaseBank.builder().bankName("平顶山银行").bankCode("BOP").build(),
            BaseBank.builder().bankName("齐商银行").bankCode("ZBCB").build(),
            BaseBank.builder().bankName("青岛银行").bankCode("QDCCB").build(),
            BaseBank.builder().bankName("齐鲁银行").bankCode("QLBANK").build(),
            BaseBank.builder().bankName("青海省农村信用社").bankCode("QHRC").build(),
            BaseBank.builder().bankName("泉州银行").bankCode("BOQZ").build(),
            BaseBank.builder().bankName("青海银行").bankCode("BOQH").build(),
            BaseBank.builder().bankName("日照银行").bankCode("RZB").build(),
            BaseBank.builder().bankName("上海银行").bankCode("SHBANK").build(),
            BaseBank.builder().bankName("深圳农村商业银行").bankCode("SRCB").build(),
            BaseBank.builder().bankName("盛京银行").bankCode("SJBANK").build(),
            BaseBank.builder().bankName("山西省农村信用社").bankCode("SXRCU").build(),
            BaseBank.builder().bankName("上海农商银行").bankCode("SHRCB").build(),
            BaseBank.builder().bankName("石嘴山银行").bankCode("SZSBK").build(),
            BaseBank.builder().bankName("四川省农村信用社联合社").bankCode("SCRCU").build(),
            BaseBank.builder().bankName("山东省农村信用社联合社").bankCode("SDRCU").build(),
            BaseBank.builder().bankName("绍兴银行").bankCode("SXCB").build(),
            BaseBank.builder().bankName("顺德农商银行").bankCode("SDEB").build(),
            BaseBank.builder().bankName("上饶银行").bankCode("SRBANK").build(),
            BaseBank.builder().bankName("陕西信合").bankCode("SXRCCU").build(),
            BaseBank.builder().bankName("苏州银行").bankCode("BOSZ").build(),
            BaseBank.builder().bankName("遂宁银行").bankCode("SNCCB").build(),
            BaseBank.builder().bankName("天津银行").bankCode("TCCB").build(),
            BaseBank.builder().bankName("台州银行").bankCode("TZCB").build(),
            BaseBank.builder().bankName("泰安银行").bankCode("TACCB").build(),
            BaseBank.builder().bankName("天津农商银行").bankCode("TRCB").build(),
            BaseBank.builder().bankName("天津滨海农村商业银行").bankCode("TJBHB").build(),
            BaseBank.builder().bankName("潍坊银行").bankCode("BANKWF").build(),
            BaseBank.builder().bankName("外换银行").bankCode("KEB").build(),
            BaseBank.builder().bankName("吴江农村商业银行").bankCode("WJRCB").build(),
            BaseBank.builder().bankName("乌鲁木齐银行").bankCode("URMQCCB").build(),
            BaseBank.builder().bankName("威海市商业银行").bankCode("WHCCB").build(),
            BaseBank.builder().bankName("温州银行").bankCode("WZCB").build(),
            BaseBank.builder().bankName("武汉农村商业银行").bankCode("WHRCB").build(),
            BaseBank.builder().bankName("无锡农村商业银行").bankCode("WRCB").build(),
            BaseBank.builder().bankName("网商银行").bankCode("ANTBANK").build(),
            BaseBank.builder().bankName("兴业银行").bankCode("CIB").build(),
            BaseBank.builder().bankName("西安银行").bankCode("XABANK").build(),
            BaseBank.builder().bankName("厦门国际银行").bankCode("XIB").build(),
            BaseBank.builder().bankName("邢台银行").bankCode("XTB").build(),
            BaseBank.builder().bankName("新疆农村信用社").bankCode("XJRCU").build(),
            BaseBank.builder().bankName("厦门银行").bankCode("XMBANK").build(),
            BaseBank.builder().bankName("新韩银行").bankCode("BOSH").build(),
            BaseBank.builder().bankName("云南红塔银行").bankCode("YXCCB").build(),
            BaseBank.builder().bankName("鄞州银行").bankCode("NBYZ").build(),
            BaseBank.builder().bankName("烟台银行").bankCode("YTBANK").build(),
            BaseBank.builder().bankName("营口沿海银行").bankCode("YKYHCCB").build(),
            BaseBank.builder().bankName("营口银行").bankCode("BOYK").build(),
            BaseBank.builder().bankName("宜宾市商业银行").bankCode("YBCCB").build(),
            BaseBank.builder().bankName("友利银行").bankCode("WOORI").build(),
            BaseBank.builder().bankName("云南省农村信用社").bankCode("YNRCC").build(),
            BaseBank.builder().bankName("中国工商银行").bankCode("ICBC").build(),
            BaseBank.builder().bankName("中国农业银行").bankCode("ABC").build(),
            BaseBank.builder().bankName("中国建设银行").bankCode("CCB").build(),
            BaseBank.builder().bankName("招商银行").bankCode("CMB").build(),
            BaseBank.builder().bankName("中国银行").bankCode("BOC").build(),
            BaseBank.builder().bankName("中国邮政储蓄银行").bankCode("PSBC").build(),
            BaseBank.builder().bankName("中信银行").bankCode("CITIC").build(),
            BaseBank.builder().bankName("中国民生银行").bankCode("CMBC").build(),
            BaseBank.builder().bankName("中国光大银行").bankCode("CEB").build(),
            BaseBank.builder().bankName("枣庄银行").bankCode("ZZYH").build(),
            BaseBank.builder().bankName("珠海华润银行").bankCode("RBOZ").build(),
            BaseBank.builder().bankName("张家港农村商业银行").bankCode("ZRCBANK").build(),
            BaseBank.builder().bankName("浙江省农村信用社联合社").bankCode("ZJNX").build(),
            BaseBank.builder().bankName("自贡银行").bankCode("ZGCCB").build(),
            BaseBank.builder().bankName("浙江民泰商业银行").bankCode("MTBANK").build(),
            BaseBank.builder().bankName("浙商银行").bankCode("CZBANK").build(),
            BaseBank.builder().bankName("郑州银行").bankCode("ZZBANK").build(),
            BaseBank.builder().bankName("中原银行").bankCode("ZYB").build(),
            BaseBank.builder().bankName("张家口银行").bankCode("ZJKCCB").build(),
            BaseBank.builder().bankName("浙江稠州商业银行").bankCode("CZCB").build(),
            BaseBank.builder().bankName("浙江泰隆商业银行").bankCode("ZJTLCB").build()
    );
}
