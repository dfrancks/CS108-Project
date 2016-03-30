USE c_cs108_csebas;

set @@auto_increment_increment=1;

DROP TABLE IF EXISTS achievement;
DROP TABLE IF EXISTS friendship;
DROP TABLE IF EXISTS message;
DROP TABLE IF EXISTS result;
DROP TABLE IF EXISTS answer;
DROP TABLE IF EXISTS question;
DROP TABLE IF EXISTS quiz;
DROP TABLE IF EXISTS user;

CREATE TABLE user (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  username CHAR(64) NOT NULL,
  password CHAR(64) NOT NULL,
  is_admin BOOLEAN NOT NULL,
  date_joined TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
  is_active BOOLEAN NOT NULL DEFAULT 1;
  privacy_setting INT NOT NULL DEFAULT 0;
);

CREATE TABLE quiz (
  quiz_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  single_page BOOLEAN NOT NULL,
  randomized BOOLEAN NOT NULL,
  immediate_correction BOOLEAN NOT NULL,
  title CHAR(64) NOT NULL,
  description CHAR(64) NOT NULL,
  deleted BOOL NOT NULL DEFAULT 0,
  verified BOOL NOT NULL DEFAULT 0,
  FOREIGN KEY(user_id) REFERENCES user(user_id)
);

CREATE TABLE question (
  question_id INT AUTO_INCREMENT PRIMARY KEY,
  quiz_id INT NOT NULL,
  type INT NOT NULL,
  points INT NOT NULL DEFAULT 1,
  question_text CHAR(255) NOT NULL,
  secondary_question_text CHAR(255) DEFAULT "",
  placement INT,
  FOREIGN KEY(quiz_id) REFERENCES quiz(quiz_id)
);

CREATE TABLE answer (
  answer_id INT AUTO_INCREMENT PRIMARY KEY,
  question_id INT NOT NULL,
  answer_text CHAR(64) NOT NULL,
  secondary_id INT,
  correct BOOL NOT NULL DEFAULT 1,
  FOREIGN KEY(question_id) REFERENCES question(question_id)
);

CREATE TABLE result (
  result_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  quiz_id INT NOT NULL,
  score INT NOT NULL,
  date_taken TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  time_elapsed TIME NOT NULL,
  deleted BOOL NOT NULL DEFAULT 0,
  FOREIGN KEY(user_id) REFERENCES user(user_id),
  FOREIGN KEY(quiz_id) REFERENCES quiz(quiz_id)
);

CREATE TABLE message (
  message_id INT AUTO_INCREMENT PRIMARY KEY,
  recipient_id INT NOT NULL,
  sender_id INT NOT NULL,
  message_text CHAR(255) NOT NULL,
  type INT NOT NULL,
  created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  quiz_id INT,
  FOREIGN KEY(recipient_id) REFERENCES user(user_id),
  FOREIGN KEY(sender_id) REFERENCES user(user_id),
  FOREIGN KEY(quiz_id) REFERENCES quiz(quiz_id)
);

CREATE TABLE friendship (
  friendship_id INT AUTO_INCREMENT PRIMARY KEY,
  to_user_id INT NOT NULL,
  from_user_id INT NOT NULL,
  status INT NOT NULL DEFAULT 0,
  FOREIGN KEY(to_user_id) REFERENCES user(user_id),
  FOREIGN KEY(from_user_id) REFERENCES user(user_id)
);

CREATE TABLE achievement (
  achievement_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  quiz_id INT,
  type INT NOT NULL,
  FOREIGN KEY(user_id) REFERENCES user(user_id),
  FOREIGN KEY(quiz_id) REFERENCES quiz(quiz_id)
);

CREATE TABLE announcement (
  announcement_id INT AUTO_INCREMENT PRIMARY KEY,
  announcement_text CHAR(255) NOT NULL,
  is_active BOOL NOT NULL DEFAULT 1
);

CREATE TABLE report (
  report_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  quiz_id INT NOT NULL,
  type INT NOT NULL,
  FOREIGN KEY(user_id) REFERENCES user(user_id),
  FOREIGN KEY(quiz_id) REFERENCES quiz(quiz_id)
);
