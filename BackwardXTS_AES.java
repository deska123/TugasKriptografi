import java.util.Random;

public class BackwardXTS_AES
{
    /*
        Fungsi Utama XTS-AES melakukan dekripsi data satu unit
    */
    public static String dekripsiSatuUnit(String cipherData, String kunci)
    {
        Random ran = new Random();
        String tweak = "", plainData = "";
        for(int i = 0; i < 32; i++) {
            tweak += ran.nextInt(10) + "";
        }
        int banyakBlok;
        if(cipherData.length() % 32 != 0) {
            banyakBlok = (cipherData.length() / 32) + 1;
        } else {
            banyakBlok = cipherData.length() / 32;
        }
        
        // Membagi Cipher Text ke dalam Blok-Blok berukuran 128 bit atau 16 byte
        String[] cipherBlok = new String[banyakBlok];
        int j = 0;
        for(int i = 0; i < cipherBlok.length; i++) {
            if(i == cipherBlok.length - 1) {
                cipherBlok[i] = cipherData.substring(j, cipherData.length());
            } else {
                cipherBlok[i] = cipherData.substring(j, j + 32);
            }
            j += 32;
        }
        
        // Melakukan dekripsi per blok dari blok ke 0 hingga blok ke m-2
        String[] plainBlok = new String[banyakBlok];
        for(int i = 0; i < cipherBlok.length - 2; i++) {
            plainBlok[i] = dekripsiSatuBlok(tweak, cipherBlok[i], kunci);
        }
        
        // Memperoleh ukuran byte blok terakhir (m)
        int b = cipherBlok[cipherBlok.length - 1].length();
           
        // Dekripsi Blok m-1 dan blok m
        if(b < 32) {
            String CC = dekripsiSatuBlok(tweak, cipherBlok[cipherBlok.length - 2], kunci);
            String cM = CC.substring(0, b);
            String cP = CC.substring(b, CC.length());
            String pP = cipherBlok[cipherBlok.length - 1] + cP;
            plainBlok[cipherBlok.length - 2] = dekripsiSatuBlok(tweak, pP, kunci);
            plainBlok[cipherBlok.length - 2] = cM;
        } else {
            plainBlok[cipherBlok.length - 2] = dekripsiSatuBlok(tweak, cipherBlok[cipherBlok.length - 2], kunci);
            plainBlok[cipherBlok.length - 1] = dekripsiSatuBlok(tweak, cipherBlok[cipherBlok.length - 1], kunci);
        }
        for(int i = 0; i < plainBlok.length; i++) {
            plainData += plainBlok[i];
        }
        return plainData;
    }
    
    /*
        Fungsi melakukan dekripsi satu blok sebesar 128 bit atau 16 byte
    */
    public static String dekripsiSatuBlok(String i, String cipherBlok, String kunci)
    {
        String plainBlok = "";
        String key1 = "", key2 = "";
        for(int j = 0; j < 32; j++) {
            key1 += kunci.charAt(j);
        }
        for(int j = 32; j < 64; j++) {
            key2 += kunci.charAt(j);
        }
        String temp = ForwardAES.enkripsi(i, key2);
        String T = "";
        for(int j = 0; j < i.length(); j += 2) {
            int angka = Utility.binToDes(Utility.hexToBin(temp.charAt(j) + temp.charAt(j + 1) + ""));
            int angkaTerakhir = Utility.binToDes(Utility.hexToBin(temp.charAt(i.length() - 2) + temp.charAt(i.length() - 1) + ""));
            if(j == 0) {
                int a = 2 * (angka % 128);
                int b = (int) (135 * Math.floor(angkaTerakhir / 128));
                T += Utility.binToHexAkhir(Utility.desToBin(a ^ b));
            } else {
                int angkaSebelum = Utility.binToDes(Utility.hexToBin(temp.charAt(j - 2) + temp.charAt(j - 1) + ""));
                int a = 2 * (angka % 128);
                int b = (int) Math.floor(angkaSebelum / 128);
                T += Utility.binToHexAkhir(Utility.desToBin(a ^ b));
            }
        }
        String CC = Utility.binToHexAkhir(Utility.xorBesar(Utility.hexToBin(cipherBlok), Utility.hexToBin(T)));
        String PP = BackwardAES.dekripsi(CC, key1);
        plainBlok = Utility.binToHexAkhir(Utility.xorBesar(Utility.hexToBin(PP), Utility.hexToBin(T)));
        return plainBlok;
    }
}
