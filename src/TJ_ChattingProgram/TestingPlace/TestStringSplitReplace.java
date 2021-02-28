package TJ_ChattingProgram.TestingPlace;

import javafx.scene.paint.Color;

public class TestStringSplitReplace {
    public static void main(String[] args) {
        Color color = Color.BLUE;
        String coloredTextFormat = "##msg-color:"+color+"##";
        String inputStr = "아에이오우 으러러러러";
        String message = coloredTextFormat + inputStr;
        System.out.println("message : " + message );

        System.out.println("메세지 :" + extractMessagePart(message) );
        System.out.println("컬러 :" + extractColorPart(message) );
    }

    public static String extractMessagePart(String str){
        String[] strs = str.split("##");
        StringBuilder sb = new StringBuilder();
        if(strs.length > 2){
            for(int i = 2; i < strs.length; i++){
                sb.append(strs[i]);
            }
        }
        return sb.toString();
    }

    public static String extractColorPart(String str){
        String[] strs = str.split("##");
        return strs[1].split(":")[1];
    }
}
