package test;

public class Test {
    public static void main(String[] args) {
        int[] number = new int[]{1, 2, 3, 4, 5};
        //Third_per third_per = new Third_per(number);
        //third_per.toResult(number);
        //Fourth_per fourth_per = new Fourth_per(number);
        //fourth_per.toResult(number);
        Fi_per fi_per = new Fi_per(number);
        fi_per.toResult(number);
        int m = number.length;
        int n = 5;
        //int quantity = factorial(m)/m/factorial(m-n);
        //System.out.println("ͨ���󲻿���Բ���еĹ�ʽ��֪����Ԫ���ϵĲ�����Բ���и���Ϊ�� " + quantity);
    }


    //�׳˺���
    public static int factorial(int a){
        if(a==1) return 1;
        else return a*factorial(a-1);
    }
}
