package pt.isec.pa.apoio_poe.model.memento;

import java.io.IOException;

public interface IMementoOriginator {
    Memento getMemento() throws IOException;

    void setMemento(Memento var1) throws IOException, ClassNotFoundException;
}
