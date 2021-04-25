package com.gianvittorio.reactor.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Revenue {

    private Long movieInfoId;

    private int budget;

    private int boxOffice;
}
