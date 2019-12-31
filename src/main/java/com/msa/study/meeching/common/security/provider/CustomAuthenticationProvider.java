package com.msa.study.meeching.common.security.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
public class CustomAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private UserDetailsService userDetailsService;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

        // TODO Auto-generated method stub

    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {

        log.debug("◑ [retrieveUser] username => {}", username);
        log.debug("◑ [retrieveUser] authentication => {}", authentication);

        try {
            UserDetails loadedUser = this.getUserDetailsService().loadUserByUsername(username);
            if (loadedUser == null) {
                throw new InternalAuthenticationServiceException(
                        "UserDetailsService returned null, which is an interface contract violation");
            }
            return loadedUser;
        }
        catch (UsernameNotFoundException ex) {
//			mitigateAgainstTimingAttack(authentication);
            throw ex;
        }
        catch (InternalAuthenticationServiceException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }

    }

//	@Override
//	protected Authentication createSuccessAuthentication(Object principal,
//			Authentication authentication, UserDetails user) {
//		boolean upgradeEncoding = this.userDetailsPasswordService != null
//				&& this.passwordEncoder.upgradeEncoding(user.getPassword());
//		if (upgradeEncoding) {
//			String presentedPassword = authentication.getCredentials().toString();
//			String newPassword = this.passwordEncoder.encode(presentedPassword);
//			user = this.userDetailsPasswordService.updatePassword(user, newPassword);
//		}
//		return super.createSuccessAuthentication(principal, authentication, user);
//	}

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    protected UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }
}
