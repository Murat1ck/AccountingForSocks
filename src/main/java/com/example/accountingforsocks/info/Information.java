package com.example.accountingforsocks.info;

public class Information {
    private String name = "Сергей";
    private String nameProject = "Склад носков";
    private String date = "19.02.2023";
    private String description = "Создание веб-приложения для склада интернет-магазина носков.";

    @Override
    public String toString() {
        return "Information{" +
                "name='" + name + '\'' +
                ", nameProject='" + nameProject + '\'' +
                ", date='" + date + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}



