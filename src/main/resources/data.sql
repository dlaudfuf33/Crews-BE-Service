insert into member(email,name,nick_name,password,phone_number,ci, role) values('1234@gmail.com','sws','sw2','1234','010-0000-0000','ID001','ROLE_USER');
-- Bank 테이블에 더미 데이터 삽입
INSERT INTO bank (created_at, updated_at, bank_code, bank_name)
VALUES (NOW(), NOW(), '020', '우리은행'),
       (NOW(), NOW(), '002', '하나은행');

-- Member 테이블에 더미 데이터 삽입
INSERT INTO member (is_deleted, created_at, updated_at, ci, email, name, nick_name, password, phone_number,
                    profile_image, role)
VALUES (FALSE, NOW(), NOW(), 'CI001', 'user1@example.com', '홍길동', '길동', 'password123', '01012345678', 'profile1.png',
        'ROLE_USER'),
       (FALSE, NOW(), NOW(), 'CI002', 'user2@example.com', '김영희', '영희', 'password456', '01056781234', 'profile2.png',
        'ROLE_ADMIN');

-- Account 테이블에 더미 데이터 삽입
INSERT INTO account (balance, bank_id, created_at, member_id, updated_at, account_number, fintec_number,
                     masked_account_number, account_type)
VALUES (100000.00, 1, NOW(), 1, NOW(), '1234567890', 'FT001', '*****890', 'PERSONAL'),
       (250000.00, 2, NOW(), 2, NOW(), '9876543210', 'FT002', '*****210', 'CREW');

-- Account History 테이블에 더미 데이터 삽입
INSERT INTO account_history (account_id, after_balance_amount, created_at, transaction_amount, transaction_time,
                             updated_at, card_number, description, tran_type)
VALUES (1, 90000, NOW(), 10000, NOW(), NOW(), '1234567890121234', 'ATM Withdrawal', 'WITHDRAW'),
       (2, 260000, NOW(), 10000, NOW(), NOW(), '9876543210981234', 'Direct Deposit', 'DEPOSIT');

-- Address 테이블에 더미 데이터 삽입
INSERT INTO address (created_at, member_id, updated_at, address_do, address_dong, address_gu_gun, address_si,
                     address_type)
VALUES (NOW(), 1, NOW(), '서울특별시', '중구', '명동', '서울', 'HOME'),
       (NOW(), 2, NOW(), '경기도', '수원시', '팔달구', '수원', 'COMPANY');

-- Subject 테이블에 더미 데이터 삽입
INSERT INTO subject (id, created_at, updated_at, subject_name)
VALUES (1, NOW(), NOW(), '프로그래밍'),
       (2, NOW(), NOW(), '디자인'),
       (3, NOW(), NOW(), '마케팅');

-- Card 테이블에 더미 데이터 삽입
INSERT INTO card (is_deleted, account_id, created_at, member_id, registered_at, updated_at, card_number,
                  masked_card_number)
VALUES (FALSE, 1, NOW(), 1, NOW(), NOW(), '1234567812345678', '********12345678'),
       (FALSE, 2, NOW(), 2, NOW(), NOW(), '9876543298765432', '********98765432');

-- Agit 테이블에 더미 데이터 삽입
INSERT INTO agit (current_person, is_deleted, is_due, max_person, created_at, subject_id, updated_at, agit_name, introduction)
VALUES (1, FALSE, TRUE, 10, NOW(), 1,NOW(), 'Agit 1', 'hi'),
       (2, FALSE, FALSE, 15, NOW(), 2,NOW(), 'Agit 2', 'hi2');

-- Membership 테이블에 더미 데이터 삽입
INSERT INTO membership (agit_id, created_at, joined_at, member_id, updated_at, role)
VALUES (1, NOW(), NOW(), 1, NOW(), 'LEADER'),
       (2, NOW(), NOW(), 2, NOW(), 'MEMBER');

INSERT INTO common_dues (due_day, due_amount, created_at, updated_at, agit_id)
VALUES
    ('3', 500.00, NOW(), NOW(), 1),
    ('3', 750.50, NOW(), NOW(), 2);

-- Dues 테이블에 더미 데이터 삽입
INSERT INTO dues (due_date, created_at, due_amount, updated_at, membership_id, common_dues_id, is_payed)
VALUES (NOW(), NOW(), 1000.00, NOW(), 1, 1, FALSE),
       (NOW(), NOW(), 2000.00, NOW(), 1, 2), FALSE;


-- Feed 테이블에 더미 데이터 삽입
INSERT INTO feed (is_deleted, agit_id, created_at, member_id, updated_at, content, image, like_count)
VALUES (FALSE, 1, NOW(), 1, NOW(), '첫 번째 피드 내용입니다.', 'feed1.png', '10'),
       (FALSE, 2, NOW(), 2, NOW(), '두 번째 피드 내용입니다.', 'feed2.png', '20');

-- Heart 테이블에 더미 데이터 삽입
INSERT INTO heart (created_at, feed_id, member_id, updated_at)
VALUES (NOW(), 1, 1, NOW()),
       (NOW(), 2, 2, NOW());

-- Interesting 테이블에 더미 데이터 삽입

INSERT INTO interesting (id, created_at, subject_id, updated_at, name)
VALUES (1, NOW(), 1, NOW(), '알고리즘 최적화'),
       (2, NOW(), 1, NOW(), '인공지능 연구'),
       (3, NOW(), 1, NOW(), '데이터베이스 설계'),
       (4, NOW(), 2, NOW(), 'UX/UI 디자인'),
       (5, NOW(), 2, NOW(), '브랜드 아이덴티티'),
       (6, NOW(), 2, NOW(), '그래픽 디자인'),
       (7, NOW(), 3, NOW(), '콘텐츠 마케팅'),
       (8, NOW(), 3, NOW(), '디지털 마케팅 전략'),
       (9, NOW(), 3, NOW(), 'SNS 마케팅');

-- Interesting And Agit 테이블에 더미 데이터 삽입
INSERT INTO interesting_and_agit (agit_id, interesting_id, created_at, updated_at)
VALUES (1, 1, NOW(), NOW()),
       (2, 2, NOW(), NOW()),
       (1, 3, NOW(), NOW()),
       (1, 8, NOW(), NOW()),
       (2, 4, NOW(), NOW()),
       (2, 6, NOW(), NOW());

-- Introducing 테이블에 더미 데이터 삽입
INSERT INTO introducing (agit_id, created_at, updated_at, content, image, introduce)
VALUES (1, NOW(), NOW(), '첫 번째 소개글입니다.', 'intro1.png', '첫 번째 소개'),
       (2, NOW(), NOW(), '두 번째 소개글입니다.', 'intro2.png', '두 번째 소개');

-- Member And Interesting 테이블에 더미 데이터 삽입
INSERT INTO member_and_interesting (interesting_id, member_id, created_at, updated_at)
VALUES (1, 1, NOW(), NOW()),
       (2, 2, NOW(), NOW());

-- Regular Crewing 테이블에 더미 데이터 삽입
INSERT INTO meeting (is_deleted, agit_id, created_at, regular_time, updated_at, content, image, place, regular_name)
VALUES (FALSE, 1, NOW(), NOW(), NOW(), '첫 번째 정기 크루잉', 'crew1.png', '서울시 중구 명동', '정기 크루잉 1'),
       (FALSE, 2, NOW(), NOW(), NOW(), '두 번째 정기 크루잉', 'crew2.png', '경기도 수원시 팔달구','정기 크루잉 2');
