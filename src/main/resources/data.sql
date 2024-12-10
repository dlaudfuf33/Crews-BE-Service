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
        'https://djogyo1sj025q.cloudfront.net/banklogo/020.png'),
       (NOW(), NOW(), '081', '하나은행',
        'https://djogyo1sj025q.cloudfront.net/banklogo/081.png'),
       (NOW(), NOW(), '088', '신한은행',
        'https://djogyo1sj025q.cloudfront.net/banklogo/088.png'),
       (NOW(), NOW(), '092', '토스',
        'https://djogyo1sj025q.cloudfront.net/banklogo/092.png'),
       (NOW(), NOW(), '090', '카카오뱅크',
        'https://djogyo1sj025q.cloudfront.net/banklogo/090.png');

-- Member 테이블에 더미 데이터 삽입
INSERT INTO member (is_deleted, created_at, updated_at, ci, email, name, nick_name, password, phone_number, pin_number,
                    profile_image, role, address_id)
VALUES (FALSE, NOW(), NOW(), 'WzoJ/2Pm6gJnrsFUv40q7slT1cLwZ0FfIgp9UX4hfdrJpTDEuqwLzXqpJ0j9TpfriUW3ubLSZbKudzaCxv2A+g==',
        'rfr5lfqtRdenjBDj+qIrTVZJ4QtIsNL7NAjPknxaMgU=', 'JxYRDDYknhhKh3tGXM2Ef/Fwji8k2qMVIQ==', '빨간모자',
        '$2a$10$uYVgFLeLZfDp3J163qnzWues2IDszz4XGu676EQtxXey1kU.FS.5a', '+r6s1r6sRafj2VJCoWAOhzcHcuOwRHqRhbhX',
        '$2a$10$ZGLbG50LWUQfq8.HsDpX7.PPzI06db8aDFc9gZd1JAUDNoOEzQcAW', '', 'ROLE_USER', 3),
       (FALSE, NOW(), NOW(), 'WzoJ/2Pm6gJnrsFUv40q7slT1cLwZ0FfIgp9UX4hfdrJpTDEuqwLzXqpJ0j9TpfriUW3ubLSZbKudzaCxv2A+g==',
        'rfr5lfqtRNenjBDj+qIrTec/0j/FCMp8K+AOr2XkTeU=',
        'IDYcChYdmAp/pDeCoVOVi9A81CGdRavsoQ==', '파란양말', '$2a$10$uYVgFLeLZfDp3J163qnzWues2IDszz4XGu676EQtxXey1kU.FS.5a',
        '+r6s1r6sRafj2FOeP9RogHIAy9YQ2+JmrERz', '$2a$10$ZGLbG50LWUQfq8.HsDpX7.PPzI06db8aDFc9gZd1JAUDNoOEzQcAW',
        '', 'ROLE_USER', 3),
       (FALSE, NOW(), NOW(), 'hwgZ+PMgXXBTq8vaDLNadekJUl05WE/vP3ecLXPZuF/lr6EvAcckZLAQ99zcvN1fzbifYhy9vPnY9EmBQ/R+1Q==',
        'rfr5lfqtR9enjBDj+qIrTfalh2JTwOP1C9lN6E8mzcQ=',
        'IT8JDSEgmR9LTmGtnUQK/WC8mbwMYIYLYQ==', '초록갑판장', '$2a$10$uYVgFLeLZfDp3J163qnzWues2IDszz4XGu676EQtxXey1kU.FS.5a',
        '+r6s1r6sRafj2FKNAnd/8QQz/1uow2V5RIVO', '$2a$10$ZGLbG50LWUQfq8.HsDpX7.PPzI06db8aDFc9gZd1JAUDNoOEzQcAW',
        '', 'ROLE_USER', 3),
       (FALSE, NOW(), NOW(), '+DUlNncyt3CDYMNG4CVib4KVdQIdNX/ClnysAttChlub2Cdbk7VKww==',
        'rfr5lfqtRtenjBDj+qIrTUfTtFbeePtyFDGM1VaYsiQ=',
        'JjoAChYdmTNTu5/5Yrms0tOCt+k/4mYbrg==', '노란선장', '$2a$10$uYVgFLeLZfDp3J163qnzWues2IDszz4XGu676EQtxXey1kU.FS.5a',
        '+r6s1r6sRafi2VMOi7JvxXW5/naPQxVPUGDU', '$2a$10$ZGLbG50LWUQfq8.HsDpX7.PPzI06db8aDFc9gZd1JAUDNoOEzQcAW',
        '', 'ROLE_USER', 3),
       (FALSE, NOW(), NOW(), 'wlyoEby5gnl8p3Davp9s9VPo4fajyhK6CGU/ZzZP5hvcVE1njrIF/yye3kUkmtkW9fbrydZRmCwluLf7uzhmEw==',
        'rfr5lfqtQdenjBDj+qIrTdWRLdl+ULDnS6vLZhqjzYY=',
        'Jhc4CxYYmTdGWPnACJFontYApiS2LgbO/g==', '보라구명조끼',
        '$2a$10$uYVgFLeLZfDp3J163qnzWues2IDszz4XGu676EQtxXey1kU.FS.5a', '+r6s1r6sRafi2VIdthF4tAOKyvs3W5JQuKHp',
        '$2a$10$ZGLbG50LWUQfq8.HsDpX7.PPzI06db8aDFc9gZd1JAUDNoOEzQcAW',
        '', 'ROLE_USER', 3),
       (FALSE, NOW(), NOW(), '7WAUv5dcl1FR8fDcGcfFlQFd/JKIUmGFgPVLUB9VHEOzmDCbooto7iSwPsOZ8dMB75LOQtKiUCoqXhcsyp3Img==',
        'rfr5lfqtQNenjBDj+qIrTWTnHu3z6KhgVEMKWwMdsmY=',
        'JhM4CgYEnjhvGeU/4NvI3eKA6cmNc0x+wA==', '검은돛', '$2a$10$uYVgFLeLZfDp3J163qnzWues2IDszz4XGu676EQtxXey1kU.FS.5a',
        '+r6s1r6sRafi2FPBKKUes0aNc86XxAqnkV3N', '$2a$10$ZGLbG50LWUQfq8.HsDpX7.PPzI06db8aDFc9gZd1JAUDNoOEzQcAW',
        '', 'ROLE_USER', 3),
       (FALSE, NOW(), NOW(), '6rNw8sbO5ch1MPfs+bZ89d8eD3gthcBX7zq1M6I6sALL7GXcijAaJm56XGo3gzqJcCgkjQGrPW7L000ufofWZA==',
        'rfr5lfqtQ9enjBDj+qIrTXV9S7BlIIHpdHpJHCnfMkc=',
        'JhIoDSQZnjd/x9ta2HiESssX6jpTvXpCEA==', '흰배꼽종', '$2a$10$uYVgFLeLZfDp3J163qnzWues2IDszz4XGu676EQtxXey1kU.FS.5a',
        '+r6s1r6sRafi2FLSFQYJwjC+R0Mv3I24eZzw', '$2a$10$ZGLbG50LWUQfq8.HsDpX7.PPzI06db8aDFc9gZd1JAUDNoOEzQcAW',
        '', 'ROLE_USER', 3),
       (FALSE, NOW(), NOW(), 'xXQ38LNCrz+iHfQOAjX4fAm+qTfvcaegUDewyyvyTYTw1us1yntwU2qb7s9eEh9lK6YN3SAb40/gBu+IPQAaEw==',
        'q+vxj+DcFPO+gA25t64pRrdRP+KjceF7Kv34XV1/0g==',
        'IDscDSgwmQlD23Uoa4+RK+s9ZrOo6GcIjQ==', '타이타닉선장',
        '$2a$10$fawdDTD/KvFqFRx6QtIOPuD04XEqN/wsWyrBRE5RrT2QGV6LbZAKG',
        '+r6s1r6sRafi2FLSFQYJwjC+R0Mv3I24eZzw', '$2a$10$Wyco1VTEXptOWBdJG8qLn..zTL9IRnt0ETyKNp7T7sLAvezUHySVy',
        '', 'ROLE_ADMIN', 3);


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
       (4, NOW(), NOW(), '스터디', 2),
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

