package dto.responses;

import java.io.Serializable;
import java.util.List;

public class CreateGroupChatResponse implements Serializable {
    private List<String> responses;
    private boolean isCreated;

    public CreateGroupChatResponse(List<String> responses, boolean isCreated) {
        this.responses = responses;
        this.isCreated = isCreated;
    }

    public CreateGroupChatResponse() {
    }

    public List<String> getResponses() {
        return responses;
    }

    public void setResponses(List<String> responses) {
        this.responses = responses;
    }

    public boolean isCreated() {
        return isCreated;
    }

    public void setCreated(boolean created) {
        isCreated = created;
    }

    @Override
    public String toString() {
        return "CreateGroupChatResponse{" +
                "responses=" + responses +
                ", isCreated=" + isCreated +
                '}';
    }
}
