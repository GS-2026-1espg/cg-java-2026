-- =====================================================================
-- Conecta Periferia (CitizenTech) - Database Design (GS 2026.2 - FIAP)
-- =====================================================================

-- Limpeza de tabelas existentes (caso existam)
BEGIN
    FOR t IN (SELECT column_value AS nome
              FROM TABLE(sys.odcivarchar2list('ALERTA','RELATO','MEDICAO','LINHA_PONTO','PONTO','LINHA','REGIAO')))
    LOOP
        BEGIN
            EXECUTE IMMEDIATE 'DROP TABLE ' || t.nome || ' CASCADE CONSTRAINTS';
        EXCEPTION
            WHEN OTHERS THEN
                IF SQLCODE != -942 THEN RAISE; END IF;
        END;
    END LOOP;
END;
/

CREATE TABLE REGIAO (
    id_regiao             NUMBER GENERATED ALWAYS AS IDENTITY,
    nome                  VARCHAR2(80)  NOT NULL,
    zona                  VARCHAR2(20)  NOT NULL,
    distancia_terminal_km NUMBER(5,1)   NOT NULL,
    renda_media           NUMBER(10,2)  NOT NULL,
    populacao             NUMBER(9)     NOT NULL,
    CONSTRAINT pk_regiao PRIMARY KEY (id_regiao),
    CONSTRAINT uq_regiao_nome UNIQUE (nome),
    CONSTRAINT ck_regiao_zona CHECK (zona IN ('Norte','Sul','Leste','Oeste','Centro')),
    CONSTRAINT ck_regiao_dist CHECK (distancia_terminal_km >= 0),
    CONSTRAINT ck_regiao_renda CHECK (renda_media >= 0),
    CONSTRAINT ck_regiao_pop CHECK (populacao > 0)
);

CREATE TABLE LINHA (
    id_linha             NUMBER GENERATED ALWAYS AS IDENTITY,
    codigo               VARCHAR2(10)  NOT NULL,
    nome                 VARCHAR2(100) NOT NULL,
    id_regiao            NUMBER        NOT NULL,
    frota_atual          NUMBER(4)     NOT NULL,
    intervalo_medio_min  NUMBER(4,1)   NOT NULL,
    CONSTRAINT pk_linha PRIMARY KEY (id_linha),
    CONSTRAINT uq_linha_codigo UNIQUE (codigo),
    CONSTRAINT fk_linha_regiao FOREIGN KEY (id_regiao) REFERENCES REGIAO (id_regiao),
    CONSTRAINT ck_linha_frota CHECK (frota_atual >= 0),
    CONSTRAINT ck_linha_intervalo CHECK (intervalo_medio_min > 0)
);

CREATE TABLE PONTO (
    id_ponto   NUMBER GENERATED ALWAYS AS IDENTITY,
    nome       VARCHAR2(100) NOT NULL,
    endereco   VARCHAR2(150) NOT NULL,
    latitude   NUMBER(9,6)   NOT NULL,
    longitude  NUMBER(9,6)   NOT NULL,
    id_regiao  NUMBER        NOT NULL,
    CONSTRAINT pk_ponto PRIMARY KEY (id_ponto),
    CONSTRAINT fk_ponto_regiao FOREIGN KEY (id_regiao) REFERENCES REGIAO (id_regiao),
    CONSTRAINT ck_ponto_lat CHECK (latitude BETWEEN -90 AND 90),
    CONSTRAINT ck_ponto_lon CHECK (longitude BETWEEN -180 AND 180)
);

-- Tabela associativa: N linhas passam por N pontos, em uma ordem
CREATE TABLE LINHA_PONTO (
    id_linha  NUMBER    NOT NULL,
    id_ponto  NUMBER    NOT NULL,
    ordem     NUMBER(3) NOT NULL,
    CONSTRAINT pk_linha_ponto PRIMARY KEY (id_linha, id_ponto),
    CONSTRAINT fk_lp_linha FOREIGN KEY (id_linha) REFERENCES LINHA (id_linha),
    CONSTRAINT fk_lp_ponto FOREIGN KEY (id_ponto) REFERENCES PONTO (id_ponto),
    CONSTRAINT ck_lp_ordem CHECK (ordem > 0)
);

