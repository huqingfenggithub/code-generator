create table if not exists code_generator_entity
(
    id              int primary key auto_increment,
    jdbc            varchar(255),
    alias            varchar(255),
    user_name            varchar(255),
    pwd            varchar(255),
    schema          varchar(255),
    table_string       varchar(255),
    tf_type          varchar(255),
    mbp_package      varchar(255),
    swagger        boolean,
    lombok          boolean,
    table_name_format varchar(255)
);