VALUES (1, FALSE, TRUE, 10, NOW(), 2, NOW(), '청년자기경영독서모임', '퇴근하고 30분 함께 독서와 이야기', 4),
       (1, FALSE, TRUE, 15, NOW(), 3, NOW(), 'Funny Run!', '러닝 동기부여가 필요하신분들 누구나!', 4),
       (1, FALSE, TRUE, 20, NOW(), 4, NOW(), '유앤캠⛺️', '캠핑, 여행, 맛집 좋아하시는분들 환영입니다.', 4),
       (1, FALSE, TRUE, 30, NOW(), 5, NOW(), '퇴강산🐶', '반려견과 산책 및 정보 공유 활동.', 4),
       (1, FALSE, FALSE, 25, NOW(), 6, NOW(), '🎲보드게임 위너🎲', '보드게임을 잘 몰라도 일단 모여봐요!', 4),
       (1, FALSE, FALSE, 20, NOW(), 3, NOW(), '🍃함께 나마스떼', '[나만의 시간, 나만의 요가]', 4),
       (1, FALSE, FALSE, 15, NOW(), 2, NOW(), '🙋‍♂️프리토킹 시즌2', '철학, 사회적 이슈를 영어로 프리토킹하는 모임입니다.', 4),
       (1, FALSE, FALSE, 8, NOW(), 3, NOW(), '[등산모임]Thumb Up 👍', '같이 대한민국 동네 산을 정복하자!', 4),
       (1, FALSE, FALSE, 8, NOW(), 3, NOW(), '뭉쳐야 산타‍🎄', '등산, 운동 좋아하시는 분 환영~!', 4),
       (1, FALSE, FALSE, 12, NOW(), 7, NOW(), '매먹사(매운거 먹는 사람들)', '<신입모집> 매운거 먹으면서 친구 만들자!.', 4),
       (1, FALSE, FALSE, 5, NOW(), 7, NOW(), '논알콤🪇클럽', '카페,수다,문화생활,무알콜,건전하게 잘노는 모임.', 4),
