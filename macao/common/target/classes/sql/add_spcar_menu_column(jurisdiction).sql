ALTER TABLE `yinghai_taxigo`.`spcar_menu`
  ADD COLUMN `jurisdiction` TINYINT DEFAULT 0 NULL COMMENT '权限限制，0非1是' AFTER `priority`;