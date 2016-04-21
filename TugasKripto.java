import java.util.Scanner;

public class TugasKripto 
{

    public static void main(String[] args) 
    {
        Scanner in = new Scanner(System.in);
        //System.out.print("Apakah yang ingin Anda lakukan ? (enkrip/dekrip) : ");
        //if(in.next().equals("enkrip")) {
            System.out.print("Masukkan data (hex) yang ingin Anda enkrip : ");
            String plain = in.next().trim().toUpperCase();
            System.out.print("Masukkan kunci (sebanyak 128-bit) : ");
            String kunci = in.next().trim().toUpperCase();
            ForwardAES.enkripsi(plain, kunci);
        /*} else {
            System.out.println("Belum tersedia");
        }*/
    }  
}
