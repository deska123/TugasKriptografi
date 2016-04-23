public class ForwardAES 
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
    //Matriks untuk tahap mix Columns
    public static String[][] mix =
    {
        {"02","03","01","01"},
        {"01","02","03","01"},
        {"01","01","02","03"},
        {"03","01","01","02"}
    };
    
    /*
        Forward Transformation (Substitute Bytes Transformation)
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
        ShiftRows Transformation
    */
    public static String[][] shiftRows(String[][] masukan)
    {
        
        String[][] baru = new String[4][4];
        for(int i = 0; i < 4; i++) {
            baru[0][i] = masukan[0][i];
        }
        for(int i = 0; i < 4; i++) {
            if(i == 3) {
                baru[1][i] = masukan[1][0];
            } else {
                baru[1][i] = masukan[1][i + 1];
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
            if(i == 0) {
                baru[3][i] = masukan[3][3];
            } else {
                baru[3][i] = masukan[3][i - 1];
            }
        }
        return baru;
    }
    
    /*
        Mix Columns Transformation
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
        AddRoundKey transformation
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
        Fungsi Utama Enkripsi AES sebanyak 10 ronde
    */
    public static String enkripsi(String plain, String kunci)
    {
        String[][] data = Utility.ubahKeArrayString(plain);
        String[][] key = Utility.ubahKeArrayString(kunci);
        data = addRoundKey(data,key);
        String[] daftarKunci = KeyExpansion.function(kunci);
        for(int ronde = 0; ronde < 10; ronde++) {
            if(ronde == 10) {
                data = addRoundKey(shiftRows(subBytes(data)), Utility.ubahKeArrayString(daftarKunci[ronde]));
            } else {
                data = addRoundKey(mixColumns(mix, shiftRows(subBytes(data))), Utility.ubahKeArrayString(daftarKunci[ronde]));
            }
        }
        return Utility.stringArrToString(data);
    }
}
