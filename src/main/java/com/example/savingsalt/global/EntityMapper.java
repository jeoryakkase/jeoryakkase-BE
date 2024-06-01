package com.example.savingsalt.global;

import java.util.List;

public interface EntityMapper<E, D> {

    E toEntity(D dto);

    List<E> toEntity(List<D> dtos);

    D toDto(E entity);

    List<D> toDto(List<E> entities);
}
