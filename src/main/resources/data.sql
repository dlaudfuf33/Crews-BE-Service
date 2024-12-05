-- Address 테이블에 더미 데이터 삽입
INSERT INTO address (id, unique_address_key, created_at, updated_at, address_do, address_si, address_gu_gun,
                     address_dong)
VALUES (1, '없음-서울-관악구-신림동', NOW(), NOW(), '없음', '서울', '관악구', '신림동'),
       (2, '경기-수원시-팔달구-백현동', NOW(), NOW(), '경기', '수원시', '팔달구', '백현동'),
       (3, '경기-용인시-수지구-죽전동', NOW(), NOW(), '경기', '용인시', '수지구', '죽전동'),
       (4, '없음-서울-마포구-상암동', NOW(), NOW(), '없음', '서울', '마포구', '상암동'),
       (5, '없음-서울-마포구-노고산동', NOW(), NOW(), '없음', '서울', '마포구', '노고산동'),
       (6, '없음-서울-서초구-양재동', NOW(), NOW(), '없음', '서울', '서초구', '양재동'),
       (7, '없음-서울-서초구-서초동', NOW(), NOW(), '없음', '서울', '서초구', '서초동'),
       (8, '경기-고양시-일산동구-성석동', NOW(), NOW(), '경기', '고양시', '일산동구', '성석동'),
       (9, '경기-수원시-영통구-이의동', NOW(), NOW(), '경기', '수원시', '영통구', '이의동'),
       (10, '경기-수원시-권선구-서둔동', NOW(), NOW(), '경기', '수원시', '권선구', '서둔동'),
       (11, '경기-성남시-분당구-백현동', NOW(), NOW(), '경기', '성남시', '분당구', '백현동'),
       (12, '경기-성남시-분당구-정자동', NOW(), NOW(), '경기', '성남시', '분당구', '정자동'),
       (13, '없음-서울-강남구-역삼동', NOW(), NOW(), '없음', '서울', '강남구', '역삼동'),
       (14, '경기-용인시-수지구-동천동', NOW(), NOW(), '경기', '용인시', '수지구', '동천동'),
       (15, '없음-서울-종로구-삼청동', NOW(), NOW(), '없음', '서울', '종로구', '삼청동');


-- Bank 테이블에 더미 데이터 삽입
INSERT INTO bank (created_at, updated_at, bank_code, bank_name, bank_image)
VALUES (NOW(), NOW(), '020', '우리은행',
        'https://hwamockyee.s3.ap-northeast-2.amazonaws.com/crews/banklogo/woori.png'),
       (NOW(), NOW(), '081', '하나은행',
        'https://hwamockyee.s3.ap-northeast-2.amazonaws.com/crews/banklogo/hana.png'),
       (NOW(), NOW(), '088', '신한은행',
        'https://hwamockyee.s3.ap-northeast-2.amazonaws.com/crews/banklogo/shinhan.png'),
       (NOW(), NOW(), '092', '토스',
        'https://hwamockyee.s3.ap-northeast-2.amazonaws.com/crews/banklogo/toss.png'),
       (NOW(), NOW(), '090', '카카오뱅크',
        'https://hwamockyee.s3.ap-northeast-2.amazonaws.com/crews/banklogo/kakao.png');

-- Member 테이블에 더미 데이터 삽입
INSERT INTO member (is_deleted, created_at, updated_at, ci, email, name, nick_name, password, phone_number, pin_number,
                    profile_image, role)
