package com.example.controller;

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
public class PendudukController
{
    @Autowired
    PendudukService pendudukDAO;

    @Autowired
    KeluargaService keluargaDAO;

    @Autowired
    KelurahanService kelurahanDAO;

    @Autowired
    KecamatanService kecamatanDAO;

    @Autowired
    KotaService kotaDAO;
    
    @RequestMapping("/")
    public String index(){
        return "index";
    }
   
    @GetMapping("/penduduk")
    public String cariNik(@RequestParam(value = "nik", required = false) String nik, Model model){
        if(nik == null){
            model.addAttribute("error", "Masukkan input NIK");
            return "error";
        }

        PendudukModel penduduk = pendudukDAO.selectPenduduk(nik);
        if(penduduk == null){
            model.addAttribute("error", "NIK tidak ditemukan");
            return "error";
        }

        KeluargaModel keluarga = keluargaDAO.selectKeluargaByID(penduduk.getId_keluarga());
        KelurahanModel kelurahan = kelurahanDAO.selectKelurahan(keluarga.getId_kelurahan());
        KecamatanModel kecamatan = kecamatanDAO.selectKecamatan(kelurahan.getId_kecamatan());
        KotaModel kota = kotaDAO.selectKota(kecamatan.getId_kota());

        model.addAttribute("penduduk", penduduk);
        model.addAttribute("keluarga", keluarga);
        model.addAttribute("kelurahan", kelurahan);
        model.addAttribute("kecamatan", kecamatan);
        model.addAttribute("kota", kota);

        if(penduduk.getJenis_kelamin().equals("1")){
            penduduk.setJenis_kelamin("Perempuan");
        } else{
            penduduk.setJenis_kelamin("Laki-laki");
        }

        if(penduduk.getIs_wni().equals("1")){
            penduduk.setIs_wni("WNI");
        }
        else{
            penduduk.setIs_wni("WNA");
        }

        if(penduduk.getIs_wafat().equals("1")){
            penduduk.setIs_wafat("Mati");
        }
        else{
            penduduk.setIs_wafat("Hidup");
        }

        return "view-penduduk";
    }
    
  
    /*@RequestMapping(value = "/penduduk/ubah", method = RequestMethod.GET)
    public String editPenduduk(){
    	return "edit-penduduk";
    } */
    
    
    @GetMapping("/penduduk/ubah/{nik}")
    public String ubahPenduduk(@PathVariable(value = "nik") String nik, Model model){
        PendudukModel penduduk = pendudukDAO.selectPenduduk(nik);

        if(penduduk != null){
            model.addAttribute("penduduk", penduduk);
        } else {
            model.addAttribute("error", "NIK tidak ditemukan");
            return "view/error";
        }

        return "edit-penduduk-form";
    }

    @PostMapping("/penduduk/ubah/{nik}")
    public String ubahPendudukSubmit(@PathVariable(value = "nik") String nikLama, @Valid @ModelAttribute PendudukModel penduduk, BindingResult result, Model model){

        if(result.hasErrors()){
            model.addAttribute("error", result.getModel().toString());
            return "error";
        }

        PendudukModel penduduklama = pendudukDAO.selectPenduduk(nikLama);

        KeluargaModel keluarga = keluargaDAO.selectKeluargaByID(penduduk.getId_keluarga());
        KelurahanModel kelurahan = kelurahanDAO.selectKelurahan(keluarga.getId_kelurahan());
        KecamatanModel kecamatan = kecamatanDAO.selectKecamatan(kelurahan.getId_kecamatan());

        KeluargaModel keluargaLama = keluargaDAO.selectKeluargaByID(penduduklama.getId_keluarga());
        KelurahanModel kelurahanLama = kelurahanDAO.selectKelurahan(keluargaLama.getId_kelurahan());

        String nik = nikLama;

        if((!penduduk.getTanggal_lahir().equals(penduduklama.getTanggal_lahir())) || (!kelurahan.getId_kecamatan().equals(kelurahanLama.getId_kecamatan())) || (!penduduk.getJenis_kelamin().equals(penduduklama.getJenis_kelamin()))){
            String[] tglLahir = penduduk.getTanggal_lahir().split("-");
            if(penduduk.getJenis_kelamin().equals("1")){
                tglLahir[2] = (Integer.parseInt(tglLahir[2]) + 40) + "";
            }

            nik = kecamatan.getKode_kecamatan().substring(0,6) + tglLahir[2] + tglLahir[1] + tglLahir[0].substring(2);

            List<PendudukModel> similar = pendudukDAO.selectSimilarNIK(nik + "%");
            String nomor = "0001";
            if(similar.size() > 0){
                int nomorSimilar = Integer.parseInt(similar.get(similar.size()-1).getNik().substring(12)) + 1;
                nomor = nomorSimilar + "";
            }

            int counter = 4 - nomor.length();
            for(int i = 0; i < counter; i++){
                nomor = "0" + nomor;
            }

            nik = nik + nomor;
        }

        penduduk.setNik(nik);

        penduduk.setId(penduduklama.getId());

        pendudukDAO.updatePenduduk(penduduk);

        model.addAttribute("confirmation", "Penduduk dengan NIK " + nikLama + " berhasil diubah");

        return "confirmation";
    }
    
    

