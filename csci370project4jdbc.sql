drop schema if exists csci370project4jdbc;
create schema csci370project4jdbc;

create table csci370project4jdbc.Cinemas
(
	id int not null auto_increment,
    name varchar(255) not null,
    x int not null,
	y int not null,
    matinee varchar(15) not null,
    afternoon varchar(15) not null,
    evening varchar(15) not null,
    late varchar(15) not null,
    
    playg tinyint(1) not null,
    playpg tinyint(1) not null,
    playpg13 tinyint(1) not null,
    playr tinyint(1) not null,
    plaync17 tinyint(1) not null,
    
    playgeneral tinyint(1) not null,
    
    primary key (id)
);

insert into csci370project4jdbc.Cinemas
(
	name,
    x,
    y,
    
    matinee,
    afternoon,
    evening,
    late,
    
    playg,
    playpg,
    playpg13,
    playr,
    plaync17,
    
    playgeneral
) values
	('Multiplex', 50, 50, '1:00 p.m.', '4:00 p.m.', '7:00 p.m.', '12:00 a.m.', 1, 1, 1, 1, 1, 1),
    ('Odeon', 25, 25, '1:00 p.m.', '5:00 p.m.', '8:00 p.m.', '12:00 a.m.', 1, 1, 1, 1, 1, 1),
    ('Mom & Pop Movies', 75, 75, '1:30 p.m.', '4:00 p.m.', '9:00 p.m.', '12:00 a.m.', 1, 1, 1, 0, 0, 1),
    ('Indie Arts Theater', 60, 40, '1:00 p.m.', '5:00 p.m.', '7:00 p.m.', '12:00 a.m.', 1, 1, 1, 1, 1, 0);

create table csci370project4jdbc.Movies
(
	id int not null auto_increment,
	title varchar(255) not null,
    rating varchar(15) not null,
    isgeneral tinyint(1) not null,
    primary key (id)
);
insert into csci370project4jdbc.Movies (title, rating, isgeneral) values
	('Movie1', 'G', 1),
    ('Movie2', 'R', 0),
    ('Movie3', 'NC-17', 0),
    ('Movie4', 'PG-13', 1),
    ('The Day After Trinity', 'NR', 0),
    ('SM370', 'NC-17', 0);

create table csci370project4jdbc.Showtimes
(
	id int not null auto_increment,
    movieid int not null,
    cinemaid int not null,
    showtime varchar(15) not null,
    primary key (id)
);

insert into csci370project4jdbc.Showtimes (movieid, cinemaid, showtime) values
	(1,4,'1:00 p.m.'),
    (1,1,'4:00 p.m.'),
    (2,2,'8:00 p.m.'),
    (2,3,'12:00 a.m.'),
    (3,1,'1:00 p.m.'),
    (3,3,'1:30 p.m.'),
    (4,3,'9:00 p.m.'),
    (4,4,'5:00 p.m.'),
    (5,4,'7:00 p.m.'),
    (6,4,'12:00 a.m.');
    
use csci370project4jdbc;
set @inputx = 49;
set @inputy = 51;
set @inputd = 20;
select c.name,
	c.x,
    c.y,
	sqrt(power(@inputx-c.x, 2) + power(@inputy-c.y, 2)) as distance
from cinemas as c
where sqrt(power(@inputx-c.x, 2) + power(@inputy-c.y, 2)) < @inputd;

use csci370project4jdbc;
set @moviename = 'Movie1';
select count(*) as CinemaCount
from
(
	select c.name as CinemaName,
		concat('(', c.x, ',', c.y, ')') as Address
	from showtimes as s
	inner join movies as m on s.movieid = m.id
	inner join cinemas as c on s.cinemaid = c.id
	where m.title = @moviename
	group by c.name
) as insidequery;

DROP PROCEDURE IF EXISTS csci370project4jdbc.NearbyCinemas;
DELIMITER //
CREATE PROCEDURE csci370project4jdbc.NearbyCinemas (IN x int, y int, r int)
BEGIN
SELECT c.name,
	c.x,
    c.y,
    x as inputx,
    y as inputy,
    r as radius,
    sqrt(power(x-c.x, 2) + power(y-c.y, 2)) as distance
FROM cinemas as c
WHERE sqrt(power(x-c.x, 2) + power(y-c.y, 2)) < r
ORDER BY distance ASC;
END //
-- Example call to the above procedure
-- CALL csci370project4jdbc.NearbyCinemas(49, 51, 50);
