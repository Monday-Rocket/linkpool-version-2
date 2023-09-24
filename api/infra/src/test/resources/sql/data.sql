INSERT INTO `job_group` (`id`, `name`) VALUES
(1, '디자인'),
(2, '기획'),
(3, 'IT개발'),
(4, '마케팅'),
(5, '세일즈'),
(6, '경영/비즈니스'),
(7, '인테리어/건축'),
(8, '사무직'),
(9, '엔지니어'),
(10, '음악'),
(11, '작가'),
(12, '학생'),
(13, '주부'),
(14, '기타'),
(15, '서비스'),
(16, '의료인');

INSERT INTO `user` (`id`, `deleted`, `job_group_id`, `nickname`, `profile_image`, `uid`) VALUES
(1, b'0', 3, 'local-tester', '01', 'test-uid');