    @GetMapping("/penduduk/tambah")
    public String tambahPenduduk(Model model){

        PendudukModel penduduk = new PendudukModel();
        penduduk.setGolongan_darah("");
        penduduk.setJenis_kelamin("");
        penduduk.setIs_wni("");
        penduduk.setStatus_perkawinan("");
        penduduk.setIs_wafat("");

        model.addAttribute("penduduk",penduduk);

        return "add-penduduk";
    }
    
    @PostMapping("/penduduk/tambah")
    public String tambahPendudukSubmit(@Valid @ModelAttribute PendudukModel penduduk, Model model, BindingResult result){

        if(result.hasErrors()){
            model.addAttribute("error", result.getModel().toString());
            return "error";
        }

        KeluargaModel keluarga = keluargaDAO.selectKeluargaByID(penduduk.getId_keluarga());

        if(keluarga == null){
            model.addAttribute("error", "ID Keluarga tidak ditemukan");
            return "error";
        }

        KelurahanModel kelurahan = kelurahanDAO.selectKelurahan(keluarga.getId_kelurahan());
        KecamatanModel kecamatan = kecamatanDAO.selectKecamatan(kelurahan.getId_kecamatan());

        String[] tglLahir = penduduk.getTanggal_lahir().split("-");
        if(penduduk.getJenis_kelamin().equals("1")){
            tglLahir[2] = (Integer.parseInt(tglLahir[2]) + 40) + "";
        }
        String nik = kecamatan.getKode_kecamatan().substring(0,6) + tglLahir[2] + tglLahir[1] + tglLahir[0].substring(2);

        List<PendudukModel> similar = pendudukDAO.selectSimilarNIK(nik + "%");
        String nomor = "0001";
        if(similar.size() > 0){
            int nomorSimilar = Integer.parseInt(similar.get(similar.size()-1).getNik().substring(12)) + 1;
            nomor = nomorSimilar + "";
        }

        int counter = 4 - nomor.length();
        for(int i = 0; i < counter; i++){
            nomor = "0" + nomor;
        }

        nik = nik + nomor;
        penduduk.setNik(nik);

        pendudukDAO.addPenduduk(penduduk);

        model.addAttribute("confirmation", "Penduduk dengan NIK " + nik + " berhasil ditambahkan");

        return "confirmation";
    }

   
 
    
    @PostMapping("/penduduk/mati")
    public String nonaktifPenduduk(@RequestParam(value="nik", required = false) String nik, Model model){

        if(nik == null){
            model.addAttribute("error", "Masukkan input NIK");
            return "error";
        }

        PendudukModel penduduk = pendudukDAO.selectPenduduk(nik);
        if(penduduk == null){
            model.addAttribute("error", "NIK tidak ditemukan");
            return "error";
        }

        pendudukDAO.nonaktifkanPenduduk(nik);

        KeluargaModel keluarga = keluargaDAO.selectKeluargaByID(penduduk.getId_keluarga());

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

        keluargaDAO.updateKeluarga(keluarga);

        model.addAttribute("confirmation", "Penduduk dengan NIK " + nik + " sudah tidak aktif");
        model.addAttribute("mati", nik);

        return "confirmation";
    }
    
