package model.persistence;

import model.*;

public interface ICommand
{
    void execute();
    void undo();
}
