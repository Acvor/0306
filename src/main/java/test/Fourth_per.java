package test;

public class Fourth_per {
    private int[] number;

    public Fourth_per(int[] number) {
        this.number = number;
    }
    public void toResult(int[] number){
        this.number = number;
        //第0位分别和1,2,3绑定
        for (int j=1;j<=3;j++) {
            String tempString = String.valueOf(number[0]) + String.valueOf(number[j]);
            Integer tempInt = Integer.valueOf(tempString);
            int[] pro = new int[3];
            pro[0] = tempInt;
            if(j==1) {
                pro[1] = number[2];
                pro[2] = number[3];
            }
            if(j==2) {
                pro[1] = number[1];
                pro[2] = number[3];
            }

            if(j==3) {
                pro[1] = number[1];
                pro[2] = number[2];
            }
            Third_per third_per = new Third_per(pro);
            third_per.toResult();
        }
    }

}
