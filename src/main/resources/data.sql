-- 删除表（注意外键顺序）
DROP TABLE IF EXISTS t_tree_node;
DROP TABLE IF EXISTS t_book_author_mapping;
DROP TABLE IF EXISTS t_book;
DROP TABLE IF EXISTS t_author;
DROP TABLE IF EXISTS t_book_store;

-- 书店表
CREATE TABLE t_book_store (
                              id BIGINT NOT NULL AUTO_INCREMENT,
                              name VARCHAR(50) NOT NULL,
                              website VARCHAR(100),
                              PRIMARY KEY (id),
                              CONSTRAINT business_key_book_store UNIQUE (name)
);

-- 书籍表
CREATE TABLE t_book (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        name VARCHAR(50) NOT NULL,
                        edition INT NOT NULL,
                        price DECIMAL(10,2) NOT NULL,
                        store_id BIGINT,
                        PRIMARY KEY (id),
                        CONSTRAINT business_key_book UNIQUE (name, edition),
                        CONSTRAINT fk_book__book_store FOREIGN KEY (store_id) REFERENCES t_book_store(id)
);

-- 作者表
CREATE TABLE t_author (
                          id BIGINT NOT NULL AUTO_INCREMENT,
                          first_name VARCHAR(25) NOT NULL,
                          last_name VARCHAR(25) NOT NULL,
                          gender CHAR(1) NOT NULL,
                          PRIMARY KEY (id),
                          CONSTRAINT business_key_author UNIQUE (first_name, last_name),
                          CONSTRAINT ck_author_gender CHECK (gender IN ('M', 'F'))
);

-- 书籍作者映射表
CREATE TABLE t_book_author_mapping (
                                       book_id BIGINT NOT NULL,
                                       author_id BIGINT NOT NULL,
                                       PRIMARY KEY (book_id, author_id),
                                       CONSTRAINT fk_book_author_mapping__book FOREIGN KEY (book_id) REFERENCES t_book(id) ON DELETE CASCADE,
                                       CONSTRAINT fk_book_author_mapping__author FOREIGN KEY (author_id) REFERENCES t_author(id) ON DELETE CASCADE
);

-- 初始书店数据
INSERT INTO t_book_store(id, name) VALUES
                                       (1, 'O''REILLY'),
                                       (2, 'MANNING');

-- 初始书籍数据
INSERT INTO t_book(id, name, edition, price, store_id) VALUES
                                                           (1, 'Learning GraphQL', 1, 50.00, 1),
                                                           (2, 'Learning GraphQL', 2, 55.00, 1),
                                                           (3, 'Learning GraphQL', 3, 51.00, 1),
                                                           (4, 'Effective TypeScript', 1, 73.00, 1),
                                                           (5, 'Effective TypeScript', 2, 69.00, 1),
                                                           (6, 'Effective TypeScript', 3, 88.00, 1),
                                                           (7, 'Programming TypeScript', 1, 47.50, 1),
                                                           (8, 'Programming TypeScript', 2, 45.00, 1),
                                                           (9, 'Programming TypeScript', 3, 48.00, 1),
                                                           (10, 'GraphQL in Action', 1, 80.00, 2),
                                                           (11, 'GraphQL in Action', 2, 81.00, 2),
                                                           (12, 'GraphQL in Action', 3, 80.00, 2);

-- 初始作者数据
INSERT INTO t_author(id, first_name, last_name, gender) VALUES
                                                            (1, 'Eve', 'Procello', 'F'),
                                                            (2, 'Alex', 'Banks', 'M'),
                                                            (3, 'Dan', 'Vanderkam', 'M'),
                                                            (4, 'Boris', 'Cherny', 'M'),
                                                            (5, 'Samer', 'Buna', 'M');

-- 映射表数据
INSERT INTO t_book_author_mapping(book_id, author_id) VALUES
                                                          (1, 1), (2, 1), (3, 1),
                                                          (1, 2), (2, 2), (3, 2),
                                                          (4, 3), (5, 3), (6, 3),
                                                          (7, 4), (8, 4), (9, 4),
                                                          (10, 5), (11, 5), (12, 5);

-- 树节点表
CREATE TABLE t_tree_node (
                             id BIGINT NOT NULL AUTO_INCREMENT,
                             name VARCHAR(20) NOT NULL,
                             parent_id BIGINT,
                             PRIMARY KEY (id),
                             CONSTRAINT business_key_tree_node UNIQUE (parent_id, name),
                             CONSTRAINT fk_tree_node__parent FOREIGN KEY (parent_id) REFERENCES t_tree_node(id)
);

-- 树结构数据
INSERT INTO t_tree_node(id, name, parent_id) VALUES
                                                 (1, 'Home', NULL),
                                                 (2, 'Food', 1),
                                                 (3, 'Drinks', 2),
                                                 (4, 'Coca Cola', 3),
                                                 (5, 'Fanta', 3),
                                                 (6, 'Bread', 2),
                                                 (7, 'Baguette', 6),
                                                 (8, 'Ciabatta', 6),
                                                 (9, 'Clothing', 1),
                                                 (10, 'Woman', 9),
                                                 (11, 'Casual wear', 10),
                                                 (12, 'Dress', 11),
                                                 (13, 'Miniskirt', 11),
                                                 (14, 'Jeans', 11),
                                                 (15, 'Formal wear', 10),
                                                 (16, 'Suit', 15),
                                                 (17, 'Shirt', 15),
                                                 (18, 'Man', 9),
                                                 (19, 'Casual wear', 18),
                                                 (20, 'Jacket', 19),
                                                 (21, 'Jeans', 19),
                                                 (22, 'Formal wear', 18),
                                                 (23, 'Suit', 22),
                                                 (24, 'Shirt', 22);
