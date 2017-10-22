package com.example.service;


import java.util.List;

import com.example.model.KecamatanModel;


public interface KecamatanService
{
	KecamatanModel selectKecamatan(String id);
    List<KecamatanModel> selectAllKecamatan(String kota);
    List<KecamatanModel> selectAllKecamatan();
}
