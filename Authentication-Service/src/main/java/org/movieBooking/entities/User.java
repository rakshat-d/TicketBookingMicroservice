package org.movieBooking.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false)
	private String password;

	private boolean isAdmin = false;

	private String isOauthAccount = "N";

	private String deletedFlag;

//	private LocalDateTime credentialsExpiryDate;

	private boolean isAccountExpired = false;

	private boolean isAccountLocked = false;

	private boolean isReadOnlyUser = false;

	@Override
	public String toString() {
		if (isAdmin) {
			return "User [id=" + id + ", uname=" + username + ", Admin User]";
		} else {
			return "User [id=" + id + ", uname=" + username + "]";
		}
	}
}
