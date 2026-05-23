Spring Boot 與 React 製作的電商練習專案

主要目的是練習：
- 前後端分離開發
- RESTful API 設計
- JWT 登入驗證
- MySQL 資料庫操作
- React 串接後端 API
目前功能還在持續增加中。

----------------------------------------

## 使用技術

### Backend
- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- Maven

### Frontend
- React
- Vite
- JavaScript (ES6+)
- Axios
- React Router

### Database
- MySQL

-----------------------------------------

# 目前功能

## 會員功能
- 會員登入 / 註冊
- JWT 身分驗證
- Spring Security 權限控管
- BCrypt PasswordEncoder 密碼加密
- 忘記密碼（Email 驗證）

## 商品功能
- 商品列表
- 商品詳細資料
- 商品新增 / 修改 / 刪除（管理員）

## 購物車功能
- 加入購物車
- 修改商品數量
- 刪除購物車商品

## 訂單功能
- 建立訂單
- 查詢訂單
- 訂單狀態管理
- 模擬金流結帳（ECPay 綠界）
- 綠界付款流程串接
- Payment Callback 處理

## 技術學習
- Spring AOP（基礎概念與簡單實作）

-----------------------------------------

# 專案結構
- aspect(aop練習)
- auth(登入控制/密碼重設)
- cart(購物車)
- config
- convert(list json 轉換元件)
- coupon(折價眷-待完成)
- dto(資料傳輸物件)
- ecpay(綠界結帳相關)
- exception(例外處理)
- mail(信件fomate)
- order(訂單)
- product(產品)
- security(安全驗證相關)
- user(使用者)

-----------------------------------------

# Backend 啟動方式

###### 1. Clone 專案 ######

git clone https://github.com/weiway0311/interview_backend.git

---

###### 2. 新增 secretkey.properties ######

jwt_token=

imgbb_apikey=

app_domain_fe=http://localhost:5173

app_domain_be=http://localhost:8080

mail_username=

mail_password=

#==============jdbc================

datasource_username=

datasource_password=

---

###### 3. 建立 MySQL Database ######

CREATE DATABASE backendDB;
use backenddb;

--建立 user table
create table user_detail (
	user_id varchar(100) not null primary key,
	email varchar(100),
	register_date Date,
	full_name varchar(100),
	nick_name varchar(100),
	gender varchar(10),
	phone varchar(30),
	birth Date,
	user_level int default 1,
	authority varchar(30) default 'ROLE_USER',
	password varchar(2000),
	photo varchar(100),
	is_enabled BOOLEAN DEFAULT TRUE
);

INSERT INTO user_detail (
    user_id, email, register_date, full_name, nick_name, gender, phone, 
	birth, user_level, authority, password, photo, is_enabled
)
VALUES (
    'test001', 'youremail@gmail.com', STR_TO_DATE('2025-06-17', '%Y-%m-%d'), 'howord', 'lo', 'man', '0960111111',
    STR_TO_DATE('1998-03-11', '%Y-%m-%d'), 1, 'ROLE_ADMIN', 'i000', '', TRUE
);


--建立 product table
CREATE TABLE product (
    id VARCHAR(50) PRIMARY KEY,                -- ID 例如 "-OTGhna7sEy51ZwTaL84"
    category VARCHAR(100),                     -- 狗分類
    title VARCHAR(255),                        -- 狗的產品
    description TEXT,                          -- 狗的描述
    content TEXT,                              -- 狗的說明
    image_url TEXT,                             -- 主要圖片 URL
    images_url JSON,                            -- 多張圖片 URL 陣列（用 JSON 格式儲存）
    is_enabled TINYINT(1),                     -- 1=啟用, 0=停用
    origin_price INT,                          -- 原價
    price INT,                                 -- 價格
    unit VARCHAR(50),                          -- 單位
    num INT                                     -- 數量
);

-- 建立 product 的 index
explain select * from backenddb.product where category='生活圖書';
create index idx_product_category on backenddb.product(category);

