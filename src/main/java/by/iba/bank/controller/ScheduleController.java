package by.iba.bank.controller;

import by.iba.bank.model.entity.Loan;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.converter.LocalDateStringConverter;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

public class ScheduleController implements Initializable {


    @FXML
    private TableColumn<Loan,BigDecimal> balance;

    @FXML
    private TableColumn<Loan, String> date;

    @FXML
    private TableColumn<Loan, BigDecimal> monthPayment;

    @FXML
    private TableView<Loan> table;
    private Loan loan;



    public void uploadTable(Loan temploan){
        loan = temploan;
        table.getItems().clear();
        date.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        monthPayment.setCellValueFactory( new PropertyValueFactory<>("monthlyPayment"));
        balance.setCellValueFactory(new PropertyValueFactory<>("amount"));

        BigDecimal tempAmount = loan.getAmount().multiply(BigDecimal.valueOf(1).add(loan.getInterestRate()).add(BigDecimal.valueOf(0.01)));
        LocalDate localDate = LocalDate.parse(loan.getIssueDate());

        //table.getItems().add(loan);


        for(int i = 0; i <= loan.getTerm()*12; i++){
            Loan loanForTable = new Loan();
            loanForTable.setAmount(tempAmount);
            loanForTable.setIssueDate(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
            loanForTable.setMonthlyPayment(loan.getMonthlyPayment());

            table.getItems().add(loanForTable);

            tempAmount = tempAmount.subtract(loan.getMonthlyPayment());
            localDate = localDate.plusMonths(1);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       // uploadTable();
    }
}
