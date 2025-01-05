// Composite key class for UserBook
package bp.books.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Embeddable
public class UserBookId implements Serializable {
    private Integer userId;
    private Long bookId;

    public UserBookId() {}

    public UserBookId(Integer userId, Long bookId) {
        this.userId = userId;
        this.bookId = bookId;
    }

    // Getters and setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserBookId that = (UserBookId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(bookId, that.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, bookId);
    }
}
