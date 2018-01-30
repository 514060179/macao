ALTER TABLE `spcar_order`
ADD COLUMN `comment`  int(255) NULL DEFAULT 0 COMMENT '是否评论，0否1是' AFTER `monthly`;