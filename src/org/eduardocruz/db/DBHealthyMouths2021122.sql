Drop database if exists DBHealthyMouths2021122;
create database DBHealthyMouths2021122;
use DBHealthyMouths2021122;

create table Pacientes(
	codigoPaciente int not null,
    nombresPaciente varchar(50) not null,
    apellidosPaciente varchar(50) not null,
    sexo char not null,
    fechaNacimiento date not null,
    direccionPaciente varchar(100) not null,
    telefonoPersonal varchar(8),
    fechaPrimeraVisita date,
    primary key PK_CodigoPaciente (codigoPaciente)
);

create table Especialidades(
	codigoEspecialidad int not null auto_increment,
    descripcion varchar(100) not null,
    primary key PK_CodigoEspecialidad (codigoEspecialidad)
);

create table Medicamentos(
	codigoMedicamento int not null auto_increment,
    nombreMedicamento varchar(100) not null,
    primary key PK_CodigoMedicamento (codigoMedicamento)
);

create table Doctores(
	numeroColegiado int not null,
    nombresDoctor varchar(50) not null,
    apellidosDoctor varchar(50) not null,
    telefonoContacto varchar(8) not null,
    codigoEspecialidad int not null,
    primary key PK_NumeroColegiado (numeroColegiado),
    constraint FK_Doctores_Especialidades foreign key (codigoEspecialidad)
		references Especialidades(codigoEspecialidad)
);

create table Recetas(
	codigoReceta int not null auto_increment,
    fechaReceta date not null,
    numeroColegiado int not null,
    primary key PK_CodigoReceta (codigoReceta),
    constraint FK_Recetas_Doctores foreign key (numeroColegiado)
		references Doctores (numeroColegiado)
);

create table Citas(
	codigoCita int not null auto_increment,
    fechaCita date not null,
    horaCita time not null,
    tratamiento varchar(100) not null,
    descCActual varchar(255) not null,
    codigoPaciente int not null,
    numeroColegiado int not null,
    primary key PK_CodigoCita (codigoCita),
    constraint FK_Citas_Pacientes foreign key (codigoPaciente)
		references Pacientes(codigoPaciente),
	constraint FK_Citas_Doctores foreign key (numeroColegiado)
		references Doctores (numeroColegiado)
);

create table DetalleRecetas(
	codigoDetalleReceta int not null auto_increment,
    dosis varchar(150) not null,
    codigoReceta int not null,
    codigoMedicamento int not null,
    primary key PK_CodigoDetalleReceta (codigoDetalleReceta),
    constraint FK_DetalleReceta_Receta foreign key (codigoReceta)
		references Recetas(codigoReceta),
	constraint FK_DetalleReceta_Medicamentos foreign key (codigoMedicamento)
    references Medicamentos(codigoMedicamento)
);

-- -------------------------------------------------------------------------------
-- ---------------------------Procedimientos Almacenados--------------------------
-- ---------------------------Pacientes-------------------------------------------
-- Agregar Paciente
Delimiter //
	create procedure sp_AgregarPaciente(in codigoPaciente int, in nombresPaciente varchar(50), in apellidosPaciente varchar(50),
		in sexo char , in fechaNacimiento date, in direccionPaciente varchar(100), in telefonoPersonal varchar(8), in fechaPrimeraVisita date)
        Begin
			insert into Pacientes(codigoPaciente, nombresPaciente, apellidosPaciente, sexo, fechaNacimiento, direccionPaciente, telefonoPersonal, fechaPrimeraVisita)
				values(codigoPaciente, nombresPaciente, apellidosPaciente, sexo, fechaNacimiento, direccionPaciente, telefonoPersonal, fechaPrimeraVisita);
        End //
Delimiter ;

