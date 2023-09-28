package org.movieBooking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Theatre {
    private Long id;
    private String name;
    private Long cityId;

    private List<Long> movies;

}
