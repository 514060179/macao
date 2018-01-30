/*
SQLyog  v12.2.6 (64 bit)
MySQL - 5.7.16-log : Database - yinghai_macao
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`yinghai_macao` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `yinghai_macao`;

/*Table structure for table `area` */

DROP TABLE IF EXISTS `area`;

CREATE TABLE `area` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '区id',
  `city_id` int(11) DEFAULT NULL COMMENT '市id',
  `area_name` varchar(100) DEFAULT NULL COMMENT '区名称',
  `code` int(8) DEFAULT NULL,
  `cost` double NOT NULL COMMENT '費用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

/*Table structure for table `city` */

DROP TABLE IF EXISTS `city`;

CREATE TABLE `city` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '市id',
  `city_name` varchar(100) DEFAULT NULL COMMENT '市名称',
  `code` int(8) DEFAULT NULL COMMENT '行政区域代码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Table structure for table `driver` */

DROP TABLE IF EXISTS `driver`;

CREATE TABLE `driver` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '司机姓名',
  `loc_x` double NOT NULL COMMENT '高德地图，经度',
  `loc_y` double NOT NULL COMMENT '高德地图，纬度',
  `loc_str` text NOT NULL,
  `direction` varchar(255) NOT NULL COMMENT '位置方向',
  `status` varchar(3) NOT NULL DEFAULT '999' COMMENT '司机状态',
  `waitting_id` varchar(255) DEFAULT NULL,
  `license` varchar(255) NOT NULL COMMENT '實際上被用作司機的「車牌號碼」',
  `image` varchar(255) NOT NULL DEFAULT 'empty-person-image.jpg' COMMENT '司机头像',
  `driver_type` varchar(2) NOT NULL COMMENT '司机类型',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `last_update` datetime DEFAULT NULL COMMENT '更新时间',
  `device_id` varchar(255) NOT NULL COMMENT '设备id',
  `verification` varchar(25) DEFAULT NULL COMMENT '验证码',
  `user_token` varchar(512) DEFAULT NULL,
  `country_code` varchar(10) NOT NULL COMMENT '区号',
  `tel` varchar(100) NOT NULL COMMENT '手机号码',
  `given_name` varchar(255) NOT NULL COMMENT '名字',
  `family_name` varchar(255) NOT NULL COMMENT '姓氏',
  `userId` int(11) DEFAULT '0',
  `rating` double NOT NULL DEFAULT '5' COMMENT '司机评分',
  `english_capability` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否懂英文',
  `profile_image` varchar(255) NOT NULL DEFAULT '' COMMENT '驾照图片',
  `order_count` double NOT NULL DEFAULT '0' COMMENT '总单数',
  `cancel_count` double NOT NULL DEFAULT '0' COMMENT '取消单数',
  `finish_count` double NOT NULL DEFAULT '0' COMMENT '完成单数',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已拉黑',
  `license_true` varchar(128) NOT NULL DEFAULT '' COMMENT '驾照编码',
  `license_till` datetime DEFAULT NULL COMMENT '驾照到期日期',
  `shift` varchar(45) DEFAULT NULL COMMENT '工作时间段',
  `device_type` varchar(255) DEFAULT '1' COMMENT '設備類型：1ios2android',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

/*Table structure for table `macao_order` */

DROP TABLE IF EXISTS `macao_order`;

