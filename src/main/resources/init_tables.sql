alter role postgres with password '1234';

create database dinners
  with owner postgres;

create table if not exists day_menu
(
  id serial not null
    constraint day_menu_pk
      primary key,
  date timestamp not null
);

alter table day_menu owner to postgres;

create unique index if not exists day_menu_date_uindex
  on day_menu (date);

create table if not exists meals
(
  id serial not null
    constraint meals_pk
      primary key,
  name varchar not null,
  type varchar(20) not null,
  menu_id integer
    constraint meals_day_menu_id_fk
      references day_menu
      on delete cascade
);

alter table meals owner to postgres;

create table if not exists groups
(
  id serial not null
    constraint groups_pk
      primary key,
  name varchar(50) not null,
  dinner_time varchar(50) not null,
  is_default boolean
);

alter table groups owner to postgres;

create unique index if not exists groups_name_uindex
  on groups (name);

create unique index if not exists groups_dinner_time_uindex
  on groups (dinner_time);

create table if not exists users
(
  id serial not null
    constraint users_pk
      primary key,
  username varchar(20) not null,
  full_name varchar(50) not null,
  role varchar(20) not null,
  group_id integer
    constraint users_groups_id_fk
      references groups
      on delete restrict,
  password varchar(100) not null
);

alter table users owner to postgres;

create unique index if not exists users_username_uindex
  on users (username);

create table if not exists orders
(
  id serial not null
    constraint orders_pk
      primary key,
  user_id integer
    constraint orders_users_id_fk
      references users
      on delete cascade,
  meal_id integer
    constraint orders_meals_id_fk
      references meals
      on delete cascade,
  is_chosen boolean
);

alter table orders owner to postgres;

create unique index if not exists u_by_meal
  on orders (meal_id, user_id);