INSERT INTO `product` VALUES ('-OTGetqk6FUdDAGfQ7cZ','測試分類','貓的產品','貓的描述','貓的說明',
'https://plus.unsplash.com/premium_photo-1667030474693-6d0632f97029?w=700&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8Y2F0fGVufDB8fDB8fHww',
'["https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?w=700&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8Y2F0fGVufDB8fDB8fHww", 
"https://plus.unsplash.com/premium_photo-1677545183884-421157b2da02?w=700&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NXx8Y2F0fGVufDB8fDB8fHww", 
"https://images.unsplash.com/photo-1517331156700-3c241d2b4d83?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1948&q=80", 
"https://images.unsplash.com/photo-1472491235688-bdc81a63246e?w=700&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Nnx8Y2F0fGVufDB8fDB8fHww", 
"https://images.unsplash.com/photo-1552944150-6dd1180e5999?w=700&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NzV8fGNhdHxlbnwwfHwwfHx8MA=="]',1,10000,5000,'測試單位',2),
('-OTGhna7sEy51ZwTaL84','測試分類','狗的產品','狗的描述','狗的說明','https://plus.unsplash.com/premium_photo-1666777247416-ee7a95235559?w=700&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8ZG9nfGVufDB8fDB8fHww','["https://images.unsplash.com/photo-1561037404-61cd46aa615b?w=700&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTB8fGRvZ3xlbnwwfHwwfHx8MA==", "https://plus.unsplash.com/premium_photo-1667673941713-ad4d4751c93b?w=700&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTd8fGRvZ3xlbnwwfHwwfHx8MA==", "https://images.unsplash.com/photo-1548199973-03cce0bbc87b?w=700&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTl8fGRvZ3xlbnwwfHwwfHx8MA==", "https://plus.unsplash.com/premium_photo-1719177518229-79d47d45d49a?w=700&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mzd8fGRvZ3xlbnwwfHwwfHx8MA==", "https://images.unsplash.com/photo-1576201836106-db1758fd1c97?w=700&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NTF8fGRvZ3xlbnwwfHwwfHx8MA=="]',1,12000,5100,'測試單位',1),('0MWCWrblTmmJZ4wBYbxBNA','遊戲圖書','尼爾：幸田和磨美術集','','','https://i.ibb.co/mrWSqyKD/56d63a73a96b.jpg','["https://i.ibb.co/PGgpp1Gs/d2d4c431b3af.jpg", "https://i.ibb.co/MkghzLCc/b97736cfc88b.jpg", "https://i.ibb.co/gFRsPLrf/bba566efb336.jpg", "https://i.ibb.co/hxb9NWbQ/9860510a44e7.jpg"]',1,799,631,'本',4),('1CwOhrcCQVGpaZwxl8B26w','繪畫圖書','貓狗的爆笑同居生活(01)','','','https://i.ibb.co/yczFmnJB/dd2ae4abb461.jpg','["https://i.ibb.co/rGTwFFSy/9ac9650e786e.jpg", "https://i.ibb.co/hxQxZKCY/176441d6a97b.jpg", "https://i.ibb.co/BK4xVmgj/72cb9cdce7b9.jpg", "https://i.ibb.co/DH35nwdN/1c28df6a6371.jpg"]',1,100,300,'本',10),('ag0icu8ITbWXiFwjgabEEA','生活圖書','戈登．拉姆齊10分鐘上菜','','','https://i.ibb.co/mVMKf6MW/b8b7d042f420.jpg','["https://i.ibb.co/7JtV9xBH/6c6b406a7257.jpg", "https://i.ibb.co/r2vjGhv8/c485c06a76ba.jpg", "https://i.ibb.co/5WkvgJZ6/9a62df9b9195.jpg", "https://i.ibb.co/mF5RJSyW/e19b74c13aec.jpg"]',1,100,300,'本',10),('I51BqnRdTxyjMHfAtEn6qA','生活圖書','圖解巴赫花精：解析創傷、釋放壓力，療癒心靈與情的最佳解方','','','https://i.ibb.co/rGzv3ByK/bb8bc0872950.jpg','["https://i.ibb.co/nNrm1hVm/a6810092d396.jpg", "https://i.ibb.co/YBLTr27Q/104025876e99.jpg", "https://i.ibb.co/1Y6kbMjD/0e22bd7ac16c.jpg", "https://i.ibb.co/HfGJTBM6/ca20b25d2de0.jpg"]',1,100,300,'本',10),('lNiak-kPTuq75l_MLM0CuA','繪畫圖書','FAKE NEWS－鋪天蓋地的假訊息','','','https://i.ibb.co/Mx1qHWL6/8f71a2385f56.jpg','["https://i.ibb.co/1YppHqt9/03eff7aeeacd.jpg", "https://i.ibb.co/fY5SsMXr/88df34d8a88c.jpg", "https://i.ibb.co/nqYNZJzB/c9995495f482.jpg", "https://i.ibb.co/9H2jnJxj/66b0830ed8c8.jpg"]',1,100,300,'本',10),('oyuZWRfhRfqo3rRvQVjlAg','繪畫圖書','酷洛米讀歎異抄：打開原有心靈的鑰匙','','','https://i.ibb.co/99DgTcrN/672f505f1638.jpg','["https://i.ibb.co/TMvVcRcr/260b595a2f01.jpg", "https://i.ibb.co/JwnsmV1b/71f3837ef782.jpg", "https://i.ibb.co/7Jvx6sTw/700713fb49cd.jpg", "https://i.ibb.co/0yKn4KWx/1355c1f502e2.jpg"]',1,280,221,'本',10),('QaWIDsl8S_aGUTaALL_3XQ','生活圖書','不開瓦斯也會煮！202道微波爐料理','','','https://i.ibb.co/sdyXG1s2/5a5a91b4b639.jpg','["https://i.ibb.co/gL0SmDHP/e68e5ce2be07.jpg", "https://i.ibb.co/YF3FGH8L/582c4ed556e0.jpg", "https://i.ibb.co/Y7f675RZ/d849de9070f5.jpg", "https://i.ibb.co/JwsSjYNv/87738ae4b9bd.jpg"]',1,100,300,'本',10),('S254oosoTMGBD-HJ45AWdg','遊戲圖書','DEEMO II：美術設定集','','','https://i.ibb.co/gLTFRNRz/d08b3430612e.jpg','["https://i.ibb.co/99JQS1Q0/ba9bb9bb26ec.jpg", "https://i.ibb.co/ZRL7K8F2/3fa815915e0e.jpg", "https://i.ibb.co/dJ5cprLT/e194fa803ca1.jpg", "https://i.ibb.co/sv0K3g99/cda71f13b637.jpg"]',1,100,300,'本',10),('T8yUShhEQRecf3M-NpYVAg','生活圖書','探訪京都好去處：深掘滿懷憧憬的店鋪、人與景色','','','https://i.ibb.co/ds9yx64V/2c47a9ce40d7.jpg','["https://i.ibb.co/0y7nnCc1/443f91eea944.jpg", "https://i.ibb.co/xK7YdMww/464f62570889.jpg", "https://i.ibb.co/3njWggK/7be8598257f0.jpg", "https://i.ibb.co/GQ6bQQxw/ef26289193de.jpg"]',1,100,300,'本',10),('WkoeTWkNTGCVC6y6_XPwHw','生活圖書','每天10分鐘 矯正錯誤姿勢緩解痠痛','','','https://i.ibb.co/wFsDmmqt/f4e04cd767b1.jpg','["https://i.ibb.co/3yZSxwxq/52f48879373c.jpg", "https://i.ibb.co/hJNVYMkM/db63cced6a03.jpg", "https://i.ibb.co/HfLmZ6TH/d60324949d6e.jpg", "https://i.ibb.co/vK93F7z/652af660efb6.jpg"]',1,100,300,'本',10),('_Ww-txsVTu2dEtEHnmOFpQ','生活圖書','1cm + me：尋找每天進步1cm的自己','','','https://i.ibb.co/Z6BBbLVW/11ef65297334.jpg','["https://i.ibb.co/Mj8HPKM/96aa65651774.jpg", "https://i.ibb.co/mC9TXFDH/231b5865a2b2.jpg", "https://i.ibb.co/Mk31LqgD/48a69222ea25.jpg", \"https://i.ibb.co/ksnVJ4jF/092121526150.jpg"]',1,300,100,'本',10);


