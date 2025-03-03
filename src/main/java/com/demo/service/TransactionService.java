package com.demo.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.entity.Transactions;
import com.demo.repository.TransactionsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class TransactionService {


    @Autowired
    private TransactionsRepository transactionsRepository;

    public void saveTransaction(Transactions transaction) {
        transactionsRepository.save(transaction);
    } 
    
    public Transactions converToTransactions(Map<String, Object> map) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ObjectMapper mapper = new ObjectMapper();

        Transactions transactions = new Transactions();
        String transID = (String) map.get("transID");
        Integer orderStatus = (Integer) map.get("order_status");
        String status = (String) map.get("status");
        
        String strAmt = (String) map.get("bill_amt");
        double billAmt = (double) Double.valueOf(strAmt);

        String descriptor = (String) map.get("descriptor");
        String strDate = (String) map.get("tdate");
        Date tdate = sdf.parse(strDate);

        String billCurrency = (String) map.get("billCurrency");

        String response = (String) map.get("response");
        String reference = (String) map.get("reference");
        String mop = (String) map.get("mop");
        String ccno = (String) map.get("ccno");
        String rrn = (String) map.get("rrn");

        String upa = (String) map.get("upa");
        String authstatus = (String) map.get("authstatus");
        String authurl = (String) map.get("authurl");

        Map<String, Object> mapInfo = (Map<String, Object>) map.get("info");
        String jsonInfo = mapper.writeValueAsString(mapInfo);

        transactions.setTransID(transID);
        transactions.setBillCurrency(billCurrency);
        transactions.setAuthstatus(status);
        transactions.setOrderStatus(orderStatus);
        transactions.setStatus(status);         
        transactions.setBillAmt(billAmt);
        transactions.setDescriptor(descriptor);
        transactions.setTdate(tdate);
        transactions.setBillCurrency(billCurrency);
        transactions.setResponse(response);
        transactions.setReference(reference);
        transactions.setMop(mop);
        transactions.setCcno(ccno);
        transactions.setRrn(rrn);
        transactions.setUpa(upa);
        transactions.setAuthstatus(authstatus);
        transactions.setAuthurl(authurl);
        transactions.setInfo(jsonInfo);
        return transactions ;

    }
}
