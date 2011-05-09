
drop table if exists tuple;

create table tuple (
  unique1           integer not null,
  idx               integer not null,
  one               integer not null,
  ten               integer not null,
  twenty            integer not null,
  twentyfive        integer not null,
  fifty             integer not null,
  evenonepercent    integer not null,
  oddonepercent     integer not null,
  stringu1          varchar(24) not null,
  stringu2          varchar(24) not null,
  string4           varchar(24) not null,
  created           date
)
