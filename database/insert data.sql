use jsp-servlet-jdbc;

insert into role(code,name) values('ADMIN','ADMIN');
insert into role(code,name) values('USER','USER');

insert into user(username,password,fullname,status, roleid) values('admin','123456','admin',1,1);
insert into user(username,password,fullname,status, roleid) values('tranhoa','123456','Trần Văn Hóa',1,2);
insert into user(username,password,fullname,status, roleid) values('tranminh','123456','Trần Minh',1,2);