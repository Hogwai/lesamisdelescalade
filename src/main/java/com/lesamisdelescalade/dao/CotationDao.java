package com.lesamisdelescalade.dao;

import com.lesamisdelescalade.model.Cotation;

import java.util.List;

public interface CotationDao {
    List<Cotation> search(Cotation criteria);
    List<Cotation> getAllCotationInfos();
    Cotation getById(Integer cotationId);
}
