package fr.kainovaii.obsidian.core.security.user;

public interface UserDetailsService
{
    UserDetails loadByUsername(String username);

    UserDetails loadById(Object id);
}
