package dto.requests;

import java.io.Serializable;

public class GetContactsRequest implements Serializable {
    private int IdUser;

    public int getIdUser() {
        return IdUser;
    }

    public void setIdUser(int idUser) {
        IdUser = idUser;
    }
    public GetContactsRequest(){

    }
    public GetContactsRequest(int idUser){
        this.IdUser = idUser;
    }
}
