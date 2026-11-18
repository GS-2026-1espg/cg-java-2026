
-- REGIAO (5)
INSERT INTO REGIAO (nome, zona, distancia_terminal_km, renda_media, populacao) VALUES ('Cidade Tiradentes', 'Leste', 28.5, 1450.00, 211000);
INSERT INTO REGIAO (nome, zona, distancia_terminal_km, renda_media, populacao) VALUES ('Guaianases',        'Leste', 24.0, 1600.00, 268000);
INSERT INTO REGIAO (nome, zona, distancia_terminal_km, renda_media, populacao) VALUES ('Itaim Paulista',    'Leste', 26.0, 1550.00, 224000);
INSERT INTO REGIAO (nome, zona, distancia_terminal_km, renda_media, populacao) VALUES ('Sao Mateus',        'Leste', 19.0, 1900.00, 155000);
INSERT INTO REGIAO (nome, zona, distancia_terminal_km, renda_media, populacao) VALUES ('Pinheiros',         'Oeste',  6.5, 9800.00, 140000);

-- LINHA (5)
INSERT INTO LINHA (codigo, nome, id_regiao, frota_atual, intervalo_medio_min) VALUES ('3001-10', 'Cidade Tiradentes - Terminal Itaquera',     1, 18, 14.0);
INSERT INTO LINHA (codigo, nome, id_regiao, frota_atual, intervalo_medio_min) VALUES ('3002-10', 'Guaianases - Terminal Itaquera',            2, 22, 10.0);
INSERT INTO LINHA (codigo, nome, id_regiao, frota_atual, intervalo_medio_min) VALUES ('3003-10', 'Itaim Paulista - Terminal Itaquera',        3, 20, 12.0);
INSERT INTO LINHA (codigo, nome, id_regiao, frota_atual, intervalo_medio_min) VALUES ('3004-10', 'Sao Mateus - Terminal Sao Mateus',          4, 25,  8.0);
INSERT INTO LINHA (codigo, nome, id_regiao, frota_atual, intervalo_medio_min) VALUES ('7001-10', 'Pinheiros - Terminal Largo da Batata',      5, 30,  5.0);

-- PONTO (6)
INSERT INTO PONTO (nome, endereco, latitude, longitude, id_regiao) VALUES ('Terminal Cidade Tiradentes', 'Av. Cidade Tiradentes, s/n',    -23.5870, -46.4000, 1);
INSERT INTO PONTO (nome, endereco, latitude, longitude, id_regiao) VALUES ('Estacao Guaianases',         'Praca Cel. Paiva, s/n',          -23.5420, -46.4130, 2);
INSERT INTO PONTO (nome, endereco, latitude, longitude, id_regiao) VALUES ('Estacao Itaim Paulista',     'Rua Itaim Paulista, s/n',        -23.4930, -46.3900, 3);
INSERT INTO PONTO (nome, endereco, latitude, longitude, id_regiao) VALUES ('Terminal Sao Mateus',        'Av. Mateo Bei, s/n',             -23.6110, -46.4770, 4);
INSERT INTO PONTO (nome, endereco, latitude, longitude, id_regiao) VALUES ('Terminal Largo da Batata',   'Largo da Batata, s/n',           -23.5670, -46.6930, 5);
INSERT INTO PONTO (nome, endereco, latitude, longitude, id_regiao) VALUES ('Terminal Itaquera',          'Av. Radial Leste, s/n',          -23.5400, -46.4710, 2);

-- LINHA_PONTO (8)
INSERT INTO LINHA_PONTO (id_linha, id_ponto, ordem) VALUES (1, 1, 1);
INSERT INTO LINHA_PONTO (id_linha, id_ponto, ordem) VALUES (1, 6, 2);
INSERT INTO LINHA_PONTO (id_linha, id_ponto, ordem) VALUES (2, 2, 1);
INSERT INTO LINHA_PONTO (id_linha, id_ponto, ordem) VALUES (2, 6, 2);
INSERT INTO LINHA_PONTO (id_linha, id_ponto, ordem) VALUES (3, 3, 1);
INSERT INTO LINHA_PONTO (id_linha, id_ponto, ordem) VALUES (3, 6, 2);
INSERT INTO LINHA_PONTO (id_linha, id_ponto, ordem) VALUES (4, 4, 1);
INSERT INTO LINHA_PONTO (id_linha, id_ponto, ordem) VALUES (5, 5, 1);

