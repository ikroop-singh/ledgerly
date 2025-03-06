package com.ledgerly.app.expense_tracker.scheduler;

import com.ledgerly.app.expense_tracker.entity.Transaction;
import com.ledgerly.app.expense_tracker.entity.User;
import com.ledgerly.app.expense_tracker.service.EmailService;
import com.ledgerly.app.expense_tracker.service.transaction.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@AllArgsConstructor
@Component
public class MonthlySummaryMail {
    private EmailService emailService;

    private TemplateEngine templateEngine;

    private TransactionService transactionService;

    @Scheduled(cron = "0 0 9 1 * *")
    public void sendMonthlySummary(){
        System.out.println("ran cron job");
        List<Transaction> transactions=transactionService.fetchMonthlyTransactions();

        Month month= LocalDate.now().minusMonths(1).getMonth();

        Map<User, Map<String,BigDecimal>>userSummary=new HashMap<>();
        Map<User,Map<String,Map<String,BigDecimal>>>categorySummary=new HashMap<>();


        for(Transaction transaction:transactions){
            User user=transaction.getUser();
            String category=transaction.getCategory();

            if(!userSummary.containsKey(user))
                userSummary.put(user,new HashMap<>());

            if(!categorySummary.containsKey(user))
                categorySummary.put(user,new HashMap<>());

            if(transaction.getType().equals(Transaction.Type.EXPENSE)){

                //calculate for user data
                Map<String, BigDecimal> userData = userSummary.get(user);
                userData.put("EXPENSE", userData.getOrDefault("EXPENSE", BigDecimal.ZERO).add(transaction.getAmount()));

                //calculate for category
                Map<String,Map<String, BigDecimal>> categoryOfUser = categorySummary.get(user);
                Map<String,BigDecimal> categoryData=categoryOfUser.computeIfAbsent(category,c->new HashMap<>());
                categoryData.put("EXPENSE", categoryData.getOrDefault("EXPENSE", BigDecimal.ZERO).add(transaction.getAmount()));

            }
            else{

                //calculate for user data
                Map<String, BigDecimal> userData = userSummary.get(user);
                userData.put("INCOME", userData.getOrDefault("INCOME", BigDecimal.ZERO).add(transaction.getAmount()));

                //calculate income for category

                Map<String,Map<String, BigDecimal>> categoryOfUser = categorySummary.get(user);
                Map<String,BigDecimal> categoryData=categoryOfUser.computeIfAbsent(category,c->new HashMap<>());
                categoryData.put("INCOME", categoryData.getOrDefault("INCOME", BigDecimal.ZERO).add(transaction.getAmount()));

            }

        }

        for(Map.Entry<User,Map<String,BigDecimal>> entry:userSummary.entrySet()){
            User user=entry.getKey();
            String username=user.getUsername();
            String email=user.getEmail();
            Map<String,Map<String,BigDecimal>> userCategorySummary=categorySummary.get(user);

            BigDecimal income=entry.getValue().getOrDefault("INCOME",BigDecimal.ZERO);
            BigDecimal expenses=entry.getValue().getOrDefault("EXPENSE",BigDecimal.ZERO);
            BigDecimal netIncome=income.subtract(expenses);

            Context context=new Context();
            context.setVariable("name",username);
            context.setVariable("income",income);
            context.setVariable("expenses",expenses);
            context.setVariable("netIncome",netIncome);
            context.setVariable("month",month);
            context.setVariable("userCategorySummary",userCategorySummary);

            String emailContent= templateEngine.process("email-summary",context);

            emailService.sendEmail(email,"Your monthly insights",emailContent);

        }
    }



}
