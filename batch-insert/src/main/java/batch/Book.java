package batch;

import org.springframework.data.domain.Persistable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
class Book implements Persistable<Long> {

    @Id
    private Long id;
    private String title;

    public Book(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    Book() {
    }

    public String getTitle() {
        return title;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return true;
    }
}
