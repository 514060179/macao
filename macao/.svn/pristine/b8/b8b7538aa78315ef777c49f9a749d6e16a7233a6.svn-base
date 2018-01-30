CREATE TABLE `spcar_version_control` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `version_id` VARCHAR(255) NOT NULL,
  `force_update` TINYINT(1) DEFAULT '0' COMMENT '是否強制更新:0代表否,1代表是',
  `device_type` VARCHAR(1) DEFAULT '1' COMMENT '設備類型:1代表ios,2代表Android',
  `realm` VARCHAR(255) NOT NULL COMMENT '客戶端類型:driver:司機端,passenger:乘客端',
  `create_time` DATETIME NOT NULL COMMENT '創建日期',
  `update_time` DATETIME NOT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4