package com.influans.sp.service;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author hazem
 */
@Ignore
public class UserServiceTest {
    /**
     * @verifies return empty list if no user is connected on this session
     * @see UserService#listUsers(String)
     */
    @Test
    public void listUsers_shouldReturnEmptyListIfNoUserIsConnectedOnThisSession() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies throw an error if session does not exist
     * @see UserService#listUsers(String)
     */
    @Test
    public void listUsers_shouldThrowAnErrorIfSessionDoesNotExist() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies return users list if session exists
     * @see UserService#listUsers(String)
     */
    @Test
    public void listUsers_shouldReturnUsersListIfSessionExists() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies not return disconnected users
     * @see UserService#listUsers(String)
     */
    @Test
    public void listUsers_shouldNotReturnDisconnectedUsers() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies create new user if sessionId and username are valid
     * @see UserService#connectUser(com.influans.sp.dto.UserDto)
     */
    @Test
    public void connectUser_shouldCreateNewUserIfSessionIdAndUsernameAreValid() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies not create an new user if username already exists for the given sessionId
     * @see UserService#connectUser(com.influans.sp.dto.UserDto)
     */
    @Test
    public void connectUser_shouldNotCreateAnNewUserIfUsernameAlreadyExistsForTheGivenSessionId() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies return correct is isAdmin value
     * @see UserService#connectUser(com.influans.sp.dto.UserDto)
     */
    @Test
    public void connectUser_shouldReturnCorrectIsIsAdminValue() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies throw and error if sessionId is empty
     * @see UserService#connectUser(com.influans.sp.dto.UserDto)
     */
    @Test
    public void connectUser_shouldThrowAndErrorIfSessionIdIsEmpty() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies throw and error if username is empty
     * @see UserService#connectUser(com.influans.sp.dto.UserDto)
     */
    @Test
    public void connectUser_shouldThrowAndErrorIfUsernameIsEmpty() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies throw and error if sessionId is not valid
     * @see UserService#connectUser(com.influans.sp.dto.UserDto)
     */
    @Test
    public void connectUser_shouldThrowAndErrorIfSessionIdIsNotValid() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies reconnect user if it was previously disconnected
     * @see UserService#connectUser(com.influans.sp.dto.UserDto)
     */
    @Test
    public void connectUser_shouldReconnectUserIfItWasPreviouslyDisconnected() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies throw an error if user was not found
     * @see UserService#disconnectUser(com.influans.sp.dto.UserDto)
     */
    @Test
    public void disconnectUser_shouldThrowAnErrorIfUserWasNotFound() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies set user as disconnected
     * @see UserService#disconnectUser(com.influans.sp.dto.UserDto)
     */
    @Test
    public void disconnectUser_shouldSetUserAsDisconnected() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies throw an error if sessions is not found
     * @see UserService#disconnectUser(com.influans.sp.dto.UserDto)
     */
    @Test
    public void disconnectUser_shouldThrowAnErrorIfSessionsIsNotFound() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }
}
