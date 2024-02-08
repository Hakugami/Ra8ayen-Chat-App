package model.entities;

public class BlockedUsers {
    int blockId;
    int blockingUserId;
    int blockedUserId;
    String blockDate;

    public BlockedUsers(int blockId, int blockingUserId, int blockedUserId, String blockDate) {
        this.blockId = blockId;
        this.blockingUserId = blockingUserId;
        this.blockedUserId = blockedUserId;
        this.blockDate = blockDate;
    }

    public BlockedUsers() {
    }

    public int getBlockId() {
        return blockId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }

    public int getBlockingUserId() {
        return blockingUserId;
    }

    public void setBlockingUserId(int blockingUserId) {
        this.blockingUserId = blockingUserId;
    }

    public int getBlockedUserId() {
        return blockedUserId;
    }

    public void setBlockedUserId(int blockedUserId) {
        this.blockedUserId = blockedUserId;
    }

    public String getBlockDate() {
        return blockDate;
    }

    public void setBlockDate(String blockDate) {
        this.blockDate = blockDate;
    }

    @Override
    public String toString() {
        return "BlockedUser{" +
                "blockId=" + blockId +
                ", blockingUserId=" + blockingUserId +
                ", blockedUserId=" + blockedUserId +
                ", blockDate='" + blockDate + '\'' +
                '}';
    }
}