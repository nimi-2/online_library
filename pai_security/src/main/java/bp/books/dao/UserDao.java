package bp.books.dao;

import bp.books.entity.User;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface UserDao extends CrudRepository<User, Integer> {
    public Optional<User> findByLogin(String login);
}