package com.example.ktsitsa;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredients implements Parcelable {
    private String ID;
    private String name;
    private String category;
    private Boolean IsSelected;

    //The empty constuctor uses for FireBase demands
    public Ingredients(){}
    //this class represents ingredient by id,name,category.
    public Ingredients(String ID, String name, String category)  {
        this.ID = ID;
        this.name = name;
        this.category = category;
        this.IsSelected = false;
    }

    protected Ingredients(Parcel in) {
        ID = in.readString();
        name = in.readString();
        category = in.readString();
        byte tmpIsSelected = in.readByte();
        IsSelected = tmpIsSelected == 0 ? null : tmpIsSelected == 1;
    }

    public static final Creator<Ingredients> CREATOR = new Creator<Ingredients>() {
        @Override
        public Ingredients createFromParcel(Parcel in) {
            return new Ingredients(in);
        }

        @Override
        public Ingredients[] newArray(int size) {
            return new Ingredients[size];
        }
    };

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public Boolean isSelected() {
        return IsSelected;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSelected(Boolean selected) {
        IsSelected = selected;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(name);
        dest.writeString(category);
        dest.writeByte((byte) (IsSelected == null ? 0 : IsSelected ? 1 : 2));
    }
}