-- 자기계발/공부
       (1, FALSE, FALSE, 10, NOW(), 2, NOW(), '주린이 탈출 모임', 'ETF&채권&경제 토론하는 모임입니다.', 4)
        ,
       (1, FALSE, FALSE, 15, NOW(), 2, NOW(), '외국물 한잔해', '술마시며 영어 및 다양한 외국어만을 사용하는 모임입니다.', 2)
        ,
       (1, FALSE, FALSE, 20, NOW(), 2, NOW(), '모각코', '모두 모여 각자 코딩하자!.', 4)
        ,

-- 운동
       (1, FALSE, FALSE, 30, NOW(), 3, NOW(), '쥐락펴락', '더 클라임을 주로 다니며 가고싶은 암장,벙 오픈 후 같이 가요!', 4)
        ,
       (1, FALSE, FALSE, 25, NOW(), 3, NOW(), '헬스메이트♟️', '적은 인원을 자세히 차근하근 알려줘요 4년차이상 현직자들 많음!', 4)
        ,
       (1, FALSE, FALSE, 20, NOW(), 3, NOW(), '클립보드', '소중한 인연들을 연결하는 나만의 클립보드', 4)
        ,

-- 여행
       (1, FALSE, FALSE, 10, NOW(), 4, NOW(), '토끼굴🐰', '다양한 취미를 가진 사람들이 모여 여행하는 모임!', 4)
        ,
       (1, FALSE, FALSE, 5, NOW(), 4, NOW(), '무말랭이', '🤪2030 일단 드루왕😀.', 4)
        ,
       (1, FALSE, FALSE, 15, NOW(), 4, NOW(), '로드트립🚓', '자동차로 떠나는 와인딩을 즐기는 모임입니다.', 3)
        ,

-- 반려동물
       (1, FALSE, FALSE, 10, NOW(), 5, NOW(), '멍글냥글', '반려견과 함께 행복을 나눠요.', 10)
        ,
       (1, FALSE, FALSE, 8, NOW(), 5, NOW(), '캣 카페 집사', '고양이 카페를 함께 방문하는 모임입니다.', 5)
        ,

-- 게임/오락
       (1, FALSE, FALSE, 20, NOW(), 6, NOW(), '레디언트', '발로란트 소모임🔫.', 2)
        ,
       (1, FALSE, FALSE, 30, NOW(), 6, NOW(), '우아즈', '[배그]우아즈: UAZ🔥.', 3)
        ,

-- 식도락
       (1, FALSE, FALSE, 15, NOW(), 7, NOW(), '사리곰탕', '다양한 음식을 함께 즐기는 모임입니다.', 14)
        ,
       (1, FALSE, FALSE, 20, NOW(), 7, NOW(), '면사랑', '면요리 맛집을 탐방합니다.', 15);


-- Account 테이블에 더미 데이터 삽입
INSERT INTO account (balance, bank_id, created_at, member_id, updated_at, account_number, fintec_number,
                     masked_account_number, account_type, product_name)
