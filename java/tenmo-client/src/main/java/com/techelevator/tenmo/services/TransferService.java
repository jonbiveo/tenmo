package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Scanner;

public class TransferService {

    private String BASE_URL;
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser currentUser;

    public TransferService(String url, AuthenticatedUser currentUser) {
        this.BASE_URL = url;
        this.currentUser = currentUser;
    }

    public Transfer[] transferList() {
        Transfer [] result = null;
        try {
            result = restTemplate.exchange(BASE_URL + "account/transfers/" + currentUser.getUser().getId(), HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
            System.out.println("-------------------------------------------\n" +
                    "Transfers\n" +
                    "ID          From/To                 Amount\n" +
                    "-------------------------------------------\n");
            String toOrFrom = "";
            String name = "";
            for (Transfer transfer : result) {
                if (currentUser.getUser().getId() == transfer.getAccountTo()) {
                    toOrFrom = "To: ";
                    name = transfer.getUserFrom();
                } else {
                    toOrFrom = "From: ";
                    name = transfer.getUserTo();
                }
                System.out.println(transfer.getTransferId() +"\t\t" + toOrFrom + name + "\t\t$" + transfer.getAmount());
            }
            System.out.println("-------------------------------------------\n" +
                    "Please enter transfer ID to view details (0 to cancel): ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (Integer.parseInt(input) != 0) {
                boolean foundTransferId = false;
                for (Transfer transfer : result) {
                    if (Integer.parseInt(input) == transfer.getTransferId()) {
                        Transfer tempTransfer = restTemplate.exchange(BASE_URL + "transfers/" + transfer.getTransferId(), HttpMethod.GET, makeAuthEntity(), Transfer.class).getBody();
                        foundTransferId = true;
                        System.out.println("--------------------------------------------\n" +
                                "Transfer Details\n" +
                                "--------------------------------------------\n" +
                                " ID: " + tempTransfer.getTransferId() + "\n" +
                                " From: " + tempTransfer.getUserFrom() + "\n" +
                                " To: " + tempTransfer.getUserTo() + "\n" +
                                " Type: " + tempTransfer.getTransferType() + "\n" +
                                " Status: " + tempTransfer.getTransferStatus() + "\n" +
                                " Amount: " + tempTransfer.getAmount());
                    }
                }
                if (!foundTransferId) {
                    System.out.println("Invalid transfer ID!");
                }
            }
        } catch (Exception e) {
            System.out.println("Oh no! All the TEnmo gnomes took your money!");
        }
        return result;
    }

    public void sendBucks() {
        listUsersForTransfer();
        enterUserIDAndAmount();
    }

    public void listUsersForTransfer() {
        User[] users = null;
        try {
            users = restTemplate.exchange(BASE_URL + "listusers", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
            System.out.println("-------------------------------------------\n" +
                    "Users\n" +
                    "ID\t\t\tName\n" +
                    "-------------------------------------------");
            for (User user : users) {
                if (user.getId() != currentUser.getUser().getId()) {
                    System.out.println(user.getId() + "\t\t" + user.getUsername());
                }
            }
        } catch (Exception e) {
            System.out.println("Bad input.");
        }
    }

    public void enterUserIDAndAmount() {
        try {
            Scanner scanner = new Scanner(System.in);
            Transfer transfer = new Transfer();
            System.out.println("-------------------------------------------\n" +
                    "Enter ID of user you are sending to (0 to cancel): ");
            transfer.setAccountTo(Integer.parseInt(scanner.nextLine()) + 1000);
            transfer.setAccountFrom(currentUser.getUser().getId() + 1000);

            if (transfer.getAccountTo() != 0) {
                System.out.println("Enter amount: ");
                try {
                    transfer.setAmount(new BigDecimal(Double.parseDouble(scanner.nextLine())));
                } catch (NumberFormatException e) {
                    System.out.println("Error when entering amount.");
                }
                Transfer transfer1 = restTemplate.exchange(BASE_URL + "transfer", HttpMethod.POST, makeTransferEntity(transfer), Transfer.class).getBody();
                System.out.println("Transfer Successful!!");
            }
        } catch (RestClientResponseException ex) {
            System.out.println(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
        } catch (RestClientException e) {
            System.out.println("Bad input");
        }
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        return entity;
    }

    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }
}
