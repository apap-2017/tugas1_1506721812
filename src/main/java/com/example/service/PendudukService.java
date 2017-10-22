package com.example.service;

import java.util.List;


import com.example.model.PendudukModel;

public interface PendudukService
{
	List<PendudukModel> selectAllPenduduk();
    List<PendudukModel> selectAnggotaKeluarga(String id);
    List<PendudukModel> selectSimilarNIK(String nik);
    PendudukModel selectPenduduk(String nik);
    void addPenduduk(PendudukModel penduduk);
    void updatePenduduk(PendudukModel penduduk);
    void nonaktifkanPenduduk(String nik);
    List<PendudukModel> selectPendudukByKelurahan(String id_kelurahan);
	
	
}