VALUES (300000.00, 5, NOW(), 1, NOW(), '+7+s1LaoQafh0Vei4Dt32g5FMUETvsAzvvdFXNQ=', 'FT2401', '*****345', 'CREW',
        '카카오 모임통장 상품'), -- bank_id = 5 (카카오뱅크)
       (250000.00, 4, NOW(), 2, NOW(), '/7uv1LqkR6bm3VCl5WBsBWKw42ZvBl+SY0oZdWc=', 'FT2012', '*****432', 'CREW',
        '토스 모임통장 상품'),  -- bank_id = 4 (토스)
       (400000.00, 5, NOW(), 3, NOW(), '+76t17yuR6Lh2FGk56Nv8JVvqwV4C8MPMJu5o58=', 'FT2203', '*****223', 'CREW',
        '카카오 모임통장 상품'), -- bank_id = 5 (카카오뱅크)
       (200000.00, 4, NOW(), 4, NOW(), '+L2u1L2vRqTq3VKj4Dd6XvjWEl6CNfIEXNdbn6E=', 'FT2014', '*****334', 'CREW',
        '토스 모임통장 상품'),  -- bank_id = 4 (토스)
       (500000.00, 5, NOW(), 5, NOW(), '+byv0b2oQaPn21Ki4YSXCuKaK2ysXWf8ihKxzK8=', 'FT2015', '*****445', 'CREW',
        '카카오 모임통장 상품'), -- bank_id = 5 (카카오뱅크)
       (275000.00, 4, NOW(), 6, NOW(), '/rur0bqoQKLm3FGh4oVRCq70QTagDsj7u0O5brc=', 'FT2036', '*****556', 'CREW',
        '토스 모임통장 상품'),  -- bank_id = 4 (토스)
       (300000.00, 5, NOW(), 1, NOW(), '+72v0r+uRqPh3Veh4QEffmtr6c6A0RoozU0f9Lk=', 'FT2201', '*****345', 'CREW',
        '카카오 모임통장 상품'), -- bank_id = 5 (카카오뱅크)
       (250000.00, 4, NOW(), 2, NOW(), '/7uv0LytQ6Xl21em4AxszADNSApdl37c5zgwd/I=', 'FT202', '*****432', 'CREW',
        '토스 모임통장 상품'),  -- bank_id = 4 (토스)
       (400000.00, 5, NOW(), 3, NOW(), '+76t17yuQKHh3VCk526sGuxNnFNLCLvyJ8SFFv8=', 'FT2023', '*****223', 'CREW',
        '카카오 모임통장 상품'), -- bank_id = 5 (카카오뱅크)
       (200000.00, 4, NOW(), 4, NOW(), '+L2u1L2vQ6Ll2lCj4Jk+aj92OZBr+2+3ns2EKjw=', 'FT2024', '*****334', 'CREW',
        '토스 모임통장 상품'),  -- bank_id = 4 (토스)
       (500000.00, 5, NOW(), 5, NOW(), '+byv1bqoQ6Xn3Vei4bbkRAbtmHWk6CjRaUEvaRs=', 'FT2035', '*****445', 'CREW',
        '카카오 모임통장 상품'), -- bank_id = 5 (카카오뱅크)
       (275000.00, 4, NOW(), 6, NOW(), '/ruo0rupQ6Pl3Fah4rtqi6tcnUqFZFtdl7IE9VA=', 'FT206', '*****556', 'CREW',
        '토스 모임통장 상품');


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
       (FALSE, 7, NOW(), 1, NOW(), NOW(), '5555666677778888', '8635****8888',
        'https://hwamockyee.s3.ap-northeast-2.amazonaws.com/crews/kakaoCard.jpg', '카카오 모임카드'),

-- 보드게임 모임 (토스 모임통장 상품 - 계좌 8)
       (FALSE, 8, NOW(), 2, NOW(), NOW(), '6666777788889999', '7777****9999',
        'https://hwamockyee.s3.ap-northeast-2.amazonaws.com/crews/tossCard.png', '토스 모임카드'),

-- 러닝 클럽 (카카오 모임통장 상품 - 계좌 9)
       (FALSE, 9, NOW(), 3, NOW(), NOW(), '7777888899990000', '8569****0000',
        'https://hwamockyee.s3.ap-northeast-2.amazonaws.com/crews/kakaoCard.jpg', '카카오 모임카드'),

-- 역사 탐방 (토스 모임통장 상품 - 계좌 10)
       (FALSE, 10, NOW(), 4, NOW(), NOW(), '8888999900001111', '7826****1111',
        'https://hwamockyee.s3.ap-northeast-2.amazonaws.com/crews/tossCard.png', '토스 모임카드');