-- MEDICAO (8)
INSERT INTO MEDICAO (id_linha, id_ponto, dt_medicao, faixa_horario, tempo_viagem_min, lotacao_pct, intervalo_min) VALUES (1, 1, TO_DATE('2026-11-03 06:30','YYYY-MM-DD HH24:MI'), 'PICO_AM',   118, 98, 16.0);
INSERT INTO MEDICAO (id_linha, id_ponto, dt_medicao, faixa_horario, tempo_viagem_min, lotacao_pct, intervalo_min) VALUES (1, 1, TO_DATE('2026-11-03 18:15','YYYY-MM-DD HH24:MI'), 'PICO_PM',   125, 100, 18.0);
INSERT INTO MEDICAO (id_linha, id_ponto, dt_medicao, faixa_horario, tempo_viagem_min, lotacao_pct, intervalo_min) VALUES (2, 2, TO_DATE('2026-11-03 06:45','YYYY-MM-DD HH24:MI'), 'PICO_AM',    95, 92, 11.0);
INSERT INTO MEDICAO (id_linha, id_ponto, dt_medicao, faixa_horario, tempo_viagem_min, lotacao_pct, intervalo_min) VALUES (2, 2, TO_DATE('2026-11-03 14:00','YYYY-MM-DD HH24:MI'), 'ENTREPICO',  62, 55, 10.0);
INSERT INTO MEDICAO (id_linha, id_ponto, dt_medicao, faixa_horario, tempo_viagem_min, lotacao_pct, intervalo_min) VALUES (3, 3, TO_DATE('2026-11-04 06:20','YYYY-MM-DD HH24:MI'), 'PICO_AM',   104, 95, 13.0);
INSERT INTO MEDICAO (id_linha, id_ponto, dt_medicao, faixa_horario, tempo_viagem_min, lotacao_pct, intervalo_min) VALUES (4, 4, TO_DATE('2026-11-04 07:00','YYYY-MM-DD HH24:MI'), 'PICO_AM',    70, 80,  8.0);
INSERT INTO MEDICAO (id_linha, id_ponto, dt_medicao, faixa_horario, tempo_viagem_min, lotacao_pct, intervalo_min) VALUES (5, 5, TO_DATE('2026-11-04 07:30','YYYY-MM-DD HH24:MI'), 'PICO_AM',    35, 60,  5.0);
INSERT INTO MEDICAO (id_linha, id_ponto, dt_medicao, faixa_horario, tempo_viagem_min, lotacao_pct, intervalo_min) VALUES (5, 5, TO_DATE('2026-11-04 22:00','YYYY-MM-DD HH24:MI'), 'NOITE',      28, 25,  9.0);

-- RELATO (5) - dados simulados, sem informacao pessoal
INSERT INTO RELATO (id_regiao, id_linha, dt_relato, categoria, descricao, tempo_perdido_min) VALUES (1, 1, TO_DATE('2026-11-05','YYYY-MM-DD'), 'LOTACAO',    'Onibus passa lotado e nao para no ponto no horario de pico.', 40);
INSERT INTO RELATO (id_regiao, id_linha, dt_relato, categoria, descricao, tempo_perdido_min) VALUES (1, 1, TO_DATE('2026-11-06','YYYY-MM-DD'), 'FREQUENCIA', 'Espera de mais de 25 minutos no ponto pela manha.',          25);
INSERT INTO RELATO (id_regiao, id_linha, dt_relato, categoria, descricao, tempo_perdido_min) VALUES (2, 2, TO_DATE('2026-11-06','YYYY-MM-DD'), 'ATRASO',     'Atraso constante na saida do terminal.',                     20);
INSERT INTO RELATO (id_regiao, id_linha, dt_relato, categoria, descricao, tempo_perdido_min) VALUES (3, 3, TO_DATE('2026-11-07','YYYY-MM-DD'), 'LOTACAO',    'Baldeacao em Itaquera com fila grande para embarcar.',       35);
INSERT INTO RELATO (id_regiao, id_linha, dt_relato, categoria, descricao, tempo_perdido_min) VALUES (3, NULL, TO_DATE('2026-11-08','YYYY-MM-DD'), 'SEGURANCA', 'Ponto com pouca iluminacao a noite.',                       NULL);

-- ALERTA (5)
INSERT INTO ALERTA (id_linha, id_medicao, dt_alerta, nivel, mensagem, status) VALUES (1, 1, TO_DATE('2026-11-03','YYYY-MM-DD'), 'CRITICO', 'Tempo de viagem acima de 90 min e lotacao proxima de 100% no pico da manha.', 'ABERTO');
INSERT INTO ALERTA (id_linha, id_medicao, dt_alerta, nivel, mensagem, status) VALUES (1, 2, TO_DATE('2026-11-03','YYYY-MM-DD'), 'CRITICO', 'Tempo de viagem acima de 120 min no pico da tarde.',                         'EM_ANALISE');
INSERT INTO ALERTA (id_linha, id_medicao, dt_alerta, nivel, mensagem, status) VALUES (2, 3, TO_DATE('2026-11-03','YYYY-MM-DD'), 'ALTO',    'Viagem acima de 90 min no pico da manha.',                                  'ABERTO');
INSERT INTO ALERTA (id_linha, id_medicao, dt_alerta, nivel, mensagem, status) VALUES (3, 5, TO_DATE('2026-11-04','YYYY-MM-DD'), 'ALTO',    'Lotacao de 95% e intervalo de 13 min no pico da manha.',                    'ABERTO');
INSERT INTO ALERTA (id_linha, id_medicao, dt_alerta, nivel, mensagem, status) VALUES (4, NULL, TO_DATE('2026-11-05','YYYY-MM-DD'), 'BAIXO',  'Monitoramento preventivo: lotacao em 80% no pico.',                         'RESOLVIDO');

COMMIT;
