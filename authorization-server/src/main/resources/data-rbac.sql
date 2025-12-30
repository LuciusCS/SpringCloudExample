-- Role Data
INSERT INTO role (name) VALUES ('ADMIN');
INSERT INTO role (name) VALUES ('USER');

-- Permission Data
INSERT INTO permission (name) VALUES ('system:user:view');
INSERT INTO permission (name) VALUES ('system:user:add');
INSERT INTO permission (name) VALUES ('system:user:edit');
INSERT INTO permission (name) VALUES ('system:user:delete');

-- Role-Permission Mapping
-- ADMIN has all permissions
INSERT INTO role_permission (role_id, permission_id) SELECT r.id, p.id FROM role r, permission p WHERE r.name = 'ADMIN';
-- USER has only view permission
INSERT INTO role_permission (role_id, permission_id) SELECT r.id, p.id FROM role r, permission p WHERE r.name = 'USER' AND p.name = 'system:user:view';

-- User Data (password is '123456' BCrypt encoded: $2a$10$8.u0S3i98edH.xyC.G.u0OqE/OedQ8p4u4d4u4d4u4d4u4d4u4d4u)
INSERT INTO user (username, password, enabled, email, account_non_expired, account_non_locked, credentials_non_expired, version, create_date) 
VALUES ('admin', '$2a$10$8.u0S3i98edH.xyC.G.u0OqE/OedQ8p4u4d4u4d4u4d4u4d4u4d4u', 1, 'admin@example.com', 1, 1, 1, 1, NOW());

INSERT INTO user (username, password, enabled, email, account_non_expired, account_non_locked, credentials_non_expired, version, create_date) 
VALUES ('user', '$2a$10$8.u0S3i98edH.xyC.G.u0OqE/OedQ8p4u4d4u4d4u4d4u4d4u4d4u', 1, 'user@example.com', 1, 1, 1, 1, NOW());

-- User-Role Mapping
INSERT INTO user_role (user_id, role_id) SELECT u.id, r.id FROM user u, role r WHERE u.username = 'admin' AND r.name = 'ADMIN';
INSERT INTO user_role (user_id, role_id) SELECT u.id, r.id FROM user u, role r WHERE u.username = 'user' AND r.name = 'USER';
