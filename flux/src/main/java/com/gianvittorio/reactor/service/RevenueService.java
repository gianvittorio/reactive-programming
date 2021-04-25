package com.gianvittorio.reactor.service;

import com.gianvittorio.reactor.domain.Revenue;

import static com.gianvittorio.reactor.util.CommonUtil.delay;

public class RevenueService {

    public Revenue getRevenue(long movieId) {
        delay(1000);

        return Revenue.builder()
                .movieInfoId(movieId)
                .budget(1_000_1000)
                .boxOffice(5_000_1000)
                .build();
    }
}