call sp_AgregarPaciente (1001, 'Javier Estuardo', 'Herrera Chinchilla', 'm', '2002-02-14', 'Zona 1 Mixco', '65487921', now());
call sp_AgregarPaciente (1002, 'Javier Estuardo', 'Herrera Chinchilla', 'm', '2002-02-14', 'Zona 1 Mixco', '65487921', '2015-03-22');
call sp_AgregarPaciente (1004, 'Fares Araón', 'Cabrera Méndez', 'm', '2004-06-20', 'Zona 1 San Cristóbal', '15630486', '2020-02-14');
call sp_AgregarPaciente (1003, 'Abimael Lopez', 'Cuyán Martínez', 'm', '2001-12-22', 'Zona 2 Villa Nueva', '19786503', '2022-03-16');
call sp_AgregarPaciente (1005, 'Gabriel Fernando', 'López Martínez', 'm', '1999-05-15', 'Zona 3 San Cristóbal', '87320154', '2021-03-03');
call sp_AgregarPaciente (1006, 'Ian Javier', 'Gómez Giménez', 'm', '2000-11-20', 'Zona 4 Guatemala', '89751203', '2017-12-10');
call sp_AgregarPaciente (1007, 'Erik Gaspar', 'Hernández Mejía', 'm', '2004-10-16', 'Zona 1 Villa Nueva', '98601248', '2019-09-22');
call sp_AgregarPaciente (1008, 'Gustavo Adolfo', 'Morales Muñoz', 'm', '2003-02-01', 'Zona 2 Guatemala', '12015634', '2014-11-15');
call sp_AgregarPaciente (1009, 'Jorge Luis', 'Pérez Pineda', 'm', '1998-02-14', 'Zona 1 Mixco', '87963214', '2019-04-24');
call sp_AgregarPaciente (1010, 'Juan Ángel', 'Quispe Portillo', 'm', '1998-05-15', 'Zona 4 Villa Nueva', '98630154', '2022-06-14');

-- Listar Paciente
Delimiter //
	create procedure sp_ListarPacientes()
		Begin
			select P.codigoPaciente, P.nombresPaciente, P.apellidosPaciente, P.sexo, P.fechaNacimiento, P.direccionPaciente,
				P.telefonoPersonal, P.fechaPrimeraVisita from Pacientes P;
        End //
Delimiter ;

call sp_ListarPacientes;
-- Buscar Paciente
Delimiter //
	create procedure sp_BuscarPaciente(in codPaciente int)
		Begin
			select P.codigoPaciente, P.nombresPaciente, P.apellidosPaciente, P.sexo, P.fechaNacimiento, P.direccionPaciente,
				P.telefonoPersonal, P.fechaPrimeraVisita from Pacientes P where codigoPaciente = codPaciente;
        End //
Delimiter ;

call sp_BuscarPaciente(1001);
-- Eliminar Paciente
Delimiter //
	create procedure sp_EliminarPaciente(in codPaciente int)
		Begin
			delete from Pacientes where codigoPaciente = codPaciente;
        End //
Delimiter ;

-- call sp_EliminarPaciente(1001);
-- Editar Paciente
Delimiter //
	create procedure sp_EditarPaciente(in codPaciente int, in nPaciente varchar(50), in aPaciente varchar(50),
		in sx char , in fNacimiento date, in dPaciente varchar(100), in telefonoP varchar(8), in fPVisita date)
        Begin
			update Pacientes P set P.nombresPaciente = nPaciente, P.apellidosPaciente = aPaciente, P.sexo = sx, P.fechaNacimiento = fNacimiento,
				P.direccionPaciente = dPaciente, P.telefonoPersonal = telefonoP, P.fechaPrimeraVisita = fPVisita
				where codigoPaciente = codPaciente;
        End //
Delimiter ;

call sp_EditarPaciente(1001, 'Daniel Alexander', 'Tablas Monzón', 'm', '2003-06-25', 'Zona 1 Mixco', 45320198, now());


-- ---------------------------Especialidades-------------------------------------------
-- Agregar Especialidad
Delimiter //
	create procedure sp_AgregarEspecialidad(in descripcion varchar(100))
        Begin
			insert into Especialidades(descripcion)
				values(descripcion);
        End //
Delimiter ;

