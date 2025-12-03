package com.mjc.school.service;

import com.mjc.school.service.dto.NewsDtoRequest;
import com.mjc.school.service.dto.NewsDtoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;


public interface NewsService extends BaseService<NewsDtoRequest, NewsDtoResponse, Long>{

    Page<NewsDtoResponse> getAll(List<String> tagNames,
                                 List<Long> tagIds,
                                 String authorName,
                                 String title,
                                 String content,
                                 Pageable pageable);

    List<NewsDtoResponse> getByParams(List<String> tagNames,
                                      List<Long> tagIds,
                                      String authorName,
                                      String title,
                                      String content,
                                      Sort sort);
}
