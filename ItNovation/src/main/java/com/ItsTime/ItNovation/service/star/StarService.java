package com.ItsTime.ItNovation.service.star;

import com.ItsTime.ItNovation.domain.star.StarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StarService {
    private final StarRepository starRepository;
}
