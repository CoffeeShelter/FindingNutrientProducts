CREATE DATABASE IF NOT EXISTS NUTRIENT;

USE NUTRIENT;

CREATE TABLE IF NOT EXISTS PRODUCT
(
	ID INT NOT NULL AUTO_INCREMENT,
    NAME VARCHAR(200) NOT NULL,
    URL VARCHAR(300) NOT NULL,
    PRICE INT NOT NULL,
    IMAGE VARCHAR(300) NOT NULL,
    PRIMARY KEY(ID)
);

CREATE TABLE IF NOT EXISTS MARK
(
	ID INT NOT NULL AUTO_INCREMENT,
    KIND VARCHAR(100) NOT NULL,
    COUNTRY VARCHAR(30) NOT NULL,
    INFO VARCHAR(500) NOT NULL,
    PRIMARY KEY(ID)
);

CREATE TABLE IF NOT EXISTS NUTRIENT_PRODUCT
(
	P_ID INT NOT NULL,
    M_ID INT NOT NULL,
    FOREIGN KEY (P_ID) REFERENCES PRODUCT(ID) ON UPDATE CASCADE,
    FOREIGN KEY (M_ID) REFERENCES MARK(ID) ON UPDATE CASCADE
);

INSERT INTO MARK(KIND, COUNTRY, INFO) VALUE ('건강기능식품', '한국', '식품의약품안전처가 건강기능식품 규정에 따라 일정한 절차를 거쳐 만들어진 제품으로 유용한 기능성을 나타내고 있는 것을 인증');
INSERT INTO MARK(KIND, COUNTRY, INFO) VALUE ('GMP', '한국', '식품의약품안전처가 건강기능식품이 제조 업소에서 제품표준, 제조관리 기준 등을 기준으로 안전하게 생산되었다는 것을 인증');
INSERT INTO MARK(KIND, COUNTRY, INFO) VALUE ('유기농', '미국', '최소 3년 이상 합성비료, 방사선 처리, 합성 농약을 일체 사용하지 않고 농산물의 농사방법과 가공방법 등을 준수했음을 인증');
INSERT INTO MARK(KIND, COUNTRY, INFO) VALUE ('USP', '미국', '식이보충제 완제품, 식이보충제 원료, 의약품 원료, 약학 첨가물에 관한 검증 서비스를 제공하고 있으며 USP(US Pharmacopeial Convention, 미국 약전) 검증 조건을 충족하는 제품 및 원재료에 USP 검증마크 표시');
INSERT INTO MARK(KIND, COUNTRY, INFO) VALUE ('유기농', '호주', '유기농 원료 비중이 95% 이상인 제품에 인증마크 표시');