VALUES (FALSE, NOW(), NOW(), 'CI011', 'guest11@test.com',
        '홍길동', '빨간모자', 'password123', '01012341111', '000000',
        'https://picsum.photos/250/250', 'ROLE_USER'),
       (FALSE, NOW(), NOW(), 'CI012', 'guest12@test.com',
        '김영희', '파란양말', 'password456', '01056781234', '000000',
        'https://picsum.photos/250/250', 'ROLE_USER'),
       (FALSE, NOW(), NOW(), 'CI013', 'guest13@test.com',
        '이철수', '초록갑판장', 'password789', '01098765432', '000000',
        'https://picsum.photos/250/250', 'ROLE_USER'),
       (FALSE, NOW(), NOW(), 'CI014', 'guest14@test.com',
        '박민수', '노란선장', 'password012', '01011112222', '000000',
        'https://picsum.photos/250/250', 'ROLE_USER'),
       (FALSE, NOW(), NOW(), 'CI015', 'guest15@test.com',
        '최영준', '보라구명조끼', 'password345', '01033334444', '000000',
        'https://picsum.photos/250/250', 'ROLE_USER'),
       (FALSE, NOW(), NOW(), 'CI016', 'guest16@test.com',
        '오현정', '검은돛', 'password678', '01055556666', '000000',
        'https://picsum.photos/250/250', 'ROLE_USER'),
       (FALSE, NOW(), NOW(), 'CI017', 'guest17@test.com',
        '윤수민', '흰배꼽종', 'password901', '01077778888', '000000',
        'https://picsum.photos/250/250', 'ROLE_USER'),
       (FALSE, NOW(), NOW(), 'CI018', 'guest18@test.com',
        '한지민', '분홍객실', 'password234', '01099990000', '000000',
        'https://picsum.photos/250/250', 'ROLE_USER'),
       (FALSE, NOW(), NOW(), 'CI019', 'guest19@test.com',
        '이하늘', '연두구명보트', 'password567', '01000001111', '000000',
        'https://picsum.photos/250/250', 'ROLE_USER'),
       (FALSE, NOW(), NOW(), 'CI020', 'guest20@test.com',
        '김희선', '연보라도크', 'password890', '01022223333', '000000',
        'https://picsum.photos/250/250', 'ROLE_USER'),
       (FALSE, NOW(), NOW(), 'CI021', 'guest21@test.com',
        '박서준', '주황등대', 'password234', '01011221122', '000000',
        'https://picsum.photos/250/250', 'ROLE_USER'),
       (FALSE, NOW(), NOW(), 'CI022', 'guest22@test.com',
        '손예진', '하늘색앵커', 'password345', '01033443344', '000000',
        'https://picsum.photos/250/250', 'ROLE_USER'),
       (FALSE, NOW(), NOW(), 'CI023', 'guest23@test.com',
        '이병헌', '자주색조타수', 'password456', '01055665566', '000000',
        'https://picsum.photos/250/250', 'ROLE_USER'),
       (FALSE, NOW(), NOW(), 'CI024', 'guest24@test.com',
        '김태희', '청록갑판', 'password567', '01077887788', '000000',
        'https://picsum.photos/250/250', 'ROLE_USER'),
       (FALSE, NOW(), NOW(), 'CI025', 'guest25@test.com',
        '정우성', '금색망원경', 'password678', '01099009900', '000000',
        'https://picsum.photos/250/250', 'ROLE_USER'),
       (FALSE, NOW(), NOW(), 'CI026', 'guest26@test.com',
        '고소영', '은색항해일지', 'password789', '01000112233', '000000',
        'https://picsum.photos/250/250', 'ROLE_USER'),
       (FALSE, NOW(), NOW(), 'CI027', 'guest27@test.com',
        '원빈', '갈색나침반', 'password890', '01022334455', '000000',
        'https://picsum.photos/250/250', 'ROLE_USER'),
       (FALSE, NOW(), NOW(), 'CI028', 'guest28@test.com',
        '김혜수', '회색비상구', 'password901', '01044556677', '000000',
        'https://picsum.photos/250/250', 'ROLE_USER'),

       (FALSE, NOW(), NOW(), 'CI029', 'guest29@test.com',
        '하정우', '민트색부표', 'password012', '01066778899', '000000',
        'https://picsum.photos/250/250', 'ROLE_USER'),
       (FALSE, NOW(), NOW(), 'CI030', 'guest30@test.com',
        '한효주', '버건디해달', 'password123', '01088990011', '000000',
        'https://picsum.photos/250/250', 'ROLE_USER');


-- Subject 테이블에 더미 데이터 삽입
INSERT INTO subject (id, created_at, updated_at, subject_name)
VALUES (1, NOW(), NOW(), '미분류'),
       (2, NOW(), NOW(), '자기계발/공부'),
       (3, NOW(), NOW(), '운동'),
       (4, NOW(), NOW(), '여행'),
       (5, NOW(), NOW(), '반려동물'),
       (6, NOW(), NOW(), '게임/오락'),
       (7, NOW(), NOW(), '식도락');

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

-- Agit 테이블에 더미 데이터 삽입
INSERT INTO agit (current_person, is_deleted, is_due, max_person, created_at, subject_id, updated_at, agit_name,
                  introduction, address_id)
