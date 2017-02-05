package com.c.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CsrfHeaderFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(request) {
			@Override
			public String getHeader(String name) {
				if (name.equals("X-XSRF-TOKEN")) {
					Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
					if (cookie != null) {
						return cookie.getValue();
					} else {
						return super.getHeader(name);
					}
				} else {
					return super.getHeader(name);
				}
			}
		};

		filterChain.doFilter(wrapper, response);
	}
}
