package com.mjc.school.service;


public interface BaseService<T,R,K>{

    R getById(K id);

    R create(T createRequest);

    R update(K id, T updateRequest);

    R patch(K id, T patchRequest);

    void deleteById(K id);


}