VALUES (3, FALSE, FALSE, 10, NOW(), 2, NOW(), '독서 하는 모임', '함께 책을 읽는 모임입니다.', 1),
       (5, FALSE, TRUE, 15, NOW(), 3, NOW(), '러닝하는 클럽', '함께 뛰는 러닝 모임입니다.', 2),
       (8, FALSE, FALSE, 20, NOW(), 4, NOW(), '캠핑하는 모임', '자연 속 캠핑을 즐기는 모임입니다.', 3),
       (10, FALSE, FALSE, 30, NOW(), 5, NOW(), '강아지하는 산책 모임', '반려견과 함께하는 산책 모임입니다.', 4),
       (6, FALSE, TRUE, 25, NOW(), 6, NOW(), '보드게임하는 모임', '보드게임을 즐기는 모임입니다.', 5),
       (9, FALSE, FALSE, 20, NOW(), 7, NOW(), '요가하는 클래스', '심신 안정을 위한 요가 시간.', 6),
       (7, FALSE, FALSE, 15, NOW(), 2, NOW(), '외국어하는 스터디', '외국어를 배우는 모임입니다.', 7),
       (4, FALSE, TRUE, 8, NOW(), 3, NOW(), '등산하는 동호회', '등산을 즐기는 자연 모임입니다.', 8),
       (2, FALSE, FALSE, 12, NOW(), 4, NOW(), '미식하는 탐방', '다양한 음식을 탐방하는 모임입니다.', 9),
       (1, FALSE, TRUE, 5, NOW(), 5, NOW(), '디저트하는 클럽', '디저트를 함께 나누는 모임입니다.', 10),
       -- 자기계발/공부
       (3, FALSE, TRUE, 10, NOW(), 2, NOW(), '독서 모임', '매주 함께 책을 읽고 토론하는 모임입니다.', 1),
       (5, FALSE, FALSE, 15, NOW(), 2, NOW(), '외국어 스터디', '영어 및 다양한 외국어를 공부하는 모임입니다.', 2),
       (8, FALSE, FALSE, 20, NOW(), 2, NOW(), '코딩 스터디', '프로그래밍과 IT 지식을 나누는 모임입니다.', 3),

-- 운동
       (10, FALSE, FALSE, 30, NOW(), 3, NOW(), '러닝 클럽', '함께 뛰면서 건강과 체력을 기르는 모임입니다.', 4),
       (7, FALSE, TRUE, 25, NOW(), 3, NOW(), '등산 동호회', '산을 오르며 자연을 느끼는 활동을 함께합니다.', 5),
       (12, FALSE, FALSE, 20, NOW(), 3, NOW(), '요가 클래스', '심신의 안정을 위한 요가 수업입니다.', 6),

-- 여행
       (4, FALSE, FALSE, 10, NOW(), 4, NOW(), '캠핑 모임', '자연 속에서 캠핑을 즐기는 모임입니다.', 7),
       (2, FALSE, TRUE, 5, NOW(), 4, NOW(), '역사 탐방', '역사적인 명소를 함께 여행합니다.', 8),
       (6, FALSE, FALSE, 15, NOW(), 4, NOW(), '로드트립', '자동차로 떠나는 여행을 즐기는 모임입니다.', 9),

-- 반려동물
       (3, FALSE, FALSE, 10, NOW(), 5, NOW(), '강아지 산책 모임', '반려견과 함께 산책하는 모임입니다.', 10),
       (5, FALSE, TRUE, 8, NOW(), 5, NOW(), '캣 카페 탐방', '고양이 카페를 함께 방문하는 모임입니다.', 11),

-- 게임/오락
       (9, FALSE, FALSE, 20, NOW(), 6, NOW(), '보드게임 모임', '다양한 보드게임을 즐기는 모임입니다.', 12),
       (15, FALSE, FALSE, 30, NOW(), 6, NOW(), '온라인 게임 팀', '온라인 게임을 함께 즐기는 모임입니다.', 13),

-- 식도락
       (6, FALSE, FALSE, 15, NOW(), 7, NOW(), '미식 탐방', '다양한 음식을 함께 즐기는 모임입니다.', 14),
       (8, FALSE, TRUE, 20, NOW(), 7, NOW(), '디저트 클럽', '디저트 카페를 함께 탐방합니다.', 15);


-- Account 테이블에 더미 데이터 삽입
INSERT INTO account (balance, bank_id, created_at, member_id, updated_at, account_number, fintec_number,
                     masked_account_number, account_type, product_name)
