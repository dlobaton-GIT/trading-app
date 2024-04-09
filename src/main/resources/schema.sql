DROP TABLE IF EXISTS Security;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Orders;
DROP TABLE IF EXISTS Trade;

CREATE TABLE Security (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(10) NOT NULL
);

CREATE TABLE Users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL,
                       password VARCHAR(20) NOT NULL
);

CREATE TABLE Orders (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       user_id INT NOT NULL,
                       FOREIGN KEY (user_id) REFERENCES USERS(id),
                       security_id INT NOT NULL,
                       FOREIGN KEY (security_id) REFERENCES SECURITY(id),
                       type VARCHAR(50) NOT NULL,
                       price INT NOT NULL,
                       quantity INT NOT NULL,
                       full_filled BOOLEAN NOT NULL
);

CREATE TABLE Trade (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        sell_order_id INT NOT NULL,
                        FOREIGN KEY (sell_order_id) REFERENCES ORDERS(id),
                        buy_order_id INT NOT NULL,
                        FOREIGN KEY (buy_order_id) REFERENCES ORDERS(id),
                        price INT NOT NULL,
                        quantity INT NOT NULL
);