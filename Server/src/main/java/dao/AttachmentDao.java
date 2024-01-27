package dao;

import model.entities.Attachment;

import java.util.List;

public interface AttachmentDao extends Dao<Attachment> {
    boolean save(Attachment attachment); //Create
    Attachment get(int id); //Read
    List<Attachment> getAll(); //Read all
    boolean update(Attachment attachment); //Update
    boolean delete(Attachment attachment); //Delete
}