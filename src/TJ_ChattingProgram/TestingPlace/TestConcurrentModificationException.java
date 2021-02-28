package TJ_ChattingProgram.TestingPlace;

import java.util.List;
import java.util.Vector;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
* @package : TJ_ChattingProgram
* @name : TestConcurrentModificationException.java
* @date : 2021-02-26 오후 1:48
* @author : alsdn
* @usage : 향상된 for 루프 테스트, 루프 내에서 컬렉션에 조작을 가할경우 예외가 발생한다.
**/
public class TestConcurrentModificationException {
    public static void main(String[] args) {
        List<String> list = new Vector<String>();
        list.add("123");
        list.add("456");
        list.add("789");

        List<String> filteredList = list.stream().filter(
                str-> {
                    System.out.println(str);
                    if (str.equals("123")) {
                        list.remove(str);
                        return false;
                    }
                    return true;
                }
        ).collect(Collectors.toList());


//        for(var s : list){
//            System.out.println(s);
//            list.remove(s);
//        }


    }
}
