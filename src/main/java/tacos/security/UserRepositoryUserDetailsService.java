package tacos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tacos.domain.User;
import tacos.data.UserRepository;

@Service
public class UserRepositoryUserDetailsService 
        implements UserDetailsService {

  private UserRepository userRepo;

  private static final org.slf4j.Logger log =
          org.slf4j.LoggerFactory.getLogger(UserRepositoryUserDetailsService.class);

  @Autowired
  public UserRepositoryUserDetailsService(UserRepository userRepo) {
    this.userRepo = userRepo;
  }
  
  @Override
  public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException {
    User user = userRepo.findByUsername(username);
    if (user != null) {
      log.debug("User " + username + " is not null --> " + user);
      return user;
    }
    log.debug("user " + username + " is null ");
    throw new UsernameNotFoundException(
                    "User '" + username + "' not found");
  }

}
