use only_one;

DROP TABLE IF EXISTS lecture_histories;
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS favorites;
DROP TABLE IF EXISTS connection_status;
DROP TABLE IF EXISTS blacklists;
DROP TABLE IF EXISTS course_reviews;
DROP TABLE IF EXISTS studying_courses;
DROP TABLE IF EXISTS lectures;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS members;

CREATE TABLE `members` (
`member_id` BIGINT NOT NULL AUTO_INCREMENT,
`name` VARCHAR(100) NOT NULL,
`email` VARCHAR(255) NOT NULL UNIQUE,
`password` VARCHAR(255) NOT NULL,
`role` VARCHAR(50) NOT NULL,
`enrolled_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
`acc_count` INT NOT NULL DEFAULT 0,
PRIMARY KEY (`member_id`)
);
-- - 2. 강좌 (Course)
CREATE TABLE `courses` (
`course_id` BIGINT NOT NULL AUTO_INCREMENT,
`title` VARCHAR(255) NOT NULL,
`member_id` BIGINT NOT NULL,
PRIMARY KEY (`course_id`),
FOREIGN KEY (`member_id`) REFERENCES `members` (`member_id`) ON DELETE CASCADE
);
-- - 3. 강의 (Lecture)
CREATE TABLE `lectures` (
`lecture_id` BIGINT NOT NULL AUTO_INCREMENT,
`course_id` BIGINT NOT NULL,
`title` VARCHAR(255) NOT NULL,
PRIMARY KEY (`lecture_id`),
FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`) ON DELETE CASCADE
);
-- - 4. 수강중인강좌 (Studying Course)
CREATE TABLE `studying_courses` (
`studying_id` BIGINT NOT NULL AUTO_INCREMENT,
`course_id` BIGINT NOT NULL,
`member_id` BIGINT NOT NULL,
`status` VARCHAR(50) NOT NULL DEFAULT '수강 중',
PRIMARY KEY (`studying_id`),
FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`) ON DELETE CASCADE,
FOREIGN KEY (`member_id`) REFERENCES `members` (`member_id`) ON DELETE CASCADE
);
-- - 5. 강좌평 (Course Review)
CREATE TABLE `course_reviews` (
`review_id` BIGINT NOT NULL AUTO_INCREMENT,
`course_id` BIGINT NOT NULL,
`member_id` BIGINT NOT NULL,
`contents` TEXT NOT NULL,
`rating` FLOAT NOT NULL,
PRIMARY KEY (`review_id`),
FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`) ON DELETE CASCADE,
FOREIGN KEY (`member_id`) REFERENCES `members` (`member_id`) ON DELETE CASCADE
);
-- - 6. 블랙리스트 (Blacklist)
CREATE TABLE `blacklists` (
`list_id` BIGINT NOT NULL AUTO_INCREMENT,
`member_id` BIGINT NOT NULL,
PRIMARY KEY (`list_id`),
FOREIGN KEY (`member_id`) REFERENCES `members` (`member_id`) ON DELETE CASCADE
);
-- - 7. 접속여부 (Connection Status)
CREATE TABLE `connection_status` (
`member_id` BIGINT NOT NULL,
PRIMARY KEY (`member_id`),
FOREIGN KEY (`member_id`) REFERENCES `members` (`member_id`) ON DELETE CASCADE
);
-- - 8. 선택목록 (Favorites / Wishlist)
CREATE TABLE `favorites` (
`fav_id` BIGINT NOT NULL AUTO_INCREMENT,
`member_id` BIGINT NOT NULL,
`course_id` BIGINT NOT NULL,
PRIMARY KEY (`fav_id`),
FOREIGN KEY (`member_id`) REFERENCES `members` (`member_id`) ON DELETE CASCADE,
FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`) ON DELETE CASCADE
);
-- - 9. 결제 (Payment)
CREATE TABLE `payments` (
`pay_id` BIGINT NOT NULL AUTO_INCREMENT,
`member_id` BIGINT NOT NULL,
`payed_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
`price` INT NOT NULL,
PRIMARY KEY (`pay_id`),
FOREIGN KEY (`member_id`) REFERENCES `members` (`member_id`) ON DELETE CASCADE
);
-- - 10. 강의수강이력 (Lecture History)
CREATE TABLE `lecture_histories` (
`history_id` BIGINT NOT NULL AUTO_INCREMENT,
`member_id` BIGINT NOT NULL,
`lecture_id` BIGINT NOT NULL,
`status` VARCHAR(50) NOT NULL DEFAULT '수강완료',
PRIMARY KEY (`history_id`),
FOREIGN KEY (`member_id`) REFERENCES `members` (`member_id`) ON DELETE CASCADE,
FOREIGN KEY (`lecture_id`) REFERENCES `lectures` (`lecture_id`) ON DELETE CASCADE
);

---

-- - 외래 키 제약 조건 재활성화

---

SET FOREIGN_KEY_CHECKS = 1;