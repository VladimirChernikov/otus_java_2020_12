package ru.otus.crm.model;

import ru.otus.crm.model.annotation.Id;

public class Manager {
    @Id
    public Long no;
    public String label;

    public Manager() {
    }

    public Manager(String label) {
        this.label = label;
    }

    public Manager(Long no, String label) {
        this.no = no;
        this.label = label;
    }

    public Long getNo() {
        return no;
    }

    public void setNo(Long no) {
        this.no = no;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "no=" + no +
                ", label='" + label + '\'' +
                '}';
    }
}
