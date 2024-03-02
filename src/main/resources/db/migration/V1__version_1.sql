-- Versão 1.0. Inclui manutenção do acervo pessoal e ficha de leitura.


-- Tabela Dados do Usuário
--
-- Nesta tabela ficarão gravados os dados de acesso dos usuários.
-- No final deste script é inserido o usuário administrador do sistema,
-- exigido para o cadastro dos demais usuários. Somente o administrador
-- terá acesso via API às funcionalidades de cadastro e manutenção de
-- outros usuários, exceto o login e alteração de nome de usuário e Senha
-- de acesso da própria conta, que poderá ser feito por qualquer usuário.
--
-- Campos:
--
-- id: Identificador chave primária do usuário. Este identificador
-- é no formato UUID (Universally Unique Identifier) e é gerado no
-- cadastro do usuário pelo próprio Postgre.
--
-- user_name: Nome de usuário para acesso ao sistema.
--
-- password: Senha para acesso ao sistema.
--
-- role: Função do usuário. Pode ser ADMIN (Administrador) ou USER
-- (usuário comum). No caso de ADMIN, tem acesso irrestrito ao sistema.
--
-- is_enabled: Esta flag indica se o usuário tem permissão de acesso
-- ao sistema (true) ou não tem permissão (false). Ela será verificada
-- a cada requisição à API.

CREATE TABLE public.user_data (
	id uuid NOT NULL DEFAULT gen_random_uuid(),
	user_name text NOT NULL UNIQUE,
	password text NOT NULL,
	role text NOT NULL,
	is_enabled bool NOT NULL,
	CONSTRAINT user_pkey PRIMARY KEY (id)
);


-- Tabela Formato do Livro
--
-- Nesta tabela ficarão gravados os formatos de livros, revistas e 
-- periódicos. Os formatos padrão são inseridos no final do script.
--
-- Campos:
-- 
-- id: Identificador chave primária do formato.
--
-- description: descrição do formato.

CREATE TABLE public.book_format (
	id int4 NOT NULL,
	description text NOT NULL,
	CONSTRAINT book_format_pkey PRIMARY KEY (id)
);


-- Tabela Capa do Livro
--
-- A capa do livro geralmente é um arquivo jpeg ou png que pode ter de
-- alguns kilobytes até alguns megabytes de tamanho. Este arquivo não ficará
-- gravado no banco de dados em alguma columa Blob, mas será copiado
-- para um diretório padrão no servidor, e o nome do arquivo dentro deste
-- diretório é que será gravado nesta tabela. Desta forma, o banco de dados
-- ficará mais leve, e a cada vez que fizer uma consulta pelos livros que 
-- estão cadastrados, não precisa se preocupar em tratar no código para não
-- carregar a imagem quando precisa somente dos demais dados do livro, como
-- para preencher uma lista de livros numa página HTML, por exemplo, evitando
-- com isso o desperdício de dados e agilizando o carregamento da página.
--
-- Campos:
--
-- id: Identificador chave primária da capa.
--
-- file_path: Nome do arquivo no diretório padrão.

CREATE TABLE public.book_cover (
	id uuid NOT NULL,
	file_path text NOT NULL UNIQUE,
	CONSTRAINT book_cover_pkey PRIMARY KEY (id)
);


-- Tabela Livro
--
-- Nesta tabela ficarão gravados todos os dados de um livro, revista ou periódico.
-- Estes podem estar no formato físico ou digital.
-- 
-- Campos:
--
-- id: Identificador chave primária.
--
-- id_format: Chave estrangeira para o formato de livro.
--
-- id_cover: Chave estrangeira para a capa do livro que estará salva no diretório
-- de mídias do servidor.
--
-- title: Título do livro.
--
-- subtitle: Subtítulo do livro.
--
-- author: Autor(es) do livro.
--
-- publisher: Editora do livro.
-- 
-- isbn: Número ISBN (International Standard Book Number) do livro.
--
-- edition: Número da edição do livro.
--
-- volume: Volume do livro.
--
-- release_year: Ano de lançamento do livro.
--
-- number_of_pages: Número de páginas do livro.
--
-- summary: Sumário do livro.
--
-- acquisition_date: Data de aquisição do livro.
--
-- registration_date: Data de cadastro do livro.
--
-- last_update_date: Data da última atualização do registro.
--
-- is_deleted: Esta Flag indica se o livro está excluído (true) ou 
-- não está excluído (false). Não há a remoção física do registro no
-- banco de dados.