-- Membership 테이블에 더미 데이터 삽입
INSERT INTO membership (agit_id, created_at, joined_at, member_id, updated_at, agit_role)
VALUES (1, NOW(), NOW(), 1, NOW(), 'LEADER'),
       (2, NOW(), NOW(), 2, NOW(), 'LEADER'),
       (3, NOW(), NOW(), 3, NOW(), 'LEADER'),
       (4, NOW(), NOW(), 4, NOW(), 'LEADER'),
       (5, NOW(), NOW(), 5, NOW(), 'LEADER'),
       (6, NOW(), NOW(), 6, NOW(), 'LEADER'),
       (7, NOW(), NOW(), 7, NOW(), 'LEADER'),
       (8, NOW(), NOW(), 1, NOW(), 'LEADER'),
       (9, NOW(), NOW(), 2, NOW(), 'LEADER'),
       (10, NOW(), NOW(), 3, NOW(), 'LEADER'),
       (11, NOW(), NOW(), 4, NOW(), 'LEADER'),
       (12, NOW(), NOW(), 5, NOW(), 'LEADER'),
       (13, NOW(), NOW(), 6, NOW(), 'LEADER'),
       (14, NOW(), NOW(), 7, NOW(), 'LEADER'),
       (15, NOW(), NOW(), 1, NOW(), 'LEADER'),
       (16, NOW(), NOW(), 2, NOW(), 'LEADER'),
       (17, NOW(), NOW(), 3, NOW(), 'LEADER'),
       (18, NOW(), NOW(), 4, NOW(), 'LEADER'),
       (19, NOW(), NOW(), 5, NOW(), 'LEADER'),
       (20, NOW(), NOW(), 6, NOW(), 'LEADER'),
       (21, NOW(), NOW(), 7, NOW(), 'LEADER'),
       (22, NOW(), NOW(), 1, NOW(), 'LEADER'),
       (23, NOW(), NOW(), 2, NOW(), 'LEADER'),
       (24, NOW(), NOW(), 3, NOW(), 'LEADER'),
       (25, NOW(), NOW(), 4, NOW(), 'LEADER');

INSERT INTO common_dues (due_day, due_amount, created_at, updated_at, agit_id)
VALUES ('1', 5000.00, NOW(), NOW(), 1),
       ('1', 7500.00, NOW(), NOW(), 2),
       ('14', 75000.00, NOW(), NOW(), 3),
       ('14', 15000.00, NOW(), NOW(), 4);


-- Dues 테이블에 더미 데이터 삽입
INSERT INTO dues (due_date, created_at, due_amount, updated_at, membership_id, common_dues_id, is_payed, product_name,
                  account_number, agit_name)
VALUES (NOW(), NOW(), 1000.00, NOW(), 1, 1, FALSE, '우리 일반통장 상품', '1234', '독서 하는 모임'),
       (NOW(), NOW(), 2000.00, NOW(), 1, 2, FALSE, '우리 일반통장 상품', '1234', '러닝하는 클럽'),
       (NOW(), NOW(), 2000.00, NOW(), 1, 3, FALSE, '우리 일반통장 상품', '1234', '캠핑하는 모임'),
       (NOW(), NOW(), 2000.00, NOW(), 1, 4, FALSE, '우리 일반통장 상품', '1234', '강아지하는 산책 모임'),
       (NOW(), NOW(), 2000.00, NOW(), 1, 2, FALSE, '우리 일반통장 상품', '1234', '러닝하는 클럽'),
       (NOW(), NOW(), 2000.00, NOW(), 1, 2, FALSE, '우리 일반통장 상품', '1234', '러닝하는 클럽');


-- Feed 테이블에 더미 데이터 삽입
INSERT INTO feed (is_deleted, agit_id, created_at, member_id, updated_at, content, image, like_count)
VALUES (FALSE, 1, NOW(), 1, NOW(), '첫 번째 피드 내용입니다.', 'feed1.png', '10'),
       (FALSE, 2, NOW(), 2, NOW(), '두 번째 피드 내용입니다.', 'feed2.png', '20');

-- Heart 테이블에 더미 데이터 삽입
INSERT INTO heart (created_at, feed_id, member_id, updated_at)
VALUES (NOW(), 1, 1, NOW()),
       (NOW(), 2, 2, NOW());


