package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        Reservation reservation = reservationRepository2.findById(reservationId).get();
        Payment payment = new Payment();
        int billAmount = reservation.getNumberOfHours() * reservation.getSpot().getPricePerHour();

        if (amountSent < billAmount) {
            throw new Exception("Insufficient Amount");
        }

        String paymentMode = mode.toUpperCase();
        if(paymentMode.equals("CASH")) {
            payment.setPaymentCompleted(true);
            payment.setPaymentMode(PaymentMode.CASH);
        }
        else if (paymentMode.equals("UPI")){
            payment.setPaymentCompleted(true);
            payment.setPaymentMode(PaymentMode.UPI);
        }
        else if (paymentMode.equals("CARD")) {
            payment.setPaymentCompleted(true);
            payment.setPaymentMode(PaymentMode.CARD);
        }
        else {
            payment.setPaymentCompleted(false);
            throw new Exception("Payment mode not detected");
        }

        payment.setReservation(reservation);

        reservationRepository2.save(reservation);

        return payment;
    }
}
