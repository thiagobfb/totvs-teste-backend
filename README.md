# TOTVS TESTE BACKEND

### Documento de Referência

For further reference, please consider the following sections:

* [Documentação oficial do Apache Maven](https://maven.apache.org/guides/index.html)
* [Guia de referência do Spring Boot Maven Plugin](https://docs.spring.io/spring-boot/docs/3.3.0/maven-plugin/reference/html/)
* [Criar uma imagem OCI](https://docs.spring.io/spring-boot/docs/3.3.0/maven-plugin/reference/html/#build-image)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.3.0/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)
* [Suporte ao Docker Compose](https://docs.spring.io/spring-boot/docs/3.3.0/reference/htmlsingle/index.html#features.docker-compose)
* [Migração com Flyway](https://docs.spring.io/spring-boot/docs/3.3.0/reference/htmlsingle/index.html#howto.data-initialization.migration-tool.flyway)

### Guias

Os guias a seguir ilustram como usar alguns recursos concretamente:

* [Acessando dados com JPA](https://spring.io/guides/gs/accessing-data-jpa/)

### Suporte ao Docker Compose

Este projeto contém um arquivo Docker Compose chamado compose.yaml.
Neste arquivo, os seguintes serviços foram definidos:

* postgres: [`postgres:latest`](https://hub.docker.com/_/postgres)

Por favor, revise as tags das imagens utilizadas e configure-as para serem as mesmas que você está usando em produção.

