begin;

update member set money = 10000 where member_id = 'transactionTestA';
update member set money = 30000 where member_id = 'transactionTestB';

commit;