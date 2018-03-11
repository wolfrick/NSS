package aes;
import aes.Helpers;
import aes.Encryptor;
import java.util.Scanner;
public class Driver 
{
	static Helpers helper;
	static Encryptor encrypt;
	static Decryptor decrypt;
	public static void main(String[] args)
	{
		helper = new Helpers();
		String input="", key="";
		Scanner sc = new Scanner(System.in);
		int choice;
		
		printMenu();
		
		choice = sc.nextInt();
		switch(choice)
		{
			case 1: System.out.print("Please Enter the PlainText in Hexadecimal format:");
					sc.nextLine();
					input = sc.nextLine();
					System.out.println("Enter the key in hexadecimal format:");
					key = sc.nextLine();
					int length = key.length();
					if(length!=32)
					{
						System.out.println("Input key is not 128-bit");
						System.exit(0);
					}
					length = input.length();
					while(length%32!=0)
					{
						input = input+'0';
						length++;
					}
					input = helper.hexatoBits(input);
					length = input.length();
					key = helper.hexatoBits(key);
					String CipherText="";
					for(int i=0;i<length/128;i++)
					{
						encrypt = new Encryptor(input.substring(i*128, (i+1)*128), key);
						CipherText += encrypt.performEncryption();
					}
					//System.out.println("CipherText for corresponding PlainText is: "+helper.stringToHex(CipherText));
					break;
			
			case 2: System.out.print("Please Enter the CipherText in Hexadecimal format:");
					sc.nextLine();
					input = sc.nextLine();
					System.out.println("Enter the key in hexadecimal format:");
					key = sc.nextLine();
					length = key.length();
					if(length!=32)
					{
						System.out.println("Input key is not 128-bit");
						System.exit(0);
					}
					length = input.length();
					while(length%32!=0)
					{
						input = input+'0';
						length++;
					}
					input = helper.hexatoBits(input);
					length = input.length();
					key = helper.hexatoBits(key);
					String PlainText="";
					for(int i=0;i<length/128;i++)
					{
						decrypt = new Decryptor(input.substring(i*128, (i+1)*128), key);
						PlainText += decrypt.performDecryption();
					}
					//System.out.println("PlainText for corresponding CipherText is: "+helper.stringToHex(PlainText));
					break;
		}
	}
	
	public static void printMenu()
	{
		System.out.println("Menu");
		System.out.println("1. Hexadecimal PlainText");
		System.out.println("2. Hexadecimal CipherText");
		System.out.println("Please Enter your choice:");
	}
	
}