CREATE TABLE public.book (
	id uuid NOT NULL,
	id_format int4 NOT NULL,
	id_cover uuid NOT NULL UNIQUE,
	title text NOT NULL,
	subtitle text NULL,
	author text NOT NULL,
	publisher text NOT NULL,
	isbn text NOT NULL,
	edition int4 NOT NULL,
	volume int4 NOT NULL,
	release_year int4 NOT NULL,
	number_of_pages int4 NOT NULL,
	summary text NULL,
	acquisition_date timestamp(6) NOT NULL,
	registration_date timestamp(6) NOT NULL,
	last_update_date timestamp(6) NOT NULL,
	is_deleted bool NOT NULL,
	CONSTRAINT book_pkey PRIMARY KEY (id),
	CONSTRAINT fk_book_format FOREIGN KEY (id_format) REFERENCES public.book_format(id),
	CONSTRAINT fk_book_cover FOREIGN KEY (id_cover) REFERENCES public.book_cover(id)
);


-- Tabela Pessoa
--
-- Nesta tabela ficarão gravados os dados de pessoas para quem foram feitas 
-- doações ou empréstimos de livros físicos.
--
-- Campos:
--
-- id: Identificador chave primária.
--
-- name: Nome da pessoa.
--
-- description: Descrição da pessoa ou outros dados como endereço.
--
-- registration_date: Data de cadastro da pessoa.
--
-- last_update_date: Data da última atualização do registro.
--
-- is_deleted: Esta Flag indica se a pessoa está excluída (true) ou 
-- não está excluída (false). Não há a remoção física do registro no
-- banco de dados.

CREATE TABLE public.person (
	id uuid NOT NULL,
	"name" text NOT NULL,
	description text NOT NULL,
	registration_date timestamp(6) NOT NULL,
	last_update_date timestamp(6) NOT NULL,
	is_deleted bool NOT NULL,
	CONSTRAINT person_pkey PRIMARY KEY (id)
);


-- Tabela Empréstimo
--
-- Nesta tabela ficarão gravados os dados de empréstimo de livros físicos realizados.
-- O empréstimo, no contexto desta API, é uma relação em que uma pessoa pode tomar
-- um ou mais livros emprestados, por isso, neste registro não há referência para
-- um livro. O livros emprestados estão na tabela loan_item (item de empréstimo).
-- 
-- Campos:
--
-- id: Identificador chave primária do empréstimo.
--
-- id_person: Chave estrangeira para a pessoa tomadora do empréstimo.
--
-- date: Data em que o empréstimo foi realizado.
-- 
-- notes: Alguma informação adicional sobre o empréstimo.
--
-- last_update_date: Data da última atualização do registro.
--
-- is_deleted: Esta Flag indica se o empréstimo está excluído (true) ou 
-- não está excluído (false). Não há a remoção física do registro no
-- banco de dados.

CREATE TABLE public.loan (
	id uuid NOT NULL,
	id_person uuid NOT NULL,
	"date" timestamp(6) NOT NULL,
	notes text NULL,
	last_update_date timestamp(6) NOT NULL,
	is_deleted bool NOT NULL,
	CONSTRAINT loan_pkey PRIMARY KEY (id),
	CONSTRAINT fk_loan_person FOREIGN KEY (id_person) REFERENCES public.person(id)
);


