ALTER TABLE `spcar_driver`
ADD COLUMN `country_code2`  varchar(10) NULL COMMENT '备用手机区号' AFTER `sign`,
ADD COLUMN `tel2`  varchar(100) NULL COMMENT '备用手机号' AFTER `country_code2`;