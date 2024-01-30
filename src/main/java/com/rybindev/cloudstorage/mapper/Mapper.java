package com.rybindev.cloudstorage.mapper;

public interface Mapper<F, T> {
    T map(F object);
}
