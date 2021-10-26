package batch;

import java.util.List;

class SpringDataBatch implements BatchInsert{

    BookRepository bookRepository;

    public SpringDataBatch(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    @Override
    public void insertInBatch(List<Book> books) {
        bookRepository.saveAll(books);
    }
}
