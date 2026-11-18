-- =====================================================================
-- Conecta Periferia (CitizenTech) - 03_dql.sql : consultas (SQL Developer: selecione uma consulta + Ctrl+Enter, ou F5 para todas)
-- =====================================================================

-- ---------------------------------------------------------------------
-- Consulta 1 (INNER JOIN, 2 tabelas)
-- Lista cada linha com o nome e a renda media da regiao que atende.
-- ---------------------------------------------------------------------
SELECT l.codigo,
       l.nome        AS linha,
       r.nome        AS regiao,
       r.renda_media,
       l.intervalo_medio_min
FROM   LINHA l
INNER JOIN REGIAO r ON r.id_regiao = l.id_regiao;

-- ---------------------------------------------------------------------
-- Consulta 2 (INNER JOIN + GROUP BY, 2 tabelas)
-- Ranking de linhas criticas (tela 3 do prototipo): tempo, lotacao e
-- intervalo medios por linha, so nos horarios de pico.
-- ---------------------------------------------------------------------
SELECT l.codigo,
       l.nome                         AS linha,
       COUNT(m.id_medicao)            AS qtd_medicoes,
       ROUND(AVG(m.tempo_viagem_min)) AS tempo_medio_min,
       ROUND(AVG(m.lotacao_pct))      AS lotacao_media_pct,
       ROUND(AVG(m.intervalo_min), 1) AS intervalo_medio_min
FROM   LINHA l
INNER JOIN MEDICAO m ON m.id_linha = l.id_linha
WHERE  m.faixa_horario IN ('PICO_AM','PICO_PM')
GROUP BY l.codigo, l.nome
ORDER BY tempo_medio_min DESC;

-- ---------------------------------------------------------------------
-- Consulta 3 (LEFT OUTER JOIN + WHERE + ORDER BY, 2 tabelas)
-- Regioes da Zona Leste com seus relatos; regioes sem relato aparecem com NULL.
-- ---------------------------------------------------------------------
SELECT r.nome            AS regiao,
       r.renda_media,
       rel.categoria,
       rel.tempo_perdido_min,
       rel.dt_relato
FROM   REGIAO r
LEFT OUTER JOIN RELATO rel ON rel.id_regiao = r.id_regiao
WHERE  r.zona = 'Leste'
ORDER BY r.nome, rel.dt_relato;
