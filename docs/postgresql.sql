create database kalgooksoo
    with owner kalgooksoo tablespace pg_default;

comment on database kalgooksoo is 'kalgooksoo';

create schema cms;

comment on schema cms is 'cms';

alter schema cms owner to kalgooksoo;

