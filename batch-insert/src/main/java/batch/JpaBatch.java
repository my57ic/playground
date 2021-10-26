package batch;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

class JpaBatch {

    @Autowired
    EntityManager entityManager;


    @Transactional
    public void insertInBatch(List<Book> books) {
        books.forEach(book -> entityManager.persist(book));
    }
}