-- 建立 訂單 tables
-- 主表
CREATE TABLE orders (
    order_id VARCHAR(50) PRIMARY KEY,       -- 訂單ID
    user_id VARCHAR(100) NOT NULL,			-- 使用者id
    recipient_name VARCHAR(100),			-- 收件者名稱
    recipient_phone VARCHAR(20),			-- 收件者電話
    recipient_email VARCHAR(100),			-- 收件者email
    recipient_address VARCHAR(200),			-- 收件者地址
    total_price DECIMAL(10,2),				-- 訂單總額
    status VARCHAR(20),						-- 付款狀況
    created_at DATETIME,					-- 建立日期

  CONSTRAINT fk_orders_user
        FOREIGN KEY (user_id) REFERENCES user_detail(user_id)
);
-- 訂單商品(明細)
CREATE TABLE order_product (
    order_product_id VARCHAR(50) PRIMARY KEY,	-- 主鍵(自訂)
    order_id VARCHAR(50) NOT NULL,			 	-- 外來鍵、訂單ID
    product_id VARCHAR(50) NOT NULL,			-- 外來鍵、商品ID
    qty INT,									-- 商品數量
    price DECIMAL(10,2),						-- 商品單價
    total_price DECIMAL(10,2),					-- 商品總額

  CONSTRAINT fk_order_product_order
        FOREIGN KEY (order_id) REFERENCES orders(order_id)
        ON DELETE CASCADE,
	--跟隨table orders的id，若id不存在則一同刪除此資料
        
  CONSTRAINT fk_order_product_product
        FOREIGN KEY (product_id) REFERENCES product(id),
	--純宣告，不操作

  UNIQUE (order_id, product_id)				-- 定義 訂單ID、商品ID 為unique，此為唯一值不可重複
);