-- Interesting And Agit 테이블에 더미 데이터 삽입
INSERT INTO introducing (agit_id, created_at, updated_at, content, image, introduce)
VALUES
    -- agit_id = 1: 청년자기경영독서모임
    (1, NOW(), NOW(), '책을 함께 읽고 토론하는 청년 자기경영 독서모임입니다.', 'https://djogyo1sj025q.cloudfront.net/imsi/1.png',
     '퇴근 후 30분, 자기계발 도서로 성장하는 청년들의 아지트!'),

    -- agit_id = 2: Funny Run!
    (2, NOW(), NOW(), '함께 달리며 건강과 웃음을 나누는 러닝 모임입니다.', 'https://djogyo1sj025q.cloudfront.net/imsi/2.jpeg',
     '초보도 환영! 달리기를 통해 긍정 에너지와 건강을 충전해보세요.'),

    -- agit_id = 3: 유앤캠⛺️
    (3, NOW(), NOW(), '자연을 만끽하는 캠핑과 다양한 여행을 즐기는 사람들의 모임.', 'https://djogyo1sj025q.cloudfront.net/imsi/3.jpeg',
     '힐링 캠핑부터 맛집 탐방까지! 여유와 즐거움을 한데 모았습니다.'),

    -- agit_id = 4: 퇴강산🐶
    (4, NOW(), NOW(), '반려견과 산책하며 정보와 즐거움을 공유하는 모임.', 'https://djogyo1sj025q.cloudfront.net/imsi/4.jpeg',
     '강아지와 함께 자연 속을 거닐며 소통하고 친목을 다지는 시간!'),

    -- agit_id = 5: 🎲보드게임 위너🎲
    (5, NOW(), NOW(), '보드게임을 통해 즐거운 시간을 보내는 친목 모임.', 'https://djogyo1sj025q.cloudfront.net/imsi/5.jpeg',
     '룰 몰라도 OK! 가벼운 마음으로 모여 다양한 보드게임을 즐겨보아요.'),

    -- agit_id = 6: 🍃함께 나마스떼
    (6, NOW(), NOW(), '요가로 몸과 마음의 균형을 찾는 힐링 모임.', 'https://djogyo1sj025q.cloudfront.net/imsi/6.jpeg',
     '나만의 호흡과 자세로 하루를 정리하고 재충전할 수 있는 시간!'),

    -- agit_id = 7: 🙋‍♂️프리토킹 시즌2
    (7, NOW(), NOW(), '영어로 철학, 사회 이슈 등 다양한 주제를 자유롭게 토론하는 모임.', 'https://djogyo1sj025q.cloudfront.net/imsi/7.jpeg',
     '언어와 사고의 경계를 허물며 지적 성장과 친목을 함께 누리세요.'),

    -- agit_id = 8: [등산모임]Thumb Up 👍
    (8, NOW(), NOW(), '가까운 산을 함께 오르며 건강과 추억을 만드는 등산 모임.', 'https://djogyo1sj025q.cloudfront.net/imsi/8.jpeg',
     '땀 흘리며 정상을 향해! 자연 속에서 힐링과 소통을 즐기세요.'),

    -- agit_id = 9: 뭉쳐야 산타‍🎄
    (9, NOW(), NOW(), '등산과 운동을 좋아하는 이들이 모여 함께 성장하는 커뮤니티.', 'https://djogyo1sj025q.cloudfront.net/imsi/9.jpeg',
     '산 오르고 땀 흘리며 새로운 친구도 사귀고 건강도 챙기는 1석2조!'),

    -- agit_id = 10: 매먹사(매운거 먹는 사람들)
    (10, NOW(), NOW(), '매운 음식을 좋아하는 사람들이 모여 입맛과 우정을 나누는 모임.', 'https://djogyo1sj025q.cloudfront.net/imsi/10.jpeg',
     '불타는 매운맛으로 스트레스 해소! 함께 매운 음식 탐방 출발~'),

    -- agit_id = 11: 논알콤🪇클럽
    (11, NOW(), NOW(), '무알콜로도 충분히 즐겁게 대화하고 문화생활을 하는 모임.', 'https://djogyo1sj025q.cloudfront.net/imsi/11.jpeg',
     '건전한 수다, 카페 라이프, 전시 관람으로 알찬 하루를 만드세요!'),

    -- agit_id = 12: 주린이 탈출 모임
    (12, NOW(), NOW(), 'ETF, 채권 등 경제 전반을 토론하며 투자 지식을 쌓는 모임.', 'https://djogyo1sj025q.cloudfront.net/imsi/12.png',
     '금융 초보 졸업! 함께 공부하며 든든한 경제적 발판을 만들어봐요.'),

    -- agit_id = 13: 외국물 한잔해
    (13, NOW(), NOW(), '술 한잔 기울이며 영어 등 다양한 외국어로 소통하는 모임.', 'https://djogyo1sj025q.cloudfront.net/imsi/13.jpeg',
     '언어와 문화를 맛보는 즐거움! 해외여행 기분을 여기서 느껴보세요.'),

    -- agit_id = 14: 모각코
    (14, NOW(), NOW(), '함께하지만 각자 집중해 코딩 실력을 향상시키는 개발자 모임.', 'https://djogyo1sj025q.cloudfront.net/imsi/14.jpeg',
     '조용히 코드에 집중하지만, 끝나면 정보 공유로 성장하는 공간!'),

    -- agit_id = 15: 쥐락펴락
    (15, NOW(), NOW(), '암벽등반을 통해 도전정신을 키우고 서로 도움을 주는 모임.', 'https://djogyo1sj025q.cloudfront.net/imsi/15.jpeg',
     '암장 정복! 새로운 난이도에 함께 도전하며 한계를 뛰어넘는 즐거움.'),

    -- agit_id = 16: 헬스메이트♟️
    (16, NOW(), NOW(), '전문 트레이너와 함께 개인 맞춤형 운동을 배우는 피트니스 모임.', 'https://djogyo1sj025q.cloudfront.net/imsi/16.jpeg',
     '차근차근 성장하는 운동 습관, 건강한 라이프스타일을 함께 만듭니다.'),

    -- agit_id = 17: 클립보드
    (17, NOW(), NOW(), '다양한 사람들과의 만남을 통해 인맥을 넓히는 네트워킹 모임.', 'https://djogyo1sj025q.cloudfront.net/imsi/17.jpeg',
     '경험과 이야기를 연결하는 클립보드, 새로운 인연이 기다립니다.'),

    -- agit_id = 18: 토끼굴🐰
    (18, NOW(), NOW(), '다양한 취미를 가진 이들이 여행을 통해 추억을 쌓는 모임.', 'https://djogyo1sj025q.cloudfront.net/imsi/18.jpeg',
     '여러 분야의 사람들이 어우러져 함께 떠나는 다채로운 여행 모험!'),

    -- agit_id = 19: 무말랭이
    (19, NOW(), NOW(), '2030 젊은 층이 모여 가벼운 만남과 즐거움을 추구하는 모임.', 'https://djogyo1sj025q.cloudfront.net/imsi/19.jpeg',
     '딱히 이유 없어도 좋아요! 일단 모이면 즐겁게 노는 자유로운 공간.'),

    -- agit_id = 20: 로드트립🚓
    (20, NOW(), NOW(), '자동차로 와인딩 코스를 달리며 풍경과 자유를 만끽하는 모임.', 'https://djogyo1sj025q.cloudfront.net/imsi/20.jpeg',
     '엔진 소리와 바람을 벗 삼아, 도로 위의 힐링과 낭만을 찾으세요!'),

    -- agit_id = 21: 멍글냥글
    (21, NOW(), NOW(), '반려동물과 함께 교감하며 정보와 추억을 나누는 반려생활 모임.', 'https://djogyo1sj025q.cloudfront.net/imsi/21.jpeg',
     '멍멍, 냥냥! 함께 행복을 나누고 반려동물 케어 노하우를 공유해요.'),

    -- agit_id = 22: 캣 카페 집사
    (22, NOW(), NOW(), '고양이 카페를 순회하며 고양이 친구들과 교감하는 모임.', 'https://djogyo1sj025q.cloudfront.net/imsi/22.jpeg',
     '냥이들의 매력 속으로! 커피와 고양이의 조합으로 힐링해보세요.'),

    -- agit_id = 23: 레디언트
    (23, NOW(), NOW(), '발로란트 게임을 즐기며 팀워크와 실력을 키우는 게이머 모임.', 'https://djogyo1sj025q.cloudfront.net/imsi/23.jpeg',
     '전략적 플레이와 유쾌한 소통! 발로란트에서 빛나는 순간을 함께.'),

    -- agit_id = 24: 우아즈
    (24, NOW(), NOW(), '배틀그라운드를 함께 즐기며 실력 향상과 친목을 도모하는 모임.', 'https://djogyo1sj025q.cloudfront.net/imsi/24.png',
     '우아즈(UAZ) 타고 치킨을 향해! 게임 속에서 웃음과 스릴을 공유하세요.'),

    -- agit_id = 25: 사리곰탕
    (25, NOW(), NOW(), '다양한 음식을 함께 시도하고 맛을 공유하는 미식가들의 모임.', 'https://djogyo1sj025q.cloudfront.net/imsi/25.jpeg',
     '입맛의 스펙트럼을 넓히며 미식 경험을 쌓는 맛있는 시간!'),
    (26, NOW(), NOW(), '면사랑모임~.', 'https://djogyo1sj025q.cloudfront.net/imsi/26.jpeg',
     '입맛의 스펙트럼을 넓히며 미식 경험을 쌓는 맛있는 시간!');
