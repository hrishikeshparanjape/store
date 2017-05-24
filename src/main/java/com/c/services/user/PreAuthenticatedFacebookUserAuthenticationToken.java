package com.c.services.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.c.domain.user.Customer;
import com.c.domain.user.CustomerRole;

public class PreAuthenticatedFacebookUserAuthenticationToken implements Authentication {
	
	private static final long serialVersionUID = 6112068730815382097L;

	private Customer customer;
	
	private String facebookAccessToken;
	
	public PreAuthenticatedFacebookUserAuthenticationToken(Customer customer, String token) {
		this.customer = customer;
		this.facebookAccessToken = token;
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
		Set<CustomerRole> roles = customer.getCustomerRoles();
		for(CustomerRole role : roles) {
			grantedAuthorities.add(new GrantedAuthority() {
				
				private static final long serialVersionUID = 1L;

				@Override
				public String getAuthority() {
					return role.toString();
				}
			});
		}
		return grantedAuthorities;
	}

	@Override
	public Object getCredentials() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getDetails() {
		return this.facebookAccessToken;
	}

	@Override
	public Object getPrincipal() {
		return customer.getEmail();
	}

	@Override
	public boolean isAuthenticated() {
		return true;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		// TODO Auto-generated method stub		
	}

}
