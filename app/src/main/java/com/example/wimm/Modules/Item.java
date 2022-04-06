package com.example.wimm.Modules;

public class Item {

 String name;
 String price;

    public Item(String name, String price)
    {
        this.name = name;
        this.price = price;
    }


    public String GetName()
    {
        return name;
    }

    public String GetPrice()
    {
        return price;
    }

    public void SetPrice(String price)
    {
        this.price = price;
    }


}
