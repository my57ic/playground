package batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

class JdbcBatch implements BatchInsert{

    private final DataSource dataSource;

    private final Logger logger = LoggerFactory.getLogger(JdbcBatch.class);

    public JdbcBatch(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertInBatch(List<Book> books) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO book (id, title) values(?,?)");
            books.forEach(value -> {
                setParamInBatch(statement, value);
            });
            statement.executeBatch();
        } catch (SQLException e) {
            logger.error("error", e);
        }
    }

    private void setParamInBatch(PreparedStatement statement, Book book) {
        try {
            statement.setLong(1, book.getId());
            statement.setString(2, book.getTitle());
            statement.addBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
