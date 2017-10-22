package com.example.service;


import java.util.List;

import com.example.model.KeluargaModel;

public interface KeluargaService
{
	List<KeluargaModel> selectAllKeluarga();
    List<KeluargaModel> selectSimilarNKK(String nkk);
    KeluargaModel selectKeluarga(String nkk);
    KeluargaModel selectKeluargaByID(String id);
    void addKeluarga(KeluargaModel keluarga);
    void updateKeluarga(KeluargaModel keluarga);
}