    // fitur 8 gabsss apa2 :( error Could not parse as expression: "/penduduk/cari" (search-penduduk:16)
    
    @GetMapping("/penduduk/cari")
    public String cariPenduduk(@RequestParam(value="kt", required=false) String kota,
                               @RequestParam(value="kc", required=false) String kecamatan,
                               @RequestParam(value="kl", required=false) String kelurahan,
                               Model model){
        if(kota == null){
            model.addAttribute("kota", kotaDAO.selectAllKota());
            return "search-penduduk-kota";
        } else{

            KotaModel kotaModel = kotaDAO.selectKota(kota);

            if(kotaModel == null){
                model.addAttribute("error", "Kota tidak ditemukan");
                return "error";
            }

            if(kecamatan == null){
                model.addAttribute("kota", kotaDAO.selectKota(kota));
                model.addAttribute("kecamatan", kecamatanDAO.selectAllKecamatan(kota));
                return "search-penduduk-kecamatan";
            } else{

                KecamatanModel kecamatanModel = kecamatanDAO.selectKecamatan(kecamatan);

                if(kecamatanModel == null){
                    model.addAttribute("error", "Kecamatan tidak ditemukan");
                    return "error";
                }

                if(!kecamatanModel.getId_kota().equals(kota)){
                    model.addAttribute("error", "Kecamatan tidak ditemukan");
                    return "error";
                }

                if(kelurahan == null){
                    model.addAttribute("kota", kotaDAO.selectKota(kota));
                    model.addAttribute("kecamatan", kecamatanDAO.selectKecamatan(kecamatan));
                    model.addAttribute("kelurahan", kelurahanDAO.selectAllKelurahan(kecamatan));
                    return "search-penduduk-kelurahan";
                }
            }
        }

        KelurahanModel kelurahanModel = kelurahanDAO.selectKelurahan(kelurahan);

        if(kelurahanModel == null){
            model.addAttribute("error", "Kelurahan tidak ditemukan");
            return "error";
        }

        KecamatanModel kecamatanModel = kecamatanDAO.selectKecamatan(kecamatan);

        if(!kecamatanModel.getId_kota().equals(kota)){
            model.addAttribute("error", "Kecamatan tidak ditemukan");
            return "error";
        }

        if(!kelurahanModel.getId_kecamatan().equals(kecamatan)){
            model.addAttribute("error", "Kelurahan tidak ditemukan");
            return "error";
        }

        List<PendudukModel> pendudukList = pendudukDAO.selectPendudukByKelurahan(kelurahan);
        PendudukModel muda = pendudukList.get(0);
        PendudukModel tua = pendudukList.get(0);
        for(int i = 0 ; i< pendudukList.size(); i++){
            if(pendudukList.get(i).getJenis_kelamin().equals("1")){
                pendudukList.get(i).setJenis_kelamin("Perempuan");
            } else{
                pendudukList.get(i).setJenis_kelamin("Laki-laki");
            }

            if(muda.getTanggal_lahir().compareTo(pendudukList.get(i).getTanggal_lahir()) < 0){
                muda = pendudukList.get(i);
            }

            if(tua.getTanggal_lahir().compareTo(pendudukList.get(i).getTanggal_lahir()) > 0){
                tua = pendudukList.get(i);
            }
        }

        model.addAttribute("tua", tua);
        model.addAttribute("muda", muda);
        model.addAttribute("penduduk", pendudukList);

        model.addAttribute("kota", kotaDAO.selectKota(kota));
        model.addAttribute("kecamatan", kecamatanDAO.selectKecamatan(kecamatan));
        model.addAttribute("kelurahan", kelurahanDAO.selectKelurahan(kelurahan));

        return "search-penduduk-result";
    }
    
    
}
    
 


    