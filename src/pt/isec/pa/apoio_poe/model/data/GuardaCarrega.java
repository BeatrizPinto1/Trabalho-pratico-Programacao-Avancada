package pt.isec.pa.apoio_poe.model.data;

import pt.isec.pa.apoio_poe.model.fsm.SistemState;
import pt.isec.pa.apoio_poe.model.memento.CareTaker;

import java.io.*;

public class GuardaCarrega {

    public static void guardar(Sistema sistema, CareTaker careTaker, SistemState estado, String nome) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("saves/"+nome+".bin"));
            Object[] objectsnew = {sistema, careTaker, estado};
            oos.writeObject(objectsnew);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object[] carrega(String nome) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("saves/"+nome));
            Object[] objectsnew = (Object[]) ois.readObject();
            ois.close();
            return objectsnew;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