call sp_AgregarEspecialidad ('Alergología');
call sp_AgregarEspecialidad ('Anestesiología');
call sp_AgregarEspecialidad ('Endocrinología');
call sp_AgregarEspecialidad ('Reanimación');
call sp_AgregarEspecialidad ('Infectología');
call sp_AgregarEspecialidad ('Patología oral');
call sp_AgregarEspecialidad ('Patología maxilofacial');
call sp_AgregarEspecialidad ('Cirugía maxilofacial');
call sp_AgregarEspecialidad ('Radiología oral');
call sp_AgregarEspecialidad ('Ortopedia dentofacial');
-- Listar Especialidades
Delimiter //
	create procedure sp_ListarEspecialidades()
		Begin
			select E.codigoEspecialidad, E.descripcion from Especialidades E;
        End //
Delimiter ;

-- call sp_ListarEspecialidades;
-- Buscar Especialidad
Delimiter //
	create procedure sp_BuscarEspecialidad(in codEspecialidad int)
		Begin
			select E.codigoEspecialidad, E.descripcion from Especialidades E where codigoEspecialidad = codEspecialidad;
        End //
Delimiter ;

-- call sp_BuscarEspecialidad();
-- Eliminar Especialidad
Delimiter //
	create procedure sp_EliminarEspecialidad(in codEspecialidad int)
		Begin
			delete from Especialidades where codigoEspecialidad = codEspecialidad;
        End //
Delimiter ;

-- call sp_EliminarEspecialidad();
-- Editar Especialidad
Delimiter //
	create procedure sp_EditarEspecialidad(in codEspecialidad int, in descr varchar(100))
        Begin
			update Especialidades E set E.descripcion = descr
				where codigoEspecialidad = codEspecialidad;
        End //
Delimiter ;

-- call sp_EditarEspecialidad();


-- ---------------------------Medicamentos-------------------------------------------
-- Agregar Medicamento
Delimiter //
	create procedure sp_AgregarMedicamento(in nombreMedicamento varchar(100))
        Begin
			insert into Medicamentos(nombreMedicamento)
				values(nombreMedicamento);
        End //
Delimiter ;

call sp_AgregarMedicamento ('Clorhexidina');
call sp_AgregarMedicamento ('Flúor');
call sp_AgregarMedicamento ('Tetraciclina');
call sp_AgregarMedicamento ('Antifunguicos');
call sp_AgregarMedicamento ('Antiséptico');
call sp_AgregarMedicamento ('Metronidazol');
call sp_AgregarMedicamento ('Clindamicina');
call sp_AgregarMedicamento ('Azitromicina');
call sp_AgregarMedicamento ('Eritromicina');
call sp_AgregarMedicamento ('Antibióticos');
-- Listar Medicamentos
Delimiter //
	create procedure sp_ListarMedicamentos()
		Begin
			select M.codigoMedicamento, M.nombreMedicamento from Medicamentos M;
        End //
Delimiter ;

-- call sp_ListarMedicamentos;
-- Buscar Medicamento
Delimiter //
	create procedure sp_BuscarMedicamento(in codMedicamento int)
		Begin
			select M.codigoMedicamento, M.nombreMedicamento from Medicamentos M where codigoMedicamento = codMedicamento;
        End //
Delimiter ;

-- call sp_BuscarMedicamento();
-- Eliminar Medicamento
Delimiter //
	create procedure sp_EliminarMedicamento(in codMedicamento int)
		Begin
			delete from Medicamentos where codigoMedicamento = codMedicamento;
        End //
Delimiter ;

-- call sp_EliminarMedicamento();
-- Editar Medicamento
Delimiter //
	create procedure sp_EditarMedicamento(in codMedicamento int, in nMedicamento varchar(100))
        Begin
			update Medicamentos M set M.nombreMedicamento = nMedicamento
				where codigoMedicamento = codMedicamento;
        End //
Delimiter ;

-- call sp_EditarMedicamento();



-- ---------------------------Doctores-------------------------------------------
-- Agregar Doctor
Delimiter //
	create procedure sp_AgregarDoctor(in  numeroColegiado int, in nombresDoctor varchar(50), in apellidosDoctor varchar(50), in telefonoContacto varchar(8), in codigoEspecialidad int)
        Begin
			insert into Doctores(numeroColegiado, nombresDoctor, apellidosDoctor, telefonoContacto, codigoEspecialidad)
				values(numeroColegiado, nombresDoctor, apellidosDoctor, telefonoContacto, codigoEspecialidad);
        End //