-- 建立 購物車 tables
-- 主表
CREATE TABLE cart (
    cart_id VARCHAR(50) PRIMARY KEY,	 -- 主表id
    user_id VARCHAR(50),				 -- 使用者id
    final_total DECIMAL(10, 2),			 -- 總價
	
--設定user_id參考userdetail table
  CONSTRAINT fk_cart_user				-- 外鍵名稱，可以自訂，也可省略
	FOREIGN KEY (user_id)				-- 指定目前這個資料表的 user_id 欄位是外鍵
	REFERENCES user_detail(user_id) 	-- 指定這個外鍵對應的主表是user_detail(user_id)
	ON DELETE CASCADE					-- 如果使用者被刪除，對應的 cart 資料也會自動刪除
	ON UPDATE CASCADE 					-- 如果使用者被更新，對應的 cart 資料也會自動更新
);
--項目
CREATE TABLE cart_item (
    cart_item_id VARCHAR(50) PRIMARY KEY,			-- 項目id
    cart_id VARCHAR(50),				-- 購物車id(主表)
    product_id VARCHAR(50),				-- 產品id
    qty INT,							-- 品項數量
	price DECIMAL(10, 2),       		-- 單價
    total_price DECIMAL(10, 2),			-- 品項總價

  FOREIGN KEY (cart_id) REFERENCES cart(cart_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);


-- password_reset_token(重設密碼紀錄)

CREATE TABLE password_reset_token (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  token VARCHAR(64) NOT NULL,
  expiry_date TIMESTAMP NOT NULL,
  used BOOLEAN DEFAULT FALSE,
  user_id VARCHAR(100) NOT NULL,
  CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES user_detail(user_id) ON DELETE CASCADE
);


