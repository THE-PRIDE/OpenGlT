package com.mengyu.RxUtils;

/**
 * Created by LMY on 18/8/17.
 * RX测试 实体类1
 */

public class Team {

    private int No;
    private String TeamName;
    private Person person;

    public int getNo() {
        return No;
    }

    public void setNo(int no) {
        No = no;
    }

    public String getTeamName() {
        return TeamName;
    }

    public void setTeamName(String teamName) {
        TeamName = teamName;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
