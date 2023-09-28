package org.movieBooking.config;

import org.springframework.beans.factory.annotation.Value;

public class AppConstants {

	@Value("${auth.bcrypt.encoder-strength}")
	public static int bCryptEncoderStrength = 10;

}