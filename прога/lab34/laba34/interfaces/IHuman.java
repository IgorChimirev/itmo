package interfaces;

import Objects.Entity;
public interface IHuman {
    public void DrivingSomewhere(Entity first, Entity second);
    public void Eat(Entity first,Entity second);
    public void HorseWalk(Entity first, Entity second);
    public void performAction(Entity first,Entity second);
    //public void Fight(Entity first,  Entity second);
}
