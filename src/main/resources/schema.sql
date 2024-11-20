-- 1. 기존 CREWS 데이터베이스 삭제 (존재할 경우)
DROP DATABASE IF EXISTS CREWS;

-- 2. CREWS 데이터베이스 생성
CREATE DATABASE CREWS;

-- 3. CREWS 데이터베이스 사용
USE CREWS;

-- Bank 테이블
CREATE TABLE bank
(
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    bank_code  VARCHAR(255) NOT NULL,
    bank_name  VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (bank_code)
);

-- Member 테이블
CREATE TABLE member
(
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    is_deleted    BOOLEAN               DEFAULT FALSE,
    created_at    DATETIME(6),
    updated_at    DATETIME(6),
    ci            VARCHAR(88) NOT NULL,
    email         VARCHAR(255) NOT NULL,
    name          VARCHAR(255) NOT NULL,
    nick_name     VARCHAR(16) NOT NULL,
    password      VARCHAR(255) NOT NULL,
    phone_number  VARCHAR(255) NOT NULL,
    profile_image VARCHAR(255),
    role          VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER',
    PRIMARY KEY (id),
    UNIQUE (email)
);

-- Account 테이블
CREATE TABLE account
(
    id                    BIGINT                    NOT NULL AUTO_INCREMENT,
    balance               DECIMAL(38, 2) DEFAULT 0,
    bank_id               BIGINT,
    created_at            DATETIME(6),
    member_id             BIGINT,
    updated_at            DATETIME(6),
    account_number        VARCHAR(255)              NOT NULL,
    fintec_number         VARCHAR(255)              NOT NULL,
    masked_account_number VARCHAR(255)              NOT NULL,
    account_type          ENUM ('CREW', 'PERSONAL') NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (bank_id) REFERENCES bank (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);

-- Account History 테이블
CREATE TABLE account_history
(
    id                   BIGINT                       NOT NULL AUTO_INCREMENT,
    account_id           BIGINT,
    after_balance_amount BIGINT                       NOT NULL,
    created_at           DATETIME(6),
    transaction_amount   BIGINT                       NOT NULL,
    transaction_time     DATETIME(6)                  NOT NULL,
    updated_at           DATETIME(6),
    card_number          VARCHAR(255),
    description          VARCHAR(255)                 NOT NULL,
    tran_type            ENUM ('DEPOSIT', 'WITHDRAW') NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (account_id) REFERENCES account (id)
);

-- Address 테이블
CREATE TABLE address
(
    id             BIGINT                            NOT NULL AUTO_INCREMENT,
    created_at     DATETIME(6),
    member_id      BIGINT,
    updated_at     DATETIME(6),
    address_do     VARCHAR(255),
    address_dong   VARCHAR(255)                      NOT NULL,
    address_gu_gun VARCHAR(255)                      NOT NULL,
    address_si     VARCHAR(255)                      NOT NULL,
    address_type   ENUM ('COMPANY', 'HOME', 'OTHER') NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);
-- Dues 테이블
CREATE TABLE dues
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    due_date    TINYINT      NOT NULL,
    created_at  DATETIME(6),
    due_amount  BIGINT       NOT NULL,
    updated_at  DATETIME(6),
    member_data VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);
-- Subject 테이블
CREATE TABLE subject
(
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    created_at   DATETIME(6),
    updated_at   DATETIME(6),
    subject_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);
-- Agit 테이블
CREATE TABLE agit
(
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    current_person INT     DEFAULT 1,
    is_deleted     BOOLEAN DEFAULT FALSE,
    is_due         BOOLEAN DEFAULT FALSE NOT NULL,
    introduction   VARCHAR(255) NOT NULL,

    max_person     INT     DEFAULT 30,
    created_at     DATETIME(6),
    dues_id        BIGINT,
    subject_id     BIGINT,
    updated_at     DATETIME(6),
    agit_name      VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (agit_name),
    FOREIGN KEY (dues_id) REFERENCES dues (id),
    FOREIGN KEY (subject_id) REFERENCES subject (id)
);