-- Tabela Item de Empréstimo
--
-- Nesta tabela estarão gravados todos os livros físicos tomados em empréstimo por
-- alguma pessoa.
--
-- Campos:
--
-- id_book: Chave estrangeira para o livro tomado em empréstimo. Ela compõe a chave
-- primária composta da tabela.
--
-- id_loan: Chave estrangeira para o empréstimo. Ela compõe a chave primária composta
-- da tabela.
--
-- return_date: Data da devolução do livro emprestado.
--
-- notes: Alguma informação adicional sobre o empréstimo.
--
-- last_update_date: Data da última atualização do registro.
--
-- returned: Esta Flag indica se o livro já foi devolvido (true) ou não foi devolvido
-- (false).
--
-- is_deleted: Esta Flag indica se o item deempréstimo está excluído (true) ou 
-- não está excluído (false). Não há a remoção física do registro no
-- banco de dados.

CREATE TABLE public.loan_item (
	id_book uuid NOT NULL,
	id_loan uuid NOT NULL,
	return_date timestamp(6) NULL,
	notes text NULL,
	last_update_date timestamp(6) NOT NULL,	
	returned bool NOT NULL,
	is_deleted bool NOT NULL,
	CONSTRAINT loan_item_pkey PRIMARY KEY (id_book, id_loan),
	CONSTRAINT fk_loan_item_book FOREIGN KEY (id_book) REFERENCES public.book(id),
	CONSTRAINT fk_loan_item_loan FOREIGN KEY (id_loan) REFERENCES public.loan(id)
);


-- Tabela Livro Descartado
--
-- Nesta tabela ficarão gravados os dados sobre os livros físicos que foram descartados
-- do acervo.
-- 
-- Campos:
--
-- id_book: Identificador chave primária. No caso, é também chave estrangeira para a
-- tabela livro. Desta forma se impõe que um livro pode ser descartado somente uma vez. 
--
-- reason: Motivo do descarte do livro (dano, liberação de espaço, etc.).
--
-- last_update_date: Data da última atualização do registro.

CREATE TABLE public.discarded_book (
	id_book uuid NOT NULL,
	"date" timestamp(6) NOT NULL,
	reason text NULL,
	last_update_date timestamp(6) NOT NULL,
	CONSTRAINT discarded_book_pkey PRIMARY KEY (id_book),
	CONSTRAINT fk_discarded_book_book FOREIGN KEY (id_book) REFERENCES public.book(id)
);


-- Tabela Livro Doado
--
-- Nesta tabela ficarão gravados os dados sobre os livros físicos que foram doados para
-- alguma pessoa.
--
-- Campos:
--
-- id_book: Identificador chave primária. No caso, é também chave estrangeira para a tabela
-- livro. Desta forma se impõe que um livro pode ser doado somente uma vez.
--
-- id_person: Chave estrangeira para a pessoa que foi realizada a doação.
--
-- date: Data que o livro foi doado para a pessoa.
--
-- notes: Alguma informação adicional sobre o empréstimo.
--
-- last_update_date: Data da última atualização do registro.

CREATE TABLE public.donated_book (
	id_book uuid NOT NULL,
	id_person uuid NOT NULL,
	"date" timestamp(6) NOT NULL,
	notes text NULL,
	last_update_date timestamp(6) NOT NULL,
	CONSTRAINT donated_book_pkey PRIMARY KEY (id_book),
	CONSTRAINT fk_donated_book_book FOREIGN KEY (id_book) REFERENCES public.book(id),
	CONSTRAINT fk_donated_book_person FOREIGN KEY (id_person) REFERENCES public.person(id)
);


-- Tabela  Ficha de Leitura
--
-- Nesta tabela ficarão gravados os dados sobre as leituras realizadas. Funciona como
-- uma ficha de leitura. É nela que serão gravados os dados do app Android de Ficha de
-- Leitura desenvolvido.
--
-- Campos:
--
-- id: Identificador chave primária.
--
-- id_book: Livro registrado na ficha de leitura.
-- 
-- begin_date: Data de início da leitura do livro.
--
-- end_date: Data de término da leitura do livro.
--
-- notes: Alguma informação adicional sobre a leitura.
--
-- registration_date: Data de cadatro da leitura.
--
-- last_update_date: Data da última atualização do registro.
--
-- reading_completed: Esta Flag indica se a leitura já foi concluída (true) ou ainda não
-- foi concluída (false).
--
-- is_deleted: Esta Flag indica se o registro está excluído (true) ou 
-- não está excluído (false). Não há a remoção física do registro no
-- banco de dados.

