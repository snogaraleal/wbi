# --- !Ups

create table country (
  id                        bigint not null,
  iso                       varchar(255),
  name                      varchar(255),
  region_id                 bigint,
  constraint pk_country primary key (id)
);

create table indicator (
  id                        bigint not null,
  ident                     varchar(255),
  name                      varchar(255),
  status                    integer,
  source_id                 bigint,
  constraint ck_indicator_status check (status in (0,1,2)),
  constraint pk_indicator primary key (id)
);

create table point (
  id                        bigint not null,
  series_id                 bigint,
  year                      integer,
  value                     float,
  constraint pk_point primary key (id)
);

create table region (
  id                        bigint not null,
  ident                     varchar(255),
  name                      varchar(255),
  constraint pk_region primary key (id)
);

create table series (
  id                        bigint not null,
  indicator_id              bigint,
  country_id                bigint,
  constraint pk_series primary key (id)
);

create table source (
  id                        bigint not null,
  ident                     integer,
  name                      varchar(255),
  constraint pk_source primary key (id)
);

create table topic (
  id                        bigint not null,
  ident                     integer,
  name                      varchar(255),
  constraint pk_topic primary key (id)
);


create table indicator_topic (
  indicator_id                   bigint not null,
  topic_id                       bigint not null,
  constraint pk_indicator_topic primary key (indicator_id, topic_id)
);

create sequence country_seq;

create sequence indicator_seq;

create sequence point_seq;

create sequence region_seq;

create sequence series_seq;

create sequence source_seq;

create sequence topic_seq;

alter table country add constraint fk_country_region_1 foreign key (region_id) references region (id);
create index ix_country_region_1 on country (region_id);
alter table indicator add constraint fk_indicator_source_2 foreign key (source_id) references source (id);
create index ix_indicator_source_2 on indicator (source_id);
alter table point add constraint fk_point_series_3 foreign key (series_id) references series (id);
create index ix_point_series_3 on point (series_id);
alter table series add constraint fk_series_indicator_4 foreign key (indicator_id) references indicator (id);
create index ix_series_indicator_4 on series (indicator_id);
alter table series add constraint fk_series_country_5 foreign key (country_id) references country (id);
create index ix_series_country_5 on series (country_id);

alter table indicator_topic add constraint fk_indicator_topic_indicator_01 foreign key (indicator_id) references indicator (id);

alter table indicator_topic add constraint fk_indicator_topic_topic_02 foreign key (topic_id) references topic (id);

# --- !Downs

drop table if exists country cascade;

drop table if exists indicator cascade;

drop table if exists indicator_topic cascade;

drop table if exists point cascade;

drop table if exists region cascade;

drop table if exists series cascade;

drop table if exists source cascade;

drop table if exists topic cascade;

drop sequence if exists country_seq;

drop sequence if exists indicator_seq;

drop sequence if exists point_seq;

drop sequence if exists region_seq;

drop sequence if exists series_seq;

drop sequence if exists source_seq;

drop sequence if exists topic_seq;

