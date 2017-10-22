package com.example.dao;
import java.util.List;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


import com.example.model.PendudukModel;

@Mapper
public interface PendudukMapper
{
	@Select("select * from penduduk")
    List<PendudukModel> selectAllPenduduk();

    @Select("select * from penduduk where nik = #{nik}")
    PendudukModel selectPenduduk(@Param("nik") String nik);

    @Select("select * from penduduk where nik LIKE #{nik}")
    List<PendudukModel> selectSimilarNIK(@Param("nik") String nik);

    @Select("select * from penduduk where id_keluarga = #{id}")
    List<PendudukModel> selectAnggotaKeluarga(@Param("id") String id);

    @Insert("insert into penduduk ( nik, nama, tempat_lahir, tanggal_lahir, jenis_kelamin, is_wni, id_keluarga, agama, pekerjaan, status_perkawinan, status_dalam_keluarga, golongan_darah, is_wafat) values ( #{nik}, #{nama}, #{tempat_lahir}, #{tanggal_lahir}, #{jenis_kelamin}, #{is_wni}, #{id_keluarga}, #{agama}, #{pekerjaan}, #{status_perkawinan}, #{status_dalam_keluarga}, #{golongan_darah}, #{is_wafat})")
    void addPenduduk(PendudukModel penduduk);

    @Update("update penduduk set is_wafat = 1 where nik = #{nik}")
    void nonaktifkanPenduduk(String nik);

    @Update("update penduduk set nik = #{nik}, nama = #{nama}, tempat_lahir = #{tempat_lahir}, tanggal_lahir = #{tanggal_lahir}, jenis_kelamin = #{jenis_kelamin}, is_wni = #{is_wni}, id_keluarga = #{id_keluarga}, agama = #{agama}, pekerjaan = #{pekerjaan}, status_perkawinan = #{status_perkawinan}, status_dalam_keluarga = #{status_dalam_keluarga}, golongan_darah = #{golongan_darah}, is_wafat = #{is_wafat} where id = #{id}")
    void updatePenduduk(PendudukModel penduduk);

    @Select("select * from penduduk p, keluarga k where p.id_keluarga = k.id and id_kelurahan = #{id_kelurahan}")
    List<PendudukModel> selectPendudukByKelurahan(String id_kelurahan);
	    
	    
}
