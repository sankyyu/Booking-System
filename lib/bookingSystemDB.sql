drop database courseBooking;
create database courseBooking;
use courseBooking;
/*
drop table grade;
drop table user_teacher;
drop table user_stu;
drop table courseinfo;
drop table course;
drop table student;
drop table class;
drop table dept;
drop table college;
*/

create table college(
collID char(2) primary key,collName varchar(60) not null
);

create table dept(
deptID char(4) primary key,collID char(2) not null,deptName varchar(60),
constraint dept_fk foreign key(collID) references college(collID)
);


create table major(
majorID char(6) primary key, deptID char(4), collID char(2) not null,
majorName varchar(60) not null,
constraint class_fk1 foreign key(deptID) references dept(deptID),
constraint class_fk2 foreign key(collID) references college(collID)
);


create table student(
stuID char(12) primary key,firstname varchar(20) not null,middlename varchar(20),lastname varchar(20) not null, 

gender char(6) check(gender='Male' or gender='Female'),

stuBirth date,nationality varchar(15),
collID char(2) not null,deptID char(4), majorID char(6) not null,
enrollTime date not null, graduateTime date,Email varchar(30),address varchar(50),phone varchar(15),
constraint stu_fk1 foreign key(deptID) references dept(deptID),
constraint stu_fk2 foreign key(collID) references college(collID),
constraint stu_fk3 foreign key(majorID) references major(majorID)
);

create table professor(
profID char(8) primary key,firstame varchar(20) not null,middlename varchar(20),lastname varchar(20) not null,
gender char(6) check(gender='Male' or gender='Female'),
birth date,nationality varchar(15),
collID char(2) not null,deptID char(4),
comeTime date not null, leaveTime date,
constraint prof_fk1 foreign key(deptID) references dept(deptID),
constraint prof_fk2 foreign key(collID) references college(collID)
);

create table course(
courseID char(8) primary key,courseName varchar(50) not null,credit numeric(3,1) not null,
collID char(2) not null, deptID char(4), majorID char(6) not null,
constraint cou_fk1 foreign key(deptID) references dept(deptID),
constraint cou_fk2 foreign key(collID) references college(collID),
constraint cou_fk3 foreign key(majorID) references major(majorID)
);

create table courseinfo(
courseID char(8),courseDay char(1),courseTime char(1), location char(6), teacher varchar(20) not null,
capacity tinyint unsigned default 30, restCapacity tinyint unsigned default 30, 
description varchar(1000),
constraint couinfo_fk1 foreign key(courseID) references course(courseID),
constraint couinfo_key primary key(courseID,courseDay,courseTime,location)
);

create table grade(
stuID char(12) not null,courseID char(8) not null,courseName varchar(50) not null,instructor varchar(20) not null,
score tinyint unsigned default 0, credit numeric(4,1) default 0,
constraint grade_fk2 foreign key(stuID) references student(stuID),
constraint grade_key primary key(stuID,courseID)
);

create table user_admin(
adminID char(12) primary key,
PWD char(15) not null
);

create table user_stu(
stuID char(12) primary key,
PWD char(15) not null,
constraint user2_fk foreign key(stuID) references student(stuID)
);

insert into college values('01','School of Engineering & Science');
insert into college values('02','School of Business');
insert into college values('03','School of Systems & Enterprises');
insert into college values('04','College of Arts and Letters');

insert into dept values('0101','01','Computer Science');
insert into dept values('0102','01','Chemical Engineering & Materials Science');
insert into dept values('0103','01','Civil, Environmental & Ocean Engineering');
insert into dept values('0104','01','Electrical & Computer Engineering');
insert into dept values('0105','01','Mathematical Sciences');
insert into dept values('0106','01','Physics & Engineering Physics');
insert into dept values('0201','02','School of Business');

insert into major values('010401', '0104', '01','Electrical Engineering');
insert into major values('010402', '0104', '01','Computer Engineering');
insert into major values('010403', '0104', '01','Information and Data Engineering');
insert into major values('010101', '0101', '01','Computer Science');
insert into major values('020101', '0201', '02','MBA/Executive MBA');
insert into major values('020102', '0201', '02','Business Intelligence & Analytics');
insert into major values('020103', '0201', '02','Enterprise Project Management');
insert into major values('020104', '0201', '02','Finance');
insert into major values('020105', '0201', '02','Information Systems');

