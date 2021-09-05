package pan.bo.yu.petadoption;
//目的 myRef2中的傳送轉換
public class TextString {
    private String z1;
    private String z2;
    private String z3;
    private int z4;

    public TextString(){}
    public TextString(String text_1,String text_2){
        this.z1=text_1;
        this.z2=text_2;

    }
    public TextString(String text_1,String text_2,String text_3,int text_4){
        this.z1=text_1;
        this.z2=text_2;
        this.z3=text_3;
        this.z4=text_4;
    }

    public String getZ1(){
        return z1;
    }
    public String getZ2(){
        return z2;
    }
    public String getZ3(){
        return z3;
    }
    public int getZ4(){
        return z4;
    }
}
