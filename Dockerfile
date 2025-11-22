# ===============================
# ESTÁGIO 1: BUILD (Compilação)
# ===============================
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o arquivo de dependências (pom.xml) e baixa as dependências
# (Isso é feito antes de copiar o código para aproveitar o cache do Docker)
COPY pom.xml .
# Baixa as dependências (modo offline para agilizar builds futuros se usar cache)
RUN mvn dependency:go-offline

# Copia o código fonte do projeto
COPY src ./src

# Compila o projeto e gera o .jar (Pula os testes para agilizar o build do container)
RUN mvn clean package -DskipTests

# ===============================
# ESTÁGIO 2: RUNTIME (Execução)
# ===============================
FROM eclipse-temurin:17-jre-alpine

# Define o diretório de trabalho
WORKDIR /app

# Copia apenas o arquivo .jar gerado no estágio anterior
# O comando cp pega do estágio 'build' na pasta target
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta 8080 (padrão do Spring Boot)
EXPOSE 8080

# Define o comando que será executado ao iniciar o container
ENTRYPOINT ["java", "-jar", "app.jar"]