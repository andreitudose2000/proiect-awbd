INSERT INTO CARSERVICE VALUES(1, 'Adresa1', 'Service1');
INSERT INTO CARSERVICE VALUES(2, 'Adresa2', 'Service2');
INSERT INTO CARSERVICE VALUES(3, 'Adresa3', 'Service3');
INSERT INTO CARSERVICE VALUES(4, 'Adresa4', 'Service4');


INSERT INTO MECHANIC VALUES(1, 'Prenume1', 'Nume1', 'SUSPENSION', 1);
INSERT INTO MECHANIC VALUES(2, 'Prenume2', 'Nume2', 'SUSPENSION', 3);
INSERT INTO MECHANIC VALUES(3, 'Prenume3', 'Nume3', 'SUSPENSION', 2);
INSERT INTO MECHANIC VALUES(4, 'Prenume4', 'Nume4', 'SUSPENSION', 3);



INSERT INTO CLIENT VALUES(1, '1999-07-07', 'email@email.com', 'Prenume1', 'Nume1', '123 456 7890');
INSERT INTO CLIENT VALUES(2, '1999-07-07', 'email@email.com', 'Prenume2', 'Nume2', '123 456 7890');
INSERT INTO CLIENT VALUES(3, '1999-07-07', 'email@email.com', 'Prenume3', 'Nume3', '123 456 7890');


INSERT INTO TASK VALUES(1, 'Comment');
INSERT INTO TASK VALUES(2, 'Comment');
INSERT INTO TASK VALUES(3, 'Comment');


INSERT INTO PART VALUES(1, 'Brand1', 'Name1');
INSERT INTO PART VALUES(2, 'Brand2', 'Name2');
INSERT INTO PART VALUES(3, 'Brand3', 'Name3');


INSERT INTO TASK_PART VALUES(1, 1);
INSERT INTO TASK_PART VALUES(1, 2);
INSERT INTO TASK_PART VALUES(1, 3);
INSERT INTO TASK_PART VALUES(2, 3);
INSERT INTO TASK_PART VALUES(2, 2);
INSERT INTO TASK_PART VALUES(3, 1);
INSERT INTO TASK_PART VALUES(3, 3);


INSERT INTO APPOINTMENT VALUES(1, 'Comment', '2024-01-01T8:47:56Z', '2024-01-01T09:47:56Z', 2, 2, 1 );
INSERT INTO APPOINTMENT VALUES(2, 'Comment', '2024-01-01T10:47:56Z', '2024-01-01T12:47:56Z', 2, 3, 2 );
INSERT INTO APPOINTMENT VALUES(3, 'Comment', '2023-01-01T13:47:56Z', '2023-01-01T14:47:56Z', 1, 2, 3 );