Delimiter ;

call sp_AgregarDoctor(654812, 'Carlos Estuardo', 'Herrera Polanco', '54983212', 2);
call sp_AgregarDoctor(893201, 'Addiel Alexander', 'Hernández Cabrera', '69472031', 4);
call sp_AgregarDoctor(986340, 'Jonathan Alfonso', 'Martínez Zambrano', '48623021', 1);
call sp_AgregarDoctor(456982, 'Juan Pérez', 'Juárez Monterroso', '89730152', 3);
call sp_AgregarDoctor(896203, 'Jorge Mario', 'López Moreira', '12304598', 5);
call sp_AgregarDoctor(206541, 'Diego Alexander', 'Pita Barrechea', '54980124', 7);
call sp_AgregarDoctor(653021, 'Carlos Roberto', 'Contreras de León', '98653014', 9);
call sp_AgregarDoctor(654870, 'César David', 'Espinoza Duarte', '89754102', 6);
call sp_AgregarDoctor(410248, 'Emanuel Enrique', 'García Gómez', '89714024', 8);
call sp_AgregarDoctor(986532, 'Félix María', 'Gonzáles Gutiérrez', '48623021', 10);

-- Listar Doctores
Delimiter //
	create procedure sp_ListarDoctores()
		Begin
			select D.numeroColegiado, D.nombresDoctor, D.apellidosDoctor, D.telefonoContacto, D.codigoEspecialidad from Doctores D;
        End //
Delimiter ;

-- call sp_ListarDoctores;
-- Buscar Doctor
Delimiter //
	create procedure sp_BuscarDoctor(in nColegiado int)
		Begin
			select D.numeroColegiado, D.nombresDoctor, D.apellidosDoctor, D.telefonoContacto, D.codigoEspecialidad from Doctores D where numeroColegiado = nColegiado;
        End //
Delimiter ;

-- call sp_BuscarDoctor();
-- Eliminar Doctor
Delimiter //
	create procedure sp_EliminarDoctor(in nColegiado int)
		Begin
			delete from Doctores where numeroColegiado = nColegiado;
        End //
Delimiter ;

-- call sp_EliminarDoctor();
-- Editar Doctor
Delimiter //
	create procedure sp_EditarDoctor(in nColegiado int, in nDoctor varchar(50), in aDoctor varchar(50), in tContacto varchar(8), in codEspecialidad int)
        Begin
			update Doctores D set D.nombresDoctor = nDoctor, D.apellidosDoctor = aDoctor, D.telefonoContacto = tContacto, D.codigoEspecialidad = codEspecialidad
				where numeroColegiado = nColegiado;
        End //
Delimiter ;

-- call sp_EditarDoctor();



-- ---------------------------Recetas-------------------------------------------
-- Agregar Receta
Delimiter //
	create procedure sp_AgregarReceta(in fechaReceta date, in numeroColegiado int)
        Begin
			insert into Recetas(fechaReceta, numeroColegiado)
				values(fechaReceta, numeroColegiado);
        End //
Delimiter ;

call sp_AgregarReceta ('2015-04-22', 893201);
call sp_AgregarReceta ('2017-10-03', 654812);
call sp_AgregarReceta ('2020-06-15', 986340);
call sp_AgregarReceta ('2018-03-16', 206541);
call sp_AgregarReceta ('2022-06-05', 410248);
call sp_AgregarReceta ('2019-07-15', 986532);
call sp_AgregarReceta ('2021-09-11', 410248);
call sp_AgregarReceta ('2020-07-30', 654870);
call sp_AgregarReceta ('2019-08-29', 653021);
call sp_AgregarReceta ('2022-04-25', 896203);
-- Listar Recetas
Delimiter //
	create procedure sp_ListarRecetas()
		Begin
			select R.codigoReceta, R.fechaReceta, R.numeroColegiado from Recetas R;
        End //
Delimiter ;

-- call sp_ListarRecetas;
-- Buscar Receta
Delimiter //
	create procedure sp_BuscarReceta(in codReceta int)
		Begin
			select R.codigoReceta, R.fechaReceta, R.numeroColegiado from Recetas R where codigoReceta = codReceta;
        End //
