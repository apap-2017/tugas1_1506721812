package com.example.service;

 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.example.dao.KeluargaMapper;

import com.example.model.KeluargaModel;





@Service
public class KeluargaServiceDatabase implements KeluargaService
{
    @Autowired
    private KeluargaMapper keluargaMapper;

    @Override
    public List<KeluargaModel> selectAllKeluarga ()
    {
        return keluargaMapper.selectAllKeluarga();
    }

    @Override
    public KeluargaModel selectKeluargaByID(String id){
        return keluargaMapper.selectKeluargaByID(id);
    }

    @Override
    public KeluargaModel selectKeluarga(String nkk){
        return keluargaMapper.selectKeluarga(nkk);
    }

    @Override
    public List<KeluargaModel> selectSimilarNKK(String nkk) {
        return keluargaMapper.selectSimilarNKK(nkk);
    }

    @Override
    public void addKeluarga(KeluargaModel keluarga){
        keluargaMapper.addKeluarga(keluarga);
    }

    @Override
    public void updateKeluarga(KeluargaModel keluarga){
        keluargaMapper.updateKeluarga(keluarga);
    }
}
 
