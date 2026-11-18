# Comandos principais - CadeMeuBus (citizentech-api)

Projeto Maven (existe um `pom.xml`) com Spring Boot e Java 21.

## Variaveis de ambiente (antes de rodar)

```bash
export DB_URL="jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL"
export DB_USER="seu_usuario"
export DB_PASSWORD="sua_senha"
```

## Maven

```bash
mvn clean install          # compila e gera o pacote
mvn test                   # roda os testes
mvn spring-boot:run        # roda a API (http://localhost:8080)
```

Rodar o jar gerado:

```bash
java -jar target/citizentech-api-0.0.1-SNAPSHOT.jar
```

## Docker

```bash
docker build -t citizentech-api .
docker run -p 8080:8080 -e DB_URL=... -e DB_USER=... -e DB_PASSWORD=... citizentech-api
```

## Teste rapido

```bash
curl http://localhost:8080/health
```
