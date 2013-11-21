CREATE TABLE `test` (
 `id` int(11) NOT NULL AUTO_INCREMENT,
 `long` bigint(20) DEFAULT NULL,
 `string` varchar(255) DEFAULT NULL,
 `text` text,
 `enum` enum('normal','deleted') NOT NULL DEFAULT 'normal',
 `date` date DEFAULT NULL,
 `datetime` datetime DEFAULT NULL,
 `timestamp` timestamp NULL DEFAULT NULL,
 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `test`.`test` (`id`, `long`, `string`, `text`, `enum`, `date`, `datetime`, `timestamp`) VALUES (NULL, '123456789123456789', 'Test string', 'Test text', 'normal', '2013-11-10', '2013-11-10 20:40:22', '2013-11-10 20:40:23');
INSERT INTO `test`.`test` (`id`, `long`, `string`, `text`, `enum`, `date`, `datetime`, `timestamp`) VALUES (NULL, '123456789123456790', 'Second string', 'Second text text', 'deleted', '2013-11-11', '2013-11-10 20:40:23', '2013-11-10 20:40:24');


