package bp.books.configuration;

import bp.books.dao.UserDao;
import bp.books.entity.User;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserAuthenticationDetails implements UserDetailsService {
    @Autowired
    private UserDao dao;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = dao.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Zły login lub hasło."));

        List<GrantedAuthority> grupa = new ArrayList<>();
        grupa.add(new SimpleGrantedAuthority("normalUser"));
        return new org.springframework.security.core.userdetails.User(user.getLogin(),
                user.getPassword(), true, true,
                true, true, grupa);
    }

}
