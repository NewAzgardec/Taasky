package utils;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XMLHelper {
    private static final String PATH = "C:\\Users\\Masha\\IntelliJIDEAProjects\\AipLaba\\data.xml";

    public static void writeToXML(List<?> objects){
        try(FileOutputStream fileOS = new FileOutputStream(PATH)){
            XMLEncoder encoder=null;
            encoder=new XMLEncoder(fileOS);
            encoder.writeObject(objects);
            encoder.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    public static List<?> readFromXML(){
        try(FileInputStream fileIS = new FileInputStream(PATH)){
            XMLDecoder decoder = new XMLDecoder(fileIS);
            List<?> decodedList = (ArrayList<?>) decoder.readObject();
            decoder.close();
            return decodedList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
