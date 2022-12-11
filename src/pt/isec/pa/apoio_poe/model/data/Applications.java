package pt.isec.pa.apoio_poe.model.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Applications {
    private Map<Students, Proposals> mapApplications;

    public Applications(List<Proposals> proposals) {
        this.mapApplications = new HashMap<>();
    }

    public boolean addAplications(Students student, Proposals proposal) {
        if(proposal == null || student == null)
            return false;

        /*if(mapBooks.containsValue(book))
            return -1;*/
        if(mapApplications.containsKey(student))
            return false;

        mapApplications.put(student, proposal);
        return true;
    }

    public Proposals findApllication(Students student) {
        if(mapApplications == null || mapApplications.size() == 0)
            return null;

        //for(Book b : mapBooks.values())
        //  if(b.getId() == bookId)
        //    return b;
        return mapApplications.get(student);
    }

    public boolean removeBook(Students student)
    {
        if(mapApplications == null)
            return false;

        return mapApplications.remove(student) != null;
    }

    public Map<Students, Proposals> getMapApplications() {
        return mapApplications;
    }

    public void setMapApplications(Map<Students, Proposals> mapApplications) {
        this.mapApplications = mapApplications;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Proposals proposal : mapApplications.values()){
            sb.append(proposal.toString());
        }
        return sb.toString();
    }

    //TODO:listas de alunos com autopropostas e autopropostas de alunos nao é o mm?

}
