package edu.cczu.table.model;

public interface Course {
    int getId();
    String getName();
    String getLocation();

    int getDay();
    int getBeginBlock();
    int getLength();

    void setDay(int day);
    void setBeginBlock(int beginBlock);
    void setLength(int length);
}
