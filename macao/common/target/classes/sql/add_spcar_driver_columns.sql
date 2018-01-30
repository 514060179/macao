ALTER TABLE `spcar_driver`
MODIFY COLUMN `spcar_driver_id`  int(11) NOT NULL AUTO_INCREMENT FIRST ,
ADD COLUMN `im_name`  varchar(64) NULL AFTER `device_type`,
ADD COLUMN `sign`  varchar(1024) NULL AFTER `im_name`;