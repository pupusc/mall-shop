-- MySQL Script generated by MySQL Workbench
-- Wed Apr  5 19:21:15 2017
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `b2b` DEFAULT CHARACTER SET utf8 ;
USE `b2b` ;

-- -----------------------------------------------------
-- Table `mydb`.`wm_offline_account`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `b2b`.`wm_offline_account` (
  `account_id` BIGINT(10) NULL AUTO_INCREMENT COMMENT '主键',
  `bank_name` VARCHAR(45) NULL COMMENT '店铺名称',
  `bank_no` VARCHAR(45) NULL COMMENT '银行账号',
  `bank_status` VARCHAR(45) NULL COMMENT '银行状态 0: 启用 1:禁用',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  `del_flag` TINYINT NULL COMMENT '删除状态 0：否 1：是',
  `del_time` DATETIME NULL COMMENT '删除时间',
  PRIMARY KEY (`account_id`),
  UNIQUE INDEX `account_id_UNIQUE` (`account_id` ASC),
  UNIQUE INDEX `bank_no_UNIQUE` (`bank_no` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`wm_online_account`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `b2b`.`wm_online_account` (
  `account_id` BIGINT(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pay_id` BIGINT(10) NULL COMMENT '支付外键',
  `pay_type` TINYINT NULL COMMENT '支付类型 0：支付宝 1: 微信 2:银联',
  `is_use` TINYINT NULL COMMENT '是否启用 0：是 1: 否',
  `is_default` TINYINT NULL COMMENT '是否默认 0: 是 1: 否',
  PRIMARY KEY (`account_id`),
  UNIQUE INDEX `account_id_UNIQUE` (`account_id` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`wm_receivable`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `b2b`.`wm_receivable` (
  `receivable_id` BIGINT(10) NOT NULL AUTO_INCREMENT COMMENT '收款记录: 交易主键',
  `receivable_no` VARCHAR(45) NOT NULL COMMENT '流水号',
  `order_code` VARCHAR(45) NOT NULL COMMENT '订单编号',
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `online_account_id` BIGINT(10) NULL COMMENT '收款账号 账号id 外键',
  `offline_account_id` BIGINT(10) NULL COMMENT '收款账号 线下',
  `receivable_account` VARCHAR(45) NULL COMMENT '收款账户',
  `receivable_status` TINYINT NOT NULL DEFAULT 2 COMMENT '收款状态 1.已收款 2.未收款',
  `comment` VARCHAR(100) NULL COMMENT '备注',
  `del_flag` TINYINT NULL COMMENT '删除flag 0：未删除 1：已删除',
  `del_time` DATETIME NULL COMMENT '删除时间',
  PRIMARY KEY (`receivable_id`),
  UNIQUE INDEX `bill_id_UNIQUE` (`receivable_id` ASC),
  UNIQUE INDEX `receivable_no_UNIQUE` USING BTREE (`receivable_no` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`refund`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `b2b`.`refunds` (
  `refund_id` BIGINT(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_time` DATETIME NULL COMMENT '申请时间',
  `update_time` DATETIME NULL,
  `refund_code` VARCHAR(45) NOT NULL COMMENT '退款单号',
  `refund_account` VARCHAR(45) NULL COMMENT '退款账户',
  `return_order_code` VARCHAR(45) NOT NULL COMMENT '退货单编号',
  `refund_status` TINYINT NOT NULL DEFAULT 1 COMMENT '退款单状态：1.待退款 2.拒绝退款 3.已完成',
  `refund_comment` VARCHAR(50) NULL,
  `offline_account_id` BIGINT(10) NULL COMMENT '退款单，线下支付账号',
  `online_account_id` BIGINT(10) NULL,
  `del_flag` TINYINT NULL COMMENT '删除标志 0：未删除 1:已删除',
  `del_time` DATETIME NULL,
  `wm_refundscol` VARCHAR(45) NULL,
  PRIMARY KEY (`refund_id`),
  UNIQUE INDEX `refund_id_UNIQUE` (`refund_id` ASC),
  UNIQUE INDEX `refund_code_UNIQUE` (`refund_code` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`wm_invoice`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `b2b`.`wm_invoice` (
  `invoice_id` BIGINT(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `customer_id` BIGINT(10) NULL COMMENT '客户Id',
  `order_code` VARCHAR(45) NULL COMMENT '订单编号',
  `invoice_type` TINYINT NULL COMMENT '发票类型 1:增值税发票 2.普通发票',
  `invoice_title` VARCHAR(45) NULL COMMENT '发票抬头',
  `taxpayer_code` VARCHAR(45) NULL COMMENT '纳税人识别号',
  `register_address` VARCHAR(45) NULL COMMENT '注册地址',
  `register_phone` VARCHAR(20) NULL COMMENT '注册电话',
  `bank_account` VARCHAR(45) NULL COMMENT '开户银行',
  `invoice_item` BIGINT(10) NULL COMMENT '开票项目',
  `invoice_item_name` VARCHAR(45) NULL COMMENT '开票项目名称',
  `invoice_receipt` VARCHAR(45) NULL COMMENT '收件信息/收件地址',
  `create_time` DATETIME NULL COMMENT '开票时间',
  `update_time` DATETIME NULL,
  `del_flag` TINYINT NULL COMMENT '删除标志 0：未删 1：已删',
  `del_time` DATETIME NULL COMMENT '删除时间',
  `invoice_status` TINYINT NULL COMMENT '开票状态 0：待开票 1：已开票',
  PRIMARY KEY (`invoice_id`),
  UNIQUE INDEX `invoice_id_UNIQUE` (`invoice_id` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`wm_invoice_qualification`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `b2b`.`wm_invoice_qualification` (
  `invoice_qualification_id` BIGINT(10) NOT NULL AUTO_INCREMENT COMMENT '主键id ',
  `taxpayer_code` VARCHAR(45) NULL COMMENT '纳税人识别号',
  `register_address` VARCHAR(45) NULL COMMENT '注册地址',
  `register_phone` VARCHAR(20) NULL,
  `bank_account` VARCHAR(45) NULL COMMENT '银行基本开户号',
  `bank_name` VARCHAR(20) NULL,
  `business_license_url` VARCHAR(50) NULL COMMENT '营业执照地址',
  `taxpayer_qualification_url` VARCHAR(50) NULL COMMENT '一般纳税人认证资格复印件',
  `invoice_status` TINYINT NULL COMMENT '审核状态 0:待审核 1:已审核 2:审核未通过',
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `del_flag` TINYINT NULL COMMENT '删除 0:未删除 1:已删除',
  `del_time` DATETIME NULL,
  PRIMARY KEY (`invoice_qualification_id`),
  UNIQUE INDEX `invoice_qualification_id_UNIQUE` (`invoice_qualification_id` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`wm_invoice_item`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `b2b`.`wm_invoice_item` (
  `invoice_item_id` BIGINT(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `invoice_item_name` VARCHAR(10) NULL COMMENT '开票项目',
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `del_flag` TINYINT NULL COMMENT '删除标志 0:未删除 1:已删除',
  `del_time` DATETIME NULL COMMENT '删除时间',
  UNIQUE INDEX `invoice_item_id_UNIQUE` (`invoice_item_id` ASC),
  PRIMARY KEY (`invoice_item_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`return_order`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `b2b`.`return_order` (
  `return_order_id` VARCHAR(32) NOT NULL COMMENT '退货的主键',
  `return_order_code` VARCHAR(45) NOT NULL COMMENT '退款单号 R',
  `order_code` VARCHAR(45) NULL,
  `order_id` VARCHAR(32) NOT NULL COMMENT '订单主键',
  `return_reason` TINYINT NULL COMMENT '退货原因 0:商家发错货、1:货品与描述不符、2:货品少件/受损/污渍等、3:货品质量问题、4:其他（这是我们配置好还是让商户自行配置？）',
  `return_way` TINYINT NULL COMMENT '退货方式',
  `apply_price` DECIMAL(10,2) NULL COMMENT '申请金额',
  `return_goods_price` DECIMAL(10,2) NULL COMMENT '商品退货金额',
  `return_price` DECIMAL(10,2) NULL COMMENT '应退总额',
  `apply_status` TINYINT NULL COMMENT '申请状态 0:已申请 1:未申请',
  `return_status` TINYINT NULL COMMENT '退单状态 0:待审核 1:待填写物流信息 2:待商家收货 3：待退款 4.已完成 5.审核未通过 6.拒绝收货 7.拒绝退款',
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `del_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '删除 0 :未删 1：删除',
  `del_time` DATETIME NULL COMMENT '删除时间',
  PRIMARY KEY (`return_order_id`),
  UNIQUE INDEX `return_order_id_UNIQUE` (`return_order_id` ASC),
  UNIQUE INDEX `return_order_code_UNIQUE` (`return_order_code` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`return_order_image`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `b2b`.`return_order_image` (
  `image_id` VARCHAR(32) NOT NULL COMMENT '图片主键',
  `return_order_id` VARCHAR(32) NOT NULL COMMENT '退货单主键',
  `return_order_imageurl` VARCHAR(45) NULL COMMENT '退货单图片地址',
  `create_time` DATETIME NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '修改时间',
  `del_flag` TINYINT NULL DEFAULT 0 COMMENT '删除标志',
  `del_time` DATETIME NULL COMMENT '删除时间',
  PRIMARY KEY (`image_id`),
  UNIQUE INDEX `image_id_UNIQUE` (`image_id` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`return_order_goods`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `b2b`.`return_order_goods` (
  `return_order_goods_id` VARCHAR(32) NOT NULL COMMENT '主键',
  `return_order_id` VARCHAR(32) NULL COMMENT '退货单',
  `sku_id` VARCHAR(32) NULL COMMENT '货品id',
  `sku_num` INT NULL COMMENT '货品数量',
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `del_flag` TINYINT NULL,
  `del_time` DATETIME NULL,
  PRIMARY KEY (`return_order_goods_id`),
  UNIQUE INDEX `return_order_goods_id_UNIQUE` (`return_order_goods_id` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`return_order_log`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `b2b`.`return_order_log` (
  `log_id` VARCHAR(32) NOT NULL COMMENT '日志主键',
  `operate_type` TINYINT NULL COMMENT '操作方 0: 平台 1:店铺 2：客户',
  `operator` VARCHAR(32) NULL COMMENT '操作人 userid',
  `operator_name` VARCHAR(32) NULL COMMENT '用户名称',
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `log_type` TINYINT NULL COMMENT '日志类别 0：退货订单发货确认 1:订货单出库审核 2：订货单财务审核 3.订货单订单审核 4.修改订货单 5.创建订货单',
  PRIMARY KEY (`log_id`),
  UNIQUE INDEX `log_id_UNIQUE` (`log_id` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`return_logistics`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `b2b`.`return_logistics` (
  `logistics_id` VARCHAR(32) NOT NULL COMMENT '物流信息主键',
  `return_order_id` VARCHAR(32) NOT NULL COMMENT '退款单主键',
  `logistics_company` VARCHAR(45) NULL COMMENT '物流公司',
  `logistics_no` VARCHAR(45) NULL COMMENT '物流单号',
  `create_time` DATETIME NULL COMMENT '退货日期',
  `update_time` DATETIME NULL COMMENT '修改时间',
  `del_time` DATETIME NULL COMMENT '删除时间',
  `dal_flag` TINYINT NULL,
  PRIMARY KEY (`logistics_id`),
  UNIQUE INDEX `logistics_id_UNIQUE` (`logistics_id` ASC))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

