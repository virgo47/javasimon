create table person (
	id integer auto_increment primary key,
	login varchar(32),
	name varchar(64),
	address varchar(64)
);

insert into person values (null, 'admin', 'Administrator', null);