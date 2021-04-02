package com.myPackage;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

public class AddressBookJSONTest {

    @Before
    public void setup(){
        RestAssured.baseURI="http://localhost";
        RestAssured.port=4000;
    }

    public AddressBookData[] getContactlist(){
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
        AddressBookData[] restAssureBookData=getContactlist();
        System.out.println(restAssureBookData);
        Assert.assertEquals(4,restAssureBookData.length);
    }

    @Test
    public void whenNewContact_isAdded_Sholdreturn201ResponseCode(){
        AddressBookData[] jsonServerBookData=getContactlist();

        AddressBookData jsonServerBookData1=new AddressBookData("5","Manish","Deva","Parampur","Malshes","Bihar","40091191","998292981","manish@gmail.com","2018-05-16");
        Response response=addContactToJsonServer(jsonServerBookData1);
        int statusCode= response.statusCode();

        Assert.assertEquals(201,statusCode);
    }

    @Test
    public void givenNewData_Should_Retun200ResponseCode() throws SQLException {
        AddressBookData[] serverEmpData=getContactlist();

        RequestSpecification requestSpecification=RestAssured.given();
        requestSpecification.header("Content-Type","application/json");
        requestSpecification.body("{\"firstname\":\"Manasi\",\"lastname\":\"Patil\",\"address\":\"Bagha\",\"city\":\"South\",\"state\":\"Goa\",\"zip\":\"400919\",\"phonenumber\":\"88726616\",\"email\":\"manasi@gmail.com\",\"date\":\"2019-03-07\"}");
        Response response=requestSpecification.put("/contacts/update/2");

        int statusCode=response.getStatusCode();
        Assert.assertEquals(200,statusCode);
    }

    @Test
    public void givenDelete_Command_ShouldRetun200ResponseCode() throws SQLException {
        AddressBookData[] serverBookData=getContactlist();
        String empJson=new Gson().toJson(serverBookData);

        RequestSpecification requestSpecification=RestAssured.given();
        requestSpecification.header("Content-Type","application/json");
        requestSpecification.body(empJson);
        Response response=requestSpecification.delete("/contacts/delete/5");

        int statusCode=response.getStatusCode();
        Assert.assertEquals(200,statusCode);
    }
}
