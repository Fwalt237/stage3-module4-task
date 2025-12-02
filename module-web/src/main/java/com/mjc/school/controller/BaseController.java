package com.mjc.school.controller;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

public interface BaseController<T,R,K>{

    ResponseEntity<EntityModel<R>> getById(K id);

    ResponseEntity<EntityModel<R>> create(T createRequest);

    ResponseEntity<EntityModel<R>> update(K id, T updateRequest);

    ResponseEntity<EntityModel<R>> patch(K id, T patchRequest);

    ResponseEntity<Void> deleteById(K id);

}
