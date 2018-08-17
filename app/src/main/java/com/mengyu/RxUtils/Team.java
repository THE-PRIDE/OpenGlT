package com.mengyu.RxUtils;

import java.util.List;

/**
 * Created by LMY on 18/8/17.
 * RX测试 实体类1
 */

public class Team {

    private int No;
    private String TeamName;

    private Person person;

    private List<Person> listPerson;

    public List<Person> getListPerson() {
        return listPerson;
    }

    public void setListPerson(List<Person> listPerson) {
        this.listPerson = listPerson;
    }

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
