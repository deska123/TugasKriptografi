public class BackwardAES 
{
    // AES Inverse S-Boxes
    public static String[][] sBox =
    {
        {"52","09","6A","D5","30","36","A5","38","BF","40","A3","9E","81","F3","D7","FB"},
        {"7C","E3","39","82","9B","2F","FF","87","34","8E","43","44","C4","DE","E9","CB"},
        {"54","7B","94","32","A6","C2","23","3D","EE","4C","95","0B","42","FA","C3","4E"},
        {"08","2E","A1","66","28","D9","24","B2","76","5B","A2","49","6D","8B","D1","25"},
        {"72","F8","F6","64","86","68","98","16","D4","A4","5C","CC","5D","65","B6","92"},
        {"6C","70","48","50","FD","ED","B9","DA","5E","15","46","57","A7","8D","9D","84"},
        {"90","D8","AB","00","8C","BC","D3","0A","F7","E4","58","05","B8","B3","45","06"},
        {"D0","2C","1E","8F","CA","3F","0F","02","C1","AF","BD","03","01","13","8A","6B"},
        {"3A","91","11","41","4F","67","DC","EA","97","F2","CF","CE","F0","B4","E6","73"},
        {"96","AC","74","22","E7","AD","35","85","E2","F9","37","E8","1C","75","DF","6E"},
        {"47","F1","1A","71","1D","29","C5","89","6F","B7","62","0E","AA","18","BE","1B"},
        {"FC","56","3E","4B","C6","D2","79","20","9A","DB","C0","FE","78","CD","5A","F4"},
        {"1F","DD","A8","33","88","07","C7","31","B1","12","10","59","27","80","EC","5F"},
        {"60","51","7F","A9","19","B5","4A","0D","2D","E5","7A","9F","93","C9","9C","EF"},
        {"A0","E0","3B","4D","AE","2A","F5","B0","C8","EB","BB","3C","83","53","99","61"},
        {"17","2B","04","7E","BA","77","D6","26","E1","69","14","63","55","21","0C","7D"}
    };
    //Matriks untuk tahap Inverse Mix Columns
    public static String[][] mix =
    {
        {"0E","0B","0D","09"},
        {"09","0E","0B","0D"},
        {"0D","09","0E","0B"},
        {"0B","0D","09","0E"}
    };
    
    /*
        Inverse ShiftRows Transformation
    */
    public static String[][] shiftRows(String[][] masukan)
    {  
        String[][] baru = new String[4][4];
        for(int i = 0; i < 4; i++) {
            baru[0][i] = masukan[0][i];
        }
        for(int i = 0; i < 4; i++) {
            if(i == 0) {
                baru[1][i] = masukan[1][3];
            } else {
                baru[1][i] = masukan[1][i - 1];
            }
        }
        for(int i = 0; i < 4; i++) {
            if(i == 2 || i == 3) {
                baru[2][i] = masukan[2][i - 2];
            } else {
                baru[2][i] = masukan[2][i + 2];
            }
        }
        for(int i = 0; i < 4; i++) {
            if(i == 3) {
                baru[3][i] = masukan[3][0];
            } else {
                baru[3][i] = masukan[3][i + 1];
            }
        }
        return baru;
    }
    
    /*
        Backward Transformation (Inverse Substitute Bytes Transformation)
    */
    public static String[][] subBytes(String[][] masukan)
    {
        String[][] baru = new String[4][4];
        int k = 0;
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                int x = Integer.parseInt(Utility.hexSpesial(masukan[i][j].charAt(0) + ""));
                int y = Integer.parseInt(Utility.hexSpesial(masukan[i][j].charAt(1) + ""));
                baru[i][j] = sBox[x][y];
                k += 2;
            }
        }
        return baru;
    }
    
    /*
        Inverse AddRoundKey transformation
    */
    public static String[][] addRoundKey(String[][] data, String[][] kunci) 
    {
        String[][] baru = new String[4][4];
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                baru[i][j] = Utility.binToHexAkhir(Utility.xorBesar(Utility.hexToBin(data[i][j]), Utility.hexToBin(kunci[i][j])));
            }
        }
        return baru;
    }
    
    /*
        Inverse Mix Columns Transformation
    */
    public static String[][] mixColumns(String[][] masukan1, String[][] masukan2)
    {
        String[][] baru = new String[4][4];
        String temp = "";
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                temp = Utility.kaliString(masukan1[i][0], masukan2[0][j]);
                for(int k = 1; k < 4; k++) {
                    temp = Utility.xorBesar(temp, Utility.kaliString(masukan1[i][k], masukan2[k][j]));
                }
                baru[i][j] = Utility.binToHexAkhir(temp);
            }
        }
        return baru;
    }
    
    /*
        Fungsi Utama Dekripsi AES sebanyak 10 ronde
    */
    public static String dekripsi(String cipher, String kunci)
    {
        String[] daftarKunci = KeyExpansion.function(kunci);
        String[][] data = Utility.ubahKeArrayString(cipher);
        data = addRoundKey(data, Utility.ubahKeArrayString(daftarKunci[9]));
        for(int ronde = 8; ronde >= 0; ronde--) {
            data = subBytes(shiftRows(data));
            data = addRoundKey(data, Utility.ubahKeArrayString(daftarKunci[ronde]));  
            data = mixColumns(mix, data);  
        }
        data = subBytes(shiftRows(data));
        data = addRoundKey(data, Utility.ubahKeArrayString(kunci));  
        return Utility.stringArrToString(data);
    }
}
