# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table Product (
  product_id                varchar(255) not null,
  product_name              varchar(255),
  ean                       varchar(255),
  list_price                float,
  wholesale_price           float,
  currency                  varchar(3),
  constraint pk_Product primary key (product_id))
;

create sequence Product_seq;




# --- !Downs

drop table if exists Product cascade;

drop sequence if exists Product_seq;

