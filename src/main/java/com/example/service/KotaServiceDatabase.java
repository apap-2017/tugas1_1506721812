package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.KotaMapper;

import com.example.model.KotaModel;
@Service
public class KotaServiceDatabase implements KotaService
{
    @Autowired
    private KotaMapper kotaMapper;

    @Override
    public KotaModel selectKota(String id){
        return kotaMapper.selectKota(id);
    }

    @Override
    public List<KotaModel> selectAllKota(){
        return kotaMapper.selectAllKota();
    }
}