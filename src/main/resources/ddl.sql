-- 1. 기존 CREWS 데이터베이스 삭제 (존재할 경우)
DROP DATABASE IF EXISTS CREWS;

-- 2. CREWS 데이터베이스 생성
CREATE DATABASE CREWS;

-- 3. CREWS 데이터베이스 사용
USE CREWS;
#
# -- 테이블 생성 순서에 따라 DROP TABLE 및 CREATE TABLE 문을 작성합니다.
#
# -- 1. member 테이블
# DROP TABLE IF EXISTS member;
#
# CREATE TABLE member
# (
#     id            bigint       NOT NULL AUTO_INCREMENT,
#     is_deleted    tinyint(1) DEFAULT 0,
#     created_at    datetime(6) DEFAULT NULL,
#     updated_at    datetime(6) DEFAULT NULL,
#     email         varchar(255) NOT NULL UNIQUE,
#     name          varchar(255) NOT NULL,
#     nick_name     varchar(255) NOT NULL,
#     password      varchar(255) NOT NULL,
#     phone_number  varchar(255) NOT NULL,
#     profile_image varchar(255) DEFAULT NULL,
#     PRIMARY KEY (id)
# );
#
# -- 2. bank 테이블
# DROP TABLE IF EXISTS bank;
#
# CREATE TABLE bank
# (
#     id         bigint       NOT NULL AUTO_INCREMENT,
#     created_at datetime(6) DEFAULT NULL,
#     updated_at datetime(6) DEFAULT NULL,
#     bank_code  varchar(255) NOT NULL UNIQUE,
#     bank_name  varchar(255) NOT NULL,
#     PRIMARY KEY (id)
# );
#
# -- 3. subject 테이블
# DROP TABLE IF EXISTS subject;
#
# CREATE TABLE subject
# (
#     id           bigint       NOT NULL AUTO_INCREMENT,
#     created_at   datetime(6) DEFAULT NULL,
#     updated_at   datetime(6) DEFAULT NULL,
#     subject_name varchar(255) NOT NULL,
#     PRIMARY KEY (id)
# );
#
# -- 4. due 테이블
# DROP TABLE IF EXISTS due;
#
# CREATE TABLE due
# (
#     id          bigint       NOT NULL AUTO_INCREMENT,
#     due_date    tinyint      NOT NULL,
#     created_at  datetime(6) DEFAULT NULL,
#     updated_at  datetime(6) DEFAULT NULL,
#     due_amount  bigint       NOT NULL,
#     member_data varchar(255) NOT NULL,
#     PRIMARY KEY (id)
# );
#
# -- 5. account 테이블
# DROP TABLE IF EXISTS account;
#
# CREATE TABLE account
# (
#     id                    bigint       NOT NULL AUTO_INCREMENT,
#     balance               bigint DEFAULT 0,
#     bank_id               bigint DEFAULT NULL,
#     member_id             bigint DEFAULT NULL,
#     created_at            datetime(6) DEFAULT NULL,
#     updated_at            datetime(6) DEFAULT NULL,
#     account_number        varchar(255) NOT NULL,
#     fintec_number         varchar(255) NOT NULL,
#     identified_number     varchar(255) NOT NULL,
#     masked_account_number varchar(255) NOT NULL,
#     account_type          ENUM('CREW','PERSONAL') NOT NULL,
#     PRIMARY KEY (id),
#     UNIQUE KEY uq_account_number (account_number),
#     KEY                   idx_bank_id (bank_id),
#     KEY                   idx_member_id (member_id),
#     CONSTRAINT fk_account_bank FOREIGN KEY (bank_id) REFERENCES bank (id),
#     CONSTRAINT fk_account_member FOREIGN KEY (member_id) REFERENCES member (id)
# );
#
# -- 6. card 테이블
# DROP TABLE IF EXISTS card;
#
# CREATE TABLE card
# (
#     id                 bigint       NOT NULL AUTO_INCREMENT,
#     is_deleted         tinyint(1) DEFAULT 0,
#     account_id         bigint DEFAULT NULL,
#     member_id          bigint DEFAULT NULL,
#     registered_at      datetime(6) NOT NULL,
#     created_at         datetime(6) DEFAULT NULL,
#     updated_at         datetime(6) DEFAULT NULL,
#     card_number        varchar(255) NOT NULL,
#     masked_card_number varchar(255) NOT NULL,
#     PRIMARY KEY (id),
#     UNIQUE KEY uq_card_number (card_number),
#     KEY                idx_account_id (account_id),
#     KEY                idx_member_id (member_id),
#     CONSTRAINT fk_card_account FOREIGN KEY (account_id) REFERENCES account (id),
#     CONSTRAINT fk_card_member FOREIGN KEY (member_id) REFERENCES member (id)
# );
#
# -- 7. transaction 테이블
# DROP TABLE IF EXISTS transaction;
#
# CREATE TABLE transaction
# (
#     id                   bigint       NOT NULL AUTO_INCREMENT,
#     account_id           bigint       DEFAULT NULL,
#     after_balance_amount bigint       NOT NULL,
#     transaction_amount   bigint       NOT NULL,
#     transaction_time     datetime(6) NOT NULL,
#     created_at           datetime(6) DEFAULT NULL,
#     updated_at           datetime(6) DEFAULT NULL,
#     card_number          varchar(255) DEFAULT NULL,
#     description          varchar(255) NOT NULL,
#     tran_type            ENUM('DEPOSIT','WITHDRAW') NOT NULL,
#     PRIMARY KEY (id),
#     KEY                  idx_account_id (account_id),
#     CONSTRAINT fk_transaction_account FOREIGN KEY (account_id) REFERENCES account (id)
# );
#
# -- 8. agit 테이블
# DROP TABLE IF EXISTS agit;
#
# CREATE TABLE agit
# (
#     id             bigint       NOT NULL AUTO_INCREMENT,
#     current_person int    DEFAULT 1,
#     is_deleted     tinyint(1) DEFAULT 0,
#     is_due         bit(1)       NOT NULL,
#     max_person     int    DEFAULT 10,
#     dues_id        bigint DEFAULT NULL,
#     agit_name      varchar(255) NOT NULL UNIQUE,
#     created_at     datetime(6) DEFAULT NULL,
#     updated_at     datetime(6) DEFAULT NULL,
#     PRIMARY KEY (id),
#     KEY            idx_dues_id (dues_id),
#     CONSTRAINT fk_agit_dues FOREIGN KEY (dues_id) REFERENCES due (id)
# );
#
# -- 9. introducing 테이블
# DROP TABLE IF EXISTS introducing;
#
# CREATE TABLE introducing
# (
#     id         bigint       NOT NULL AUTO_INCREMENT,
#     agit_id    bigint       NOT NULL UNIQUE,
#     content    varchar(255) NOT NULL,
#     introduce  varchar(255) NOT NULL,
#     image      varchar(255) DEFAULT NULL,
#     created_at datetime(6) DEFAULT NULL,
#     updated_at datetime(6) DEFAULT NULL,
#     PRIMARY KEY (id),
#     CONSTRAINT fk_introducing_agit FOREIGN KEY (agit_id) REFERENCES agit (id)
# );
#
# -- 10. feed 테이블
# DROP TABLE IF EXISTS feed;
#
# CREATE TABLE feed
# (
#     id         bigint       NOT NULL AUTO_INCREMENT,
#     is_deleted tinyint(1) DEFAULT 0,
#     agit_id    bigint       DEFAULT NULL,
#     member_id  bigint       DEFAULT NULL,
#     content    varchar(255) NOT NULL,
#     image      varchar(255) DEFAULT NULL,
#     like_count varchar(255) DEFAULT '0',
#     created_at datetime(6) DEFAULT NULL,
#     updated_at datetime(6) DEFAULT NULL,
#     PRIMARY KEY (id),
#     KEY        idx_agit_id (agit_id),
#     KEY        idx_member_id (member_id),
#     CONSTRAINT fk_feed_agit FOREIGN KEY (agit_id) REFERENCES agit (id),
#     CONSTRAINT fk_feed_member FOREIGN KEY (member_id) REFERENCES member (id)
# );
#
# -- 11. feed_like 테이블
# DROP TABLE IF EXISTS feed_like;
#
# CREATE TABLE feed_like
# (
#     id         bigint NOT NULL AUTO_INCREMENT,
#     feed_id    bigint DEFAULT NULL,
#     member_id  bigint DEFAULT NULL,
#     created_at datetime(6) DEFAULT NULL,
#     updated_at datetime(6) DEFAULT NULL,
#     PRIMARY KEY (id),
#     KEY        idx_feed_id (feed_id),
#     KEY        idx_member_id (member_id),
#     CONSTRAINT fk_feed_like_feed FOREIGN KEY (feed_id) REFERENCES feed (id),
#     CONSTRAINT fk_feed_like_member FOREIGN KEY (member_id) REFERENCES member (id)
# );
#
# -- 12. interesting 테이블
# DROP TABLE IF EXISTS interesting;
#
# CREATE TABLE interesting
# (
#     id         bigint       NOT NULL AUTO_INCREMENT,
#     subject_id bigint DEFAULT NULL,
#     name       varchar(255) NOT NULL,
#     created_at datetime(6) DEFAULT NULL,
#     updated_at datetime(6) DEFAULT NULL,
#     PRIMARY KEY (id),
#     KEY        idx_subject_id (subject_id),
#     CONSTRAINT fk_interesting_subject FOREIGN KEY (subject_id) REFERENCES subject (id)
# );
#
# -- 13. interesting_and_agit 테이블
# DROP TABLE IF EXISTS interesting_and_agit;
#
# CREATE TABLE interesting_and_agit
# (
#     id             bigint NOT NULL AUTO_INCREMENT,
#     agit_id        bigint DEFAULT NULL,
#     interesting_id bigint DEFAULT NULL,
#     created_at     datetime(6) DEFAULT NULL,
#     updated_at     datetime(6) DEFAULT NULL,
#     PRIMARY KEY (id),
#     KEY            idx_agit_id (agit_id),
#     KEY            idx_interesting_id (interesting_id),
#     CONSTRAINT fk_interesting_agit_agit FOREIGN KEY (agit_id) REFERENCES agit (id),
#     CONSTRAINT fk_interesting_agit_interesting FOREIGN KEY (interesting_id) REFERENCES interesting (id)
# );
#
# -- 14. member_interesting 테이블
# DROP TABLE IF EXISTS member_interesting;
#
# CREATE TABLE member_interesting
# (
#     id             bigint NOT NULL AUTO_INCREMENT,
#     interesting_id bigint DEFAULT NULL,
#     member_id      bigint DEFAULT NULL,
#     created_at     datetime(6) DEFAULT NULL,
#     updated_at     datetime(6) DEFAULT NULL,
#     PRIMARY KEY (id),
#     KEY            idx_interesting_id (interesting_id),
#     KEY            idx_member_id (member_id),
#     CONSTRAINT fk_member_interesting_interesting FOREIGN KEY (interesting_id) REFERENCES interesting (id),
#     CONSTRAINT fk_member_interesting_member FOREIGN KEY (member_id) REFERENCES member (id)
# );
#
# -- 15. member_join_agit 테이블
# DROP TABLE IF EXISTS member_join_agit;
#
# CREATE TABLE member_join_agit
# (
#     id         bigint NOT NULL AUTO_INCREMENT,
#     agit_id    bigint DEFAULT NULL,
#     member_id  bigint DEFAULT NULL,
#     joined_at  datetime(6) NOT NULL,
#     role       ENUM('LEADER','MEMBER') DEFAULT 'MEMBER',
#     created_at datetime(6) DEFAULT NULL,
#     updated_at datetime(6) DEFAULT NULL,
#     PRIMARY KEY (id),
#     KEY        idx_agit_id (agit_id),
#     KEY        idx_member_id (member_id),
#     CONSTRAINT fk_member_join_agit_agit FOREIGN KEY (agit_id) REFERENCES agit (id),
#     CONSTRAINT fk_member_join_agit_member FOREIGN KEY (member_id) REFERENCES member (id)
# );
#
# -- 16. address 테이블
# DROP TABLE IF EXISTS address;
#
# CREATE TABLE address
# (
#     id             bigint       NOT NULL AUTO_INCREMENT,
#     member_id      bigint       DEFAULT NULL,
#     address_do     varchar(255) DEFAULT NULL,
#     address_si     varchar(255) NOT NULL,
#     address_gu_gun varchar(255) NOT NULL,
#     address_dong   varchar(255) NOT NULL,
#     address_type   ENUM('COMPANY','HOME','OTHER') NOT NULL,
#     created_at     datetime(6) DEFAULT NULL,
#     updated_at     datetime(6) DEFAULT NULL,
#     PRIMARY KEY (id),
#     KEY            idx_member_id (member_id),
#     CONSTRAINT fk_address_member FOREIGN KEY (member_id) REFERENCES member (id)
# );
#
# -- 17. agit_and_account 테이블
# DROP TABLE IF EXISTS agit_and_account;
#
# CREATE TABLE agit_and_account
# (
#     id         bigint NOT NULL AUTO_INCREMENT,
#     account_id bigint DEFAULT NULL,
#     agit_id    bigint DEFAULT NULL,
#     created_at datetime(6) DEFAULT NULL,
#     updated_at datetime(6) DEFAULT NULL,
#     PRIMARY KEY (id),
#     KEY        idx_account_id (account_id),
#     KEY        idx_agit_id (agit_id),
#     CONSTRAINT fk_agit_account_account FOREIGN KEY (account_id) REFERENCES account (id),
#     CONSTRAINT fk_agit_account_agit FOREIGN KEY (agit_id) REFERENCES agit (id)
# );
#
# -- 18. regular 테이블
# DROP TABLE IF EXISTS regular;
#
# CREATE TABLE regular
# (
#     id            bigint       NOT NULL AUTO_INCREMENT,
#     is_deleted    tinyint(1) DEFAULT 0,
#     agit_id       bigint       DEFAULT NULL,
#     regular_time  datetime(6) NOT NULL,
#     content       varchar(255) NOT NULL,
#     image         varchar(255) DEFAULT NULL,
#     place         varchar(255) NOT NULL,
#     place_address varchar(255) NOT NULL,
#     regular_name  varchar(255) NOT NULL,
#     created_at    datetime(6) DEFAULT NULL,
#     updated_at    datetime(6) DEFAULT NULL,
#     PRIMARY KEY (id),
#     KEY           idx_agit_id (agit_id),
#     CONSTRAINT fk_regular_agit FOREIGN KEY (agit_id) REFERENCES agit (id)
# );
#
# -- 19. yaggwan 테이블
# DROP TABLE IF EXISTS yaggwan;
#
# CREATE TABLE yaggwan
# (
#     id         bigint NOT NULL AUTO_INCREMENT,
#     member_id  bigint DEFAULT NULL,
#     is_agreed  bit(1) NOT NULL,
#     agree_date datetime(6) NOT NULL,
#     created_at datetime(6) DEFAULT NULL,
#     updated_at datetime(6) DEFAULT NULL,
#     PRIMARY KEY (id),
#     KEY        idx_member_id (member_id),
#     CONSTRAINT fk_yaggwan_member FOREIGN KEY (member_id) REFERENCES member (id)
# );
