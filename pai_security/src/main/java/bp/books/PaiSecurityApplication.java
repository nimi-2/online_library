package bp.books;

import bp.books.dao.UserDao;
import bp.books.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class PaiSecurityApplication {
	@Autowired
	private UserDao dao;
	@Autowired
	private PasswordEncoder passwordEncoder;


	public static void main(String[] args) {
		SpringApplication.run(PaiSecurityApplication.class, args);
	}

	@PostConstruct
	public void init() {
		dao.save(new User("Piotr", "Piotrowski", "admin",
				passwordEncoder.encode("admin")));
		dao.save(new User("Ania", "Annowska", "ania",
				passwordEncoder.encode("ania")));
	}

}
