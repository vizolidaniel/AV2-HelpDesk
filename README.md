# HelpDesk
Exemplo de solução de HelpDesk usando JavaEE com Glassfish

## Como fazer?

### Build do projeto
```bash
mvn clean package
```

### Deploy em servidor Glassfish (5.0 - JavaEE 8) local
```bash
mvn glassfish:deploy
```

### Deploy em servidor Heroku remoto
```bash
mvn heroku:deploy-war
```

Obs.: O deploy remoto só ocorre quando você tem o Heroku CLI instalado localmente e devidamente logado via `heroku login`. É possível trocar o nome da aplicação para deploy dentro de `pom.xml > build > plugins > plugin > heroku-maven-plugin > configuration > appName`, este nome é o prefixo disponível para o Heroku publico, exemplo: se appName for `teste-web-app`, o nome de dominio da aplicação será `teste-web-app.heroku.app`

## FAQ

---

#### O [UML de relacionamento de classes](https://github.com/douglasffilho/HelpDesk/blob/main/HelpDeskEntityUML.drawio) pode ser aberto com a ferramenta [draw.io](https://draw.io) ou [APP Diagrams](https://app.diagrams.net/)

---

#### O projeto tem foco em conter implementações de tudo que é aprendido em POO Avançado:

- CRUD Web
- Relacionamento entre classes em forma de Herança, Agregação e Composição
- Diagrama de Relacionamento de Entidades
- MVC com Servlet e JSP (uso do framework Spring Web)
- Usar Maven como gestor de dependencias
- Uso de parametros em requisições para selecionar endpoints da lógica
- Uso de forward e redirect
- Tratamento de uso de caracteres especiais antes de enviar dados ao banco de dados
- Uso de todos os verbos HTTP
- Uso de Scriptlets nas JSP's
- Aplicação configurada com context root em "/" (o mesmo deve funcionar com ela em ambiente produtivo)
- Usar DAO para acesso aos dados (com ou sem Hibernate)
- Persistir dados no MySQL
- Login seguro através de formulario de login e gerenciamento de usuário na base com proteção na senha
- Diferenciação de acesso por papel (role) de usuário logado
- Não permitir registros duplicados (bom gerenciamento de chaves primarias, e unicas, no banco de dados)
- Paginação de registros em páginas de listagem
- Barra de navegação com opções (e com icones)
- Usar cookies para armazenar dados analíticos de acesso ao sistema
- Armazenar token de login em cookie onde token e cookie devem ter um TTL de 30 dias
- Disponibilizar aplicação em ambiente seguro HTTPS (SSL/TLS)
- Realizar deploy da aplicação a partir do Maven (Automação do deploy com : maven clean package heroku:deploy-war)

---

#### O sistema se trata de um modelo de Help Desk onde as principais funcionalidades são sobre atendimento de chamados criados por um usuário padrão e um usuário atendente os movimenta incluindo recursos e determinando seu status.

#### O status de um chamado pode ser "aberto", "iniciado", "em atendimento", "pendente de ação do cliente", "pendente de ação do atendente", "finalizado" e "arquivado"