Delimiter ;

-- call sp_BuscarReceta();
-- Eliminar Receta
Delimiter //
	create procedure sp_EliminarReceta(in codReceta int)
		Begin
			delete from Recetas where codigoReceta = codReceta;
        End //
Delimiter ;

-- call sp_EliminarReceta();
-- Editar Receta
Delimiter //
	create procedure sp_EditarReceta(in codReceta int, in fReceta date, in nColegiado int)
        Begin
			update Recetas R set R.fechaReceta = fReceta, R.numeroColegiado = nColegiado
				where codigoReceta = codReceta;
        End //
Delimiter ;

-- call sp_EditarReceta();



-- ---------------------------Citas-------------------------------------------
-- Agregar Cita
Delimiter //
	create procedure sp_AgregarCita(in fechaCita date, in horaCita time, in tratamiento varchar(100), in descCActual varchar(255), in codigoPaciente int, in numeroColegiado int)
        Begin
			insert into Citas(fechaCita, horaCita, tratamiento, descCActual, codigoPaciente, numeroColegiado)
				values(fechaCita, horaCita, tratamiento, descCActual, codigoPaciente, numeroColegiado);
        End //
Delimiter ;

call sp_AgregarCita ('2018-06-15', '19:30', 'Frenos dentales', 'En proceso de recuperación', 1004, 893201);
call sp_AgregarCita ('2019-12-10', '05:30', 'Extracción de diente frontal', 'En proceso de mejora', 1002, 654812);
call sp_AgregarCita ('2020-02-20', '16:45', 'Carillas dentales', 'Completamente restaurado', 1001, 986340);
call sp_AgregarCita ('2022-05-15', '11:20', 'Blanqueamiento dental', 'En espera de asignación', 1003, 896203);
call sp_AgregarCita ('2019-10-12', '15:30', 'Ortodocia Brackets', 'En proceso de mejoría', 1005, 654870);
call sp_AgregarCita ('2018-12-25', '08:55', 'Prótesis dental', 'Cancelado por complicaciones', 1007, 653021);
call sp_AgregarCita ('2020-11-15', '17:45', 'Extracción de muelas del juicio', 'En proceso de recuperación', 1009, 986532);
call sp_AgregarCita ('2016-10-13', '20:05', 'Férula dental', 'En espera de asignación', 1008, 410248);
call sp_AgregarCita ('2019-04-23', '10:25', 'Implantes dentales', 'Cancelado por complicaciones', 1006, 206541);
call sp_AgregarCita ('2018-06-30', '09:30', 'Carillas bucales', 'Completamente restaurado', 1010, 410248);
-- Listar Citas
Delimiter //
	create procedure sp_ListarCitas()
		Begin
			select C.codigoCita, C.fechaCita, C.horaCita, C.tratamiento, C.descCActual, C.codigoPaciente, C.numeroColegiado from Citas C;
        End //
Delimiter ;

-- call sp_ListarCitas;
-- Buscar Cita
Delimiter //
	create procedure sp_BuscarCita(in codCita int)
		Begin
			select C.codigoCita, C.fechaCita, C.horaCita, C.tratamiento, C.descCActual, C.codigoPaciente, C.numeroColegiado from Citas C where codigoCita = codCita;
        End //
Delimiter ;

-- call sp_BuscarCita();
-- Eliminar Medicamento
Delimiter //
	create procedure sp_EliminarCita(in codCita int)
		Begin
			delete from Citas where codigoCita = codCita;
        End //
Delimiter ;

-- call sp_EliminarCita();
-- Editar Cita
Delimiter //
	create procedure sp_EditarCita(in codCita int, in fCita date, in hCita time, in tratamiento varchar(100), in descCActual varchar(250), in codPaciente int, in numColegiado int)
        Begin
			update Citas C set C.fechaCita = fCita, C.horaCita = hCita, C.tratamiento = tratamiento, C.descCActual = descCActual, C.codigoPaciente = codPaciente, C.numeroColegiado = numColegiado
				where codigoCita = codCita;
        End //
Delimiter ;

-- call sp_EditarCita();



