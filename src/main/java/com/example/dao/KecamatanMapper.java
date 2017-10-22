package com.example.dao;

import java.util.List;


import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Select;


import com.example.model.KecamatanModel;

@Mapper
public interface KecamatanMapper
{

    @Select("select * from kecamatan where id = #{id}")
    KecamatanModel selectKecamatan(String id);

    @Select("select * from kecamatan where id_kota = #{kota}")
    List<KecamatanModel> selectAllKecamatan(String kota);

    @Select("select * from kecamatan")
    List<KecamatanModel> selectAllKecamatans();
 
}
