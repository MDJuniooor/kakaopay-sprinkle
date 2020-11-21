INSERT INTO ROOM(id) VALUES('1-room');
INSERT INTO ROOM(id) VALUES('2-room');
INSERT INTO ROOM(id) VALUES('3-room');
INSERT INTO ROOM(id) VALUES('4-room');

INSERT INTO USER(id) VALUES(1);
INSERT INTO USER(id) VALUES(2);
INSERT INTO USER(id) VALUES(3);
INSERT INTO USER(id) VALUES(4);
INSERT INTO USER(id) VALUES(5);
INSERT INTO USER(id) VALUES(6);
INSERT INTO USER(id) VALUES(7);
INSERT INTO USER(id) VALUES(8);
INSERT INTO USER(id) VALUES(9);
INSERT INTO USER(id) VALUES(10);

INSERT INTO ROOM_JOIN_INFO(id,joined_date,room_id, user_id) VALUES(1,now(),'1-room',1);
INSERT INTO ROOM_JOIN_INFO(id,joined_date,room_id, user_id) VALUES(2,now(),'2-room',1);
INSERT INTO ROOM_JOIN_INFO(id,joined_date,room_id, user_id) VALUES(3,now(),'2-room',2);
INSERT INTO ROOM_JOIN_INFO(id,joined_date,room_id, user_id) VALUES(4,now(),'3-room',1);
INSERT INTO ROOM_JOIN_INFO(id,joined_date,room_id, user_id) VALUES(5,now(),'3-room',2);
INSERT INTO ROOM_JOIN_INFO(id,joined_date,room_id, user_id) VALUES(6,now(),'3-room',3);
INSERT INTO ROOM_JOIN_INFO(id,joined_date,room_id, user_id) VALUES(7,now(),'4-room',1);
INSERT INTO ROOM_JOIN_INFO(id,joined_date,room_id, user_id) VALUES(8,now(),'4-room',2);
INSERT INTO ROOM_JOIN_INFO(id,joined_date,room_id, user_id) VALUES(9,now(),'4-room',3);
INSERT INTO ROOM_JOIN_INFO(id,joined_date,room_id, user_id) VALUES(10,now(),'4-room',4);

