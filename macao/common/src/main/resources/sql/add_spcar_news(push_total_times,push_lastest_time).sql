ALTER TABLE `yinghai_taxigo`.`spcar_news`
  ADD COLUMN `push_total_times` int(11) NULL DEFAULT 0 COMMENT '推送总次数' AFTER `realm`,
  ADD COLUMN `push_latest_time` DATETIME NULL COMMENT '最新推送时间' AFTER `push_total_times`;






