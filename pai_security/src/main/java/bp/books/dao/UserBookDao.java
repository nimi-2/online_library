package bp.books.dao;

import bp.books.entity.UserBook;
import bp.books.entity.UserBookId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBookDao extends JpaRepository<UserBook, UserBookId> {
}