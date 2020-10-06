	// Deze methode stelt de verschillende instellingen in voor de Security
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().httpBasic().and().formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/")
				.failureUrl("/login?error").permitAll().and()// Formatting
				.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))// Formatting
				.logoutSuccessUrl("/login?logout").permitAll().deleteCookies("JSESSIONID").and() // Formatting
				.authorizeRequests() // Formatting
				.antMatchers("/ecm/**").hasAnyAuthority("ADMIN", "ECM") // Formatting
				.antMatchers("/css/**").permitAll() // Formatting
				.antMatchers("/IRIS/**").hasAnyAuthority("ADMIN", "IRIS").antMatchers("/Graph/**")
				.hasAnyAuthority("ADMIN", "GRAPH") // Formatting
				.anyRequest().authenticated() // Formatting
				.and().exceptionHandling().accessDeniedPage("/error") // Formatting
		; // Formatting

	}