CREATE TABLE public.reading_record (
	id uuid NOT NULL,
	id_book uuid NOT NULL,
	begin_date timestamp(6) NOT NULL,
	end_date timestamp(6) NULL,
	notes text NULL,
	registration_date timestamp(6) NOT NULL,
	last_update_date timestamp(6) NOT NULL,
	reading_completed bool NOT NULL,
	is_deleted bool NULL,
	CONSTRAINT reading_record_pkey PRIMARY KEY (id),
	CONSTRAINT fk_reading_record_book FOREIGN KEY (id_book) REFERENCES public.book(id)
);


-- Tabela Configurações
-- 
-- Nesta tabela ficarão gravadas as configurações do usuário. Por exemplo, pode definir
-- com que cores de fonte vai exibir livros emprestados numa lista, também livros doados 
-- ou excluídos do acervo.
--
-- Campos:
--
-- key: Chave para a configuração.
--
-- id_user: Chave estrangeira para o usuário.
--
-- value_string: Valor String da configuração.
--
-- value_long: Valor Long da configuração.
--
-- value_double: Valor Double da configuração.
--
-- value_boolean: Valor Boolean da configuração.
--
-- value_blob: Valor Blob da configuração.

CREATE TABLE public.settings (
	setting_key text NOT NULL,
	id_user uuid NOT NULL,
	value_string text NULL,
	value_long int8 NULL,
	value_double float8 NULL,
	value_boolean bool NULL,
	value_blob bytea NULL,
	CONSTRAINT settings_pkey PRIMARY KEY (setting_key),
	CONSTRAINT fk_settings_user FOREIGN KEY (id_user) REFERENCES public.user_data(id)
);



-- Inserção de valores padrão no banco de dados:
-- ----------------------------------------------------------------------------



-- Os formatos de livro são especificados como:
--
-- FÍSICO:  Qualquer livro, revista ou periódico que esteja em formato impresso.
-- PDF:     Arquivo digital no formato PDF (Portable Document Format).
-- EPUB:    Arquivo digital no formato EPUD (Eletronic Publication).
-- MOBI:    Arquivo digital no formato MOBI (Mobipocket).
-- AZW:     Arquivo digital no formato AZW (Amazon Kindle).
-- OUTRO:   Arquivo digital em outro formato.
--
-- O intervalo de códigos de 50 em 50 é porque talvez eu decida inserir novos
-- formatos intercalado entre estes existentes.
-- Como não é de praxe eu converter o arquivo digital de um formato para outro
-- após a aquisição, então a informação sobre o formato específico para mim é
-- importante.

INSERT INTO public.book_format(id, description) VALUES (1, 'FÍSICO');

INSERT INTO public.book_format(id, description) VALUES (50, 'PDF');

INSERT INTO public.book_format(id, description) VALUES (100, 'EPUB');

INSERT INTO public.book_format(id, description) VALUES (150, 'MOBI');

INSERT INTO public.book_format(id, description) VALUES (200, 'AZW');

INSERT INTO public.book_format(id, description) VALUES (250, 'OUTRO');


-- Como é uma exigência que o usuário administrador já esteja cadastrado quando
-- o servidor for implantado pela primeira vez, então ele será inserido aqui.
-- Para fazer o login como administrador, use as diretivas:
--
-- userName = 'admin'
-- password = 'admim'
--
-- No banco de dados do servidor a senha não é gravada, apenas o hash dela,
-- por questões de diretivas mínimas de segurança.

INSERT INTO public.user_data(user_name, password, role, is_enabled) VALUES (
	'admin',
	'$2a$10$9yyDMo6b87a2sKu/U4TmD.KgLr/Dat/HidHeRjaosz5X0ZHy2m1iC',
	'ADMIN',
	true
 );