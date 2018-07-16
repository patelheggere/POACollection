package com.patelheggere.poacollection.models;

public class POIDetails {
    String name;
    String category;
    String subCat;
    String bName;
    String bNumber;
    String noFloor;
    String brand;
    String landMark;
    String street;
    String locality;
    String pincode;
    String comment;
    String mLattitude, mLonggitude;
    String mPOINumber;
    String mPhoneNumberr;
    String mPersonName;
    String mDate;

    public POIDetails()
    {

    }

    public POIDetails(String mPhoneNumberr, String mDate,String mPersonName, String name, String category, String subCat, String bName, String bNumber, String noFloor, String brand, String landMark, String street, String locality, String pincode, String comment, String mLattitude, String mLonggitude, String mPOINumber) {
        this.mPhoneNumberr = mPhoneNumberr;
        this.mPersonName = mPersonName;
        this.name = name;
        this.category = category;
        this.subCat = subCat;
        this.bName = bName;
        this.bNumber = bNumber;
        this.noFloor = noFloor;
        this.brand = brand;
        this.landMark = landMark;
        this.street = street;
        this.locality = locality;
        this.pincode = pincode;
        this.comment = comment;
        this.mLattitude = mLattitude;
        this.mLonggitude = mLonggitude;
        this.mPOINumber = mPOINumber;
        this.mDate = mDate;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmPhoneNumberr() {
        return mPhoneNumberr;
    }

    public void setmPhoneNumberr(String mPhoneNumberr) {
        this.mPhoneNumberr = mPhoneNumberr;
    }

    public String getmPersonName() {
        return mPersonName;
    }

    public void setmPersonName(String mPersonName) {
        this.mPersonName = mPersonName;
    }

    public String getmPOINumber() {
        return mPOINumber;
    }

    public void setmPOINumber(String mPOINumber) {
        this.mPOINumber = mPOINumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCat() {
        return subCat;
    }

    public void setSubCat(String subCat) {
        this.subCat = subCat;
    }

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public String getbNumber() {
        return bNumber;
    }

    public void setbNumber(String bNumber) {
        this.bNumber = bNumber;
    }

    public String getNoFloor() {
        return noFloor;
    }

    public void setNoFloor(String noFloor) {
        this.noFloor = noFloor;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getLandMark() {
        return landMark;
    }

    public void setLandMark(String landMark) {
        this.landMark = landMark;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getmLattitude() {
        return mLattitude;
    }

    public void setmLattitude(String mLattitude) {
        this.mLattitude = mLattitude;
    }

    public String getmLonggitude() {
        return mLonggitude;
    }

    public void setmLonggitude(String mLonggitude) {
        this.mLonggitude = mLonggitude;
    }
}
