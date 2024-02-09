package dao;

import model.entities.BlockedUsers;

import java.util.List;

public interface BlockedUserDao extends Dao<BlockedUsers> {
    boolean save(BlockedUsers blockedUser);
    int saveBlockedUser(BlockedUsers blockedUser);
    int deleteByBlockingAndBlockedUser(BlockedUsers blockedUser);
    BlockedUsers get(int id);
    List<BlockedUsers> getAll();
    boolean update(BlockedUsers blockedUser);
    boolean delete(BlockedUsers blockedUser);

    boolean FriendIsBlocked(BlockedUsers blockedUsers);

    List<BlockedUsers> getBlockedContact(BlockedUsers blockedUsers);
}