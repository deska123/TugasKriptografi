public class KeyExpansion 
{
    // AES S-Boxes
    public static String[][] sBox =
    {
        {"63","7C","77","7B","F2","6B","6F","C5","30","01","67","2B","FE","D7","AB","76"},
        {"CA","82","C9","7D","FA","59","47","F0","AD","D4","A2","AF","9C","A4","72","C0"},
        {"B7","FD","93","26","36","3F","F7","CC","34","A5","E5","F1","71","D8","31","15"},
        {"04","C7","23","C3","18","96","05","9A","07","12","80","E2","EB","27","B2","75"},
        {"09","83","2C","1A","1B","6E","5A","A0","52","3B","D6","B3","29","E3","2F","84"},
        {"53","D1","00","ED","20","FC","B1","5B","6A","CB","BE","39","4A","4C","58","CF"},
        {"D0","EF","AA","FB","43","4D","33","85","45","F9","02","7F","50","3C","9F","A8"},
        {"51","A3","40","8F","92","9D","38","F5","BC","B6","DA","21","10","FF","F3","D2"},
        {"CD","0C","13","EC","5F","97","44","17","C4","A7","7E","3D","64","5D","19","73"},
        {"60","81","4F","DC","22","2A","90","88","46","EE","B8","14","DE","5E","0B","DB"},
        {"E0","32","3A","0A","49","06","24","5C","C2","D3","AC","62","91","95","E4","79"},
        {"E7","C8","37","6D","8D","D5","4E","A9","6C","56","F4","EA","65","7A","AE","08"},
        {"BA","78","25","2E","1C","A6","B4","C6","E8","DD","74","1F","4B","BD","8B","8A"},
        {"70","3E","B5","66","48","03","F6","0E","61","35","57","B9","86","C1","1D","9E"},
        {"E1","F8","98","11","69","D9","8E","94","9B","1E","87","E9","CE","55","28","DF"},
        {"8C","A1","89","0D","BF","E6","42","68","41","99","2D","0F","B0","54","BB","16"}
    };
    // Round Constant List
    public static String[][] rCon =
    {
        {"01","02","04","08","10","20","40","80","1B","36"},
        {"00","00","00","00","00","00","00","00","00","00"},
        {"00","00","00","00","00","00","00","00","00","00"},
        {"00","00","00","00","00","00","00","00","00","00"}
    };
    
    /*
        Fungsi untuk melakukan memperoleh String konstan dari Array rCon
    */
    public static String getRCon(int ronde)
    {
        String baru = "";
        for(int i = 0; i < 4; i++) {
            baru += rCon[i][ronde];
        }
        return baru;
    }
    
    /*
        Fungsi melakukan satu-byte left shift pada kata 
    */
    public static String[] rotWord(String[] kata)
    {
        String[] baru = new String[kata.length];
        for(int i = 0; i < kata.length; i++) {
            if(i == 3) {
                baru[i] = kata[0];
            } else {
                baru[i] = kata[i + 1];
            }
        }
        return baru;
    }
    
    /*
        Substitute Bytes 
    */
    public static String[] subBytes(String[] masukan)
    {
        String[] baru = new String[masukan.length];
       
        for(int i = 0; i < 4; i++) {
            int x = Integer.parseInt(Utility.hexSpesial(masukan[i].charAt(0) + ""));
            int y = Integer.parseInt(Utility.hexSpesial(masukan[i].charAt(1) + ""));
            baru[i] = sBox[x][y];
        }
        return baru;
    }
    
    /*
        Fungsi mengisi String ke dalam Array untuk kunci dengan rentang indeks tertentu
    */
    public static String[][] isiKotak(String[][] arrayKunci, int kolom, String[] isian)
    {
        for(int i = 0; i < 4; i++) {
            arrayKunci[i][kolom] = isian[i];
        }
        return arrayKunci;
    }
    
    /*
        Fungsi Utama Pembentukan Key untuk 9 ronde utama dan 1 ronde akhir
    */
    public static String[] function(String kunciAwal)
    {
        String[] daftarKunci = new String[10];
        String[][] sebelum = Utility.ubahKeArrayString(kunciAwal);
        for(int ronde = 0; ronde < 10; ronde++) {
            String[][] temp = new String[4][4];
            
            String[] wRot = {sebelum[0][3], sebelum[1][3], sebelum[2][3], sebelum[3][3]};
            temp = isiKotak(temp, 0, rotWord(wRot));
            
            String[] wSub = {temp[0][0], temp[1][0], temp[2][0], temp[3][0]};
            temp = isiKotak(temp, 0, subBytes(wSub));
            
            String z1 = Utility.xorBesar(Utility.hexToBin(temp[0][0] + temp[1][0] + temp[2][0] + temp[3][0]), Utility.hexToBin(getRCon(ronde)));
            temp = isiKotak(temp, 0, Utility.ubahKeArrayStringSatu(Utility.binToHexAkhir(Utility.xorBesar(Utility.hexToBin(sebelum[0][0] + sebelum[1][0] + sebelum[2][0] + sebelum[3][0]), z1))));
         
            String z2 = Utility.xorBesar(Utility.hexToBin(temp[0][0] + temp[1][0] + temp[2][0] + temp[3][0]), Utility.hexToBin(sebelum[0][1] + sebelum[1][1] + sebelum[2][1] + sebelum[3][1]));
            temp = isiKotak(temp, 1, Utility.ubahKeArrayStringSatu(Utility.binToHexAkhir(z2)));
            
            String z3 = Utility.xorBesar(Utility.hexToBin(temp[0][1] + temp[1][1] + temp[2][1] + temp[3][1]), Utility.hexToBin(sebelum[0][2] + sebelum[1][2] + sebelum[2][2] + sebelum[3][2]));
            temp = isiKotak(temp, 2, Utility.ubahKeArrayStringSatu(Utility.binToHexAkhir(z3)));
            
            String z4 = Utility.xorBesar(Utility.hexToBin(temp[0][2] + temp[1][2] + temp[2][2] + temp[3][2]), Utility.hexToBin(sebelum[0][3] + sebelum[1][3] + sebelum[2][3] + sebelum[3][3]));
            temp = isiKotak(temp, 3, Utility.ubahKeArrayStringSatu(Utility.binToHexAkhir(z4)));
            
            daftarKunci[ronde] = Utility.stringArrToString(temp);
            sebelum = temp;
        }
        return daftarKunci;
    }         
}
