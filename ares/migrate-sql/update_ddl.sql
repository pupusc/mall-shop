--修改扩大Es配置elasticsearch.yml中的index.query.bool.max_clause_count: 改为50000

-- 商品日报表，修改主键
truncate TABLE goods_day;
ALTER TABLE `goods_day`
MODIFY COLUMN `ID`  integer(20) NOT NULL AUTO_INCREMENT COMMENT '自增长字段' FIRST ;

ALTER TABLE customer_month ADD STAT_DATE DATE NULL COMMENT '生成日期';
ALTER TABLE customer_month MODIFY ID BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '标识';

ALTER TABLE customer_level_month ADD STAT_DATE DATE NULL COMMENT '月报表日期';
ALTER TABLE customer_level_month MODIFY ID BIGINT(20) NOT NULL COMMENT '标识';


-- ----------------------------
-- Table structure for export_data_request
-- ----------------------------
DROP TABLE IF EXISTS `export_data_request`;
CREATE TABLE `export_data_request` (
  `ID` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '导出主键',
  `USER_ID` varchar(50) DEFAULT NULL COMMENT '用户标识',
  `COMPANY_INFO_ID` bigint(11) DEFAULT NULL COMMENT '商家标识',
  `BEGIN_DATE` date DEFAULT NULL COMMENT '开始日期',
  `END_DATE` date DEFAULT NULL COMMENT '截止日期',
  `TYPE_CD` int(4) DEFAULT NULL COMMENT '导出报表类别',
  `EXPORT_STATUS` tinyint(2) DEFAULT NULL COMMENT '导出状态',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '发起导出请求时间',
  `FINISH_TIME` datetime DEFAULT NULL COMMENT '文件成功生成时间/错误时间',
  `FILE_PATH` text COMMENT '导出文件全路径',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='导出报表请求表';



-- 修改会有报表字段
ALTER TABLE customer_level_day ADD CUSTOMER_COUNT BIGINT(10) DEFAULT 0 NOT NULL COMMENT '会员数';

-- 为商品日报表字段增加字段和指定分区
ALTER TABLE `goods_day`
DROP PRIMARY KEY,
ADD PRIMARY KEY (`ID`, `STAT_DATE`);
ALTER TABLE `goods_day` PARTITION BY RANGE (TO_DAYS(STAT_DATE))(
	PARTITION p201711
	VALUES
		LESS THAN (737029) ENGINE = INNODB
);

-- 为商品月报表字段增加字段和指定分区
ALTER TABLE `goods_month`
DROP PRIMARY KEY,
ADD PRIMARY KEY (`ID`, `STAT_MONTH`);

ALTER TABLE `goods_month` PARTITION BY RANGE (STAT_MONTH)(
	PARTITION p201711
	VALUES
		LESS THAN (201712) ENGINE = INNODB
);

--调整商品分类的字段
ALTER TABLE `goods_cate_day` MODIFY COLUMN `ID`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '由CATE_ID+SHOP_ID+标识(0:今日/1:昨日)组成' FIRST;

ALTER TABLE `goods_cate_recent_seven` DROP PRIMARY KEY,ADD PRIMARY KEY (`CATE_ID`, `SHOP_ID`);

ALTER TABLE `goods_cate_recent_thirty` DROP PRIMARY KEY,ADD PRIMARY KEY (`CATE_ID`, `SHOP_ID`);

ALTER TABLE `goods_cate_month` MODIFY COLUMN `ID`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '由CATE_ID+SHOP_ID+YYYYMM组成' FIRST ;


--调整商品品牌的字段
ALTER TABLE `goods_brand_day` MODIFY COLUMN `ID`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '由BRAND_ID+SHOP_ID+标识(0:今日/1:昨日)组成' FIRST;

ALTER TABLE `goods_brand_recent_seven` DROP PRIMARY KEY,ADD PRIMARY KEY (`BRAND_ID`, `SHOP_ID`);

ALTER TABLE `goods_brand_recent_thirty` DROP PRIMARY KEY,ADD PRIMARY KEY (`BRAND_ID`, `SHOP_ID`);

ALTER TABLE `goods_brand_month` MODIFY COLUMN `ID`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '由BRAND_ID+SHOP_ID+YYYYMM组成' FIRST ;

-- 新增客户表
CREATE TABLE customer
(
  id             VARCHAR(32)  NOT NULL
    PRIMARY KEY,
  operation_date DATE     NULL
  COMMENT '操作日期',
  send_time      DATETIME     NULL
  COMMENT '发送时间',
  receive_time   DATETIME     NULL
  COMMENT '接收时间',
  del_flag       TINYINT      NULL
  COMMENT '删除状态',
  account        VARCHAR(20)  NULL
  COMMENT '会员登录账号|手机号',
  level_id       VARCHAR(20)  NULL
  COMMENT '客户级别',
  name           VARCHAR(128) NULL
  COMMENT '会员名称',
  check_state    VARCHAR(10)      NULL
  COMMENT '账户的审核状态 0:待审核 1:已审核通过 2:审核未通过',
  check_date     DATETIME     NULL
  COMMENT '审核日期',
  city_id        VARCHAR(20)  NULL
  COMMENT '市id',
  is_boss        TINYINT  NULL
  COMMENT '是否boss添加 true:boss添加 false:注册',
  employee_id    VARCHAR(32)  NULL
  COMMENT '员工信息ID',
  company_id     VARCHAR(32)  NULL
  COMMENT '商家id',
  bind_date      DATE     NULL
  COMMENT '绑定日期'
)
  COMMENT '新增客户表';

-- 等级基础数据表
CREATE TABLE customer_level
(
  id             VARCHAR(32)   NOT NULL
    PRIMARY KEY,
  operation_date DATE      NULL
  COMMENT '操作日期',
  send_time      DATETIME      NULL
  COMMENT '发送时间',
  receive_time   DATETIME      NULL
  COMMENT '接收时间',
  del_flag       TINYINT   NULL
  COMMENT '删除状态',
  name           VARCHAR(45)   NULL
  COMMENT '客户等级名称',
  discount       DECIMAL(5, 2) NULL
  COMMENT '折扣率',
  company_id     VARCHAR(32)   NULL
  COMMENT '商家id',
  is_default     VARCHAR(10)   NULL
  COMMENT '是否默认'
)
  COMMENT '等级基础数据表';

-- 客户等级关系表
CREATE TABLE customer_and_level
(
  id                VARCHAR(32) NOT NULL
    PRIMARY KEY,
  operation_date    DATE    NULL
  COMMENT '操作日期',
  send_time         DATETIME    NULL
  COMMENT '发送时间',
  receive_time      DATETIME    NULL
  COMMENT '接收时间',
  del_flag          VARCHAR(10) NULL
  COMMENT '删除状态',
  customer_id       VARCHAR(32) NULL
  COMMENT '用户标识',
  store_id          BIGINT      NULL
  COMMENT '店铺标识',
  company_info_id   VARCHAR(32) NULL
  COMMENT '商家id',
  customer_level_id BIGINT      NULL
  COMMENT '客户等级标识',
  employee_id       VARCHAR(32) NULL
  COMMENT '客户等级标识',
  customer_type     VARCHAR(10)     NULL
  COMMENT '关系类型(0:平台绑定客户,1:商家客户)'
)
  COMMENT '客户等级关系表';

-- 新增业务员表
CREATE TABLE employee
(
  id              VARCHAR(32)  NOT NULL
    PRIMARY KEY,
  operation_date  DATE     NULL
  COMMENT '操作日期',
  send_time       DATETIME     NULL
  COMMENT '发送时间',
  receive_time    DATETIME     NULL
  COMMENT '接收时间',
  del_flag        VARCHAR(10)  NULL
  COMMENT '删除状态',
  is_employee TINYINT   NULL
  COMMENT '是否业务员 0:是业务员 1:不是业务员',
  name      VARCHAR(45) NULL
  COMMENT '名称',
  mobile   VARCHAR(50)  NULL
  COMMENT '电话',
  company_id     VARCHAR(32)      NULL
  COMMENT '商家id'
)
  COMMENT '业务员';


-- 新增店铺表
CREATE TABLE store
(
  id              VARCHAR(32)  NOT NULL
    PRIMARY KEY,
  operation_date  DATE         NULL
  COMMENT '操作日期',
  send_time       DATETIME     NULL
  COMMENT '发送时间',
  receive_time    DATETIME     NULL
  COMMENT '接收时间',
  del_flag        VARCHAR(10)  NULL
  COMMENT '删除状态',
  company_info_id BIGINT(11)   NULL
  COMMENT '公司信息ID',
  store_name      VARCHAR(150) NULL
  COMMENT '店铺名称',
  supplier_name   VARCHAR(50)  NULL
  COMMENT '商家名称',
  store_state     VARCHAR(10)      NULL
  COMMENT '店铺状态 0、开启 1、关店'
)
  COMMENT '店铺';

-- 新增店铺分类表
CREATE TABLE store_cate
(
  id              VARCHAR(32) NOT NULL
    PRIMARY KEY,
  operation_date  DATE        NULL
  COMMENT '操作日期',
  send_time       DATETIME    NULL
  COMMENT '发送时间',
  receive_time    DATETIME    NULL
  COMMENT '接收时间',
  del_flag        VARCHAR(10) NULL
  COMMENT '删除状态',
  store_id        BIGINT      NULL
  COMMENT '店铺主键',
  company_info_id BIGINT      NULL
  COMMENT '商家Id',
  cate_name       VARCHAR(45) NULL
  COMMENT '分类名称',
  cate_parent_id  BIGINT      NULL
  COMMENT '父分类ID',
  cate_path       VARCHAR(50) NOT NULL
  COMMENT '分类层次路径,例1|01|001',
  cate_grade      TINYINT     NOT NULL
  COMMENT '分类层级',
  sort            INT         NULL
  COMMENT '排序',
  is_default      VARCHAR(10)     NOT NULL
  COMMENT '是否默认,0:否1:是'
)
  COMMENT '店铺商品分类';


/*==============================================================*/
/* Table: 每日店铺分类统计表                                  */
/*==============================================================*/
create table GOODS_STORE_CATE_DAY
(
   ID                   varchar(50) not null comment '由CATE_ID+SHOP_ID+标识(0:今日/1:昨日)组成',
   CATE_ID              varchar(50) not null,
   STAT_DATE            date not null comment '日期',
   ORDER_COUNT          decimal(20) not null comment '下单笔数',
   ORDER_MONEY          decimal(12,2) not null comment '下单金额',
   ORDER_NUM            decimal(20) not null comment '下单件数',
   PAY_COUNT            decimal(20) not null comment '付款订单数',
   PAY_NUM              decimal(20) not null comment '付款件数',
   PAY_MONEY            decimal(12,2) not null comment '付款金额',
   REFUND_COUNT         decimal(20) not null comment '退货笔数',
   REFUND_MONEY         decimal(12,2) not null comment '退货金额',
   REFUND_NUM           decimal(20) not null comment '退货件数',
   SHOP_ID              varchar(50) not null comment '店铺标识',
   CREATE_TM            datetime not null comment '创建时间',
   IS_LEAF              tinyint not null,
   primary key (ID)
);

alter table GOODS_STORE_CATE_DAY comment '每日店铺分类统计表';


/*==============================================================*/
/* Table: 最近7天店铺分类统计表                         */
/*==============================================================*/
create table GOODS_STORE_CATE_RECENT_SEVEN
(
   CATE_ID              varchar(50) not null comment '商品分类ID',
   ORDER_COUNT          decimal(20) not null comment '下单笔数',
   ORDER_MONEY          decimal(12,2) not null comment '下单金额',
   ORDER_NUM            decimal(20) not null comment '下单件数',
   PAY_COUNT            decimal(20) not null comment '付款订单数',
   PAY_NUM              decimal(20) not null comment '付款件数',
   PAY_MONEY            decimal(12,2) not null comment '付款金额',
   REFUND_COUNT         decimal(20) not null comment '退货笔数',
   REFUND_MONEY         decimal(12,2) not null comment '退货金额',
   REFUND_NUM           decimal(20) not null comment '退货件数',
   SHOP_ID              varchar(50) not null comment '店铺标识',
   CREATE_TM            datetime not null comment '创建时间',
   IS_LEAF              tinyint not null,
   primary key (CATE_ID, SHOP_ID)
);

alter table GOODS_STORE_CATE_RECENT_SEVEN comment '最近7天店铺分类统计表';

/*==============================================================*/
/* Table: 最近30天店铺分类统计表                        */
/*==============================================================*/
create table GOODS_STORE_CATE_RECENT_THIRTY
(
   CATE_ID              varchar(50) not null comment '商品分类ID',
   ORDER_COUNT          decimal(20) not null comment '下单笔数',
   ORDER_MONEY          decimal(12,2) not null comment '下单金额',
   ORDER_NUM            decimal(20) not null comment '下单件数',
   PAY_COUNT            decimal(20) not null comment '付款订单数',
   PAY_NUM              decimal(20) not null comment '付款件数',
   PAY_MONEY            decimal(12,2) not null comment '付款金额',
   REFUND_COUNT         decimal(20) not null comment '退货笔数',
   REFUND_MONEY         decimal(12,2) not null comment '退货金额',
   REFUND_NUM           decimal(20) not null comment '退货件数',
   SHOP_ID              varchar(50) not null comment '店铺标识',
   CREATE_TM            datetime not null comment '创建时间',
   IS_LEAF              tinyint not null,
   primary key (CATE_ID, SHOP_ID)
);

alter table GOODS_STORE_CATE_RECENT_THIRTY comment '最近30天店铺分类统计表';

/*==============================================================*/
/* Table: 每月店铺分类统计表                                */
/*==============================================================*/
create table GOODS_STORE_CATE_MONTH
(
   ID                   varchar(50) not null comment '由CATE_ID+SHOP_ID+YYYYMM组成',
   CATE_ID              varchar(50) not null,
   STAT_MONTH           integer(6) not null comment '年月，格式为YYYYMM',
   ORDER_COUNT          decimal(20) not null comment '下单笔数',
   ORDER_MONEY          decimal(12,2) not null comment '下单金额',
   ORDER_NUM            decimal(20) not null comment '下单件数',
   PAY_COUNT            decimal(20) not null comment '付款订单数',
   PAY_NUM              decimal(20) not null comment '付款件数',
   PAY_MONEY            decimal(12,2) not null comment '付款金额',
   REFUND_COUNT         decimal(20) not null comment '退货笔数',
   REFUND_MONEY         decimal(12,2) not null comment '退货金额',
   REFUND_NUM           decimal(20) not null comment '退货件数',
   SHOP_ID              varchar(50) not null comment '店铺标识',
   CREATE_TM            datetime not null comment '创建时间',
   IS_LEAF              tinyint not null,
   primary key (ID)
);

alter table GOODS_STORE_CATE_MONTH comment '每月店铺分类统计表';

-- 为商品概览表增加已审核商品数、销售中商品数
ALTER TABLE `goods_total`
ADD COLUMN `CHECKED_TOTAL`  int(11) NOT NULL AFTER `SHOP_ID`,
ADD COLUMN `SALE_TOTAL`  int(11) NOT NULL AFTER `CHECKED_TOTAL`;

--为ES服务增加murmur3插件
sudo bin/plugin install mapper-murmur3

--添加bindDate供报表使用
ALTER TABLE customer_and_level ADD bind_time DATE NULL COMMENT '商家和平台的绑定时间';


-- 扩展金额为18位整数，2位小数
ALTER TABLE `goods_brand_day`
MODIFY COLUMN `ORDER_MONEY`  decimal(20,2) NOT NULL COMMENT '下单金额' AFTER `ORDER_COUNT`,
MODIFY COLUMN `PAY_MONEY`  decimal(20,2) NOT NULL COMMENT '付款金额' AFTER `PAY_NUM`,
MODIFY COLUMN `REFUND_MONEY`  decimal(20,2) NOT NULL COMMENT '退货金额' AFTER `REFUND_COUNT`;

ALTER TABLE `goods_brand_month`
MODIFY COLUMN `ORDER_MONEY`  decimal(20,2) NOT NULL COMMENT '下单金额' AFTER `ORDER_COUNT`,
MODIFY COLUMN `PAY_MONEY`  decimal(20,2) NOT NULL COMMENT '付款金额' AFTER `PAY_NUM`,
MODIFY COLUMN `REFUND_MONEY`  decimal(20,2) NOT NULL COMMENT '退货金额' AFTER `REFUND_COUNT`;

ALTER TABLE `goods_brand_recent_seven`
MODIFY COLUMN `ORDER_MONEY`  decimal(20,2) NOT NULL COMMENT '下单金额' AFTER `ORDER_COUNT`,
MODIFY COLUMN `PAY_MONEY`  decimal(20,2) NOT NULL COMMENT '付款金额' AFTER `PAY_NUM`,
MODIFY COLUMN `REFUND_MONEY`  decimal(20,2) NOT NULL COMMENT '退货金额' AFTER `REFUND_COUNT`;

ALTER TABLE `goods_brand_recent_thirty`
MODIFY COLUMN `ORDER_MONEY`  decimal(20,2) NOT NULL COMMENT '下单金额' AFTER `ORDER_COUNT`,
MODIFY COLUMN `PAY_MONEY`  decimal(20,2) NOT NULL COMMENT '付款金额' AFTER `PAY_NUM`,
MODIFY COLUMN `REFUND_MONEY`  decimal(20,2) NOT NULL COMMENT '退货金额' AFTER `REFUND_COUNT`;

ALTER TABLE `goods_cate_day`
MODIFY COLUMN `ORDER_MONEY`  decimal(20,2) NOT NULL COMMENT '下单金额' AFTER `ORDER_COUNT`,
MODIFY COLUMN `PAY_MONEY`  decimal(20,2) NOT NULL COMMENT '付款金额' AFTER `PAY_NUM`,
MODIFY COLUMN `REFUND_MONEY`  decimal(20,2) NOT NULL COMMENT '退货金额' AFTER `REFUND_COUNT`;

ALTER TABLE `goods_cate_month`
MODIFY COLUMN `ORDER_MONEY`  decimal(20,2) NOT NULL COMMENT '下单金额' AFTER `ORDER_COUNT`,
MODIFY COLUMN `PAY_MONEY`  decimal(20,2) NOT NULL COMMENT '付款金额' AFTER `PAY_NUM`,
MODIFY COLUMN `REFUND_MONEY`  decimal(20,2) NOT NULL COMMENT '退货金额' AFTER `REFUND_COUNT`;

ALTER TABLE `goods_cate_recent_seven`
MODIFY COLUMN `ORDER_MONEY`  decimal(20,2) NOT NULL COMMENT '下单金额' AFTER `ORDER_COUNT`,
MODIFY COLUMN `PAY_MONEY`  decimal(20,2) NOT NULL COMMENT '付款金额' AFTER `PAY_NUM`,
MODIFY COLUMN `REFUND_MONEY`  decimal(20,2) NOT NULL COMMENT '退货金额' AFTER `REFUND_COUNT`;

ALTER TABLE `goods_cate_recent_thirty`
MODIFY COLUMN `ORDER_MONEY`  decimal(20,2) NOT NULL COMMENT '下单金额' AFTER `ORDER_COUNT`,
MODIFY COLUMN `PAY_MONEY`  decimal(20,2) NOT NULL COMMENT '付款金额' AFTER `PAY_NUM`,
MODIFY COLUMN `REFUND_MONEY`  decimal(20,2) NOT NULL COMMENT '退货金额' AFTER `REFUND_COUNT`;

ALTER TABLE `goods_day`
MODIFY COLUMN `ORDER_MONEY`  decimal(20,2) NOT NULL COMMENT '下单金额' AFTER `ORDER_COUNT`,
MODIFY COLUMN `PAY_MONEY`  decimal(20,2) NOT NULL COMMENT '付款金额' AFTER `PAY_NUM`,
MODIFY COLUMN `REFUND_MONEY`  decimal(20,2) NOT NULL COMMENT '退货金额' AFTER `REFUND_COUNT`;

ALTER TABLE `goods_month`
MODIFY COLUMN `ORDER_MONEY`  decimal(20,2) NOT NULL COMMENT '下单金额' AFTER `ORDER_COUNT`,
MODIFY COLUMN `PAY_MONEY`  decimal(20,2) NOT NULL COMMENT '付款金额' AFTER `PAY_NUM`,
MODIFY COLUMN `REFUND_MONEY`  decimal(20,2) NOT NULL COMMENT '退货金额' AFTER `REFUND_COUNT`;

ALTER TABLE `goods_recent_seven`
MODIFY COLUMN `ORDER_MONEY`  decimal(20,2) NOT NULL COMMENT '下单金额' AFTER `ORDER_COUNT`,
MODIFY COLUMN `PAY_MONEY`  decimal(20,2) NOT NULL COMMENT '付款金额' AFTER `PAY_NUM`,
MODIFY COLUMN `REFUND_MONEY`  decimal(20,2) NOT NULL COMMENT '退货金额' AFTER `REFUND_COUNT`;

ALTER TABLE `goods_recent_thirty`
MODIFY COLUMN `ORDER_MONEY`  decimal(20,2) NOT NULL COMMENT '下单金额' AFTER `ORDER_COUNT`,
MODIFY COLUMN `PAY_MONEY`  decimal(20,2) NOT NULL COMMENT '付款金额' AFTER `PAY_NUM`,
MODIFY COLUMN `REFUND_MONEY`  decimal(20,2) NOT NULL COMMENT '退货金额' AFTER `REFUND_COUNT`;

ALTER TABLE `goods_store_cate_day`
MODIFY COLUMN `ORDER_MONEY`  decimal(20,2) NOT NULL COMMENT '下单金额' AFTER `ORDER_COUNT`,
MODIFY COLUMN `PAY_MONEY`  decimal(20,2) NOT NULL COMMENT '付款金额' AFTER `PAY_NUM`,
MODIFY COLUMN `REFUND_MONEY`  decimal(20,2) NOT NULL COMMENT '退货金额' AFTER `REFUND_COUNT`;

ALTER TABLE `goods_store_cate_month`
MODIFY COLUMN `ORDER_MONEY`  decimal(20,2) NOT NULL COMMENT '下单金额' AFTER `ORDER_COUNT`,
MODIFY COLUMN `PAY_MONEY`  decimal(20,2) NOT NULL COMMENT '付款金额' AFTER `PAY_NUM`,
MODIFY COLUMN `REFUND_MONEY`  decimal(20,2) NOT NULL COMMENT '退货金额' AFTER `REFUND_COUNT`;

ALTER TABLE `goods_store_cate_recent_seven`
MODIFY COLUMN `ORDER_MONEY`  decimal(20,2) NOT NULL COMMENT '下单金额' AFTER `ORDER_COUNT`,
MODIFY COLUMN `PAY_MONEY`  decimal(20,2) NOT NULL COMMENT '付款金额' AFTER `PAY_NUM`,
MODIFY COLUMN `REFUND_MONEY`  decimal(20,2) NOT NULL COMMENT '退货金额' AFTER `REFUND_COUNT`;

ALTER TABLE `goods_store_cate_recent_thirty`
MODIFY COLUMN `ORDER_MONEY`  decimal(20,2) NOT NULL COMMENT '下单金额' AFTER `ORDER_COUNT`,
MODIFY COLUMN `PAY_MONEY`  decimal(20,2) NOT NULL COMMENT '付款金额' AFTER `PAY_NUM`,
MODIFY COLUMN `REFUND_MONEY`  decimal(20,2) NOT NULL COMMENT '退货金额' AFTER `REFUND_COUNT`;

ALTER TABLE `trade_day`
MODIFY COLUMN `ORDER_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '下单金额' AFTER `ORDER_USER_NUM`,
MODIFY COLUMN `PAY_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '付款金额' AFTER `PAY_USER_NUM`,
MODIFY COLUMN `USER_PER_PRICE`  decimal(20,2) NULL DEFAULT NULL COMMENT '客单价' AFTER `ALL_CONVERSION`,
MODIFY COLUMN `ORDER_PER_PRICE`  decimal(20,2) NULL DEFAULT NULL COMMENT '笔单价' AFTER `USER_PER_PRICE`,
MODIFY COLUMN `REFUND_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '退单金额' AFTER `REFUND_USER_NUM`;

ALTER TABLE `customer_day`
MODIFY COLUMN `ORDER_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '下单金额' AFTER `ORDER_NUM`,
MODIFY COLUMN `PAY_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '付款金额' AFTER `PAY_GOODS_NUM`,
MODIFY COLUMN `ORDER_PER_PRICE`  decimal(20,2) NULL DEFAULT NULL COMMENT '笔单价' AFTER `PAY_MONEY`,
MODIFY COLUMN `REFUND_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '退单金额' AFTER `REFUND_NUM`;

ALTER TABLE `customer_level_day`
MODIFY COLUMN `ORDER_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '下单金额' AFTER `ORDER_NUM`,
MODIFY COLUMN `PAY_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '付款金额' AFTER `PAY_GOODS_NUM`,
MODIFY COLUMN `USER_PER_PRICE_THIRTY`  decimal(20,2) NULL DEFAULT NULL COMMENT '客单价' AFTER `PAY_MONEY`,
MODIFY COLUMN `ORDER_PER_PRICE`  decimal(20,2) NULL DEFAULT NULL COMMENT '笔单价' AFTER `USER_PER_PRICE_THIRTY`,
MODIFY COLUMN `REFUND_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '退单金额' AFTER `REFUND_NUM`;

ALTER TABLE `customer_level_month`
MODIFY COLUMN `ORDER_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '下单金额' AFTER `ORDER_NUM`,
MODIFY COLUMN `PAY_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '付款金额' AFTER `PAY_GOODS_NUM`,
MODIFY COLUMN `USER_PER_PRICE_THIRTY`  decimal(20,2) NULL DEFAULT NULL COMMENT '客单价' AFTER `PAY_MONEY`,
MODIFY COLUMN `ORDER_PER_PRICE`  decimal(20,2) NULL DEFAULT NULL COMMENT '笔单价' AFTER `USER_PER_PRICE_THIRTY`,
MODIFY COLUMN `REFUND_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '退单金额' AFTER `REFUND_NUM`;

ALTER TABLE `customer_level_recent_seven`
MODIFY COLUMN `ORDER_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '下单金额' AFTER `ORDER_NUM`,
MODIFY COLUMN `PAY_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '付款金额' AFTER `PAY_GOODS_NUM`,
MODIFY COLUMN `USER_PER_PRICE_THIRTY`  decimal(20,2) NULL DEFAULT NULL COMMENT '客单价' AFTER `PAY_MONEY`,
MODIFY COLUMN `ORDER_PER_PRICE`  decimal(20,2) NULL DEFAULT NULL COMMENT '笔单价' AFTER `USER_PER_PRICE_THIRTY`,
MODIFY COLUMN `REFUND_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '退单金额' AFTER `REFUND_NUM`;

ALTER TABLE `customer_level_recent_thirty`
MODIFY COLUMN `ORDER_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '下单金额' AFTER `ORDER_NUM`,
MODIFY COLUMN `PAY_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '付款金额' AFTER `PAY_GOODS_NUM`,
MODIFY COLUMN `USER_PER_PRICE_THIRTY`  decimal(20,2) NULL DEFAULT NULL COMMENT '客单价' AFTER `PAY_MONEY`,
MODIFY COLUMN `ORDER_PER_PRICE`  decimal(20,2) NULL DEFAULT NULL COMMENT '笔单价' AFTER `USER_PER_PRICE_THIRTY`,
MODIFY COLUMN `REFUND_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '退单金额' AFTER `REFUND_NUM`;


ALTER TABLE `customer_month`
MODIFY COLUMN `ORDER_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '下单金额' AFTER `ORDER_NUM`,
MODIFY COLUMN `PAY_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '付款金额' AFTER `PAY_GOODS_NUM`,
MODIFY COLUMN `ORDER_PER_PRICE`  decimal(20,2) NULL DEFAULT NULL COMMENT '笔单价' AFTER `PAY_MONEY`,
MODIFY COLUMN `REFUND_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '退单金额' AFTER `REFUND_NUM`;

ALTER TABLE `customer_recent_seven`
MODIFY COLUMN `ORDER_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '下单金额' AFTER `ORDER_NUM`,
MODIFY COLUMN `PAY_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '付款金额' AFTER `PAY_GOODS_NUM`,
MODIFY COLUMN `ORDER_PER_PRICE`  decimal(20,2) NULL DEFAULT NULL COMMENT '笔单价' AFTER `PAY_MONEY`,
MODIFY COLUMN `REFUND_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '退单金额' AFTER `REFUND_NUM`;

ALTER TABLE `customer_recent_thirty`
MODIFY COLUMN `ORDER_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '下单金额' AFTER `ORDER_NUM`,
MODIFY COLUMN `PAY_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '付款金额' AFTER `PAY_GOODS_NUM`,
MODIFY COLUMN `ORDER_PER_PRICE`  decimal(20,2) NULL DEFAULT NULL COMMENT '笔单价' AFTER `PAY_MONEY`,
MODIFY COLUMN `REFUND_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '退单金额' AFTER `REFUND_NUM`;

ALTER TABLE `customer_recent_thirty`
MODIFY COLUMN `ORDER_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '下单金额' AFTER `ORDER_NUM`,
MODIFY COLUMN `PAY_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '付款金额' AFTER `PAY_GOODS_NUM`,
MODIFY COLUMN `ORDER_PER_PRICE`  decimal(20,2) NULL DEFAULT NULL COMMENT '笔单价' AFTER `PAY_MONEY`,
MODIFY COLUMN `REFUND_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '退单金额' AFTER `REFUND_NUM`;

ALTER TABLE `customer_region_day`
MODIFY COLUMN `ORDER_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '下单金额' AFTER `ORDER_NUM`,
MODIFY COLUMN `PAY_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '付款金额' AFTER `PAY_GOODS_NUM`,
MODIFY COLUMN `USER_PER_PRICE_THIRTY`  decimal(20,2) NULL DEFAULT NULL COMMENT '客单价' AFTER `PAY_MONEY`,
MODIFY COLUMN `ORDER_PER_PRICE`  decimal(20,2) NULL DEFAULT NULL COMMENT '笔单价' AFTER `USER_PER_PRICE_THIRTY`,
MODIFY COLUMN `REFUND_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '退单金额' AFTER `REFUND_NUM`;

ALTER TABLE `customer_region_month`
MODIFY COLUMN `ORDER_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '下单金额' AFTER `ORDER_NUM`,
MODIFY COLUMN `PAY_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '付款金额' AFTER `PAY_GOODS_NUM`,
MODIFY COLUMN `USER_PER_PRICE_THIRTY`  decimal(20,2) NULL DEFAULT NULL COMMENT '客单价' AFTER `PAY_MONEY`,
MODIFY COLUMN `ORDER_PER_PRICE`  decimal(20,2) NULL DEFAULT NULL COMMENT '笔单价' AFTER `USER_PER_PRICE_THIRTY`,
MODIFY COLUMN `REFUND_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '退单金额' AFTER `REFUND_NUM`;

ALTER TABLE `customer_region_recent_seven`
MODIFY COLUMN `ORDER_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '下单金额' AFTER `ORDER_NUM`,
MODIFY COLUMN `PAY_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '付款金额' AFTER `PAY_GOODS_NUM`,
MODIFY COLUMN `USER_PER_PRICE_THIRTY`  decimal(20,2) NULL DEFAULT NULL COMMENT '客单价' AFTER `PAY_MONEY`,
MODIFY COLUMN `ORDER_PER_PRICE`  decimal(20,2) NULL DEFAULT NULL COMMENT '笔单价' AFTER `USER_PER_PRICE_THIRTY`,
MODIFY COLUMN `REFUND_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '退单金额' AFTER `REFUND_NUM`;

ALTER TABLE `customer_region_recent_thirty`
MODIFY COLUMN `ORDER_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '下单金额' AFTER `ORDER_NUM`,
MODIFY COLUMN `PAY_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '付款金额' AFTER `PAY_GOODS_NUM`,
MODIFY COLUMN `USER_PER_PRICE_THIRTY`  decimal(20,2) NULL DEFAULT NULL COMMENT '客单价' AFTER `PAY_MONEY`,
MODIFY COLUMN `ORDER_PER_PRICE`  decimal(20,2) NULL DEFAULT NULL COMMENT '笔单价' AFTER `USER_PER_PRICE_THIRTY`,
MODIFY COLUMN `REFUND_MONEY`  decimal(20,2) NULL DEFAULT NULL COMMENT '退单金额' AFTER `REFUND_NUM`;

