package com.faiz.blog.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	private CustomUserDetailService customUserDetailService;
	
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String requestTokenHeader = request.getHeader("Authorization");
		System.out.println(requestTokenHeader);
		
		String username=null;
		String jwtToken=null;
		
		if(requestTokenHeader!=null && requestTokenHeader.startsWith("Bearer"))
		{

			jwtToken=requestTokenHeader.substring(7);
			System.out.println(jwtToken);
			try
			{
				username = this.jwtTokenHelper.getUsernameFromToken(jwtToken);
				System.out.println(username);
			} 
			catch (ExpiredJwtException e) 
			{
				e.printStackTrace();
				System.out.println("jwt token has expired");
			}catch (IllegalArgumentException e)
			{
				// TODO: handle exception
				e.printStackTrace();
				System.out.println("unable to get jwt token");
			}catch(MalformedJwtException e)
			{
				System.out.println("invalid");
			}
		}
		else 
		{
			System.out.println("token empty, not started with bearer string");
		}
		
		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
		{
			final UserDetails userDetails = this.customUserDetailService.loadUserByUsername(username);
			if(this.jwtTokenHelper.validateToken(jwtToken, userDetails))
			{
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
			else {
				System.out.println("not valid");
			}
		}
		else 
		{
			System.out.println("username null");
		}
		filterChain.doFilter(request, response);
	}

}

