# CadeMeuBus - API REST

## Ficha da CitizenTech

| Campo | Conteudo |
|---|---|
| Nome da CitizenTech | CadeMeuBus |
| Cidade e bairro | Sao Paulo, extremo da Zona Leste (ex.: Cidade Tiradentes) |
| Cidadao beneficiado | Trabalhador(a) da periferia que gasta mais de 2h por dia no onibus |
| Problema | Moradores de regioes vulneraveis perdem horas em linhas lotadas e com pouca frequencia, sem criterio claro de onde reforcar a frota |
| Como funciona | Identifica regioes, linhas e horarios com pior deslocamento e recomenda a redistribuicao da frota |
| O que cada parte faz | Esta API (Java) expoe regioes, linhas, pontos, medicoes e relatos do banco Oracle |
| Link do repositorio | https://github.com/GS-2026-1espg/cg-java-2026 |
| Link do deploy (Render) | https://cg-java-2026.onrender.com |
| Indicador de impacto | Minutos economizados por trabalhador por dia |
| ODS conectados | 11, 10, 9 e 13 |

## Integrantes

Azor Tartuce (RM 563995), Eduardo Tolentino (RM 562169), Enzo Hort Ramos (RM 561872), Mateus Amaral (RM 562334).

## Como executar (rascunho)

Requisitos: JDK 21, Maven 3.9+ e acesso ao Oracle da FIAP.

Variaveis de ambiente:

| Variavel | Descricao |
|---|---|
| `DB_URL` | URL JDBC, ex.: `jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL` |
| `DB_USER` | Usuario do banco |
| `DB_PASSWORD` | Senha do banco |

```
mvn spring-boot:run
```

Teste: `GET http://localhost:8080/health`