insert into student values('10399614','Lei', '', 'Zheng', 'Male','1999-9-9','China','01','0104','010402','2015-9-1', '2017-5-15','leizheng8686@gmail.com','Jersey City','123456789');
insert into student values('10411379','Shengkai', '', 'Yu', 'Male','1992-10-25','China','01','0104','010402','2015-9-1','2017-5-15','ysk1@163.com','newport****','201****');
insert into student values('12345678','Barack', 'Hussein', 'Obama', 'Male','1961-8-4','USA','02','0201','020104','2015-9-1','2017-5-15','obama@gmail.com','Washington DC','2024561414');


insert into user_stu values('10399614','10399614');
insert into user_stu values('10411379','10411379');
insert into user_stu values('12345678','12345678');

insert into user_admin values('Lei','123456');
insert into user_admin values('Shengkai','123456');

insert into course values('EE810J','Engineering Programming: Java', 3.0,'01','0104', '010401');
insert into course values('EE810C','Engineering Programming: C++', 3.0,'01','0104', '010401');
insert into course values('CPE593B','Applied Data Structures and Algorithms', 3.0,'01','0104', '010402');
insert into course values('CPE695','Applied Machine Learning', 3.0,'01','0104', '010402');
insert into course values('CS561B','Database Management Systems I', 3.0,'01','0101', '010101');
insert into course values('CS555','Agile Methods for Software Developement', 3.0,'01','0101', '010101');
insert into course values('EE810P','Engineering Programming: Python', 3.0,'01','0104', '010401');
insert into course values('FIN510A','Financial Statement Analysis', 3.0,'02','0201', '020104');
insert into course values('FIN530A','Investment Banking', 3.0,'02','0201', '020104');
insert into course values('FIN545A','Risk Mangt for Fincl Cybersecrty', 3.0,'02','0201', '020104');
insert into course values('FIN615A','Financial Decision Making', 3.0,'02','0201', '020104');

insert into courseinfo values('EE810J','1','3','X106', 'Dov Kruger', 30, 30, 'Java is a general-purpose computer programming language that is concurrent, class-based, object-oriented, and specifically designed to have as few implementation dependencies as possible. It is intended to let application developers "write once, run anywhere" (WORA), meaning that compiled Java code can run on all platforms that support Java without the need for recompilation. Java applications are typically compiled to bytecode that can run on any Java virtual machine (JVM) regardless of computer architecture.');
insert into courseinfo values('EE810C','3','3','EE715', 'Dov Kruger ', 30, 30, 'Hello, I am an introduction.');
insert into courseinfo values('CPE593B','2','3','M205', 'Dov Kruger', 30, 0, 'Hello, I am Applied Data Structures and Algorithms.');
insert into courseinfo values('CPE695','3','4','BC210', 'Duan', 30, 30, 'Hello, I am Applied Machine Learning.');
insert into courseinfo values('CS561B','4','4','E222', 'Kim Samuel', 30, 30, 'Hello, I am Database Management Systems I.');
insert into courseinfo values('CS555','3','3','BC304', 'Rowland James', 30, 30, 'Hello, I am Agile Methods for Software Developement.');
insert into courseinfo values('EE810P','5','1','BC107', 'Mukundan Iyengar', 30, 30, 'Hello, I am Engineering Programming: Python.');
insert into courseinfo values('FIN510A','2','3','BC122', 'Liu F', 35, 35, 'Hello, I am Financial Statement Analysis.');
insert into courseinfo values('FIN530A','4','4','BC205', 'Katsikiotis V', 35, 35, 'Hello, I am Investment Banking');
insert into courseinfo values('FIN545A','1','4','BC317', 'Rohmeyer P', 35, 35, 'Hello, I am Risk Mangt for Fincl Cybersecrty.');
insert into courseinfo values('FIN615A','1','3','BC302', 'Rohmeyer P', 35, 35, 'Hello, I am Risk Mangt for Fincl Cybersecrty.');