VALUES (300000.00, 5, NOW(), 1, NOW(), '1002844028454', 'FT2401', '*****345', 'CREW',
        '카카오 모임통장 상품'),                                                                              -- bank_id = 5 (카카오뱅크)
       (250000.00, 4, NOW(), 2, NOW(), '5432482154321', 'FT2012', '*****432', 'CREW', '토스 모임통장 상품'), -- bank_id = 4 (토스)
       (400000.00, 5, NOW(), 3, NOW(), '1111222521233', 'FT2203', '*****223', 'CREW',
        '카카오 모임통장 상품'),                                                                              -- bank_id = 5 (카카오뱅크)
       (200000.00, 4, NOW(), 4, NOW(), '2222333394144', 'FT2014', '*****334', 'CREW', '토스 모임통장 상품'), -- bank_id = 4 (토스)
       (500000.00, 5, NOW(), 5, NOW(), '3337344442155', 'FT2015', '*****445', 'CREW',
        '카카오 모임통장 상품'),                                                                              -- bank_id = 5 (카카오뱅크)
       (275000.00, 4, NOW(), 6, NOW(), '4477445555266', 'FT2036', '*****556', 'CREW', '토스 모임통장 상품'), -- bank_id = 4 (토스)
       (300000.00, 5, NOW(), 1, NOW(), '1234123424465', 'FT2201', '*****345', 'CREW',
        '카카오 모임통장 상품'),                                                                              -- bank_id = 5 (카카오뱅크)
       (250000.00, 4, NOW(), 2, NOW(), '5436216262414', 'FT202', '*****432', 'CREW', '토스 모임통장 상품'),  -- bank_id = 4 (토스)
       (400000.00, 5, NOW(), 3, NOW(), '1111225624333', 'FT2023', '*****223', 'CREW',
        '카카오 모임통장 상품'),                                                                              -- bank_id = 5 (카카오뱅크)
       (200000.00, 4, NOW(), 4, NOW(), '2222336563344', 'FT2024', '*****334', 'CREW', '토스 모임통장 상품'), -- bank_id = 4 (토스)
       (500000.00, 5, NOW(), 5, NOW(), '3333446244455', 'FT2035', '*****445', 'CREW',
        '카카오 모임통장 상품'),                                                                              -- bank_id = 5 (카카오뱅크)
       (275000.00, 4, NOW(), 6, NOW(), '4444556465566', 'FT206', '*****556', 'CREW', '토스 모임통장 상품');


-- AgitAndAccount 테이블에 더미 데이터 삽입
INSERT INTO agit_and_account (agit_id, account_id, created_at, updated_at)
VALUES -- 자기계발/공부 관련 모임
       (1, 1, NOW(), NOW()),  -- 독서 모임 -> 계좌 1 (카카오 모임통장 상품)
       (3, 2, NOW(), NOW()),  -- 캠핑 모임 -> 계좌 2 (토스 모임통장 상품)
       (6, 3, NOW(), NOW()),  -- 요가 클래스 -> 계좌 3 (카카오 모임통장 상품)

-- 운동 관련 모임
       (8, 4, NOW(), NOW()),  -- 등산 동호회 -> 계좌 4 (토스 모임통장 상품)
       (10, 5, NOW(), NOW()), -- 디저트 클럽 -> 계좌 5 (카카오 모임통장 상품)
       (12, 6, NOW(), NOW()), -- 온라인 게임 팀 -> 계좌 6 (토스 모임통장 상품)

-- 여행 관련 모임
       (14, 7, NOW(), NOW()), -- 미식 탐방 -> 계좌 7 (카카오 모임통장 상품)
       (16, 8, NOW(), NOW()), -- 보드게임 모임 -> 계좌 8 (토스 모임통장 상품)

-- 기타 모임
       (18, 9, NOW(), NOW()), -- 러닝 클럽 -> 계좌 9 (카카오 모임통장 상품)
       (20, 10, NOW(), NOW());

UPDATE agit a
    JOIN agit_and_account aa ON a.id = aa.agit_id
SET a.agit_and_account_id = aa.id
WHERE aa.agit_id IS NOT NULL;


-- Account History 테이블에 더미 데이터 삽입
INSERT INTO account_history (account_id, after_balance_amount, created_at, transaction_amount, transaction_time,
                             updated_at, card_number, description, tran_type)
VALUES -- 독서 모임 (계좌 1 - 카카오 모임통장 상품)
       (1, 290000, NOW(), 10000, NOW(), NOW(), '1002844028454', '책 구매', 'WITHDRAW'),
       (1, 300000, NOW(), 10000, NOW(), NOW(), '1002844028454', '월간 모임 회비', 'DEPOSIT'),

