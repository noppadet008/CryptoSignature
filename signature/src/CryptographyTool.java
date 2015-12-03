import java.util.*;

public class CryptographyTool
{
	private static int[] setPower;
	private static long[] storagePower;
	private static int[] hashStorage;

	public static Scanner scan = new Scanner(System.in);

    /**
     * main to test method in this class.
     * @param args
     */
	public static void main(String[] args) 
	{
		System.out.println("Hello World!");
		//Scanner scan = new Scanner(System.in);
		CryptographyTool tool = new CryptographyTool();
		while (true) {
		
		log("Please choose menu:\n1.Fast Exponential\n2.Find Inverse\n3.Check Generator\n4.Hash Function (Test Beta)");
		int a, b, c;
		System.out.print(">> ");
		int choose = scan.nextInt();
        String result = "";
		switch (choose)
		{
		case 1:	
			System.out.print("Base: ");
			a = scan.nextInt();
			System.out.print("Power: ");
			b = scan.nextInt();
			System.out.print("Modulo: ");
			c = scan.nextInt();
			tool.fastExponential(a, b, c);
			break;
		
		case 2:
			System.out.print("Inverse of ");
			a = scan.nextInt();
			System.out.print("Mod ");
			b = scan.nextInt();
			tool.findInverse(a, b);
			break;
		
		case 3:
			System.out.print("Check generator of ");
			a = scan.nextInt();
			System.out.print("Mod ");
			b = scan.nextInt();
			tool.checkGenerator(a, b);
			break;
		case 4:
			tool.hashFunction();
			break;

	
		}
        log(result);
		log("**********************");
	}
		

		//long start = System.currentTimeMillis();
		
		//checkGeneratorII(a,b);
		//checkGenerator(a,b);
		//int c = findInverse(a,b);
		//System.out.println("Inverse of " + a + " mod " + b + " = " + c);
		//System.out.println(">> " + findInverse(a,b));
		/*factorizationPower(b);
		storagePower(a,c);
		combinationTogether(c);*/
		//long end = System.currentTimeMillis();
		//System.out.println("Time: " + (end-start));
		

	} // end main

    /**
     * method to find gcd with two number
     *
     * @param firstNumber int - a number to find gcd with next param
     * @param anotherNumber int - a number to find gcd with before param
     * @return a gcd in type int
     */
	  int gcd(int firstNumber, int anotherNumber) {
		if (anotherNumber == 0) {
			return firstNumber;
		}
		else if (firstNumber == 0) {
			return anotherNumber;
		}
		else {
			return gcd(anotherNumber, firstNumber%anotherNumber);
		}
	} // end funcrion gcd

