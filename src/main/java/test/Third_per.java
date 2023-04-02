package test;

public class Third_per {

    private int[] number;

    public Third_per(int[] number) {
        this.number = number;
    }

    public void toEnd(){
        System.out.print("[");
        for (int i=0;i<this.number.length;i++) {
            System.out.print(this.number[i]);
        }
        System.out.println("]");
    }

    public int[] result(){
        int tmpe = this.number[1];
        this.number[1] = this.number[2];
        this.number[2] = tmpe;

        return this.number;
    }

    public void toResult(){
        this.toEnd();
        this.result();
        this.toEnd();
    }

}
