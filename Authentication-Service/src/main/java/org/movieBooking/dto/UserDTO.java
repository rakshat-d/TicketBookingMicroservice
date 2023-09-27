package org.movieBooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.movieBooking.utils.ValidPassword;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private int id;
    private String username;

    @ValidPassword
    private String password;

    private boolean isAdmin;

    private String isOauthAccount;

    private String deletedFlag;

    private boolean isAccountExpired;

    private boolean isAccountLocked;

    private boolean isReadOnlyUser;

//    @Override
//    public String toString() {
//        if (isAdmin) {
//            return "User [id=" + id + ", uname=" + username + ", Admin User]";
//        } else {
//            return "User [id=" + id + ", uname=" + username + "]";
//        }
//    }
}