CREATE TABLE MEDICAO (
    id_medicao        NUMBER GENERATED ALWAYS AS IDENTITY,
    id_linha          NUMBER       NOT NULL,
    id_ponto          NUMBER       NOT NULL,
    dt_medicao        DATE         NOT NULL,
    faixa_horario     VARCHAR2(10) NOT NULL,
    tempo_viagem_min  NUMBER(5)    NOT NULL,
    lotacao_pct       NUMBER(3)    NOT NULL,
    intervalo_min     NUMBER(4,1)  NOT NULL,
    CONSTRAINT pk_medicao PRIMARY KEY (id_medicao),
    CONSTRAINT fk_med_linha FOREIGN KEY (id_linha) REFERENCES LINHA (id_linha),
    CONSTRAINT fk_med_ponto FOREIGN KEY (id_ponto) REFERENCES PONTO (id_ponto),
    CONSTRAINT ck_med_faixa CHECK (faixa_horario IN ('PICO_AM','ENTREPICO','PICO_PM','NOITE')),
    CONSTRAINT ck_med_tempo CHECK (tempo_viagem_min > 0),
    CONSTRAINT ck_med_lotacao CHECK (lotacao_pct BETWEEN 0 AND 100),
    CONSTRAINT ck_med_intervalo CHECK (intervalo_min > 0)
);

CREATE TABLE RELATO (
    id_relato          NUMBER GENERATED ALWAYS AS IDENTITY,
    id_regiao          NUMBER        NOT NULL,
    id_linha           NUMBER,                      
    dt_relato          DATE          NOT NULL,
    categoria          VARCHAR2(20)  NOT NULL,
    descricao          VARCHAR2(300) NOT NULL,
    tempo_perdido_min  NUMBER(4),
    CONSTRAINT pk_relato PRIMARY KEY (id_relato),
    CONSTRAINT fk_rel_regiao FOREIGN KEY (id_regiao) REFERENCES REGIAO (id_regiao),
    CONSTRAINT fk_rel_linha FOREIGN KEY (id_linha) REFERENCES LINHA (id_linha),
    CONSTRAINT ck_rel_categoria CHECK (categoria IN ('LOTACAO','ATRASO','FREQUENCIA','SEGURANCA','OUTRO')),
    CONSTRAINT ck_rel_tempo CHECK (tempo_perdido_min IS NULL OR tempo_perdido_min >= 0)
);

CREATE TABLE ALERTA (
    id_alerta   NUMBER GENERATED ALWAYS AS IDENTITY,
    id_linha    NUMBER        NOT NULL,
    id_medicao  NUMBER,                              
    dt_alerta   DATE          NOT NULL,
    nivel       VARCHAR2(10)  NOT NULL,
    mensagem    VARCHAR2(200) NOT NULL,
    status      VARCHAR2(10)  DEFAULT 'ABERTO' NOT NULL,
    CONSTRAINT pk_alerta PRIMARY KEY (id_alerta),
    CONSTRAINT fk_ale_linha FOREIGN KEY (id_linha) REFERENCES LINHA (id_linha),
    CONSTRAINT fk_ale_medicao FOREIGN KEY (id_medicao) REFERENCES MEDICAO (id_medicao),
    CONSTRAINT ck_ale_nivel CHECK (nivel IN ('BAIXO','MEDIO','ALTO','CRITICO')),
    CONSTRAINT ck_ale_status CHECK (status IN ('ABERTO','EM_ANALISE','RESOLVIDO'))
);

-- Indices nas FKs mais consultadas
CREATE INDEX ix_linha_regiao  ON LINHA (id_regiao);
CREATE INDEX ix_ponto_regiao  ON PONTO (id_regiao);
CREATE INDEX ix_med_linha     ON MEDICAO (id_linha);
CREATE INDEX ix_rel_regiao    ON RELATO (id_regiao);
CREATE INDEX ix_ale_linha     ON ALERTA (id_linha);


ALTER TABLE LINHA ADD CONSTRAINT ck_linha_codigo_len CHECK (LENGTH(codigo) >= 4);
