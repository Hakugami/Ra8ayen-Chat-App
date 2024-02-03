package model.entities;

public class ChatParticipant {
    private int chatId;
    private int participantUserId;
    private String participantStartDate;

    public ChatParticipant(int chatId, int participantUserId, String participantStartDate) {
        this.chatId = chatId;
        this.participantUserId = participantUserId;
        this.participantStartDate = participantStartDate;
    }

    public ChatParticipant(int chatId, int participantUserId) {
        this.chatId = chatId;
        this.participantUserId = participantUserId;
    }

    public ChatParticipant() {
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public int getParticipantUserId() {
        return participantUserId;
    }

    public void setParticipantUserId(int participantUserId) {
        this.participantUserId = participantUserId;
    }

    public String getParticipantStartDate() {
        return participantStartDate;
    }

    public void setParticipantStartDate(String participantStartDate) {
        this.participantStartDate = participantStartDate;
    }

    @Override
    public String toString() {
        return "ChatParticipant{" +
                "chatId=" + chatId +
                ", participantId=" + participantUserId +
                ", participantStartDate='" + participantStartDate + '\'' +
                '}';
    }
}