-- ---------------------------Detalle Receta-------------------------------------------
-- Agregar Detalle Receta
Delimiter //
	create procedure sp_AgregarDetalleReceta(in dosis varchar(150), in codigoReceta int, in codigoMedicamento int)
        Begin
			insert into DetalleRecetas(dosis, codigoReceta, codigoMedicamento)
				values(dosis, codigoReceta, codigoMedicamento);
        End //
Delimiter ;

call sp_AgregarDetalleReceta('Dos veces al día, cada 8 horas', 7, 4);
call sp_AgregarDetalleReceta('Tres veces al día, cada 8 horas', 3, 1);
call sp_AgregarDetalleReceta('Una vez al día', 2, 3);
call sp_AgregarDetalleReceta('Dos veces al día, cada 10 horas', 1, 5);
call sp_AgregarDetalleReceta('Cuatro veces al día, cada 6 horas', 4, 2);
call sp_AgregarDetalleReceta('Tres veces al día, cada 8 horas', 5, 7);
call sp_AgregarDetalleReceta('Dos veces al día, cada 10 horas', 8, 6);
call sp_AgregarDetalleReceta('Una vez al día', 9, 10);
call sp_AgregarDetalleReceta('Cuatro veces al día, cada 6 horas', 6, 9);
call sp_AgregarDetalleReceta('Cinco veces al día, cada 4 horas', 10, 8);
-- Listar Detalle Recetas
Delimiter //
	create procedure sp_ListarDetalleRecetas()
		Begin
			select D.codigoDetalleReceta, D.dosis, D.codigoReceta, D.codigoMedicamento from DetalleRecetas D;
        End //
Delimiter ;

-- call sp_ListarDetalleRecetas;
-- Buscar Detalle Receta
Delimiter //
	create procedure sp_BuscarDetalleReceta(in codDetalleReceta int)
		Begin
			select D.codigoDetalleReceta, D.dosis, D.codigoReceta, D.codigoMedicamento from DetalleRecetas D where codigoDetalleReceta = codDetalleReceta;
        End //
Delimiter ;

-- call sp_BuscarDetalleReceta();
-- Eliminar Detalle Receta
Delimiter //
	create procedure sp_EliminarDetalleReceta(in codDetalleReceta int)
		Begin
			delete from DetalleRecetas where codigoDetalleReceta = codDetalleReceta;
        End //
Delimiter ;

-- call sp_EliminarDetalleReceta();
-- Editar Detalle Receta
Delimiter //
	create procedure sp_EditarDetalleReceta(in codDetalleReceta int, in dosis varchar(150), in codReceta int, in codMedicamento int)
        Begin
			update DetalleRecetas D set D.dosis = dosis, D.codigoReceta = codReceta, D.codigoMedicamento = codMedicamento
				where codigoDetalleReceta = codDetalleReceta;
        End //
Delimiter ;

-- call sp_EditarDetalleReceta();



-- ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'admin';


create table Usuario(
	codigoUsuario int not null auto_increment,
    nombreUsuario varchar(100) not null,
    apellidoUsuario varchar(100) not null,
    usuarioLogin varchar(50) not null,
    contrasena varchar(50) not null,
    primary key PK_CodigoUsuario(codigoUsuario)
);



Delimiter //
	create procedure sp_AgregarUsuario(in nombreUsuario varchar(100), in apellidoUsuario varchar(100), in usuarioLogin varchar(50), in contrasena varchar(50))
		Begin
			insert into Usuario(nombreUsuario, apellidoUsuario, usuarioLogin, contrasena)
				values(nombreUsuario, apellidoUsuario, usuarioLogin, contrasena);
        End //
Delimiter ;
call sp_AgregarUsuario('Eduardo', 'Cruz', 'ecruz', '2021122');


Delimiter //
	create procedure sp_ListarUsuarios()
		Begin
			Select U.codigoUsuario, U.nombreUsuario, U.apellidoUsuario, U.usuarioLogin, U.contrasena from Usuario U;
        End //
Delimiter ;
call sp_ListarUsuarios();





create table Login(
	usuarioMaster varchar(50) not null,
    passwordLogin varchar(50) not null,
    primary key PL_UsuarioMaster(usuarioMaster)
);