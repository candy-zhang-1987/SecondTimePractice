package javaBase;

import java.util.ArrayList;

public class JavaBaseDemo {
    public static void main(String[] args){
/*for(int i=1;i<=5;i++){
    for(int j=0;j<5-i;j++){
        System.out.print(" ");
    }
    for(int j=0;j<i;j++){
        System.out.print("*");
    }
    System.out.println("");
}*/
/*//数组排序,升序
        int[] list1={1,3,9,5,8,4,4};
        ArrayList<Integer> list2=new ArrayList();
        list2.add(1);
        list2.add(3);
  for(int i=1;i< list1.length;i++){
      for(int j=0;j<list1.length-i;j++){
          if(list1[j]>list1[j+1]){
              int temp=list1[j];
              list1[j]=list1[j+1];
              list1[j+1]=temp;
          }
      }


  }
      for(int i=0;i<list1.length;i++){
          System.out.println(list1[i]);
      }*/
        //数组反转
        String str="China";
        String newStr="";
        int length=str.length();
        String s1="";
        for(int i =0;i<length;i++){
            Character temp;
            temp=str.charAt(length-i-1);
           String  temp2=temp.toString();
           newStr=newStr.concat(temp2);
           // System.out.println(newStr);
            //方法二：
            char s2=str.charAt(length-i-1);
            s1+=s2;
            System.out.println(s1);


        }
        System.out.println(newStr);
    }
}
