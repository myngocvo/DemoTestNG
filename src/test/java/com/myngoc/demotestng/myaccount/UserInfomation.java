package com.myngoc.demotestng.myaccount;

import com.myngoc.demotestng.BaseTest;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class UserInfomation extends BaseTest {
    @BeforeTest
    public void setUpDriver() {
        setUp();
        login();
    }

    @AfterTest
    public void tearDownDriver() {
        tearDown();
    }

    public void verifyUserInFo() {

    }

    @Test
    public void updateUserInFo() {
        
    }

    public void updateAdressInFo() {

    }

    public void changePassword() {

    }

    public void logout() {

    }
}