-- Agit And Account 테이블
CREATE TABLE agit_and_account
(
    id         BIGINT NOT NULL AUTO_INCREMENT,
    account_id BIGINT,
    agit_id    BIGINT,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    PRIMARY KEY (id),
    FOREIGN KEY (account_id) REFERENCES account (id),
    FOREIGN KEY (agit_id) REFERENCES agit (id)
);


-- Card 테이블
CREATE TABLE card
(
    id                 BIGINT       NOT NULL AUTO_INCREMENT,
    is_deleted         BOOLEAN DEFAULT FALSE,
    account_id         BIGINT,
    created_at         DATETIME(6),
    member_id          BIGINT,
    registered_at      DATETIME(6)  NOT NULL,
    updated_at         DATETIME(6),
    card_number        VARCHAR(255) NOT NULL,
    masked_card_number VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (account_id) REFERENCES account (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);


-- Feed 테이블
CREATE TABLE feed
(
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    is_deleted BOOLEAN      DEFAULT FALSE,
    agit_id    BIGINT,
    created_at DATETIME(6),
    member_id  BIGINT,
    updated_at DATETIME(6),
    content    VARCHAR(255) NOT NULL,
    image      VARCHAR(255),
    like_count VARCHAR(255) DEFAULT '0',
    PRIMARY KEY (id),
    FOREIGN KEY (agit_id) REFERENCES agit (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);

-- Heart 테이블
CREATE TABLE heart
(
    id         BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6),
    feed_id    BIGINT,
    member_id  BIGINT,
    updated_at DATETIME(6),
    PRIMARY KEY (id),
    FOREIGN KEY (feed_id) REFERENCES feed (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);

-- Interesting 테이블
CREATE TABLE interesting
(
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6),
    subject_id BIGINT,
    updated_at DATETIME(6),
    name       VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (subject_id) REFERENCES subject (id)
);

-- Interesting And Agit 테이블
CREATE TABLE interesting_and_agit
(
    id             BIGINT NOT NULL AUTO_INCREMENT,
    agit_id        BIGINT,
    interesting_id BIGINT,
    created_at     DATETIME(6),
    updated_at     DATETIME(6),
    PRIMARY KEY (id),
    FOREIGN KEY (agit_id) REFERENCES agit (id),
    FOREIGN KEY (interesting_id) REFERENCES interesting (id)
);

-- Introducing 테이블
CREATE TABLE introducing
(
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    agit_id    BIGINT,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    content    VARCHAR(255) NOT NULL,
    image      VARCHAR(255),
    introduce  VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (agit_id) REFERENCES agit (id)
);


-- Member And Interesting 테이블
CREATE TABLE member_and_interesting
(
    id             BIGINT NOT NULL AUTO_INCREMENT,
    interesting_id BIGINT,
    member_id      BIGINT,
    created_at     DATETIME(6),
    updated_at     DATETIME(6),
    PRIMARY KEY (id),
    FOREIGN KEY (interesting_id) REFERENCES interesting (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);

-- Membership 테이블
CREATE TABLE membership
(
    id         BIGINT      NOT NULL AUTO_INCREMENT,
    agit_id    BIGINT,
    created_at DATETIME(6),
    joined_at  DATETIME(6) NOT NULL,
    member_id  BIGINT,
    updated_at DATETIME(6),
    role       ENUM ('LEADER', 'MEMBER', 'STAFF') DEFAULT 'MEMBER',
    PRIMARY KEY (id),
    FOREIGN KEY (agit_id) REFERENCES agit (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);

-- Refresh Entity 테이블
CREATE TABLE refresh_entity
(
    id         BIGINT NOT NULL AUTO_INCREMENT,
    expiration VARCHAR(255),
    refresh    VARCHAR(255),
    username   VARCHAR(255),
    PRIMARY KEY (id)
);

-- Regular Crewing 테이블
CREATE TABLE meeting
(
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    is_deleted    BOOLEAN DEFAULT FALSE,
    agit_id       BIGINT,
    created_at    DATETIME(6),
    regular_time  DATETIME(6)  NOT NULL,
    updated_at    DATETIME(6),
    content       VARCHAR(255) NOT NULL,
    image         VARCHAR(255),
    place         VARCHAR(255) NOT NULL,
    regular_name  VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (agit_id) REFERENCES agit (id)
);


