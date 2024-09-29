```postgresql
-- SUPERUSER 권한을 가진 사용자 계정을 생성합니다.
CREATE USER doyevskyi WITH PASSWORD '1234' SUPERUSER;

-- 데이터베이스를 먼저 생성합니다.
CREATE DATABASE spring_boot_cms;
COMMENT ON DATABASE spring_boot_cms IS 'Spring Boot CMS';

-- 그런 다음 사용자에게 데이터베이스에 대한 모든 권한을 부여합니다.
GRANT ALL PRIVILEGES ON DATABASE spring_boot_cms TO doyevskyi;
```
