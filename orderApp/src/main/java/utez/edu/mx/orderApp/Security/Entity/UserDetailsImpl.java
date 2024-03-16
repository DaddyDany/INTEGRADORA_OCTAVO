package utez.edu.mx.orderApp.Security.Entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import utez.edu.mx.orderApp.Models.Accounts.Administrator;
import utez.edu.mx.orderApp.Models.Accounts.CommonUser;
import utez.edu.mx.orderApp.Models.Accounts.Worker;

import java.util.Collection;
import java.util.Collections;

public class UserDetailsImpl implements UserDetails {
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean enabled = true;

    private UserDetailsImpl(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl fromCommonUser(CommonUser user) {
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getRoleName());
        return new UserDetailsImpl(user.getUserName(), user.getUserPassword(), Collections.singletonList(authority));
    }

    public static UserDetailsImpl fromWorker(Worker user) {
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getRoleName());
        return new UserDetailsImpl(user.getWorkerName(), user.getWorkerPassword(), Collections.singletonList(authority));
    }

    public static UserDetailsImpl fromAdministrator(Administrator user) {
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getRoleName());
        return new UserDetailsImpl(user.getAdminName(), user.getAdminPassword(), Collections.singletonList(authority));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        return enabled;
    }
}
