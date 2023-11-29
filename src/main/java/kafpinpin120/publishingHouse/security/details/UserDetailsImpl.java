package kafpinpin120.publishingHouse.security.details;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import kafpinpin120.publishingHouse.models.User;


import java.util.Collection;
import java.util.List;

@Getter
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    //private long id;

    //private String name, email, phone;

    //@JsonIgnore
    //private String password;

    private User user;

    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    @Override
    public String getPassword() {return user.getPassword();}

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    public static UserDetailsImpl build(User user){
        SimpleGrantedAuthority role = new SimpleGrantedAuthority(user.getRole().name());
        List<GrantedAuthority> authorities = List.of(role);

        return new UserDetailsImpl(user, authorities);
    }
}
