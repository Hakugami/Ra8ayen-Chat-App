package dao;

import model.entities.Attachment;

import java.util.List;

public interface AttachmentDao extends Dao<Attachment> {
    void save(Attachment attachment); //Create
    Attachment get(int id); //Read
    List<Attachment> getAll(); //Read all
    void update(Attachment attachment); //Update
    void delete(Attachment attachment); //Delete
}