-- 캠핑 모임 (계좌 2 - 토스 모임통장 상품)
       (2, 240000, NOW(), 10000, NOW(), NOW(), '5432482154321', '캠핑 장비 구매', 'WITHDRAW'),
       (2, 250000, NOW(), 10000, NOW(), NOW(), '5432482154321', '회원 회비', 'DEPOSIT'),

-- 요가 클래스 (계좌 3 - 카카오 모임통장 상품)
       (3, 390000, NOW(), 10000, NOW(), NOW(), '1111222521233', '요가 매트 구매', 'WITHDRAW'),
       (3, 400000, NOW(), 10000, NOW(), NOW(), '1111222521233', '클래스 수강료', 'DEPOSIT'),

-- 등산 동호회 (계좌 4 - 토스 모임통장 상품)
       (4, 190000, NOW(), 10000, NOW(), NOW(), '2222333394144', '등산 지도 구매', 'WITHDRAW'),
       (4, 200000, NOW(), 10000, NOW(), NOW(), '2222333394144', '등산 장비 비용', 'DEPOSIT'),

-- 디저트 클럽 (계좌 5 - 카카오 모임통장 상품)
       (5, 490000, NOW(), 10000, NOW(), NOW(), '3337344442155', '디저트 시식비', 'WITHDRAW'),
       (5, 500000, NOW(), 10000, NOW(), NOW(), '3337344442155', '월간 회비', 'DEPOSIT'),

-- 온라인 게임 팀 (계좌 6 - 토스 모임통장 상품)
       (6, 265000, NOW(), 10000, NOW(), NOW(), '4477445555266', '게임 구독료', 'WITHDRAW'),
       (6, 275000, NOW(), 10000, NOW(), NOW(), '4477445555266', '대회 상금', 'DEPOSIT'),

-- 미식 탐방 (계좌 7 - 카카오 모임통장 상품)
       (7, 290000, NOW(), 10000, NOW(), NOW(), '1234123424465', '음식 시식 비용', 'WITHDRAW'),
       (7, 300000, NOW(), 10000, NOW(), NOW(), '1234123424465', '회원비 납부', 'DEPOSIT'),

-- 보드게임 모임 (계좌 8 - 토스 모임통장 상품)
       (8, 240000, NOW(), 10000, NOW(), NOW(), '5436216262414', '보드게임 구매', 'WITHDRAW'),
       (8, 250000, NOW(), 10000, NOW(), NOW(), '5436216262414', '월간 회비', 'DEPOSIT'),

-- 러닝 클럽 (계좌 9 - 카카오 모임통장 상품)
       (9, 390000, NOW(), 10000, NOW(), NOW(), '1111225624333', '러닝 장비 구매', 'WITHDRAW'),
       (9, 400000, NOW(), 10000, NOW(), NOW(), '1111225624333', '클럽 기부금', 'DEPOSIT'),

-- 역사 탐방 (계좌 10 - 토스 모임통장 상품)
       (10, 190000, NOW(), 10000, NOW(), NOW(), '2222336563344', '투어 가이드 비용', 'WITHDRAW'),
       (10, 200000, NOW(), 10000, NOW(), NOW(), '2222336563344', '단체비 납부', 'DEPOSIT');
-- Card 테이블에 더미 데이터 삽입
INSERT INTO card (is_deleted, account_id, created_at, member_id, registered_at, updated_at, card_number,
                  masked_card_number, card_image, card_name)
VALUES (FALSE, 1, NOW(), 1, NOW(), NOW(), '1234567812345678', '12345678********',
        'https://hwamockyee.s3.ap-northeast-2.amazonaws.com/crews/kakaoCard.jpg', '카카오 모임카드'),
       (FALSE, 2, NOW(), 2, NOW(), NOW(), '9876543298765432', '98765432********',
        'https://hwamockyee.s3.ap-northeast-2.amazonaws.com/crews/kakaoCard.jpg', '카카오 모임카드'),
-- 독서 모임 (카카오 모임통장 상품 - 계좌 1)
       (FALSE, 1, NOW(), 1, NOW(), NOW(), '1234567812345678', '5678********',
        'https://hwamockyee.s3.ap-northeast-2.amazonaws.com/crews/kakaoCard.jpg', '카카오 모임카드'),

-- 캠핑 모임 (토스 모임통장 상품 - 계좌 2)
       (FALSE, 2, NOW(), 2, NOW(), NOW(), '9876543298765432', '5432********',
        'https://hwamockyee.s3.ap-northeast-2.amazonaws.com/crews/tossCard.png', '토스 모임카드'),

