/*
mysql -uroot -p --default-character-set=utf8 < create.sql
mysql> select * from wordsearch.words;
*/
DROP DATABASE IF EXISTS `wordsearch`;
CREATE DATABASE `wordsearch`;
USE `wordsearch`;
CREATE TABLE `words` (
  `id` int(11) NOT NULL auto_increment,
  `word` varchar(255) default NULL,
  `ctime` datetime default NULL,
  `mtime` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

INSERT INTO `words`
(`word`)
VALUES
('　ねっ？」 | 　知道吗？」'),
('　ねっ？」 | 　知道吗？」'),
('　ねっ？」 | 　知道吗？」'),
('あかたし 赤だし'),
('あかたし 赤出し'),
('あかたし 赤出汁'),
('あかちやん 赤ちゃん'),
('あかつか 赤塚'),
('あかつき 暁'),
('あかつきちよう 暁町'),
('あかつつみ 赤堤'),
('あかつぱし 赤っ恥'),
('あかとんほ 赤トンボ'),
('あかとんほ 赤とんぼ');

UPDATE `words` SET ctime = now(), mtime = now();
