package com.example.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.example.dao.KelurahanMapper;

import com.example.model.KelurahanModel;



@Service
public class KelurahanServiceDatabase implements KelurahanService
{
    @Autowired
    private KelurahanMapper kelurahanMapper;

    @Override
    public KelurahanModel selectKelurahan(String id){
        return kelurahanMapper.selectKelurahan(id);
    }

    @Override
    public List<KelurahanModel> selectAllKelurahan(String kecamatan){
        return kelurahanMapper.selectAllKelurahan(kecamatan);
    }

    @Override
    public List<KelurahanModel> selectAllKelurahan(){
        return kelurahanMapper.selectAllKelurahans();
    
    }
}