-- 요가 클래스 (카카오 모임통장 상품 - 계좌 3)
       (FALSE, 3, NOW(), 3, NOW(), NOW(), '1111222233334444', '4444****4444',
        'https://hwamockyee.s3.ap-northeast-2.amazonaws.com/crews/kakaoCard.jpg', '카카오 모임카드'),

-- 등산 동호회 (토스 모임통장 상품 - 계좌 4)
       (FALSE, 4, NOW(), 4, NOW(), NOW(), '2222333344445555', '4865****5555',
        'https://hwamockyee.s3.ap-northeast-2.amazonaws.com/crews/tossCard.png', '토스 모임카드'),

-- 디저트 클럽 (카카오 모임통장 상품 - 계좌 5)
       (FALSE, 5, NOW(), 5, NOW(), NOW(), '3333444455556666', '1564****6666',
        'https://hwamockyee.s3.ap-northeast-2.amazonaws.com/crews/kakaoCard.jpg', '카카오 모임카드'),

-- 온라인 게임 팀 (토스 모임통장 상품 - 계좌 6)
       (FALSE, 6, NOW(), 6, NOW(), NOW(), '4444555566667777', '4149****7777',
        'https://hwamockyee.s3.ap-northeast-2.amazonaws.com/crews/tossCard.png', '토스 모임카드'),

-- 미식 탐방 (카카오 모임통장 상품 - 계좌 7)
       (FALSE, 7, NOW(), 7, NOW(), NOW(), '5555666677778888', '8635****8888',
        'https://hwamockyee.s3.ap-northeast-2.amazonaws.com/crews/kakaoCard.jpg', '카카오 모임카드'),

-- 보드게임 모임 (토스 모임통장 상품 - 계좌 8)
       (FALSE, 8, NOW(), 8, NOW(), NOW(), '6666777788889999', '7777****9999',
        'https://hwamockyee.s3.ap-northeast-2.amazonaws.com/crews/tossCard.png', '토스 모임카드'),

-- 러닝 클럽 (카카오 모임통장 상품 - 계좌 9)
       (FALSE, 9, NOW(), 9, NOW(), NOW(), '7777888899990000', '8569****0000',
        'https://hwamockyee.s3.ap-northeast-2.amazonaws.com/crews/kakaoCard.jpg', '카카오 모임카드'),

-- 역사 탐방 (토스 모임통장 상품 - 계좌 10)
       (FALSE, 10, NOW(), 10, NOW(), NOW(), '8888999900001111', '7826****1111',
        'https://hwamockyee.s3.ap-northeast-2.amazonaws.com/crews/tossCard.png', '토스 모임카드');


-- Membership 테이블에 더미 데이터 삽입
INSERT INTO membership (agit_id, created_at, joined_at, member_id, updated_at, role)
VALUES (1, NOW(), NOW(), 1, NOW(), 'LEADER'),
       (2, NOW(), NOW(), 2, NOW(), 'MEMBER');

INSERT INTO common_dues (due_day, due_amount, created_at, updated_at, agit_id)
VALUES ('3', 500.00, NOW(), NOW(), 1),
       ('3', 750.50, NOW(), NOW(), 2);

-- Dues 테이블에 더미 데이터 삽입
INSERT INTO dues (due_date, created_at, due_amount, updated_at, membership_id, common_dues_id, is_paid, product_name,
                  account_number, agit_name, standard_date)
VALUES (NOW(), NOW(), 1000.00, NOW(), 1, 1, FALSE, '우리 일반통장 상품', '1234', 'agit1', NOW()),
       (NOW(), NOW(), 2000.00, NOW(), 1, 2, FALSE, '우리 일반통장 상품', '1234', 'agit2', NOW());


-- Feed 테이블에 더미 데이터 삽입
INSERT INTO feed (is_deleted, agit_id, created_at, member_id, updated_at, content, image, like_count)
VALUES (FALSE, 1, NOW(), 1, NOW(), '첫 번째 피드 내용입니다.', 'feed1.png', '10'),
       (FALSE, 2, NOW(), 2, NOW(), '두 번째 피드 내용입니다.', 'feed2.png', '20');

-- Heart 테이블에 더미 데이터 삽입
INSERT INTO heart (created_at, feed_id, member_id, updated_at)
VALUES (NOW(), 1, 1, NOW()),
       (NOW(), 2, 2, NOW());


