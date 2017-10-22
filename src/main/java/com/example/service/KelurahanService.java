package com.example.service;


import java.util.List;

import com.example.model.KelurahanModel;

public interface KelurahanService
{
	KelurahanModel selectKelurahan(String id);
    List<KelurahanModel> selectAllKelurahan(String kecamatan);
    List<KelurahanModel> selectAllKelurahan();
}