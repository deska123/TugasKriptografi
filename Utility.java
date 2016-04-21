public class Utility 
{
    /*
        Mengubah String ke suatu Array String (dua bit pada string berpasangan)
    */
    public static String[][] ubahKeArrayString(String teks)
    {
        String[][] baru = new String[4][4];
        int k = 0;
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                baru[j][i] = teks.charAt(k) + "";
                baru[j][i] += teks.charAt(k + 1) + "";
                k += 2;
            }
        }
        return baru;
    }
    
    /*
        Kasus dimana karakter heksadesimalnya "A","B", dan seterusnya, lalu diubah ke bentuk bilangannya
    */
    public static String hexSpesial(String spesial)
    {
        if(spesial.equals("A")) {
            spesial = "10";
        } else if(spesial.equals("B")) {
            spesial = "11";
        } else if(spesial.equals("C")) {
            spesial = "12";
        } else if(spesial.equals("D")) {
            spesial = "13";
        } else if(spesial.equals("E")) {
            spesial = "14";
        } else if(spesial.equals("F")) {
            spesial = "15";
        }
        return spesial;
    }
    
    /*
        Shift satu kali pada perkalian Galois Field
    */
    public static String shift(String data)
    {
        String baru = "";
        for(int i = 0; i < data.length(); i++) {
            if(i == 7) {
                baru += "0";
            } else {
                baru += data.charAt(i + 1) + "";
            }
        }
        return baru;
    }
    
    /*
        Perkalian antar dua data heksadesimal
    */
    public static String kaliString(String satu, String dua)
    {
        String temp[] = new String[8];
        String a = hexToBin(satu), b = hexToBin(dua);
        String baru = a, hasilTemp = a;
        for(int i = 7; i >= 0; i--) {
            if(i == 7) {
                hasilTemp = temp[i] = a;
            } else {
                if(hasilTemp.charAt(0) == '0') {
                    hasilTemp = temp[i] = shift(hasilTemp);
                } else {
                    hasilTemp = temp[i] = xorBesar(shift(hasilTemp), "00011011");
                }
            }
        }
        for(int j = 7; j >= 0; j--) {
            if(b.charAt(j) == '1') {
                baru = xorBesar(baru, temp[j]);
            } 
        }
        return baru;
    }
    
    /*
        Data mengenai bilangan heksadesimal dengan bilangan biner-nya
    */
    public static String hexToBinSmall(int hex)
    {
        String small = "";
        String[] konversi = 
        {"0000", "0001", "0010", "0011",
        "0100", "0101", "0110", "0111",
        "1000", "1001", "1010", "1011",
        "1100", "1101", "1110", "1111"};
        if(hex == 0) {
            small = konversi[0];
        } else if(hex == 1) {
            small = konversi[1];
        } else if(hex == 2) {
            small = konversi[2];
        } else if(hex == 3) {
            small = konversi[3];
        } else if(hex == 4) {
            small = konversi[4];
        } else if(hex == 5) {
            small = konversi[5];
        } else if(hex == 6) {
            small = konversi[6];
        } else if(hex == 7) {
            small = konversi[7];
        } else if(hex == 8) {
            small = konversi[8];
        } else if(hex == 9) {
            small = konversi[9];
        } else if(hex == 10) {
            small = konversi[10];
        } else if(hex == 11) {
            small = konversi[11];
        } else if(hex == 12) {
            small = konversi[12];
        } else if(hex == 13) {
            small = konversi[13];
        } else if(hex == 14) {
            small = konversi[14];
        } else {
            small = konversi[15];
        } 
        return small;
    }
    
    /*
        Konversi dari heksadesimal ke binary
    */
    public static String hexToBin(String hex)
    {
        String bin = "";
        for(int i = 0; i < hex.length(); i++) {
            int in = 0;
            if(hex.charAt(i) == 'A') {
                in = 10;
            } else if(hex.charAt(i) == 'B') {
                in = 11;
            } else if(hex.charAt(i) == 'C') {
                in = 12;
            } else if(hex.charAt(i) == 'D') {
                in = 13;
            } else if(hex.charAt(i) == 'E') {
                in = 14;
            } else if(hex.charAt(i) == 'F') {
                in = 15;
            } else {
                in = Integer.parseInt(hex.charAt(i) + "");
            }
            bin += hexToBinSmall(in);
        }
        return bin;
    }
    
    /*
        Konversi dari Biner ke Heksadesimal
    */
    public static String binToHexAkhir(String kata)
    {
        String baru = "";
        char[] bin = kata.toCharArray();
        for(int i = 0; i < bin.length; i++) {
            if(bin[i] == '0' && bin[i + 1] == '0' && bin[i + 2] == '0' && bin[i + 3] == '0') {
                baru += "0";
            } else if(bin[i] == '0' && bin[i + 1] == '0' && bin[i + 2] == '0' && bin[i + 3] == '1') {
                baru += "1";
            } else if(bin[i] == '0' && bin[i + 1] == '0' && bin[i + 2] == '1' && bin[i + 3] == '0') {
                baru += "2";
            } else if(bin[i] == '0' && bin[i + 1] == '0' && bin[i + 2] == '1' && bin[i + 3] == '1') {
                baru += "3";
            } else if(bin[i] == '0' && bin[i + 1] == '1' && bin[i + 2] == '0' && bin[i + 3] == '0') {
                baru += "4";
            } else if(bin[i] == '0' && bin[i + 1] == '1' && bin[i + 2] == '0' && bin[i + 3] == '1') {
                baru += "5";
            } else if(bin[i] == '0' && bin[i + 1] == '1' && bin[i + 2] == '1' && bin[i + 3] == '0') {
                baru += "6";
            } else if(bin[i] == '0' && bin[i + 1] == '1' && bin[i + 2] == '1' && bin[i + 3] == '1') {
                baru += "7";
            } else if(bin[i] == '1' && bin[i + 1] == '0' && bin[i + 2] == '0' && bin[i + 3] == '0') {
                baru += "8";
            } else if(bin[i] == '1' && bin[i + 1] == '0' && bin[i + 2] == '0' && bin[i + 3] == '1') {
                baru += "9";
            } else if(bin[i] == '1' && bin[i + 1] == '0' && bin[i + 2] == '1' && bin[i + 3] == '0') {
                baru += "A";
            } else if(bin[i] == '1' && bin[i + 1] == '0' && bin[i + 2] == '1' && bin[i + 3] == '1') {
                baru += "B";
            } else if(bin[i] == '1' && bin[i + 1] == '1' && bin[i + 2] == '0' && bin[i + 3] == '0') {
                baru += "C";
            } else if(bin[i] == '1' && bin[i + 1] == '1' && bin[i + 2] == '0' && bin[i + 3] == '1') {
                baru += "D";
            } else if(bin[i] == '1' && bin[i + 1] == '1' && bin[i + 2] == '1' && bin[i + 3] == '0') {
                baru += "E";
            } else {
                baru += "F";
            } 
            i += 3;
        }
        return baru;
    }
    
    /*
        Gabungan XOR pada dua data biner
    */
    public static String xorBesar(String satu, String dua)
    {
        String baru = "";
        for(int i = 0; i < satu.length(); i++) {
            baru += (satu.charAt(i) ^ dua.charAt(i)) + "";
        }
        return baru;
    }
}
