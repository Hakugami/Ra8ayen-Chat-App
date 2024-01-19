package dto.requests;

import java.io.Serializable;

public class GetContactsRequest implements Serializable {
    private String IdUser;

    public String getIdUser() {
        return IdUser;
    }

    public void setIdUser(String idUser) {
        IdUser = idUser;
    }
    GetContactsRequest(){

    }
}
