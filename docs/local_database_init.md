```postgresql
create database spring_boot_cms;
comment on database spring_boot_cms is 'Spring Boot CMS';

CREATE USER doyevskyi WITH PASSWORD '1234';
GRANT ALL PRIVILEGES ON DATABASE spring_boot_cms TO doyevskyi;
GRANT ALL PRIVILEGES ON SCHEMA public TO doyevskyi;

```
