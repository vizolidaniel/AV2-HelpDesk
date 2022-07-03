package br.com.daniel.config;

public interface Configs {
    int SERVER_DEFAULT_PORT = 8090;
    String DATABASE_CONSOLE_PORT = "8081";
    String USERNAME = "sa";
    String PASSWORD = "";
    String DATABASE = "helpdesk";
    String CONNECTION_STRING = String.format("jdbc:h2:mem:%s", DATABASE);
}
