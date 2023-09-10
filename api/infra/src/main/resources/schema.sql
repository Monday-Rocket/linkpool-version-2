DROP TABLE IF EXISTS `folder`;
CREATE TABLE `folder` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_date_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `modified_date_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `deleted` bit(1) NOT NULL,
  `name` varchar(255) NOT NULL,
  `image` varchar(500) DEFAULT NULL,
  `owner_id` bigint DEFAULT NULL,
  `visible` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1135 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `job_group`;
CREATE TABLE `job_group` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_date_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `modified_date_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `link`;
CREATE TABLE `link` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_date_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `modified_date_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `deleted` bit(1) NOT NULL,
  `describe` varchar(550) DEFAULT NULL,
  `folder_id` bigint DEFAULT NULL,
  `image` varchar(500) DEFAULT NULL,
  `inflow_type` int NOT NULL DEFAULT '0',
  `title` varchar(30) DEFAULT NULL,
  `url` varchar(500) NOT NULL,
  `creator_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2211 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `report`;
CREATE TABLE `report` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_date_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `modified_date_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `deleted` bit(1) NOT NULL,
  `other_reason` varchar(500) DEFAULT NULL,
  `reason` int NOT NULL,
  `reporter_id` bigint NOT NULL,
  `target_id` bigint NOT NULL,
  `target_type` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_date_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `modified_date_time` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `deleted` bit(1) NOT NULL,
  `job_group_id` bigint DEFAULT NULL,
  `nickname` varchar(30) DEFAULT NULL,
  `profile_image` varchar(255) DEFAULT NULL,
  `uid` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `ix_user_uid` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=461 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;