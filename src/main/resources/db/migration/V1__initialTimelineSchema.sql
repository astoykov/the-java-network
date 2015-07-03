
-- ===== Timeline Schema

CREATE TABLE IF NOT EXISTS `timeline` (
  `id` varchar(36) NOT NULL,
  `created` datetime NOT NULL,
  `username` varchar(255) NOT NULL,
  `message` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;
