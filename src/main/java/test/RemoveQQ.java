package test;

import java.util.*;

public class RemoveQQ{
    public static void main(String[] args) throws Exception{
        String[] PrimalQQnumber = new String[]{"1231561","89571806","151235","76824338","89571806","121311","89571806","13211","76824338","123456","76824338","123456","89571806","12344","76824338","12344","76824338","112345","123456","1123344","7677471","89571806","1475639","76824338","89571806","1805385443","18117197","774768204","76824338","89571806","76824338","10172214","1345759","75776909","757747069"};
        ArrayList<String> QQnumber = new ArrayList();
        for(String i : PrimalQQnumber)
            QQnumber.add(i);

        Object[] qq = RemoveQQ.repeat(QQnumber).toArray();
        System.out.println(Arrays.toString(qq));
        System.out.println(qq.length);

    }

        public static ArrayList repeat(ArrayList list){
            ArrayList<String> nlist = new ArrayList<>();
            for(Object o : list){
                //newList中不包括该元素,那么就将该元素放进去
                if (!nlist.contains(o))
                    nlist.add((String) o);
            }

            return nlist;
        }


}