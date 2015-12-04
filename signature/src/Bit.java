/**
 * PACKAGE_NAME
 * Last modified by Admin on 12/3/2015.
 * 4:51 PM
 */
public class Bit {
    private boolean set;
    public Bit(boolean isSet){
        set = isSet;
    }
    public boolean getSet(){
        return set;
    }

    public static void main(String arg[]){//test toBit method
        byte test = 0b00010101;
        boolean[] x = toBit(test);
        for(boolean e:x){
            System.out.print((e)?1:0);
        }
        System.out.println();
        test <<= 2;
        x = toBit(test);
        for(boolean e:x){
            System.out.print((e)?1:0);
        }
    }
    public void setSet(boolean set) {
        this.set = set;
    }

    public static boolean[] toBit(byte b){
        int mask = 0x00000001;
        boolean bitGroup[] = new boolean[8];
        for(int i=7;i>=0;i--){
            bitGroup[i] = (mask&b) != 0;
            mask = mask << 1;
        }
        return bitGroup;
    }
}
