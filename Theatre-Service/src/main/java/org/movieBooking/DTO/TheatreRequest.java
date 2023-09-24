package org.movieBooking.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TheatreRequest {
    @Size(min = 3, max = 20, message = "City name should be between 3 and 20")
    private String name;

    @Min(1)
    private Long cityId;

    List<Long> movies;
}