;

-- Introducing 테이블에 더미 데이터 삽입
INSERT INTO interesting_and_agit (agit_id, interesting_id, created_at, updated_at)
VALUES (1, 2, NOW(), NOW()),
       (1, 3, NOW(), NOW()),
       (1, 4, NOW(), NOW()),

       (2, 2, NOW(), NOW()),
       (2, 3, NOW(), NOW()),
       (2, 4, NOW(), NOW()),

       (3, 2, NOW(), NOW()),
       (3, 3, NOW(), NOW()),
       (3, 4, NOW(), NOW()),

       (4, 2, NOW(), NOW()),
       (4, 3, NOW(), NOW()),
       (4, 4, NOW(), NOW()),

       (5, 2, NOW(), NOW()),
       (5, 3, NOW(), NOW()),
       (5, 4, NOW(), NOW()),

       (6, 2, NOW(), NOW()),
       (6, 3, NOW(), NOW()),
       (6, 4, NOW(), NOW()),

       (7, 2, NOW(), NOW()),
       (7, 3, NOW(), NOW()),
       (7, 4, NOW(), NOW()),

       (8, 2, NOW(), NOW()),
       (8, 3, NOW(), NOW()),
       (8, 4, NOW(), NOW()),

       (9, 2, NOW(), NOW()),
       (9, 3, NOW(), NOW()),
       (9, 4, NOW(), NOW()),

       (10, 2, NOW(), NOW()),
       (10, 3, NOW(), NOW()),
       (10, 4, NOW(), NOW()),

       (11, 6, NOW(), NOW()),
       (11, 7, NOW(), NOW()),
       (11, 8, NOW(), NOW()),

       (12, 6, NOW(), NOW()),
       (12, 7, NOW(), NOW()),
       (12, 8, NOW(), NOW()),

       (13, 6, NOW(), NOW()),
       (13, 7, NOW(), NOW()),
       (13, 8, NOW(), NOW()),

       (14, 6, NOW(), NOW()),
       (14, 7, NOW(), NOW()),
       (14, 8, NOW(), NOW()),

       (15, 6, NOW(), NOW()),
       (15, 7, NOW(), NOW()),
       (15, 8, NOW(), NOW()),

       (16, 6, NOW(), NOW()),
       (16, 7, NOW(), NOW()),
       (16, 8, NOW(), NOW()),

       (17, 6, NOW(), NOW()),
       (17, 7, NOW(), NOW()),
       (17, 8, NOW(), NOW()),

       (18, 6, NOW(), NOW()),
       (18, 7, NOW(), NOW()),
       (18, 8, NOW(), NOW()),

       (19, 6, NOW(), NOW()),
       (19, 7, NOW(), NOW()),
       (19, 8, NOW(), NOW()),

       (20, 6, NOW(), NOW()),
       (20, 7, NOW(), NOW()),
       (20, 8, NOW(), NOW()),

       (21, 14, NOW(), NOW()),
       (21, 15, NOW(), NOW()),
       (21, 16, NOW(), NOW()),

       (22, 14, NOW(), NOW()),
       (22, 15, NOW(), NOW()),
       (22, 16, NOW(), NOW()),

       (23, 14, NOW(), NOW()),
       (23, 15, NOW(), NOW()),
       (23, 16, NOW(), NOW()),

       (24, 14, NOW(), NOW()),
       (24, 15, NOW(), NOW()),
       (24, 16, NOW(), NOW()),

       (25, 14, NOW(), NOW()),
       (25, 15, NOW(), NOW()),
       (25, 16, NOW(), NOW());

-- Member And Interesting 테이블에 더미 데이터 삽입
INSERT INTO member_and_interesting (interesting_id, member_id, created_at, updated_at)
VALUES (1, 1, NOW(), NOW()),
       (2, 1, NOW(), NOW()),
       (1, 2, NOW(), NOW()),
       (2, 2, NOW(), NOW()),
       (5, 3, NOW(), NOW()),
       (6, 3, NOW(), NOW()),
       (9, 4, NOW(), NOW()),
       (11, 4, NOW(), NOW());


-- Regular Crewing 테이블에 더미 데이터 삽입
INSERT INTO meeting (is_deleted, agit_id, created_at, regular_time, updated_at, content, image, place, regular_name)
VALUES (FALSE, 5, NOW(), NOW(), NOW(), '첫 번째 정기 크루잉', 'crew1.png', '서울시 중구 명동', '정기 크루잉 1'),
       (FALSE, 5, NOW(), NOW(), NOW(), '두 번째 정기 크루잉', 'crew2.png', '경기도 수원시 팔달구', '정기 크루잉 2');

