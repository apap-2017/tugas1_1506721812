package com.example.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PendudukModel
{
	
	private String id;
    private String nik;

    @NotNull
    @Size(min=1, max=128)
    private String nama;

    @NotNull
    @Size(min=1, max=128)
    private String tempat_lahir;

    @NotNull
    private String tanggal_lahir;

    @NotNull
    @Size(min=1, max=1)
    private String jenis_kelamin;

    @NotNull
    @Size(min=1, max=1)
    private String is_wni;

    @NotNull
    @Size(min=1, max=20)
    private String id_keluarga;

    @NotNull
    @Size(min=1, max=64)
    private String agama;

    @NotNull
    @Size(min=1, max=64)
    private String pekerjaan;

    @NotNull
    @Size(min=1, max=64)
    private String status_perkawinan;

    @NotNull
    @Size(min=1, max=64)
    private String status_dalam_keluarga;

    @NotNull
    @Size(min=1, max=32)
    private String golongan_darah;

    @NotNull
    @Size(min=1, max=1)
    private String is_wafat;
}


