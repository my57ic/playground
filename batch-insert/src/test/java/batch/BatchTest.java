package batch;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.StopWatch;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.stream.LongStream;

@SpringBootTest
@DirtiesContext
class BatchTest {

    private final List<Book> books = generateList();
    private final Logger logger = LoggerFactory.getLogger(BatchTest.class);

    @Autowired
    JdbcBatch jdbcBatch;

    @Autowired
    JpaBatch jpaBatch;

    @Autowired
    SpringDataBatch springDataBatch;


    @Test
    void testJdbc() {
        measure(() -> jdbcBatch.insertInBatch(books));
    }

    @Test
    void testSpringData() {
        measure(() -> springDataBatch.insertInBatch(books));
    }

    @Test
    void testJpa() {
        measure(() -> jpaBatch.insertInBatch(books));
    }

    private void measure(Runnable action) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        action.run();
        stopWatch.stop();
        logger.info("Batch insert took [{} ms]", stopWatch.getLastTaskTimeMillis());
    }

    private List<Book> generateList() {
        int initialCapacity = 100;
        List<Book> list = new ArrayList<>(initialCapacity);
        byte[] random = new byte[8];
        Random random1 = new Random();
        LongStream.range(0, initialCapacity).forEach(x -> {
            ByteBuffer.wrap(random).putDouble(random1.nextDouble(0, 99999));
            list.add(new Book(x, Base64.getEncoder().encodeToString(random)));
        });
        return list;
    }
}