-- Interesting And Agit 테이블에 더미 데이터 삽입
INSERT INTO interesting_and_agit (agit_id, interesting_id, created_at, updated_at)
VALUES (1, 6, NOW(), NOW()),
       (1, 2, NOW(), NOW()), -- 러닝하는 클럽 -> Interesting 2
       (1, 3, NOW(), NOW()), -- 독서 모임 -> Interesting 3
       (2, 8, NOW(), NOW()), -- 독서 모임 -> Interesting 8
       (3, 4, NOW(), NOW()), -- 러닝하는 클럽 -> Interesting 4
       (2, 6, NOW(), NOW()), -- 러닝하는 클럽 -> Interesting 6
       (2, 2, NOW(), NOW()),
       (3, 3, NOW(), NOW()),
       (3, 8, NOW(), NOW()),
       (4, 4, NOW(), NOW()),
       (4, 6, NOW(), NOW()),
       (5, 7, NOW(), NOW()),
       (5, 12, NOW(), NOW()),
       (5, 25, NOW(), NOW()),
       (6, 8, NOW(), NOW()),
       (6, 15, NOW(), NOW()),
       (6, 33, NOW(), NOW()),
       (7, 6, NOW(), NOW()),
       (7, 19, NOW(), NOW()),
       (7, 40, NOW(), NOW()),
       (8, 3, NOW(), NOW()),
       (8, 28, NOW(), NOW()),
       (8, 35, NOW(), NOW()),
       (9, 11, NOW(), NOW()),
       (9, 22, NOW(), NOW()),
       (9, 38, NOW(), NOW()),
       (10, 5, NOW(), NOW()),
       (10, 17, NOW(), NOW()),
       (10, 41, NOW(), NOW()),
       (11, 4, NOW(), NOW()),
       (11, 23, NOW(), NOW()),
       (11, 39, NOW(), NOW()),
       (12, 9, NOW(), NOW()),
       (12, 20, NOW(), NOW()),
       (12, 36, NOW(), NOW()),
       (13, 13, NOW(), NOW()),
       (13, 24, NOW(), NOW()),
       (13, 32, NOW(), NOW()),
       (14, 2, NOW(), NOW()),
       (14, 21, NOW(), NOW()),
       (14, 37, NOW(), NOW()),
       (15, 10, NOW(), NOW()),
       (15, 26, NOW(), NOW()),
       (15, 30, NOW(), NOW()),
       (16, 1, NOW(), NOW()),
       (16, 14, NOW(), NOW()),
       (16, 29, NOW(), NOW()),
       (17, 16, NOW(), NOW()),
       (17, 27, NOW(), NOW()),
       (17, 34, NOW(), NOW()),
       (18, 18, NOW(), NOW()),
       (18, 31, NOW(), NOW()),
       (18, 42, NOW(), NOW()),
       (19, 12, NOW(), NOW()),
       (19, 5, NOW(), NOW()),
       (19, 26, NOW(), NOW()),
       (20, 7, NOW(), NOW()),
       (20, 19, NOW(), NOW()),
       (20, 33, NOW(), NOW()),
       (21, 8, NOW(), NOW()),
       (21, 25, NOW(), NOW()),
       (21, 30, NOW(), NOW()),
       (22, 4, NOW(), NOW()),
       (22, 20, NOW(), NOW()),
       (22, 39, NOW(), NOW()),
       (23, 2, NOW(), NOW()),
       (23, 14, NOW(), NOW()),
       (23, 38, NOW(), NOW()),
       (24, 11, NOW(), NOW()),
       (24, 27, NOW(), NOW()),
       (24, 31, NOW(), NOW()),
       (25, 3, NOW(), NOW()),
       (25, 23, NOW(), NOW()),
       (25, 35, NOW(), NOW());

