public class ForwardXTS_AES 
{
    /*
        Fungsi Utama XTS-AES melakukan enkripsi data satu unit
    */
    public static String enkripsiSatuUnit(String plainData, String kunci)
    {
        String tweak = "00000000000000000000000000000000", cipherData = "";
        int banyakBlok;
        if(plainData.length() % 32 != 0) {
            banyakBlok = (plainData.length() / 32) + 1;
        } else {
            banyakBlok = plainData.length() / 32;
        }
        
        // Membagi Plain Text ke dalam Blok-Blok berukuran 128 bit atau 16 byte
        String[] plainBlok = new String[banyakBlok];
        int j = 0;
        for(int i = 0; i < plainBlok.length; i++) {
            if(i == plainBlok.length - 1) {
                plainBlok[i] = plainData.substring(j, plainData.length());
            } else {
                plainBlok[i] = plainData.substring(j, j + 32);
            }
            j += 32;
        }
        
        // Melakukan enkripsi per blok dari blok ke 0 hingga blok ke m-2
        String[] cipherBlok = new String[banyakBlok];
        for(int i = 0; i < plainBlok.length - 2; i++) {
            cipherBlok[i] = enkripsiSatuBlok(tweak, plainBlok[i], kunci);
        }
        
        // Memperoleh ukuran byte blok terakhir (m)
        int b = plainBlok[plainBlok.length - 1].length();
           
        // Enkripsi Blok m-1 dan blok m
        if(b < 32) {
            String CC = enkripsiSatuBlok(tweak, plainBlok[plainBlok.length - 2], kunci);
            String cM = CC.substring(0, b);
            String cP = CC.substring(b, CC.length());
            String pP = plainBlok[plainBlok.length - 1] + cP;
            cipherBlok[plainBlok.length - 2] = enkripsiSatuBlok(tweak, pP, kunci);
            cipherBlok[plainBlok.length - 2] = cM;
        } else if(banyakBlok > 1){
            cipherBlok[plainBlok.length - 2] = enkripsiSatuBlok(tweak, plainBlok[plainBlok.length - 2], kunci);
            cipherBlok[plainBlok.length - 1] = enkripsiSatuBlok(tweak, plainBlok[plainBlok.length - 1], kunci);
        } else {
            cipherBlok[plainBlok.length - 1] = enkripsiSatuBlok(tweak, plainBlok[plainBlok.length - 1], kunci);
        }
        for(int i = 0; i < cipherBlok.length; i++) {
            cipherData += cipherBlok[i];
        }
        return cipherData;
    }
    
    /*
        Fungsi melakukan enkripsi satu blok sebesar 128 bit atau 16 byte
    */
    public static String enkripsiSatuBlok(String i, String plainBlok, String kunci)
    {
        String cipherBlok = "";
        String key1 = "", key2 = "";
        for(int j = 0; j < 32; j++) {
            key1 += kunci.charAt(j);
        }
        for(int j = 32; j < 64; j++) {
            key2 += kunci.charAt(j);
        }
        String temp = ForwardAES.enkripsi(i, key2);
        String T = "";
        for(int j = 0; j < temp.length(); j += 2) {
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
        String PP = Utility.binToHexAkhir(Utility.xorBesar(Utility.hexToBin(plainBlok), Utility.hexToBin(T)));
        String CC = ForwardAES.enkripsi(PP, key1);
        cipherBlok = Utility.binToHexAkhir(Utility.xorBesar(Utility.hexToBin(CC), Utility.hexToBin(T)));
        return cipherBlok;
    }
}
