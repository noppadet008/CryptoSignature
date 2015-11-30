import java.util.*;

/**
 *
 */

public class CryptoTool
{
	private static int[] setPower;
	private static long[] storagePower;
	public static void main(String[] args) 
	{
		System.out.println("Hello World!");
		Scanner scan = new Scanner(System.in);
		
		System.out.print("Base: ");
		int a = scan.nextInt();
		System.out.print("Power: ");
		int b = scan.nextInt();
		System.out.print("Modulo: ");
		int c = scan.nextInt();

		//fastExponential(a, b, c);
		//int c = findInverse(a,b);
		//System.out.println("Inverse of " + a + " mod " + b + " = " + c);
		//System.out.println(">> " + findInverse(a,b));
		//factorizationPower(b);
		//storagePower(a,c);
		//combinationTogether(c);
		

	} // end main

	private static int gcd(int firstNumber, int anotherNumber) {
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

	private static int findInverse(double firstNumber, double anotherNumber) {
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

		}

		/*System.out.println(n1 + " " + n2 + " " 
			+ r + " " + q + " " + a1 + " " + a2 + " " + b1 + " " 
			+ b2);*/
		
		if (firstNumber == max) {
			//return a2;
			if (a2 < 0) {
				/*System.out.println("Inverse of " + (int)firstNumber + " mod " + 
					(int)anotherNumber + " = " + a2 + " = " + ((int)(a2-anotherNumber*Math.floor(a2/anotherNumber))));*/
					return (int)(a2-anotherNumber*Math.floor(a2/anotherNumber));
			}
			else
				//System.out.println("Inverse of " + (int)firstNumber + " mod " + (int)anotherNumber + " = " + a2);
				return (int)a2;
		}
		else {
			//return b2
			if (b2 < 0) {
				/*System.out.println("Inverse of " + (int)firstNumber + " mod " + 
					(int)anotherNumber + " = " + b2 + " = " + ((int)(b2-anotherNumber*Math.floor(b2/anotherNumber))));*/
				return (int)(b2-anotherNumber*Math.floor(b2/anotherNumber));
			}
			else
				//System.out.println("Inverse of " + (int)firstNumber + " mod " + (int)anotherNumber + " = " + b2);
			return (int)b2;
		}

		//System.out.println(n2 == a2*max+b2*min);
		
	} // end function findInverse

	/**
	 * prove tester is prime
	 * @param tester an integer
	 * @return
	 */
	private static void LehmanTest(int tester) {
		int runNumber, power = (tester-1)/2, prime = 0, composite = 0;
		for (runNumber = 1; runNumber < tester; runNumber++) {
			if (gcd(runNumber, tester) > 1)
				composite++;
			else if (Math.pow(runNumber, power)%tester == 1 || Math.pow(runNumber, power)%tester == tester-1)
				prime++;
			else
				composite++;	
		}

		System.out.println("Prime = " + prime + "\nComposite = " + composite);
	} // end function LehmanTest

	/**
	 *
	 * @param power
	 */
	private static void factorizationPower(int power) {
		int n = 1, powerStart = 1, remain;
		while (powerStart < power) {
			powerStart *= 2;
			n++;
		}
		powerStart /= 2;
		n--;
		setPower = new int[n];
		System.out.println("\nSize setPower = " +n);
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
			System.out.println("setPower[" + k + "] = " + setPower[k]);
		}
	} // end function factorizationPower

	private static void storagePower(int base, int modulate) {
		int n = (int)(Math.log(setPower[0])/Math.log(2))+1;
		System.out.println("\nSize storagePower = " + n);
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
			System.out.println("storagePower[" + k + "] = " + storagePower[k]);
		}
	} // end function storagePower

	// It's overflow should be multiply 2 numbers and modulate before continue multiply.~~
	
	private static void combinationTogether(int modulation) {
		long sum = 1;
		int realSize = 0;
		for (int i=0;i<setPower.length;i++) {
			if (setPower[i] != 0)
				realSize++;
		}
		System.out.println("Real size: " + realSize);
		for (int i=0;i<realSize;i++) {
			sum = sum*storagePower[(int)(Math.log(setPower[i])/Math.log(2))];
			System.out.println("\n------------\nsum = " + sum);
		}
		System.out.println("\n------------\nResult = " + sum%modulation);
	} // end function combinationTogether

	private static void fastExponential(int base, int power, int modulation) {
		factorizationPower(power);
		storagePower(base, modulation);
		combinationTogether(modulation);

	}



} // end class