    /**
     * method to find inverse with extended gcd algorithm</br>
     *     not fix param (change position param is same result)
     * @param firstNumber a number
     * @param anotherNumber a number
     */
	  void findInverse(double firstNumber, double anotherNumber) {
		if (gcd((int)firstNumber, (int)anotherNumber) != 1) {
			log("Can't find inverse because they're not relative prime.");
			return;
			// Go to error!
		}
		
		int a1, b1, a2, b2, r, q, 
			temp_a2, temp_b2, n1, n2, max, min;
		
		if (firstNumber > anotherNumber) {
			max = (int)firstNumber;
			min = (int)anotherNumber;
			n1 = (int)firstNumber;
			n2 = (int)anotherNumber;
		}
		else {
			max = (int)anotherNumber;
			min = (int)firstNumber;
			n1 = (int)anotherNumber;
			n2 = (int)firstNumber;
		}
			
		
		
		a1 = b2 = 1;
		b1 = a2 = 0;
		temp_a2 = a2;
		temp_b2 = b2;

		r = n1%n2;
		q = n1/n2;

		log("n1 n2 r q a1 a2 b1 b2\n----------------------------");
		log(n1 + " " + n2 + " " 
			+ r + " " + q + " " + a1 + " " + a2 + " " + b1 + " " 
			+ b2);

		while (r != 0) {
			n1 = n2;
			n2 = r;
			a2 = a1-q*a2;
			b2 = b1-q*b2;
			a1 = temp_a2;
			b1 = temp_b2;
			temp_a2 = a2;
			temp_b2 = b2;
			r = n1%n2;
			q = n1/n2;

			log(n1 + " " + n2 + " " 
			+ r + " " + q + " " + a1 + " " + a2 + " " + b1 + " " 
			+ b2);

		}

		/*log(n1 + " " + n2 + " " 
			+ r + " " + q + " " + a1 + " " + a2 + " " + b1 + " " 
			+ b2);*/
		
		log("");
		if (firstNumber == max) {
			//return a2;
			if (a2 < 0) {
				log("Inverse of " + (int)firstNumber + " mod " + 
					(int)anotherNumber + " = " + a2 + " = " + ((int)(a2-anotherNumber*Math.floor(a2/anotherNumber))));
					//return (int)(a2-anotherNumber*Math.floor(a2/anotherNumber));
			}
			else
				log("Inverse of " + (int)firstNumber + " mod " + (int)anotherNumber + " = " + a2);
				//return (int)a2;
		}
		else {
			//return b2
			if (b2 < 0) {
				log("Inverse of " + (int)firstNumber + " mod " + 
					(int)anotherNumber + " = " + b2 + " = " + ((int)(b2-anotherNumber*Math.floor(b2/anotherNumber))));
				//return (int)(b2-anotherNumber*Math.floor(b2/anotherNumber));
			}
			else
				log("Inverse of " + (int)firstNumber + " mod " + (int)anotherNumber + " = " + b2);
			//return (int)b2;
		}

		//System.out.println(n2 == a2*max+b2*min);
		
	} // end function findInverse

    /**
     * to prove tester is prime
     * @param tester a number to test prime.
     */
	  void LehmanTest(int tester) {
		int runNumber, power = (tester-1)/2, prime = 0, composite = 0;
		for (runNumber = 1; runNumber < tester; runNumber++) {
			if (gcd(runNumber, tester) > 1)
				composite++;
			else if (Math.pow(runNumber, power)%tester == 1 || Math.pow(runNumber, power)%tester == tester-1)
				prime++;
			else
				composite++;	
		}

		log("Prime = " + prime + "\nComposite = " + composite);
	} // end function LehmanTest NOT FINISHED!!

    /**
     * method part of fastexponent funcion to create power set
     * @param power a number to represent power
     */
	  void factorizationPower(int power) {
		int n = 1, powerStart = 1, remain;
		while (powerStart < power) {
			powerStart *= 2;
			n++;
		}
		powerStart /= 2;
		n--;
		setPower = new int[n];
		log("\nSize setPower = " +n);
		setPower[0] = powerStart;
		remain = power-setPower[0];
		for (int i=1;remain > 0;i++) {
			while (powerStart > remain) {
				powerStart /= 2;
			}
			setPower[i] = powerStart;
			remain = remain-setPower[i];
		}

		for (int k=0;k<n;k++)
		{
			log("setPower[" + k + "] = " + setPower[k]);
		}
	} // end function factorizationPower

    /**
     * part of fast exponent method create storage power set
     * @param base a number base
     * @param modulate a modulator
     * @return void
     */
	  void storagePower(int base, int modulate) {
		int n = (int)(Math.log(setPower[0])/Math.log(2))+1;
		log("\nSize storagePower = " + n);
		storagePower = new long[n];
		for (int i=0;i<n;i++) {
			if (i == 0) {
				storagePower[i] = (long)Math.pow(base,1)%modulate;
			}
			else {
				storagePower[i] = (long)Math.pow(storagePower[i-1],2)%modulate;
			}
		}

		for (int k=0;k<n;k++)
		{
			log("storagePower[" + k + "] = " + storagePower[k]);
		}
	} // end function storagePower


