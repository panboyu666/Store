package pan.bo.yu.petadoption;
//目的 myRef2中的傳送轉換
public class TextString {
    private String z1;
    private String z2;
    private String z3;
    private int z4;
    private String z5;
    private String z6;
    private String z7;
    private String z8;
    private String z9;

    public TextString(){}
    public TextString(String text_1,String text_2){
        this.z1=text_1;
        this.z2=text_2;


    }
    public TextString(String text_1,String text_2,String text_3,int text_4,String text_5,String text_6
            ,String text_7,String text_8,String text_9){
        this.z1=text_1;
        this.z2=text_2;
        this.z3=text_3;
        this.z4=text_4;
        this.z5=text_5;
        this.z6=text_6;
        this.z7=text_7;
        this.z8=text_8;
        this.z9=text_9;

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
    public String getZ5(){
        return z5;
    }
    public String getZ6(){
        return z6;
    }
    public String getZ7(){
        return z7;
    }
    public String getZ8(){
        return z8;
    }
    public String getZ9(){
        return z9;
    }
}
