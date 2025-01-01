// BookDao.java
package bp.books.dao;

import bp.books.entity.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookDao extends CrudRepository<Book, Long> {
    // Domyślne metody CRUD są dziedziczone z CrudRepository
}