insert into member(email, name, nick_name, password, phone_number, ci, role)
values ('test1@test.com', 'tester1', '파란QA', '1234', '010-0000-0000', 'ID001', 'ROLE_USER');
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
                     masked_account_number, account_type, product_name)
VALUES (100000.00, 1, NOW(), 1, NOW(), '1234567890', 'FT001', '*****890', 'PERSONAL', '우리 일반통장 상품'),
       (250000.00, 2, NOW(), 2, NOW(), '9876543210', 'FT002', '*****210', 'CREW', '우리 일반통장 상품');

-- Account History 테이블에 더미 데이터 삽입
INSERT INTO account_history (account_id, after_balance_amount, created_at, transaction_amount, transaction_time,
                             updated_at, card_number, description, tran_type)
VALUES (1, 90000, NOW(), 10000, NOW(), NOW(), '1234567890121234', 'ATM Withdrawal', 'WITHDRAW'),
       (2, 260000, NOW(), 10000, NOW(), NOW(), '9876543210981234', 'Direct Deposit', 'DEPOSIT');

-- Address 테이블에 더미 데이터 삽입
INSERT INTO address (id, unique_address_key, created_at, updated_at, address_do, address_si, address_gu_gun,
                     address_dong)
VALUES (1, '없음-서울-관악구-신림동', NOW(), NOW(), '없음', '서울', '관악구', '신림동'),
       (2, '경기-수원시-팔달구-수원', NOW(), NOW(), '경기', '성남시', '분당구', '백현동');
-- Subject 테이블에 더미 데이터 삽입
INSERT INTO subject (id, created_at, updated_at, subject_name)
VALUES (1, NOW(), NOW(), '미분류'),
       (2, NOW(), NOW(), '자기계발/공부'),
       (3, NOW(), NOW(), '운동'),
       (4, NOW(), NOW(), '여행'),
       (5, NOW(), NOW(), '반려동물'),
       (6, NOW(), NOW(), '게임/오락'),
       (7, NOW(), NOW(), '식도락');


-- Card 테이블에 더미 데이터 삽입
INSERT INTO card (is_deleted, account_id, created_at, member_id, registered_at, updated_at, card_number,
                  masked_card_number)
VALUES (FALSE, 1, NOW(), 1, NOW(), NOW(), '1234567812345678', '********12345678'),
       (FALSE, 2, NOW(), 2, NOW(), NOW(), '9876543298765432', '********98765432');

-- Agit 테이블에 더미 데이터 삽입
INSERT INTO agit (current_person, is_deleted, is_due, max_person, created_at, subject_id, updated_at, agit_name,
                  introduction, address_id)
VALUES (1, FALSE, TRUE, 10, NOW(), 2, NOW(), 'Agit 1', 'hi', 1),
       (2, FALSE, FALSE, 15, NOW(), 2, NOW(), 'Agit 2', 'hi2', 2);
-- Membership 테이블에 더미 데이터 삽입
INSERT INTO membership (agit_id, created_at, joined_at, member_id, updated_at, role)
VALUES (1, NOW(), NOW(), 1, NOW(), 'LEADER'),
       (2, NOW(), NOW(), 2, NOW(), 'MEMBER');

INSERT INTO common_dues (due_day, due_amount, created_at, updated_at, agit_id)
VALUES ('3', 500.00, NOW(), NOW(), 1),
       ('3', 750.50, NOW(), NOW(), 2);

-- Dues 테이블에 더미 데이터 삽입
INSERT INTO dues (due_date, created_at, due_amount, updated_at, membership_id, common_dues_id, is_payed, product_name,
                  account_number, agit_name)
VALUES (NOW(), NOW(), 1000.00, NOW(), 1, 1, FALSE, '우리 일반통장 상품', '1234', 'agit1'),
       (NOW(), NOW(), 2000.00, NOW(), 1, 2, FALSE, '우리 일반통장 상품', '1234', 'agit2');


-- Feed 테이블에 더미 데이터 삽입
INSERT INTO feed (is_deleted, agit_id, created_at, member_id, updated_at, content, image, like_count)
VALUES (FALSE, 1, NOW(), 1, NOW(), '첫 번째 피드 내용입니다.', 'feed1.png', '10'),
       (FALSE, 2, NOW(), 2, NOW(), '두 번째 피드 내용입니다.', 'feed2.png', '20');

-- Heart 테이블에 더미 데이터 삽입
INSERT INTO heart (created_at, feed_id, member_id, updated_at)
VALUES (NOW(), 1, 1, NOW()),
       (NOW(), 2, 2, NOW());

-- Interesting 테이블에 더미 데이터 삽입

INSERT INTO interesting (id, created_at, updated_at, name, subject_id)
VALUES (1, NOW(), NOW(), '미설정', 1),

       -- 자기계발/공부
       (2, NOW(), NOW(), '외국어 공부', 2),
       (3, NOW(), NOW(), '독서 모임', 2),
       (4, NOW(), NOW(), '모각코', 2),
       (5, NOW(), NOW(), '자격증 준비', 2),

       -- 운동
       (6, NOW(), NOW(), '등산', 3),
       (7, NOW(), NOW(), '조기축구회', 3),
       (8, NOW(), NOW(), '해양 스포츠', 3),
       (9, NOW(), NOW(), '클라이밍', 3),
       (10, NOW(), NOW(), '피트니스', 3),
       (11, NOW(), NOW(), '러닝', 3),
       (12, NOW(), NOW(), '요가 및 필라테스', 3),
       (13, NOW(), NOW(), '격투기', 3),

       -- 여행
       (14, NOW(), NOW(), '관광 명소', 4),
       (15, NOW(), NOW(), '역사 탐방', 4),
       (16, NOW(), NOW(), '로드트립', 4),
       (17, NOW(), NOW(), '액티비티', 4),
       (18, NOW(), NOW(), '캠핑', 4),
       (19, NOW(), NOW(), '글램핑', 4),
       (20, NOW(), NOW(), '에코 투어리즘', 4),

       -- 반려동물
       (21, NOW(), NOW(), '훈련 및 교육', 5),
       (22, NOW(), NOW(), '펫 패션', 5),
       (23, NOW(), NOW(), '장난감', 5),
       (24, NOW(), NOW(), '산책', 5),
       (25, NOW(), NOW(), '임시보호', 5),
       (26, NOW(), NOW(), '보호소 봉사', 5),
       (27, NOW(), NOW(), '입양 캠페인', 5),

       -- 게임/오락
       (28, NOW(), NOW(), '온라인 게임', 6),
       (29, NOW(), NOW(), '콘솔 게임', 6),
       (30, NOW(), NOW(), '보드게임', 6),
       (31, NOW(), NOW(), 'e스포츠', 6),
       (32, NOW(), NOW(), 'VR/AR', 6),
       (33, NOW(), NOW(), '스트리밍', 6),
       (34, NOW(), NOW(), '레트로 게임', 6),

       -- 식도락
       (35, NOW(), NOW(), '미식 탐방', 7),
       (36, NOW(), NOW(), '스트리트 푸드', 7),
       (37, NOW(), NOW(), '비건', 7),
       (38, NOW(), NOW(), '건강식', 7),
       (39, NOW(), NOW(), '디저트', 7),
       (40, NOW(), NOW(), '와인', 7),
       (41, NOW(), NOW(), '농장 체험', 7),
       (42, NOW(), NOW(), '로컬 푸드', 7);

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
       (FALSE, 2, NOW(), NOW(), NOW(), '두 번째 정기 크루잉', 'crew2.png', '경기도 수원시 팔달구', '정기 크루잉 2');
