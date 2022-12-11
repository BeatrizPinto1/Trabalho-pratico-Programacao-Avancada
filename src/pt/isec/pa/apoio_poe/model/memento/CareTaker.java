package pt.isec.pa.apoio_poe.model.memento;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;

public class CareTaker implements Serializable {
    private final IMementoOriginator originator;
    private Deque<Memento> stackUndo = new ArrayDeque<>();
    private Deque<Memento> stackRedo = new ArrayDeque<>();

    public CareTaker(IMementoOriginator org) {
        this.originator = org;
    }

    public void gravaMemento() {
        stackRedo.clear();
        try{
            stackUndo.push(originator.getMemento());
        } catch(IOException ex) {
            System.err.println("gravaMemento: " + ex);
            stackUndo.clear();
            stackRedo.clear();
        }
    }

    public void undo() {
        if (stackUndo.isEmpty()) {
            return;
        }
        try {
            Memento atual = originator.getMemento();
            stackRedo.push(atual);
            Memento anterior = stackUndo.pop();
            originator.setMemento(anterior);
        } catch(IOException | ClassNotFoundException ex) {
            System.err.println("undo: " + ex);
            stackUndo.clear();
            stackRedo.clear();
        }
    }

    public void redo(){
        if (stackRedo.isEmpty()) {
            return;
        }
        try {
            Memento proximo = stackRedo.pop();
            stackUndo.push(originator.getMemento());
            originator.setMemento(proximo);
        } catch(IOException | ClassNotFoundException ex) {
            System.err.println("undo: " + ex);
            stackUndo.clear();
            stackRedo.clear();
        }
    }

    public void reiniciar(){
        int tamanho = stackUndo.size();

        for(int i=0; i<tamanho;i++){
            undo();
        }
    }

    /*public void clear() {
        this.stackHistorico.clear();
        this.stackRedo.clear();
    }*/
}