CREATE TABLE `macao_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status` int(11) NOT NULL COMMENT '订单状态：0:进行中1:待完成5:已完成999:取消订单998:取消支付',
  `driver_id` int(10) DEFAULT NULL COMMENT '车手id',
  `user_id` int(10) NOT NULL COMMENT '用户id',
  `confirm_id` int(11) DEFAULT NULL,
  `confirm_type` varchar(255) DEFAULT NULL,
  `reject_id` mediumtext,
  `reject_type` varchar(255) DEFAULT NULL,
  `starting_point` varchar(255) NOT NULL,
  `destination` varchar(255) NOT NULL,
  `send_time` datetime NOT NULL,
  `complete_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `rating` int(1) NOT NULL DEFAULT '0',
  `waitting_id` int(11) NOT NULL COMMENT 'order id',
  `require_vip` tinyint(1) DEFAULT '0',
  `require_english` tinyint(1) DEFAULT '0',
  `memo` varchar(1024) NOT NULL,
  `distributor_status` varchar(45) DEFAULT 'NOT_STARTED',
  `start_x` double DEFAULT '0' COMMENT '下单地点高德地图经度',
  `start_y` double DEFAULT '0' COMMENT '下单地点高德地图纬度',
  `start_address` varchar(255) NOT NULL DEFAULT '' COMMENT '下单地点',
  `end_x` double DEFAULT '0' COMMENT '目的地高德地图经度',
  `end_y` double DEFAULT '0' COMMENT '目的地高德地图纬度',
  `end_address` varchar(255) NOT NULL DEFAULT '' COMMENT '目的地位置名',
  `driverArrived` double DEFAULT '0',
  `start_address_ch` varchar(255) NOT NULL,
  `end_address_ch` varchar(255) NOT NULL,
  `start_name` varchar(255) NOT NULL,
  `end_name` varchar(255) NOT NULL,
  `amount` decimal(12,2) DEFAULT NULL,
  `pay_status` int(11) DEFAULT NULL COMMENT '支付状态（0待付款；1已支付；2申請退款；3已退款）',
  `pay_no` varchar(50) DEFAULT NULL COMMENT '支付流水号',
  `pay_time` datetime DEFAULT NULL COMMENT '支付完成时间',
  `pay_code` varchar(10) DEFAULT NULL COMMENT '支付响应代码',
  `pay_intro` varchar(255) DEFAULT NULL COMMENT '支付响应描述',
  `appointment_time` datetime DEFAULT NULL COMMENT '预约时间',
  `appointment_type` tinyint(4) DEFAULT '0' COMMENT '订类型：0即时订单1预约订单',
  `cancel_type` varchar(11) DEFAULT NULL COMMENT '取消类型：n取消下单 s取消订单',
  `pay_way` varchar(11) DEFAULT '' COMMENT '支付方式：微信支付，支付宝支付，信用卡支付',
  `pay_way_code` tinyint(4) DEFAULT '0' COMMENT '支付方式代码：0微信支付，1支付宝支付2应用卡支付',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `macao_user` */

DROP TABLE IF EXISTS `macao_user`;

CREATE TABLE `macao_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '用户名',
  `nickname` varchar(255) DEFAULT NULL COMMENT '昵称',
  `password` varchar(255) NOT NULL COMMENT '用户密码',
  `family_name` varchar(255) DEFAULT '' COMMENT 'Passenger Family name',
  `given_name` varchar(255) DEFAULT '' COMMENT 'Passenger Given Name',
  `country_code` varchar(10) NOT NULL COMMENT '用户手机号码区号086，052,053',
  `tel` varchar(200) NOT NULL COMMENT '用户手机号码',
  `user_agent` text NOT NULL,
  `verification` text NOT NULL COMMENT '验证码',
  `loc_x` double NOT NULL COMMENT '高德地图经度',
  `loc_y` double NOT NULL COMMENT '高德地图纬度',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `last_login` datetime NOT NULL,
  `device_id` text NOT NULL COMMENT '设备id',
  `user_token` text NOT NULL,
  `status` int(3) NOT NULL DEFAULT '1' COMMENT '状态码',
  `sex` tinyint(4) NOT NULL DEFAULT '0' COMMENT '性别：0男1女',
  `order_count` double NOT NULL DEFAULT '0' COMMENT '总单数',
  `cancel_count` double NOT NULL DEFAULT '0' COMMENT '取消单数',
  `finish_count` double NOT NULL DEFAULT '0' COMMENT '完成单数',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否delete',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10216 DEFAULT CHARSET=utf8;

/*Table structure for table `pay_log` */

DROP TABLE IF EXISTS `pay_log`;

CREATE TABLE `pay_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pay_result_code` varchar(255) DEFAULT NULL COMMENT '支付返回狀態碼',
  `paygateway_order_no` varchar(255) DEFAULT NULL COMMENT '支付網關訂單id',
  `order_id` varchar(255) DEFAULT NULL COMMENT '訂單id',
  `amount` decimal(12,2) DEFAULT NULL COMMENT '總額',
  `pay_time` datetime DEFAULT NULL COMMENT '支付時間',
  `is_error` tinyint(1) DEFAULT NULL COMMENT '是否支付失敗：1是0否',
  `msg` varchar(255) DEFAULT NULL COMMENT '備註',
  PRIMARY KEY (`id`),
  KEY `is_error` (`order_id`,`is_error`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