    /**
     * method to multiply in power storage set
     * @param modulation modulator
     */
	  void combinationTogether(int modulation) {
		long sum = 1;
		int realSize = 0;
          for (int aSetPower : setPower) {
              if (aSetPower != 0)
                  realSize++;
          }
		log("Real size: " + realSize);
		for (int i=0;i<realSize;i++) {
			sum = sum*storagePower[(int)(Math.log(setPower[i])/Math.log(2))];
			log("\n------------\nsum = " + sum);
		}
		log("\n------------\nResult = " + sum%modulation);
	} // end function combinationTogether

    /**
     * method equivalent pow(base,power)% modulation
     * with avoid to overflow
     * @param base int - base
     * @param power int - power
     * @param modulation int - modulator
     */
	  void fastExponential(int base, int power, int modulation) {
		factorizationPower(power);
		storagePower(base, modulation);
		combinationTogether(modulation);

	}

    /**
     * method check tester is a generator of z
     * @param tester int - less than z
     * @param z int - number represent generator set
     */
	  void checkGenerator(int tester, int z) {
		if (tester < 1 || tester > z-1) {
			log("Tester generator must be in range 0<tester<z ");
			return;
			// Go to error!
		}
		boolean[] tableGenerator = new boolean[z];
		boolean bomb = false;
		int valueGeneratorBeforeMod, valueGeneratorAfterMod, TesterPower1 = 0, TesterPowerBefore = 0;
		
		for (int i=1;i<z;i++) {
			if (i == 1) {
				valueGeneratorBeforeMod =  tester;
				valueGeneratorAfterMod = TesterPower1 = TesterPowerBefore = valueGeneratorBeforeMod % z;
			}
			else {
				valueGeneratorBeforeMod = TesterPowerBefore*TesterPower1;
				valueGeneratorAfterMod = valueGeneratorBeforeMod % z;
				TesterPowerBefore = valueGeneratorAfterMod;

			}
			
			if (!tableGenerator[valueGeneratorAfterMod]) {
				tableGenerator[valueGeneratorAfterMod] = true;
			}
			else {
				log(tester + " is NON-generator of Z" + z + "*");
				bomb = true;
				break;
				
			}
			log("checkGenerator[" + i + "] = " + valueGeneratorAfterMod);
			//System.out.println(tester + "^" + i + " mod " + z + " = " + valueGeneratorAfterMod);
		}
		
		/*log("---------------------------------");
		for (int r=0;r<z;r++) {
			log("tableGenerator[" + r + "] = " + tableGenerator[r]);
		}*/
		
		if (!bomb) {
			log(tester + " is Generator of Z" + z + "*");
		}

	} /* end function checkGenerator
		** Status: It's many bugs. << OveRfLoW!!! 
		*/

	/*private static void checkGeneratorII(int tester, int z) {
		boolean check = Math.pow(tester,(z-1)/2)%z == 1%z;
		System.out.println(Math.pow(tester,(z-1)/2)%z);
		if (check)
			System.out.println("it's NOT safe prime.");
		else
			System.out.println("it's SAFE prime.");
		
	} // end function checkGeneratorII*/

	  void hashFunction() {
		hashStorage = new int[6];
		int hashResultI = 0;
		for (int i=0;i<6;i++) {
			hashStorage[i] = scan.nextInt();
			hashResultI += (Math.pow(hashStorage[i],(i+1)))%23;
		}
		hashResultI = hashResultI % 23;
		log("hashResultI = " + hashResultI);
		hashResultI = circularShift2(hashResultI);
		log("hashResultI = " + hashResultI);
		hashResultI = hashResultI % 23;
		log("hashResultI = " + hashResultI);
		

	} // end function hashFunction (TEST BETA!!)

	  int circularShift2(int number) {
		for (int i=0; i<2; i++) {
			if ((16 & number) == 16) {
				number = ((number - 16) << 1) + 1;
			}
			else {
				number = number << 1;
			}
		}
		return number;
	} // end function circularShift

	public static void log(Object anyThings) {
		System.out.println(String.valueOf(anyThings));
	} // end function log



} // end class
