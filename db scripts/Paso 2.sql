Insert into usuario (nombre, correo, tipo, activo) values ('Juan Perez', 'test@test.com', 'ESTUDIANTE', 1), ('Maria Lopez', 'test2@test.com', 'DOCENTE', 1);
Insert into equipo (codigo, nombre, activo) values ('EQ001', 'Video beam', 1), ('EQ002', 'Lab 2', 1), ('EQ003', 'HDMI Cable', 1);
Insert into reserva  (usuario_id, equipo_id, inicio, fin, estado) values (2, 21, to_date('2024-06-01', 'YYYY-MM-DD'), to_date('2024-06-10', 'YYYY-MM-DD'), 'ACTIVA'), (3, 22, to_date('2024-06-05', 'YYYY-MM-DD'), to_date('2024-06-15', 'YYYY-MM-DD'), 'ACTIVA');

select * from usuario;
select * from equipo;
select * from reserva;
insert into equipo (codigo, nombre, activo) values ('EQ004', 'Projector', 1);
update equipo set nombre = 'Updated Projector' where codigo = 'EQ004';
delete from equipo where codigo = 'EQ004';
delete from usuario where id = 5; 

select u.nombre, e.nombre, r.inicio, r.fin, r.estado
from reserva r 
join usuario u on r.usuario_id = u.id
join equipo e on r.equipo_id = e.id;

Commit;