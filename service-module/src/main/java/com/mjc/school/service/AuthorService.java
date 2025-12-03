package com.mjc.school.service;

import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorService extends BaseService<AuthorDtoRequest, AuthorDtoResponse, Long>{

    Page<AuthorDtoResponse> getAll(String searchName, Pageable pageable);

    AuthorDtoResponse getByNewsId(Long newsId);
}
