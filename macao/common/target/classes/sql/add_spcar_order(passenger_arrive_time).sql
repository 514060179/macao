ALTER TABLE `yinghai_taxigo`.`spcar_order`   
  ADD COLUMN `passenger_arrive_time` DATETIME NULL COMMENT ''乘客上车时间'' AFTER `driver_arrive_time`;
