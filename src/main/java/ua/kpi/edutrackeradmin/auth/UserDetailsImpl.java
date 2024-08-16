package ua.kpi.edutrackeradmin.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ua.kpi.edutrackerentity.entity.Manager;
import ua.kpi.edutrackerentity.entity.Professor;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private Long id;
    private String username;
    private String name;
    private String password;
    private Manager manager;

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
    public UserDetailsImpl getUserDetailsByUsers(Manager manager){
        return new UserDetailsImpl(manager.getId(), manager.getEmail(), manager.getName(), manager.getPassword(), manager);
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

}