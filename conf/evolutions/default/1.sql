# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table Product (
  product_id                varchar(255) not null,
  product_name              varchar(255),
  ean                       varchar(255),
  list_price                double,
  wholesale_price           double,
  constraint pk_Product primary key (product_id))
;

create sequence Product_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists Product;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists Product_seq;

