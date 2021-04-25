insert into users ( id, name, login, password ) values ( nextval('seq_users'), 'User 1', 'user1', '11111' );
insert into users ( id, name, login, password ) values ( nextval('seq_users'), 'User 2', 'user2', '22222' );

insert into client ( id, name ) values ( nextval('seq_client'), 'client 1' );
insert into client ( id, name ) values ( nextval('seq_client'), 'client 2' );
insert into client ( id, name ) values ( nextval('seq_client'), 'client 3' );
