package com.lesamisdelescalade.dao.impl;

import com.lesamisdelescalade.dao.VoieDao;
import com.lesamisdelescalade.model.Voie;

import java.util.ArrayList;
import java.util.List;

public class VoieDaoImpl extends BaseDao<Voie> implements VoieDao {
    public VoieDaoImpl() {
        this.setmodelClass(Voie.class);
    }

    @Override
    public List<Voie> search(Voie criteria) {
        return new ArrayList<>();
    }
}
