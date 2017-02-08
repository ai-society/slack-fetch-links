# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table slack_command_request (
  id                            bigserial not null,
  date                          timestamp,
  channel_name                  varchar(255),
  user_name                     varchar(255),
  link                          varchar(255),
  text                          TEXT,
  constraint pk_slack_command_request primary key (id)
);


# --- !Downs

drop table if exists slack_command_request cascade;

