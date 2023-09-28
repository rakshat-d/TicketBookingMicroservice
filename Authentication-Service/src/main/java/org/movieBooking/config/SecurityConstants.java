package org.movieBooking.config;

import org.springframework.beans.factory.annotation.Value;

public class SecurityConstants {
	@Value("${auth.security.key}")
	public static final String KEY = "75c9b6ef236706f8c11d4f1c770fd02264e3aae7298d3ef5be5cf1b778e563b0";
	@Value("${auth.security.issuer}")
	public static final String ISSUER = "pixal";
	@Value("${auth.security.prefix}")
	public static final String PREFIX = "Bearer ";
}
