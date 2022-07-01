package br.com.daniel.config;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.h2.tools.Console;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class JDBCConfig {
    private static final Log log = LogFactory.getLog(JDBCConfig.class);

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setName(Configs.DATABASE)
                .addScript("classpath:jdbc/schema.sql")
                .addScript("classpath:jdbc/init-users-data.sql")
                .addScript("classpath:jdbc/init-roles-data.sql")
                .addScript("classpath:jdbc/init-users-roles-data.sql")
                .build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public InitializingBean databaseConsole() throws SQLException {
        return () -> {
            Console.main("-web", "-webAllowOthers", "-webPort", Configs.DATABASE_CONSOLE_PORT);
            log.info(String.format(
                    "Console do banco de dados iniciado em http://localhost:%s para base %s (username=%s, password=%s)",
                    Configs.DATABASE_CONSOLE_PORT,
                    Configs.CONNECTION_STRING,
                    Configs.USERNAME,
                    Configs.PASSWORD
            ));
        };
    }
}
