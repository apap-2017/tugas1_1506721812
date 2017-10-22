package com.example.model;



import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor 
public class KeluargaModel {
	
	private String id;
    private String nomor_kk;

    @NotNull
    @Size(min=1, max=256)
    private String alamat;

    @NotNull
    private String rt;

    @NotNull
    private String rw;

    @NotNull
    @Size(min=1, max=20)
    private String id_kelurahan;

    private String is_tidak_berlaku;


}
