package test;

public class Fi_per {

    private int[] number;

    public Fi_per(int[] number) {
        this.number = number;
    }

    public void toResult(int[] number){
        this.number = number;
        //第0位分别和1,2,3,4绑定
        for (int j=1;j<=4;j++) {
            String tempString = String.valueOf(number[0]) + String.valueOf(number[j]);
            Integer tempInt = Integer.valueOf(tempString);
            int[] pro = new int[4];
            pro[0] = tempInt;
            if(j==1) {
                pro[1] = number[2];
                pro[2] = number[3];
                pro[3] = number[4];
            }
            if(j==2) {
                pro[1] = number[1];
                pro[2] = number[3];
                pro[3] = number[4];
            }

            if(j==3) {
                pro[1] = number[1];
                pro[2] = number[2];
                pro[3] = number[4];
            }
            if(j==4) {
                pro[1] = number[1];
                pro[2] = number[2];
                pro[3] = number[3];
            }

            Fourth_per third_per = new Fourth_per(pro);
            third_per.toResult(pro);

        }
    }
}
