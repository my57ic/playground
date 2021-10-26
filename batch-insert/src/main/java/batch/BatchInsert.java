package batch;

import java.util.List;

interface BatchInsert {
    void insertInBatch(List<Book> values);
}
