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
        byte test = 10;
        boolean[] x = toBit(test);
        for(boolean e:x){
            System.out.print((e)?1:0);
        }
        System.out.println(fromBit(x));
        System.out.println();
        test <<= 2;
        boolean[] c = {true};
        for (boolean e : c) {
            System.out.print((e)?1:0);
        }
        System.out.println();
        System.out.println(byteFromBit(c));
    }
    public void setSet(boolean set) {
        this.set = set;
    }

    /**
     * to boolean array store in big endian msb first
     *
     * @param b a byte want to convert
     * @return bitGroup store in big endian
     */
    public static boolean[] toBit(byte b){
        int mask = 0x00000001;
        boolean bitGroup[] = new boolean[8];
        for(int i=7;i>=0;i--){
            bitGroup[i] = (mask&b) != 0;
            mask = mask << 1;
        }
        return bitGroup;
    }

    /**
     * convert group of bit to decimal int not exceed 31 bit
     * 0-((2^31)-1)
     *
     * @param group
     * @return integer converted form
     */
    public static int fromBit(boolean[] group) {
        int i = 0;
        if (group != null && group.length < 32) {
            i += (group[0]) ? 1 : 0;
            for (int j = 1; j < group.length; j++) {
                i <<= 1;
                i += (group[j]) ? 1 : 0;
            }
        }
        return i;
    }

    public static Byte byteFromBit(boolean[] group) {
        byte i = 0;
        if (group != null && group.length <= 8) {
            i += (group[0]) ? 1 : 0;
            for (int j = 1; j < group.length; j++) {
                i <<= 1;
                i += (group[j]) ? 1 : 0;
            }
        }
        return i;
    }

    public static void paintByte(boolean[] group) {
        for (boolean e : group) {
            System.out.print((e) ? 1 : 0);
        }
    }


}