-- Introducing 테이블에 더미 데이터 삽입
INSERT INTO introducing (agit_id, created_at, updated_at, content, image, introduce)
VALUES (1, NOW(), NOW(), '소개글입니다.', 'https://picsum.photos/250/250', '첫 번째 소개'),
       (2, NOW(), NOW(), '소개글입니다.', 'https://picsum.photos/250/250', '두 번째 소개'),
       (3, NOW(), NOW(), '소개글입니다.', 'https://picsum.photos/250/250', '두 번째 소개'),
       (4, NOW(), NOW(), '소개글입니다.', 'https://picsum.photos/250/250', '두 번째 소개'),
       (5, NOW(), NOW(), '소개글입니다.', 'https://picsum.photos/250/250', '두 번째 소개'),
       (6, NOW(), NOW(), '소개글입니다.', 'https://picsum.photos/250/250', '두 번째 소개'),
       (7, NOW(), NOW(), '소개글입니다.', 'https://picsum.photos/250/250', '두 번째 소개'),
       (8, NOW(), NOW(), '소개글입니다.', 'https://picsum.photos/250/250', '첫 번째 소개 내용입니다. 여기에 대해 더 알아보세요!'),
       (9, NOW(), NOW(), '소개글입니다.', 'https://picsum.photos/250/250', '우리의 새로운 프로젝트가 시작되었습니다. 함께 성장해요!'),
       (10, NOW(), NOW(), '소개글입니다.', 'https://picsum.photos/250/250', '두 번째 소개 내용입니다. 변화는 지금부터 시작됩니다!'),
       (11, NOW(), NOW(), '소개글입니다.', 'https://picsum.photos/250/250', '새로운 팀이 결성되었습니다. 모두의 참여를 기다립니다.'),
       (12, NOW(), NOW(), '소개글입니다.', 'https://picsum.photos/250/250', '우리는 꿈을 이루는 첫걸음을 내디뎠습니다. 함께 해주세요!'),
       (13, NOW(), NOW(), '소개글입니다.', 'https://picsum.photos/250/250', '여러분과 함께 이루고 싶은 목표가 있습니다. 함께 해주세요!'),
       (14, NOW(), NOW(), '소개글입니다.', 'https://picsum.photos/250/250', '팀워크와 열정으로 달성할 수 있는 모든 목표를 향해 나아가고 있습니다.'),
       (15, NOW(), NOW(), '소개글입니다.', 'https://picsum.photos/250/250', '더 나은 미래를 만들기 위한 우리의 노력에 함께해요!'),
       (16, NOW(), NOW(), '소개글입니다.', 'https://picsum.photos/250/250', '우리는 끝없이 발전하고 있으며, 여러분과 함께 할 미래가 기대됩니다.'),
       (17, NOW(), NOW(), '소개글입니다.', 'https://picsum.photos/250/250', '기회는 스스로 만드는 것입니다. 여러분과 함께 더 나은 세상을 만들어갑니다.'),
       (18, NOW(), NOW(), '소개글입니다.', 'https://picsum.photos/250/250', '새로운 시작이 필요하다면, 우리는 그 시작을 함께 할 준비가 되어 있습니다.'),
       (19, NOW(), NOW(), '소개글입니다.', 'https://picsum.photos/250/250', '모든 도전은 우리가 함께라면 두렵지 않습니다. 함께 할 준비 되셨나요?'),
       (20, NOW(), NOW(), '소개글입니다.', 'https://picsum.photos/250/250', '우리는 세상을 변화시키기 위해 한 걸음씩 나아가고 있습니다. 여러분도 함께해 주세요.'),
       (21, NOW(), NOW(), '소개글입니다.', 'https://picsum.photos/250/250', '우리의 목표는 단순한 성과가 아닌, 가치를 창출하는 것입니다. 그 길에 동참하세요!'),
       (22, NOW(), NOW(), '소개글입니다.', 'https://picsum.photos/250/250', '지금 이 순간을 함께하는 것이 중요한 이유입니다. 여러분의 참여를 기다립니다.'),
       (23, NOW(), NOW(), '소개글입니다.', 'https://picsum.photos/250/250', '우리는 변화와 혁신의 선두주자가 되고자 합니다. 여러분도 함께 성장할 수 있습니다.'),
       (24, NOW(), NOW(), '소개글입니다.', 'https://picsum.photos/250/250', '새로운 기회가 기다리고 있습니다. 여러분의 아이디어를 기다립니다.'),
       (25, NOW(), NOW(), '소개글입니다.', 'https://picsum.photos/250/250', '우리가 함께 만들 세상은 그 어떤 것보다도 더 아름답고 가치 있을 것입니다.');

-- Member And Interesting 테이블에 더미 데이터 삽입
INSERT INTO member_and_interesting (interesting_id, member_id, created_at, updated_at)
VALUES (1, 1, NOW(), NOW()),
       (2, 2, NOW(), NOW());

-- Regular Crewing 테이블에 더미 데이터 삽입
INSERT INTO meeting (is_deleted, agit_id, created_at, regular_time, updated_at, content, image, place, regular_name)
VALUES (FALSE, 5, NOW(), NOW(), NOW(), '첫 번째 정기 크루잉', 'crew1.png', '서울시 중구 명동', '정기 크루잉 1'),
       (FALSE, 5, NOW(), NOW(), NOW(), '두 번째 정기 크루잉', 'crew2.png', '경기도 수원시 팔달구', '정기 크루잉 2');

