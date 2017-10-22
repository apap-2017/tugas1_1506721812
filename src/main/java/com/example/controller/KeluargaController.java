package com.example.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.KecamatanModel;
import com.example.model.KeluargaModel;
import com.example.model.KelurahanModel;
import com.example.model.KotaModel;
import com.example.model.PendudukModel;
import com.example.service.KecamatanService;
import com.example.service.KeluargaService;
import com.example.service.KelurahanService;
import com.example.service.KotaService;
import com.example.service.PendudukService;

@Controller
public class KeluargaController
{
	@Autowired
    KeluargaService keluargaDAO;

    @Autowired
    PendudukService pendudukDAO;

    @Autowired
    KelurahanService kelurahanDAO;

    @Autowired
    KecamatanService kecamatanDAO;

    @Autowired
    KotaService kotaDAO;
    

    @GetMapping("/keluarga")
    public String cariNkk(@RequestParam(value = "nkk", required = false) String nkk, Model model){

        if(nkk == null){
            model.addAttribute("error", "Masukkan input nkk");
            return "error";
        }

        KeluargaModel keluarga = keluargaDAO.selectKeluarga(nkk);

        if(keluarga == null){
            model.addAttribute("error", "NKK tidak ditemukan");
            return "error";
        }

        List<PendudukModel> anggota = pendudukDAO.selectAnggotaKeluarga(keluarga.getId());
        KelurahanModel kelurahan = kelurahanDAO.selectKelurahan(keluarga.getId_kelurahan());
        KecamatanModel kecamatan = kecamatanDAO.selectKecamatan(kelurahan.getId_kecamatan());
        KotaModel kota = kotaDAO.selectKota(kecamatan.getId_kota());

        model.addAttribute("keluarga", keluarga);
        model.addAttribute("anggota", anggota);
        model.addAttribute("kelurahan", kelurahan);
        model.addAttribute("kecamatan", kecamatan);
        model.addAttribute("kota", kota);

        for(int i = 0 ; i < anggota.size(); i++){
            if(anggota.get(i).getIs_wni().equals("1")){
                anggota.get(i).setIs_wni("WNI");
            }
            else{
                anggota.get(i).setIs_wni("WNA");
            }

            if(anggota.get(i).getJenis_kelamin().equals("1")){
                anggota.get(i).setJenis_kelamin("Perempuan");
            }
            else{
                anggota.get(i).setJenis_kelamin("Laki-laki");
            }
        }

        return "view-keluarga";
    }
   
    /*@RequestMapping(value = "/keluarga/ubah", method = RequestMethod.GET)
    public String editPenduduk(){
    	return "edit-keluarga";
    } */
    
    @GetMapping("/keluarga/ubah/{nkk}")
    public String ubahKeluarga(@PathVariable(value = "nkk") String nkk, Model model){

        KeluargaModel keluarga = keluargaDAO.selectKeluarga(nkk);

        if(keluarga != null){
            model.addAttribute("keluarga", keluarga);
        }
        else{
            model.addAttribute("error", "NKK tidak temukan");
            return "error";
        }

        List<KelurahanModel> kelurahan = kelurahanDAO.selectAllKelurahan();
        List<KecamatanModel> kecamatan = kecamatanDAO.selectAllKecamatan();
        List<KotaModel> kota = kotaDAO.selectAllKota();

        for(int i = 0; i < kelurahan.size(); i++){
            for(int j = 0; j < kecamatan.size(); j++){
                for(int k = 0 ; k < kota.size(); k++){
                    if(kelurahan.get(i).getId_kecamatan().equals(kecamatan.get(j).getId())){
                        if(kecamatan.get(j).getId_kota().equals(kota.get(k).getId())){
                            String namaKelurahan = kota.get(k).getNama_kota()+
                                    " / " + kecamatan.get(j).getNama_kecamatan() +
                                    " / " + kelurahan.get(i).getNama_kelurahan();
                            kelurahan.get(i).setNama_kelurahan(namaKelurahan);
                        }
                    }
                }
            }
        }

        model.addAttribute("kelurahan", kelurahan);


        return "edit-keluarga-form";
    }

