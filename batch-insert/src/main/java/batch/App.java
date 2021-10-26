package batch;

import com.zaxxer.hikari.HikariDataSource;
import net.ttddyy.dsproxy.listener.logging.DefaultQueryLogEntryCreator;
import net.ttddyy.dsproxy.listener.logging.SLF4JQueryLoggingListener;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.hibernate.engine.jdbc.internal.Formatter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@SpringBootApplication
class App {


    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    @Primary
    public DataSource dataSource(DataSourceProperties properties) {
        final HikariDataSource dataSource = properties
                .initializeDataSourceBuilder().type(HikariDataSource.class).build();
        if (properties.getName() != null) {
            dataSource.setPoolName(properties.getName());
        }
        SLF4JQueryLoggingListener loggingListener = new SLF4JQueryLoggingListener();
        PrettyQueryEntryCreator creator = new PrettyQueryEntryCreator();
        creator.setMultiline(true);
        loggingListener.setQueryLogEntryCreator(new PrettyQueryEntryCreator());
        return ProxyDataSourceBuilder
                .create(dataSource)
                .name("someName")
                .listener(loggingListener)
                .build();
    }

    private static class PrettyQueryEntryCreator extends DefaultQueryLogEntryCreator {
        private final Formatter formatter = FormatStyle.NONE.getFormatter();

        @Override
        protected String formatQuery(String query) {
            return this.formatter.format(query);
        }
    }

    @Bean
    @Lazy
    JdbcBatch jdbcBatch(DataSource dataSource) {
        return new JdbcBatch(dataSource);
    }

    @Bean
    JpaBatch jpaBatch() {
        return new JpaBatch();
    }

    @Bean
    SpringDataBatch springDataBatch(BookRepository bookRepository) {
        return new SpringDataBatch(bookRepository);
    }

}
