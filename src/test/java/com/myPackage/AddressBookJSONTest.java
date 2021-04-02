package com.myPackage;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AddressBookJSONTest {

    @Before
    public void setup(){
        RestAssured.baseURI="http://localhost";
        RestAssured.port=4000;
    }

    public AddressBookData[] getEmplist(){
        Response response=RestAssured.get("/contacts");
        System.out.println("Data in json is: \n"+response.asString());
        AddressBookData[] restAssureBookData=new Gson().fromJson(response.asString(),AddressBookData[].class);
        return restAssureBookData;
    }

    public Response addContactToJsonServer(AddressBookData restAssureBookData){
        String contac=new Gson().toJson(restAssureBookData);
        RequestSpecification requestSpecification=RestAssured.given();
        requestSpecification.header("Content-Type","application/json");
        requestSpecification.body(contac);
        return requestSpecification.post("/contacts");
    }

    @Test
    public void givenContactData_shouldRetrieve_ServerData(){
        AddressBookData[] restAssureBookData=getEmplist();
        System.out.println(restAssureBookData);
        Assert.assertEquals(5,restAssureBookData.length);
    }

    @Test
    public void whenNewContact_isAdded_Sholdreturn201ResponseCode(){
        AddressBookData[] jsonServerBookData=getEmplist();

        AddressBookData jsonServerBookData1=new AddressBookData("5","Manish","Deva","Parampur","Malshes","Bihar","40091191","998292981","manish@gmail.com","2018-05-16");
        Response response=addContactToJsonServer(jsonServerBookData1);
        int statusCode= response.statusCode();

        Assert.assertEquals(201,statusCode);
    }

}