    @PostMapping("/keluarga/ubah/{nkk}")
    public String ubahKeluargaSubmit(@PathVariable(value="nkk") String nkkLama, @Valid @ModelAttribute KeluargaModel keluarga, BindingResult result, Model model){

        if(result.hasErrors()){
            model.addAttribute("error", result.getModel().toString());
            return "error";
        }

        KeluargaModel keluargaLama = keluargaDAO.selectKeluarga(nkkLama);
        KelurahanModel kelurahanLama = kelurahanDAO.selectKelurahan(keluargaLama.getId_kelurahan());
        KelurahanModel kelurahan = kelurahanDAO.selectKelurahan(keluarga.getId_kelurahan());

        String nkk = nkkLama;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();
        String[] date = dtf.format(localDate).split("/");
        String tanggalTerbit = date[2] + date[1] + date[0].substring(2);

        if((!kelurahan.getId_kecamatan().equals(kelurahanLama.getId_kecamatan())) || !tanggalTerbit.equals(keluargaLama.getNomor_kk().substring(6,12))){
            nkk = kelurahan.getKode_kelurahan().substring(0,6);

            nkk += tanggalTerbit;

            List<KeluargaModel> similar = keluargaDAO.selectSimilarNKK(nkk + "%");
            String nomor = "0001";
            if(similar.size() > 0){
                int nomorSimilar = Integer.parseInt(similar.get(similar.size()-1).getNomor_kk().substring(12)) + 1;
                nomor = nomorSimilar + "";
            }

            int counter = 4 - nomor.length();
            for(int i = 0; i < counter; i++){
                nomor = "0" + nomor;
            }

            nkk += nomor;
            keluarga.setNomor_kk(nkk);

            List<PendudukModel> anggotaPenduduk = pendudukDAO.selectAnggotaKeluarga(keluargaLama.getId());

            for(int i = 0; i < anggotaPenduduk.size(); i++){
                String temp = nkk.substring(0,6) + anggotaPenduduk.get(i).getNik().substring(6,12);

                List<PendudukModel> tempSimilar = pendudukDAO.selectSimilarNIK(temp + "%");
                String tempNomor = "0001";
                if(tempSimilar.size() > 0){
                    int nomorSimilar = Integer.parseInt(tempSimilar.get(tempSimilar.size()-1).getNik().substring(12)) + 1;
                    tempNomor = nomorSimilar + "";
                }

                int tempCounter = 4 - tempNomor.length();
                for(int j = 0; j < tempCounter; j++){
                    tempNomor = "0" + tempNomor;
                }

                temp = temp + tempNomor;

                anggotaPenduduk.get(i).setNik(temp);

                pendudukDAO.updatePenduduk(anggotaPenduduk.get(i));
            }

        } else {
            keluarga.setNomor_kk(keluargaLama.getNomor_kk());
        }

        keluarga.setId(keluargaLama.getId());

        keluarga.setIs_tidak_berlaku("1");
        List<PendudukModel> anggotaKeluarga = pendudukDAO.selectAnggotaKeluarga(keluarga.getId());

        if(anggotaKeluarga.size() == 0){
            keluarga.setIs_tidak_berlaku("0");
        }else{
            for(int i = 0 ; i < anggotaKeluarga.size(); i++){
                if(anggotaKeluarga.get(i).getIs_wafat().equals("0")){
                    keluarga.setIs_tidak_berlaku("0");
                }
            }
        }

        model.addAttribute("confirmation", "Keluarga dengan NKK " + nkkLama + " berhasil diubah");

        keluargaDAO.updateKeluarga(keluarga);

        return "confirmation";
    }
    
    
 
    @GetMapping("/keluarga/tambah")
    public String tambahKeluarga(Model model){
        KeluargaModel keluarga = new KeluargaModel();
        keluarga.setId_kelurahan("");
        model.addAttribute("keluarga", keluarga);
        List<KelurahanModel> kelurahan = kelurahanDAO.selectAllKelurahan();
        List<KecamatanModel> kecamatan = kecamatanDAO.selectAllKecamatan();
        List<KotaModel> kota = kotaDAO.selectAllKota();

        for(int i = 0; i < kelurahan.size(); i++){
            for(int j = 0; j < kecamatan.size(); j++){
                for(int k = 0 ; k < kota.size(); k++){
                    if(kelurahan.get(i).getId_kecamatan().equals(kecamatan.get(j).getId())){
                        if(kecamatan.get(j).getId_kota().equals(kota.get(k).getId())){
                            String namaKelurahan = kota.get(k).getNama_kota()+
                                    " / " + kecamatan.get(j).getNama_kecamatan() +
                                    " / " + kelurahan.get(i).getNama_kelurahan();
                            kelurahan.get(i).setNama_kelurahan(namaKelurahan);
                        }
                    }
                }
            }
        }

        model.addAttribute("kelurahan", kelurahan);

        return "add-keluarga";
    }

    @PostMapping("/keluarga/tambah")
    public String tambahKeluargaSubmit(@Valid @ModelAttribute KeluargaModel keluarga, BindingResult result, Model model){

        if(result.hasErrors()){
            model.addAttribute("error", result.getModel().toString());
            return "error";
        }

        KelurahanModel kelurahan = kelurahanDAO.selectKelurahan(keluarga.getId_kelurahan());

        String nkk = kelurahan.getKode_kelurahan().substring(0,6);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();

        String[] date = dtf.format(localDate).split("/");
        nkk += date[2] + date[1] + date[0].substring(2);

        List<KeluargaModel> similar = keluargaDAO.selectSimilarNKK(nkk + "%");
        String nomor = "0001";
        if(similar.size() > 0){
            int nomorSimilar = Integer.parseInt(similar.get(similar.size()-1).getNomor_kk().substring(12)) + 1;
            nomor = nomorSimilar + "";
        }

        int counter = 4 - nomor.length();
        for(int i = 0; i < counter; i++){
            nomor = "0" + nomor;
        }

        nkk += nomor;
        keluarga.setNomor_kk(nkk);
        keluarga.setIs_tidak_berlaku("0");

        String rt = keluarga.getRt();
        counter = 3 - rt.length();
        for(int i = 0; i < counter; i++){
            rt = "0" + rt;
        }

        String rw = keluarga.getRw();
        counter = 3 - rw.length();
        for(int i = 0; i < counter; i++){
            rw = "0" + rw;
        }

        System.out.println(rt + rw);

        keluarga.setRt(rt);
        keluarga.setRw(rw);

        keluargaDAO.addKeluarga(keluarga);

        model.addAttribute("confirmation", "Keluarga dengan NKK " + nkk + " berhasil ditambahkan");

        return "confirmation";
    }
   
    
}
    