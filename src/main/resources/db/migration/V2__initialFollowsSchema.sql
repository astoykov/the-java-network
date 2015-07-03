
-- ===== Follows Schema

CREATE TABLE IF NOT EXISTS `follows` (
  `id` varchar(36) NOT NULL,
  `created` datetime NOT NULL,
  `username` varchar(255) NOT NULL,
  `follows_user` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;
