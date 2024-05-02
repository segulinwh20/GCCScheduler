package com.classes;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import com.amdelamar.jhash.Hash;
import com.amdelamar.jhash.exception.InvalidHashException;

public class Account {
    Scanner input = new Scanner(System.in);
    private String password;
    public void createAccount(Student stud) {
        String username = stud.getFirst() + stud.getLast();
        if(userExists(username)){
            System.out.println("Username already exists");
            System.out.println("Do you want to reset your password? (Y/N)");
            String answer = input.nextLine();
            if(answer.equalsIgnoreCase("Y")){
                resetPassword(username);
                return;
            } else if (answer.equalsIgnoreCase("N")) {
                return;
            }
        }
        System.out.println("Your username is " + username);
        this.password = hashPassword(verifyPassword());
        try (FileWriter fr = new FileWriter("Accounts/"+username+".txt")) {
            fr.write(this.password);
        } catch (IOException e){
            System.out.println("Oops");
        }
    }

    private String verifyPassword() {
        while(true) {
            System.out.println("Please enter your password");
            String pass = input.nextLine();
            System.out.println("Please reenter your password");
            if(pass.equals(input.nextLine())){
                return pass;
            } else {
                System.out.println("Passwords do not match, try again");
            }
        }
    }

    private String hashPassword(String p){
        Random rand = new Random();
        int salt = rand.nextInt(50);
        return Hash.password(p.toCharArray()).saltLength(salt).create();
    }

    public boolean Login(String username, String password){
        File file = new File("Accounts/"+username+".txt");
        if(file.exists()){
            try(Scanner inFile = new Scanner(file)){
                return Hash.password(password.toCharArray()).verify(inFile.nextLine());
            } catch (FileNotFoundException | InvalidHashException e){
                System.out.println("Why no file");
            }
        } else {
            System.out.println("User does not exist");
        }
        return false;
    }


    public void resetPassword(String username) {
        File file = new File("Accounts/"+username+".txt");
        if(file.exists()){
            this.password = hashPassword(verifyPassword());
            try (FileWriter fr = new FileWriter(file.getPath())) {
                fr.write(this.password);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean userExists(String username) {
        File file = new File("Accounts/"+username+".txt");
        return file.exists();
    }
}
