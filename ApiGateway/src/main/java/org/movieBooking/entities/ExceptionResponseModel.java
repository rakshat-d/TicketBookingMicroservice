package org.movieBooking.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponseModel {
    private String errorCode;
    private String error;
    private String errorDetails;
    private LocalDateTime date;
}
